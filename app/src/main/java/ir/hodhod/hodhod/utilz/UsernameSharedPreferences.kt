package ir.hodhod.hodhod.utilz

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.Delegates

object UsernameSharedPreferences {

    //region of param
    private const val defaultValue = 0L
    private const val userNamePref = "userName_pref"
    private const val userNamePrefKey = "userName_pref_key"
    private var userNamePreference: SharedPreferences by Delegates.notNull()

    //end of region ....

    /**
     * @param ctx should be application context in order to prevent memory leakage
     *
     * @return[userNameSharedPreference]
     * */
    fun initialWith(ctx: Context): UsernameSharedPreferences {
        userNamePreference = ctx.getSharedPreferences(userNamePref, Context.MODE_PRIVATE)
        return UsernameSharedPreferences
    }

    fun saveUsername(username: String) =
        userNamePreference.edit().putString(userNamePrefKey, username).apply()

    fun getUsername() = userNamePreference.getString(userNamePrefKey, null)


}