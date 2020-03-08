package studio.saladjam.iwanttobenovelist.repository.remote

import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import studio.saladjam.iwanttobenovelist.R
import studio.saladjam.iwanttobenovelist.Util

class GoogleUtil {
    companion object {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val googleSigninOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(Util.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
}