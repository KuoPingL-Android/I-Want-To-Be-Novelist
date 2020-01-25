package studio.saladjam.iwanttobenovelist.repository

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore

class AppContainer(val context: Context) {
    val fireStore = FirebaseFirestore.getInstance()
    val userCollection = fireStore.collection("users")
    val bookCollection = fireStore.collection("books")
    val categoryCollection = fireStore.collection("categories")
    val repository = ServiceLocator.getRepository(context)
}