package com.android.l2l.twolocal.dataSourse.utils.error

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import org.json.JSONObject


object UtilsError {

    fun parseError(responseBody: ResponseBody?): ErrorApp? {
        val gson: Gson = GsonBuilder().setPrettyPrinting().create();
        val jObjError = JSONObject(responseBody?.string())

        return gson.fromJson(jObjError.toString(), ErrorApp::class.java)
    }
}