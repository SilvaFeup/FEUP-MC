package fr.kodo.myapplication.controller

import android.content.Context
import android.content.SharedPreferences
import fr.kodo.myapplication.crypto.KeyStoreUtils
import fr.kodo.myapplication.model.KEY_SUPERMARKET_PUBLIC_KEY
import fr.kodo.myapplication.model.KEY_USERNAME
import fr.kodo.myapplication.model.KEY_USER_UUID
import fr.kodo.myapplication.model.PREFS_NAME
import java.security.PublicKey



class Session(Context: Context) {
    private var prefs: SharedPreferences
    private var editor: SharedPreferences.Editor
    private var PRIVATE_MODE = 0

    init {
        prefs = Context.getSharedPreferences(PREFS_NAME, PRIVATE_MODE)
        editor = prefs.edit()
    }

    fun createSession(username: String, userUUID: String, supermarketPublicKey: String) {
        editor.putString(KEY_USERNAME, username)
        editor.putString(KEY_USER_UUID, userUUID)
        editor.putString(KEY_SUPERMARKET_PUBLIC_KEY, supermarketPublicKey)
        editor.commit()
    }

    fun getUserUUID(): String {
        if (prefs.getString(KEY_USER_UUID, null) == null) {
            throw Exception("User not logged in")
        } else {
            return prefs.getString(KEY_USER_UUID, null)!!
        }
    }

    fun getUsername(): String {
        if (prefs.getString(KEY_USERNAME, null) == null) {
            throw Exception("User not logged in")
        } else {
            return prefs.getString(KEY_USERNAME, null)!!
        }
    }

    fun getSupermarketPublicKey(): PublicKey {
        if (prefs.getString(KEY_SUPERMARKET_PUBLIC_KEY, null) == null) {
            throw Exception("User not logged in")
        } else {
            return KeyStoreUtils.getPublicKeyFromString(prefs.getString(KEY_SUPERMARKET_PUBLIC_KEY, null)!!)
        }
    }

    fun logout() {
        editor.clear()
        editor.commit()
    }
}