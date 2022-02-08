package com.android.l2l.twolocal.dataSourse.utils

import android.util.Log
import com.android.l2l.twolocal.BuildConfig
import com.android.l2l.twolocal.dataSourse.remote.api.ApiHeaders
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import java.util.*

class NetInterceptor(private val apiHeaders: ApiHeaders) : Interceptor {


    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val chainn = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .header("Content-Type", "application/json")
            .header("Authorization", apiHeaders.authorization)
            .header("Api-Key", BuildConfig.API_KEY);

        val request = chainn.build()
        val response = chain.proceed(request)
        if (BuildConfig.DEBUG) {
            val t1 = System.nanoTime()
            val requestBodyString = bodyToString(request)
            var requestLog =
                "\n" + "---------------------------REQUEST:--------------------------" + "\n"
            requestLog += String.format("Sending request %s on %s%n", request.url, chain.connection()) //info != null ? new Gson().toJson(info) :
            if (request.method == "POST" || request.method == "PUT") {
                requestLog = "\n" + requestLog + "\n" + requestBodyString
            }
            requestLog += "\n-------------------------------------------------------------" + "\n "
            Log.d("interceptor", ".\n$requestLog")
            val t2 = System.nanoTime()
            val responseLog = String.format(Locale.US, "Received response for %s in %.1fms%n", response.request.url, (t2 - t1) / 1e6)
            var bodyString = ""
            try {
                val responseBody = response.body
                val source = responseBody!!.source()
                source.request(Long.MAX_VALUE) // request the entire body.
                val buffer = source.buffer
                // clone buffer before reading from it
                bodyString = buffer.clone().readString(Charset.forName("UTF-8"))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            bodyString = prettyJson(bodyString)
            var headerStr = " \n$responseLog"
            var requestStr = " \n"
            headerStr += "\n\nURL:" + request.url + "\n"
            headerStr += "Headers:\n" + request.headers + "\n"
            Log.w("interceptor:", "\n$headerStr\n")
            requestStr += "Body:\n$bodyString"
            val chunkSize = 3500
            var i = 0
            while (i < requestStr.length) {
                Log.w("interceptor", requestStr.substring(i, Math.min(requestStr.length, i + chunkSize)))
                i += chunkSize
            }
        }
        return response
    }

    companion object {
        //
        fun bodyToString(request: Request): String {
            return try {
                val copy = request.newBuilder().build()
                val buffer = Buffer()
                copy.body?.writeTo(buffer)
                prettyJson(buffer.readUtf8())
            } catch (e: IOException) {
                "did not work"
            }
        }

        /**
         * Method make pretty format json
         */
        fun prettyJson(string: String): String {
            return try {
                val parser = JsonParser()
                val gson = GsonBuilder().setPrettyPrinting().create()
                val el = parser.parse(string)
                gson.toJson(el) // done
            } catch (e: Exception) {
                e.printStackTrace().toString()
                string
            }
        }
    }

}