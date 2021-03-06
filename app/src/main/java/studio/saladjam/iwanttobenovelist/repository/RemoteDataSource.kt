package studio.saladjam.iwanttobenovelist.repository

import android.graphics.Bitmap
import android.os.Bundle
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import studio.saladjam.iwanttobenovelist.*
import studio.saladjam.iwanttobenovelist.constants.ErrorMessages
import studio.saladjam.iwanttobenovelist.repository.dataclass.*
import studio.saladjam.iwanttobenovelist.repository.remote.FirebaseUtil
import studio.saladjam.iwanttobenovelist.searchscene.SearchFilters
import studio.saladjam.iwanttobenovelist.searchscene.getFirestoreSortingKey
import java.io.ByteArrayOutputStream
import java.lang.IllegalStateException
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object IWBNRemoteDataSource: Repository {

    override suspend fun postLikeChapter(chapter: Chapter): Result<Boolean> = suspendCoroutine { continuation ->

        val likesRef = IWBNApplication.container.getLikesRefFrom(chapter)
        val userID = IWBNApplication.user.userID
        val data = ChapterLiker(userID)

        likesRef.document(userID).set(data)
            .addOnSuccessListener { continuation.resume(Result.Success(true)) }
            .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
            .addOnFailureListener { continuation.resume(Result.Error(it)) }
    }

    override suspend fun postDislikeChapter(chapter: Chapter): Result<Boolean> = suspendCoroutine { continuation ->

        IWBNApplication.container
            .getLikesRefFrom(chapter)
            .document(IWBNApplication.user.userID)
            .delete()
            .addOnSuccessListener { continuation.resume(Result.Success(true)) }
            .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
            .addOnFailureListener { continuation.resume(Result.Error(it)) }
    }

    /** MAKE SEARCH ON BOOKS based on VALUE and FILTER */
    override suspend fun getBooksBasedOn(value: String, filter: SearchFilters): Result<List<Book>> = suspendCoroutine {continuation ->
        // SEARCH BY FILTER
        val query = FirebaseUtil.Collections.BOOKS.ref
            .orderBy(filter.getFirestoreSortingKey(), Query.Direction.DESCENDING)

        if (value.isEmpty()) {

            when(filter) {
                SearchFilters.POPULARITY -> {
                    query.get()
                        .addOnSuccessListener {
                            continuation.resume(Result.Success(it.toObjects(Book::class.java)))
                        }
                        .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
                        .addOnFailureListener { continuation.resume(Result.Error(it)) }
                }

                SearchFilters.MOST_RECENTLY -> {
                    query.get()
                        .addOnSuccessListener {
                            continuation.resume(Result.Success(it.toObjects(Book::class.java)))
                        }
                        .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
                        .addOnFailureListener { continuation.resume(Result.Error(it)) }

                }

                SearchFilters.COMPLETED -> {
                    query.whereEqualTo(Book.Companion.BookKeys.ISCOMPLETED.string, true).get()
                        .addOnSuccessListener {
                            continuation.resume(Result.Success(it.toObjects(Book::class.java)))
                        }
                        .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
                        .addOnFailureListener { continuation.resume(Result.Error(it)) }
                }

                SearchFilters.RECOMMENDED -> {
                    query//.whereIn(Book.Companion.BookKeys.CATEGORY.string, IWBNApplication.user.preferredCategories)
                        .get()
                        .addOnSuccessListener {
                            continuation.resume(Result.Success(it.toObjects(Book::class.java)))
                        }
                        .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
                        .addOnFailureListener { continuation.resume(Result.Error(it)) }
                }
            }
        } else {
            // SEARCH BY BOTH
            IWBNApplication.container.bookCollection
                .orderBy(Book.Companion.BookKeys.TITLE.string)
                .startAt(value)
                .endAt(value + "\uf8ff")
                .get()
                .addOnSuccessListener {
                    continuation.resume(Result.Success(it.toObjects(Book::class.java)))
                }
                .addOnCanceledListener {
                    continuation.resume(Result.Fail("CANCELED")) }
                .addOnFailureListener {
                    continuation.resume(Result.Error(it)) }
        }
    }

    /** BOOK - UPDATE FOLLOWER */
    override suspend fun postFollowBook(book: Book): Result<Boolean> = suspendCoroutine { continuation ->
        val bookID = book.bookID
        val userID = IWBNApplication.user.userID
        val followerRef = IWBNApplication.container.getFollowersCollectionRefFrom(bookID).document(userID)

        followerRef.set(BookFollower(userID))
            .addOnSuccessListener {
                IWBNApplication.container.getUserFollowBookCollection(userID)
                    .document(bookID)
                    .set(BookFollowee(bookID))
                    .addOnSuccessListener {
                        continuation.resume(Result.Success(true))

                        IWBNApplication.container.bookCollection.document(bookID).update("popularity", FieldValue.increment(1))

                    }
                    .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
                    .addOnFailureListener { continuation.resume(Result.Error(it)) }
            }
            .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
            .addOnFailureListener { continuation.resume(Result.Error(it)) }
    }

    override suspend fun postUnfollowBook(book: Book): Result<Boolean> = suspendCoroutine { continuation ->
        val bookID = book.bookID
        val userID = IWBNApplication.user.userID
        val followerRef = IWBNApplication.container.bookCollection.document(bookID).collection(
            FirebaseUtil.COLLECTION_FOLLOWER_KEY).document(userID)

        followerRef.delete()
            .addOnSuccessListener {
                // THEN GO to USER BOOK COLLECTION and DELETE the BOOK
                IWBNApplication.container.getUserFollowBookCollection(userID)
                    .document(bookID)
                    .delete()
                    .addOnSuccessListener {
                        continuation.resume(Result.Success(false))
                        IWBNApplication.container.bookCollection.document(bookID)
                            .update(FirebaseUtil.FIELD_POPULARITY_KEY, FieldValue.increment(-1))
                    }
                    .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
                    .addOnFailureListener { continuation.resume(Result.Error(it)) }
            }
            .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
            .addOnFailureListener { continuation.resume(Result.Error(it)) }
    }

    /** GET LIKES from BOOK */
    override suspend fun getLikesForChapter(chapter: Chapter): Result<List<String>> = suspendCoroutine {continuation ->
        IWBNApplication.container
            .getChaptersRefFrom(chapter.bookID)
            .document(chapter.chapterID)
            .collection("likes")
            .get()
            .addOnSuccessListener {
                continuation.resume(Result.Success(it.toObjects(ChapterLiker::class.java).map { it.userID }))
            }
            .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
            .addOnFailureListener { continuation.resume(Result.Error(it)) }
    }

    /** GET FOLLOWERS from BOOK */
    override suspend fun getFollowersForBook(book: Book): Result<List<String>> = suspendCoroutine {continuation ->
        IWBNApplication.container.bookCollection
            .document(book.bookID)
            .collection("followers")
            .get()
            .addOnSuccessListener {
                continuation.resume(Result.Success(it.toObjects(BookFollower::class.java).map { it.userID }))
            }
            .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
            .addOnFailureListener { continuation.resume(Result.Error(it)) }
    }

    /** GET CHAPTER DETAIL  */
    override suspend fun getChapterWithDetails(
        chapterIndex: Int,
        bookID: String
    ): Result<Pair<Chapter, List<ImageBlockRecorder>>> = suspendCoroutine { continuation ->

        val tag = "getChapterWithDetails"

        IWBNApplication.container.getChapterQueryFrom(bookID, chapterIndex).get()
            .addOnSuccessListener {

                // GOT CHAPTER OBJECT
                if(it.size() != 1) {throw IllegalStateException("suspend fun getChapterWithDetails: UNEXPECTED SIZE of ${it.size()} CHAPTERs FOUND")}

                val chapter = it.toObjects(Chapter::class.java).first()

                if (chapter.images.isNotEmpty()) {
                    // FETCH COORDINATES
                    IWBNApplication.container.getImageCoordinatesQueryFrom(bookID, chapter).get()
                        .addOnSuccessListener { coordinatesSnapshot ->

                            val coordinates = coordinatesSnapshot
                                .toObjects(ImageBlockRecorder::class.java)
                                .filter { chapter.images.contains(it.imageID) }

                            continuation.resume(Result.Success(Pair(chapter, coordinates)))

                        }
                        .addOnCanceledListener { continuation.resume(Result.Fail("$tag CANCELED")) }
                        .addOnFailureListener { continuation.resume(Result.Error(it)) }
                } else {
                    continuation.resume(Result.Success(Pair(chapter, listOf())))
                }



            }
            .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
            .addOnFailureListener { continuation.resume(Result.Error(it)) }

    }

    /** ADD DETAIL of the CHAPTER */
    override suspend fun postChapterWithDetails(
        chapter: Chapter,
        bitmapsMap: Map<String, Bitmap>,
        addOnCoordinators: List<ImageBlockRecorder>): Result<Boolean> = suspendCoroutine { continuation ->
        // SAVE ALL THE IMAGES FIRST
        val bookID = chapter.bookID
        val storageRef = IWBNApplication.container.fireStorage.reference
        var numberOfImageUploaded = 0

        var chapterID = chapter.chapterID
        val isNewChapter = chapterID.isEmpty()
        if (chapterID.isEmpty())
        {
            chapter.chapterID = IWBNApplication.container.getChaptersRefFrom(chapter.bookID).document().id
            chapterID = chapter.chapterID
        }

        if (bitmapsMap.keys.isEmpty()) {

            // DELETE OLD IMAGES
            if (chapter.images.isNotEmpty()) {
                chapter.images = listOf()
            }

            IWBNApplication.container
                .getChaptersRefFrom(bookID)
                .document(chapterID)
                .set(chapter)
                .addOnSuccessListener {

                    if (isNewChapter) {
                        IWBNApplication.container.bookCollection.document(bookID)
                            .update(Book.Companion.BookKeys.CHAPTERCOUNT.string, chapter.chapterIndex + 1)
                            .addOnSuccessListener { continuation.resume(Result.Success(true)) }
                            .addOnCanceledListener { continuation.resume(Result.Fail("postChapter : CANCELED")) }
                            .addOnFailureListener { continuation.resume(Result.Error(it)) }
                    } else {
                        continuation.resume(Result.Success(true))
                    }
                }
                .addOnCanceledListener { continuation.resume(Result.Fail("postChapter : CANCELED")) }
                .addOnFailureListener { continuation.resume(Result.Error(it)) }
        } else {
            for ((id, bitmap) in bitmapsMap) {

                val byteArrayOutput = ByteArrayOutputStream()

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutput)
                val bytes = byteArrayOutput.toByteArray()

                storageRef.child(bookID + "/${id}.jpg").putBytes(bytes)
                    .addOnSuccessListener {
                        numberOfImageUploaded += 1

                        // FILL IN THE IMAGE LOCATION
                        if (it.metadata != null &&
                            it.metadata?.path != null && it?.metadata?.bucket != null) {
                            val imageLocation = "gs://" + it.metadata!!.bucket + "/" + it.metadata!!.path
                            addOnCoordinators.filter { it.imageID == id }.first().imageLocation = imageLocation
                        }


                        if (numberOfImageUploaded == bitmapsMap.keys.count()) {
                            // UPDATE CHAPTER
                            chapter.images = bitmapsMap.keys.toList()

                            IWBNApplication.container
                                .getChaptersRefFrom(bookID)
                                .document(chapter.chapterID)
                                .set(chapter)
                                .addOnSuccessListener {

                                    // UPLOAD COORDINATES
                                    var j = 0

                                    for (coordinate in addOnCoordinators) {
                                        IWBNApplication.container
                                            .getChaptersRefFrom(bookID)
                                            .document(chapter.chapterID)
                                            .collection("coordinates")
                                            .document(coordinate.imageID)
                                            .set(coordinate)
                                            .addOnSuccessListener {

                                                j+=1

                                                if(j == addOnCoordinators.count()) {

                                                    if(isNewChapter) {
                                                        IWBNApplication.container.bookCollection.document(bookID)
                                                            .update(Book.Companion.BookKeys.CHAPTERCOUNT.string, chapter.chapterIndex + 1)
                                                            .addOnSuccessListener { continuation.resume(Result.Success(true)) }
                                                            .addOnCanceledListener { continuation.resume(Result.Fail("postChapter : CANCELED")) }
                                                            .addOnFailureListener { continuation.resume(Result.Error(it)) }
                                                    } else {
                                                        continuation.resume(Result.Success(true))
                                                    }
                                                }

                                            }
                                            .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
                                            .addOnFailureListener { continuation.resume(Result.Error(it)) }
                                    }


                                }
                                .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
                                .addOnFailureListener { continuation.resume(Result.Error(it)) }

                        }
                    }
                    .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
                    .addOnFailureListener { continuation.resume(Result.Error(it)) }
            }
        }

    }

    /** ADD a CHAPTER to the book */
    override suspend fun postChapter(chapter: Chapter): Result<Boolean> = suspendCoroutine { continuation ->

        var chapterID = chapter.chapterID
        val bookID = chapter.bookID
        val chapterIndex = chapter.chapterIndex

        val isNewChapter = chapterID.isEmpty()

        if (chapterID.isEmpty()) chapterID = IWBNApplication.container.getChaptersRefFrom(chapter.bookID).document().id

        chapter.chapterID = chapterID

        IWBNApplication.container
            .getChaptersRefFrom(bookID)
            .document(chapterID)
            .set(chapter)
            .addOnSuccessListener {

                if (isNewChapter) {
                    IWBNApplication.container.bookCollection.document(bookID)
                        .update(Book.Companion.BookKeys.CHAPTERCOUNT.string, chapterIndex + 1)
                        .addOnSuccessListener { continuation.resume(Result.Success(true)) }
                        .addOnCanceledListener { continuation.resume(Result.Fail("postChapter : CANCELED")) }
                        .addOnFailureListener { continuation.resume(Result.Error(it)) }
                } else {
                    continuation.resume(Result.Success(true))
                }
            }
            .addOnCanceledListener { continuation.resume(Result.Fail("postChapter : CANCELED")) }
            .addOnFailureListener { continuation.resume(Result.Error(it)) }
    }

    /** GET CHAPTERs FROM BOOK */
    override suspend fun getChaptersIn(book: Book): Result<List<Chapter>> = suspendCoroutine { continuation ->
        IWBNApplication.container.getChaptersRefFrom(book.bookID)
            .orderBy(Chapter.Companion.ChapterKeys.INDEX.string, Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener {
                continuation.resume(Result.Success(it.toObjects(Chapter::class.java)))
            }
            .addOnCanceledListener { continuation.resume(Result.Fail("getChaptersIn : Canceled")) }
            .addOnFailureListener { continuation.resume(Result.Error(it)) }
    }

    /** CREATE BOOK */
    override suspend fun createBook(title: String, imageBitmap: Bitmap): Result<Book> = suspendCoroutine { continuation ->
        val bookDoc = IWBNApplication.container.bookCollection.document()

        // GET BOOK ID
        val bookID = bookDoc.id

        val storageRef = IWBNApplication.container.fireStorage.reference
        val byteArrayOutput = ByteArrayOutputStream()

        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutput)
        val bytes = byteArrayOutput.toByteArray()

        val uploadTask = storageRef.child(bookID + "/cover.jpg").putBytes(bytes)

        uploadTask
            .addOnSuccessListener {
                Logger.i("${it.metadata}")

                if (it.metadata != null &&
                    it.metadata?.path != null && it?.metadata?.bucket != null) {

                    val book = Book(
                        bookID,
                        title,
                        authorID = IWBNApplication.user.userID,
                        authorName = IWBNApplication.user.name,
                        createdTime = it.metadata?.creationTimeMillis ?: Calendar.getInstance().timeInMillis,
                        cover = "gs://" + it.metadata!!.bucket + "/" + it.metadata!!.path)

                    IWBNApplication.container.bookCollection.document(bookID).set(book)
                        .addOnSuccessListener {
                            // UPDATE USER WORK BOOK AS WELL
                            val bookRef = {
                                "ref" to IWBNApplication.container.bookCollection.document(bookID)
                            }

                            IWBNApplication.container.getWrittenBookRefFrom(IWBNApplication.user.userID)
                                .document(bookID).set(bookRef)
                                .addOnSuccessListener {
                                   if (IWBNApplication.user.role == Roles.REVIEWER.value) {
                                       // UPDATE USER STATUS
                                       IWBNApplication.user.role = Roles.WRITER.value

                                       IWBNApplication.container.userCollection.document(IWBNApplication.user.userID)
                                           .set(IWBNApplication.user)
                                           .addOnSuccessListener {
                                               continuation.resume(Result.Success(book))
                                           }
                                           .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
                                           .addOnFailureListener { continuation.resume(Result.Error(it)) }
                                   } else {
                                       continuation.resume(Result.Success(book))
                                   }
                                }
                                .addOnCanceledListener { Result.Fail("CANCELED") }
                                .addOnFailureListener { continuation.resume(Result.Error(it)) }
                            Result.Success(book) }
                        .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
                        .addOnFailureListener { continuation.resume(Result.Error(it)) }


                } else {
                    continuation.resume(Result.Fail("ERROR in METADATA"))
                }

            }
            .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
            .addOnFailureListener{ continuation.resume(Result.Error(it)) }
    }

    /** UPDATE BOOK */
    override suspend fun updateBook(book: Book): Result<Boolean> = suspendCoroutine {continuation ->
        IWBNApplication.container.bookCollection.document(book.bookID).set(book)
            .addOnSuccessListener { continuation.resume(Result.Success(true)) }
            .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
            .addOnFailureListener{ continuation.resume(Result.Error(it)) }
    }

    override suspend fun getBook(bookID: String): Result<Book> = suspendCoroutine {continuation ->
        IWBNApplication.container.bookCollection.document(bookID)
            .get()
            .addOnSuccessListener outer@ {
                val book = it.toObject(Book::class.java)
                if (book != null) {
                    if (IWBNApplication.categories == null) {
                        IWBNApplication.container.categoryCollection.get()
                            .addOnSuccessListener {categorySnapshot ->
                                var list = mutableListOf<Genre>()

                                list = categorySnapshot.documents.map {document->
                                    Genre(document.id,
                                        zh = document.get("zh") as String,
                                        en = document.get("en") as String)
                                }.toMutableList()

                                IWBNApplication.categories = Categories(list)

                                book.displayedCategory = IWBNApplication.categories!!.getDisplayName("zh", book.category)
                                continuation.resume(Result.Success(book))
                            }
                            .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
                            .addOnFailureListener{ continuation.resume(Result.Error(it)) }
                    } else {
                        book.displayedCategory = IWBNApplication.categories!!.getDisplayName("zh", book.category)
                        continuation.resume(Result.Success(book))
                    }
                } else {
                    continuation.resume(Result.Fail("getBook: Something is Wrong"))
                }

            }
            .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
            .addOnFailureListener{ continuation.resume(Result.Error(it)) }
    }

    /** PROFILE PAGE */
    override suspend fun getFollowingBooks(user: User): Result<List<Book>>
            = suspendCoroutine {continuation ->

        val bookIDs = user.favoriteBooks.sortedBy { it.lastSeenDate }.map { it.bookID }.reversed()

        if (bookIDs.isEmpty()) {
            continuation.resume(Result.Success(listOf()))
        } else {
            IWBNApplication.container.bookCollection.whereIn("bookID", bookIDs).get()
                .addOnSuccessListener {
                    val books = it.toObjects(Book::class.java)
                    continuation.resume(Result.Success(books))
                }
                .addOnCanceledListener { continuation.resume(Result.Fail("getFollowingBooks CANCELED")) }
                .addOnFailureListener { continuation.resume(Result.Error(it)) }
        }
    }

    private suspend fun getBooksFromIDs(bookIDs: List<String>): Result<List<Book>> = suspendCoroutine { continuation ->
        IWBNApplication.container.bookCollection.whereIn("bookID", bookIDs).get()
            .addOnSuccessListener {
                val books = it.toObjects(Book::class.java)
                continuation.resume(Result.Success(books))
            }
            .addOnCanceledListener { continuation.resume(Result.Fail("getFollowingBooks CANCELED")) }
            .addOnFailureListener { continuation.resume(Result.Error(it)) }
    }

    /** CATEGORY PAGE */
    override suspend fun getBooks(category: String,
                                  language: String,
                                  sortedBy: String): Result<List<Book>>
            = suspendCoroutine { continuation ->

        var bookLanguage = language

        if (bookLanguage.isEmpty()) {
            bookLanguage = "zh" //TODO: CHANGE LANGUAGE TO DYNAMIC
        }

        if (category.isEmpty()) {
            // Get All Books on Specific Language <- Default User Language
            IWBNApplication.container.bookCollection
//                .whereEqualTo("language", language)
                .orderBy("createdTime", Query.Direction.DESCENDING).get()
                .addOnSuccessListener {
                    continuation.resume(Result.Success(it.toObjects(Book::class.java)))
                }
                .addOnCanceledListener {
                    continuation.resume(Result.Fail("CANCELED"))
                }
                .addOnFailureListener{
                    continuation.resume(Result.Error(it))
                }
        } else {
            IWBNApplication.container.bookCollection
//                .whereEqualTo("language", language)
//                .whereEqualTo("category", category)
                .orderBy("createdTime", Query.Direction.DESCENDING).get()
                .addOnSuccessListener {
                    continuation.resume(Result.Success(it.toObjects(Book::class.java)))
                }
                .addOnCanceledListener {
                    continuation.resume(Result.Fail("CANCELED"))
                }
                .addOnFailureListener{
                    continuation.resume(Result.Error(it))
                }
        }
    }



    /** HOME DATA */
    override suspend fun getMostPopularBooks(): Result<List<Book>> = suspendCoroutine { continuation ->
        IWBNApplication.container
            .bookCollection.orderBy("popularity", Query.Direction.DESCENDING)
            .limit(5).get()
            .addOnSuccessListener {snapShot ->

                val list = snapShot.toObjects(Book::class.java).filter { it.chapterCount > 0 }

                continuation.resume(Result.Success(list))
            }
            .addOnFailureListener {exception -> continuation.resume(Result.Error(exception)) }
            .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
    }

    override fun addBooksFollowingSnapshotListener(userID: String, callback: (List<BookFollowee>) -> Unit) {
         IWBNApplication.container.getUserFollowBookCollection(userID)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            querySnapshot?.let {
                callback(it.toObjects(BookFollowee::class.java))
            }
        }
    }


    override suspend fun getFollowingBooks(list: List<BookFollowee>): Result<List<Book>> = suspendCoroutine {continuation ->

        val finalList = mutableListOf<Book>()

        if (list.isEmpty()) {continuation.resume(Result.Success(finalList)); return@suspendCoroutine}

        for (i in 0 until list.size) {
            IWBNApplication.container.bookCollection.document(list[i].bookID)
                .get()
                .addOnSuccessListener {

                    it.toObject(Book::class.java)?.let {book ->
                        finalList.add(book)
                    }

                    if (i == list.size - 1) {
                        continuation.resume(Result.Success(finalList))
                    }

                }
                .addOnFailureListener {exception -> continuation.resume(Result.Error(exception)) }
                .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
        }
    }

    override suspend fun getUserFollowing(user: User): Result<List<Book>> = suspendCoroutine {continuation ->

        val userID = user.userID

        IWBNApplication.container.getUserFollowBookCollection(userID).get()
            .addOnSuccessListener {
                val books = it.toObjects(BookFollowee::class.java)
                val resultList = mutableListOf<Book>()

                if (books.isEmpty()) { continuation.resume(Result.Success(listOf())) }
                else {
                    for (i in 0 until  books.size) {
                        IWBNApplication.container.bookCollection.document(books[i].bookID)
                            .get()
                            .addOnSuccessListener {
                                it.toObject(Book::class.java)?.let {book ->
                                    resultList.add(book)
                                }

                                if (i == books.size - 1) {
                                    continuation.resume(Result.Success(resultList))
                                }
                            }
                            .addOnFailureListener {exception ->
                                continuation.resume(Result.Error(exception))
                            }
                            .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
                    }
                }
            }
            .addOnFailureListener {exception -> continuation.resume(Result.Error(exception)) }
            .addOnCanceledListener { continuation.resume(Result.Fail("CANCELED")) }
    }

    override suspend fun getUserRecommendedList(user: User): Result<List<Book>> = suspendCoroutine {continuation ->
        /** FETCH Recommended List */
        IWBNApplication.container.recommendation.get()
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    val results = task.result?.toObjects(Book::class.java)
                    if (results != null) {
                        val bookIDs = results.map { it.bookID }

                        if (bookIDs.isEmpty()) {
                            continuation.resume(Result.Success(results))
                            return@addOnCompleteListener
                        }

                        /** FETCH Books from the BookID within the List */
                        IWBNApplication.container.bookCollection.whereIn("bookID", bookIDs).get()
                            .addOnCompleteListener { bookTask ->
                                if (bookTask.isSuccessful) {
                                    val result = bookTask.result?.toObjects(Book::class.java)
                                    if (result != null) {continuation.resume(Result.Success(result))}
                                    else {continuation.resume(Result.Fail("getUserRecommendedList : FAILED CONVERT"))}

                                } else if (bookTask.isCanceled) {
                                    continuation.resume(Result.Fail("getUserRecommendedList : CANCELED"))
                                } else if (bookTask.exception != null) {
                                    continuation.resume(Result.Error(bookTask.exception!!))
                                } else {
                                    continuation.resume(Result.Fail("getUserRecommendedList : UNKNOWN ERROR"))
                                }
                            }
                    }
                    else continuation.resume(Result.Success(listOf()))
                } else if (task.isCanceled) {
                    continuation.resume(Result.Fail("CANCELED"))
                } else if (task.exception != null) {
                    continuation.resume(Result.Error(task.exception!!))
                } else
                {
                    continuation.resume(Result.Fail("UNKNOWN"))
                }
            }
    }

    override suspend fun getUserWork(user: User, limit: Long?): Result<List<Book>> = suspendCoroutine {continuation ->

        var ref = IWBNApplication.container.getWrittenBookRefFrom(user.userID)

        if (limit == null) {
            ref.get()
                .addOnSuccessListener {

                    val bookIDs = it.documents.map { it.id }

                    if (bookIDs.isEmpty()) {
                        continuation.resume(Result.Success(listOf()))
                        return@addOnSuccessListener
                    }

                    var counter = 0
                    val allbooks = mutableListOf<Book>()

                    for (bookID in bookIDs) {
                        IWBNApplication.container.bookCollection.document(bookID).get()
                            .addOnSuccessListener {
                                counter += 1
                                it.toObject(Book::class.java)?.let {book ->
                                    allbooks.add(book)
                                }

                                if (counter == bookIDs.count()) {
                                    allbooks.sortBy { it.createdTime }
                                    allbooks.reverse()
                                    continuation.resume(Result.Success(allbooks))
                                }
                            }
                            .addOnCanceledListener { continuation.resume(Result.Fail("getFollowingBooks CANCELED")) }
                            .addOnFailureListener { continuation.resume(Result.Error(it)) }
                    }
                }
        } else {
            ref.limit(limit).get()
                .addOnSuccessListener {

                    val bookIDs = it.documents.map { it.id }

                    if (bookIDs.isEmpty()) {
                        continuation.resume(Result.Success(listOf()))
                        return@addOnSuccessListener
                    }

                    var counter = 0
                    for (id in bookIDs) {

                    }

                    IWBNApplication.container.bookCollection
                        .whereIn(Book.Companion.BookKeys.ID.string, bookIDs).get()
                        .addOnSuccessListener {
                            val books = it.toObjects(Book::class.java)

                            books.sortBy { it.createdTime }
                            books.reverse()

                            continuation.resume(Result.Success(books))
                        }
                        .addOnCanceledListener { continuation.resume(Result.Fail("getFollowingBooks CANCELED")) }
                        .addOnFailureListener { continuation.resume(Result.Error(it)) }
                }
                .addOnCanceledListener { continuation.resume(Result.Fail("getFollowingBooks CANCELED")) }
                .addOnFailureListener { continuation.resume(Result.Error(it)) }
        }
    }

    override suspend fun checkIfUserExist(user: User): Result<Boolean> = suspendCoroutine {continuation ->
        IWBNApplication.container.userCollection
            .whereEqualTo(User.Companion.UserKeys.EMAIL.key, user.email)
            .get()
            .addOnSuccessListener {
                if (it.isEmpty) {
                    continuation.resume(Result.Success(false))
                } else {
                    IWBNApplication.user = it.toObjects(User::class.java).first()
                    UserManager.userID = IWBNApplication.user.userID
                    continuation.resume(Result.Success(true))
                }
            }
            .addOnCanceledListener { continuation.resume(Result.Fail("getFollowingBooks CANCELED")) }
            .addOnFailureListener { continuation.resume(Result.Error(it)) }
    }

    /** LOGIN */
    override suspend fun getUser(token: String): Result<User> = suspendCoroutine {continuation ->
        if (!IWBNApplication.isNetworkConnected) {
            continuation.resume(Result.Fail(ErrorMessages.NO_NETWORK))
            return@suspendCoroutine
        }

        IWBNApplication.container.userCollection.document(token).get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)

                if (user != null) {
                    continuation.resume(Result.Success(user))
                } else {
                    continuation.resume(Result.Fail(ErrorMessages.INVALID_TOKEN))
                }

            }
            .addOnCanceledListener { continuation.resume(Result.Fail("Get User Cancelled")) }
            .addOnFailureListener { continuation.resume(Result.Error(it)) }
    }

    override fun handleGoogleTask(completedTask: Task<GoogleSignInAccount>): Result<Boolean> {
        return try {
            val result = completedTask.getResult(ApiException::class.java)
            // Signed in successfully, show authenticated UI.
            Logger.i("ACCOUNT = ${result}")

            val id      = result?.id
            val name    = result?.displayName
            val email   = result?.email
            val token   = result?.idToken

            if (result == null || id == null || email == null || name == null || token == null) {
                Result.Fail(IWBNApplication.instance.getString(R.string.google_exception_null))
            } else {
                IWBNApplication.user.apply {
                    this.token          = token
                    this.googleToken    = token // tell that user was logged in from Google
                    this.userID         = id
                    this.name           = name
                    this.email          = email
                }
                Result.Success(true)
            }


        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Logger.w( "handleGoogleTask signInResult:failed code=" + e.statusCode)
            Result.Error(e)
        }

    }

    override suspend fun loginViaFacebook(
        callbackManager: CallbackManager): Result<Boolean> = suspendCoroutine {continuation ->
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(p0: LoginResult?) {
                p0?.let {result ->
                    IWBNApplication.user.fbToken = result.accessToken.token
                    IWBNApplication.user.token = result.accessToken.token

                    /** GET USER ID and EMAIL */
                    val request = GraphRequest.newMeRequest(result.accessToken) {jsonObject, graphResponse ->
                        try {
                            val email = jsonObject.
                                get(IWBNApplication.instance.getString(R.string.fb_email))
                            val id = jsonObject.
                                get(IWBNApplication.instance.getString(R.string.fb_id))
                            val name = jsonObject.
                                get(IWBNApplication.instance.getString(R.string.fb_name))

                            IWBNApplication.user.apply {
                                userID      = id as String
                                this.name   = name as String
                                this.email  = email as String
                            }

                            continuation.resume(Result.Success(true))

                        } catch (e: Exception) {
                            Logger.i("loginFromFb EXCEPTION=${e.message}")
                            continuation.resume(Result.Fail(e.toString()))
                        }
                    }


                    val parameters = Bundle()
                    parameters.putString("fields", "id, name, email")
                    request.parameters = parameters
                    request.executeAsync()
                }
            }

            override fun onCancel() {
                continuation.resume(Result.Success(false))
            }

            override fun onError(p0: FacebookException?) {
                Logger.w("[${this::class.simpleName}] exception=${p0?.message}")

                p0?.message?.let {
                    if (it.contains("ERR_INTERNET_DISCONNECTED")) {
                        continuation.resume(Result.Fail(ErrorMessages.NO_NETWORK))
                    } else {
                        continuation.resume(Result.Fail(it))
                    }
                }
            }

        })
    }

    override suspend fun loginUser(user: User): Result<Boolean> = suspendCoroutine { continuation ->
        val auth = IWBNApplication.container.auth
        if (user.fbToken != null) {

            val credential = FacebookAuthProvider.getCredential(user.fbToken!!)
            auth.signInWithCredential(credential).addOnCompleteListener {task ->
                when {
                    task.isSuccessful -> {
                        auth.currentUser?.let {firebaseUser ->
                            IWBNApplication.user.userID = firebaseUser.uid
                            UserManager.userID = firebaseUser.uid
                            IWBNApplication.container.userCollection.document(user.userID).set(user)
                                .addOnCompleteListener {task ->
                                    if (task.isSuccessful) {
                                        continuation.resume(Result.Success(true))
                                    } else {
                                        task.exception?.let {
                                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                                            continuation.resume(Result.Error(it))
                                        }
                                        continuation.resume(Result.Fail(task.exception!!.toString()))
                                    }
                                }
                        }
                    }

                    task.isCanceled -> {
                        Logger.i("FB SIGNIN CANCEL : ${task.exception}")
                        continuation.resume(Result.Fail("Cancelled"))
                    }

                    task.isComplete -> {
                        Logger.i("ERROR")
                        continuation.resume(Result.Error(task.exception!!))
                    }
                }
            }

        } else if (user.googleToken != null) {
            val credential = GoogleAuthProvider.getCredential(user.googleToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener {task ->
                when {
                    task.isSuccessful -> {
                        auth.currentUser?.let {firebaseUser ->
                            IWBNApplication.user.userID = firebaseUser.uid
                            UserManager.userID = firebaseUser.uid
                            IWBNApplication.container.userCollection.document(user.userID).set(user)
                                .addOnCompleteListener {task ->
                                    if (task.isSuccessful) {
                                        continuation.resume(Result.Success(true))
                                    } else {
                                        task.exception?.let {
                                            Logger.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                                            continuation.resume(Result.Error(it))
                                        }
                                        continuation.resume(Result.Fail(task.exception!!.toString()))
                                    }
                                }
                        }
                    }

                    task.isCanceled -> {
                        Logger.i("signInWithCredential:failure ${task.exception}")
                        continuation.resume(Result.Fail("Cancelled"))
                    }

                    task.isComplete -> {
                        Logger.i("ERROR")
                        continuation.resume(Result.Error(task.exception!!))
                    }
                }
            }
        }
    }

    override suspend fun getCategory(): Result<Categories> = suspendCoroutine {continuation ->

        if (IWBNApplication.categories != null) {
            continuation.resume(Result.Success(IWBNApplication.categories!!))
            return@suspendCoroutine
        }

        IWBNApplication.container.categoryCollection.get()
            .addOnCompleteListener {task ->
                if (task.isSuccessful) {
                    var list = mutableListOf<Genre>()

                    val result = task.result
                    if (result != null) {
                        list = result.documents.map {
                            Genre(it.id,
                                zh = it.get("zh") as String,
                                en = it.get("en") as String)
                        }.toMutableList()
                    }

                    IWBNApplication.categories = Categories(list)

                    continuation.resume(Result.Success(Categories(list)))
                } else {
                    if (task.exception != null) {
                        val exception = task.exception!!
                        Logger.w("[${this::class.simpleName}] Error getting documents. ${exception.message}")
                        continuation.resume(Result.Error(exception))
                    } else {
                        continuation.resume(Result.Fail("UNKNOWN"))
                    }
                }
            }
    }
}