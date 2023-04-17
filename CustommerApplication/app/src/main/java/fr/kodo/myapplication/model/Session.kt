package fr.kodo.myapplication.model

import android.content.Context
import android.content.SharedPreferences

class Session(Context: Context) {
    private var prefs: SharedPreferences
    private var editor: SharedPreferences.Editor

    private var PRIVATE_MODE = 0

    init {
        prefs = Context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE)
        editor = prefs.edit()
    }

    companion object {
        const val PREFS_NAME = "CustomerApplicationUserSession"
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


}