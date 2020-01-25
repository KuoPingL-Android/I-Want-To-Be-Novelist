package studio.saladjam.iwanttobenovelist

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build

object Util {

    /**
     * Determine and monitor the connectivity status
     *
     * https://developer.android.com/training/monitoring-device-state/connectivity-monitoring
     */
    fun isInternetConnected(): Boolean {
        val cm = IWBNApplication.context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        return activeNetwork?.isConnectedOrConnecting == true
    }

    fun getString(resourceId: Int): String {
        return IWBNApplication.context.getString(resourceId)
    }

    fun getColor(resourceId: Int): Int {
        return if (Build.VERSION.SDK_INT >= 23) {
            IWBNApplication.context.getColor(resourceId)
        } else {
            IWBNApplication.context.resources.getColor(resourceId)
        }
    }
}