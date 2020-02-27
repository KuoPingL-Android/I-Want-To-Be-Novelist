package studio.saladjam.iwanttobenovelist

import android.content.Context

object UserManager {
    private const val USER_KEY = "USER"
    private const val USER_ID_KEY = "USERID"

    var userID: String? = null
        get() {
            return IWBNApplication.context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE)
                .getString(USER_ID_KEY, null)
        }

        set(value) {
            field = when(value) {
                null -> {
                    IWBNApplication.context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE)
                        .edit().remove(USER_ID_KEY).apply()
                    null
                }

                else -> {
                    IWBNApplication.context.getSharedPreferences(USER_KEY, Context.MODE_PRIVATE)
                        .edit().putString(USER_ID_KEY, value).apply()
                    value
                }
            }
        }
}