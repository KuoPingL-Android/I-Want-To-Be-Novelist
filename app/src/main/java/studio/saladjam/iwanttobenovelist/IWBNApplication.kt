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
import studio.saladjam.iwanttobenovelist.repository.AppContainer
import studio.saladjam.iwanttobenovelist.repository.dataclass.User

class IWBNApplication : Application() {
    companion object {
        lateinit var context: Context
        lateinit var container: AppContainer
        var user = User()
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

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val googleSigninOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
    }

    override fun onCreate() {
        super.onCreate()
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