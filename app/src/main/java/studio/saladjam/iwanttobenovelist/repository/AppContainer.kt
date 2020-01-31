package studio.saladjam.iwanttobenovelist.repository

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.Util

class AppContainer(val context: Context) {
    val fireStore = FirebaseFirestore.getInstance()
    val userCollection = fireStore.collection("users")
    val bookCollection = fireStore.collection("books")
    val categoryCollection = fireStore.collection("categories")
    val recommendation = bookCollection.document("recommendations")
        .collection("general")

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