package fr.kodo.myapplication.model

import android.content.Context
import android.content.SharedPreferences

class Session {
    lateinit var prefs: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var context: Context

    var PRIVATE_MODE = 0

    constructor(Context: Context) {
        this.context = Context
        prefs = Context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE)
        editor = prefs.edit()
    }

    companion object {
        const val PREFS_NAME = "CustommerApplicationUserSession"
        const val KEY_USER_UUID = "userUUID"
        const val KEY_SUPERMARKET_PUBLIC_KEY = "SupermarketPublicKey"
    }

    fun createSession(userUUID: String, supermarketPublicKey: String) {
        editor.putString(KEY_USER_UUID, userUUID)
        editor.putString(KEY_SUPERMARKET_PUBLIC_KEY, supermarketPublicKey)
        editor.commit()
    }

    fun getUserUUID(): String {
        if (prefs.getString(KEY_USER_UUID, null) == null) {
            throw Exception("User not logged in")
        }
        else{
            return prefs.getString(KEY_USER_UUID, null)!!
        }
    }

    fun getSupermarketPublicKey(): String? {
        return prefs.getString(KEY_SUPERMARKET_PUBLIC_KEY, null)
    }

    fun logout() {
        editor.clear()
        editor.commit()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getString(KEY_USER_UUID, null) != null
    }


}