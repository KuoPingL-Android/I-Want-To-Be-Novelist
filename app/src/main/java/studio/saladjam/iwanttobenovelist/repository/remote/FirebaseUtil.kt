package studio.saladjam.iwanttobenovelist.repository.remote

import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.Util
import studio.saladjam.iwanttobenovelist.repository.ServiceLocator
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter

class FirebaseUtil {

    companion object {
        const val COLLECTION_BOOKS_KEY                  = "books"
        const val COLLECTION_LIKES_KEY                  = "likes"
        const val COLLECTION_USERS_KEY                  = "users"
        const val COLLECTION_WORKS_KEY                  = "works"
        const val COLLECTION_FOLLOWER_KEY               = "followers"
        const val COLLECTION_CHAPTERS_KEY               = "chapters"
        const val COLLECTION_CATEGORY_KEY               = "categories"
        const val COLLECTION_COORDINATES_KEY            = "coordinates"
        const val COLLECTION_BOOK_FOLLOWING_KEY         = "booksFollowing"
        const val COLLECTION_GENERAL_RECOMMENDATION_KEY = "general"

        const val DOCUMENT_RECOMMENDATIONS_KEY          = "recommendations"

        const val FIELD_IMAGE_ID_KEY                    = "imageID"
        const val FIELD_POPULARITY_KEY                  = "popularity"
        const val FIELD_CHAPTER_INDEX_KEY               = "chapterIndex"

        private val fireStore           = FirebaseFirestore.getInstance()
        private val fireStorage          = FirebaseStorage.getInstance()

        // Firebase Auth
        val auth = FirebaseAuth.getInstance()
        val isFBSignin: Boolean
            get() = auth.currentUser != null
    }


//    private val fireStore       = FirebaseFirestore.getInstance()

    enum class Collections(val ref: CollectionReference) {
        USERS(fireStore.collection(COLLECTION_USERS_KEY)),
        BOOKS(fireStore.collection(COLLECTION_BOOKS_KEY)),
        CATEGORY(fireStore.collection(COLLECTION_CATEGORY_KEY)),
        GENERAL_RECOMMENDATION(Documents.RECOMMENDATION.ref
            .collection(COLLECTION_GENERAL_RECOMMENDATION_KEY));

        companion object {
            fun getWrittenBookRefForUser(userID: String): CollectionReference {
                return USERS.ref.document(userID).collection(COLLECTION_WORKS_KEY)
            }

            fun getChaptersRefFrom(bookID: String): CollectionReference {
                return BOOKS.ref
                    .document(bookID)
                    .collection(COLLECTION_CHAPTERS_KEY)
            }

            fun getLikesRefFrom(chapter: Chapter): CollectionReference {
                return getChaptersRefFrom(chapter.bookID)
                    .document(chapter.chapterID)
                    .collection(COLLECTION_LIKES_KEY)
            }

            fun getBookFollowersCollectionRefFrom(bookID: String): CollectionReference {
                return BOOKS.ref
                    .document(bookID)
                    .collection(COLLECTION_FOLLOWER_KEY)
            }

            fun getUserFollowBookCollection(userID: String): CollectionReference {
                return USERS.ref
                    .document(userID)
                    .collection(COLLECTION_BOOK_FOLLOWING_KEY)
            }
        }
    }

    enum class Documents(val ref: DocumentReference) {
        RECOMMENDATION(Collections.BOOKS.ref.document(DOCUMENT_RECOMMENDATIONS_KEY));

        companion object {
            fun getDocumentForBook(bookID: String): DocumentReference {
                return Collections.BOOKS.ref.document(bookID)
            }

            fun getDocumentForBookFollower(bookID: String, userID: String): DocumentReference {
                return Collections
                    .getBookFollowersCollectionRefFrom(bookID)
                    .document(userID)
            }

            fun getDocumentForUserFollowingBooks(userID: String, bookID: String):
                    DocumentReference {
                return Collections.getUserFollowBookCollection(userID).document(bookID)
            }
        }
    }

    class Queries {
        companion object {
            fun getChapterQueryFrom(bookID: String, chapterIndex: Int): Query {
                return Collections.BOOKS.ref
                    .document(bookID)
                    .collection(COLLECTION_CHAPTERS_KEY)
                    .whereEqualTo(FIELD_CHAPTER_INDEX_KEY, chapterIndex)
            }

            fun getImageCoordinatesQueryFrom(bookID: String, chapter: Chapter): Query {
                return Collections.getChaptersRefFrom(bookID)
                    .document(chapter.chapterID)
                    .collection(COLLECTION_COORDINATES_KEY)
                    .whereIn(FIELD_IMAGE_ID_KEY, chapter.images)
            }
        }
    }

    class Storage {
        companion object {
            // Access a Cloud Firestore instance from your Activity
            fun getStorageInstance(url: String): StorageReference {
                return fireStorage.getReferenceFromUrl(url)
            }
        }
    }
}