package studio.saladjam.iwanttobenovelist

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.os.Build
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.FirebaseApp
import studio.saladjam.iwanttobenovelist.Util.getString
import studio.saladjam.iwanttobenovelist.repository.AppContainer
import studio.saladjam.iwanttobenovelist.repository.dataclass.Categories
import studio.saladjam.iwanttobenovelist.repository.dataclass.Genre
import studio.saladjam.iwanttobenovelist.repository.dataclass.User
import kotlin.properties.Delegates

class IWBNApplication : Application() {
    companion object {
        var instance: IWBNApplication by Delegates.notNull()
        lateinit var context: Context
        lateinit var container: AppContainer
        var user = User()
        var categories: Categories? = null

        val isNetworkConnected: Boolean
            get() {
                return if (Build.VERSION.SDK_INT >= 21) {
                    isNetworkConnected_API_21
                } else {
                    isNetworkConnected_API_20
                }
            }
        private var isNetworkConnected_API_21 = false
        private val isNetworkConnected_API_20: Boolean
            get() {
                val cm =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val activeNetwork = cm.activeNetworkInfo
                return activeNetwork?.isConnectedOrConnecting == true
            }

        const val PICK_IMAGE_REQUEST = 1
        const val CAMERA_IMAGE_REQUEST = 2
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        context = applicationContext
        // REGISTER NETWORK CALLBACK
        if (Build.VERSION.SDK_INT >= 21) {
            registerNetworkCallback()
        }
        // INITIALIZE FIREBASE
        FirebaseApp.initializeApp(context)
        // INITIALIZE APPCONTAINER
        container = AppContainer(context)
    }

    private fun registerNetworkCallback() {
        if (Build.VERSION.SDK_INT >= 21) {
            try {
                val connectivityManager =
                    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

                val builder = NetworkRequest.Builder()
                connectivityManager.registerNetworkCallback(
                    builder.build(),
                    object : ConnectivityManager.NetworkCallback() {
                        override fun onAvailable(network: Network) {
                            isNetworkConnected_API_21 = true // Global Static Variable
                        }

                        override fun onLost(network: Network) {
                            isNetworkConnected_API_21 = false // Global Static Variable
                        }
                    }
                )
                isNetworkConnected_API_21 = false

            } catch (e: Exception) {
                isNetworkConnected_API_21 = false
            }
        }

    }
}