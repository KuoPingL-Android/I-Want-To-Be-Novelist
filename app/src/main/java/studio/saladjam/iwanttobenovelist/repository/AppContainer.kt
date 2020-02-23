package studio.saladjam.iwanttobenovelist.repository

import android.content.Context
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
import studio.saladjam.iwanttobenovelist.repository.dataclass.Chapter
import studio.saladjam.iwanttobenovelist.repository.dataclass.User

class AppContainer(val context: Context) {
    val fireStore = FirebaseFirestore.getInstance()
    val userCollection = fireStore.collection("users")
    val bookCollection = fireStore.collection("books")
    val categoryCollection = fireStore.collection("categories")
    val recommendation = bookCollection.document("recommendations")
        .collection("general")

    fun getWrittenBookRefFrom(userID: String): CollectionReference {
        return userCollection.document(userID).collection("works")
    }

    fun getChaptersRefFrom(bookID: String): CollectionReference {
        return bookCollection
            .document(bookID)
            .collection("chapters")
    }

    fun getChapterQueryFrom(bookID: String, chapterIndex: Int): Query {
        return bookCollection
            .document(bookID)
            .collection("chapters")
            .whereEqualTo("chapterIndex", chapterIndex)
    }

    fun getImageCoordinatesQueryFrom(bookID: String, chapter: Chapter): Query {
        return bookCollection
            .document(bookID)
            .collection("chapters")
            .document(chapter.chapterID)
            .collection("coordinates")
            .whereIn("imageID", chapter.images)
    }

    fun getFollowersCollectionRefFrom(bookID: String): CollectionReference {
        return bookCollection.document(bookID).collection("followers")
    }

    fun getUserFollowBookCollection(userID: String): CollectionReference {
        return userCollection.document(userID).collection("booksFollowing")
    }

    val fireStorage = FirebaseStorage.getInstance()

    // Access a Cloud Firestore instance from your Activity
    fun getStorageInstance(url: String): StorageReference {
        return FirebaseStorage.getInstance().getReferenceFromUrl(url)
    }

    val repository = ServiceLocator.getRepository(context)
    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    // Configure sign-in to request the user's ID, email address, and basic
    // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
    val googleSigninOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(Util.getString(R.string.default_web_client_id))
        .requestEmail()
        .build()

    // Firebase Auth
    val auth = FirebaseAuth.getInstance()
    val isFBSignin: Boolean
        get() = auth.currentUser != null
}