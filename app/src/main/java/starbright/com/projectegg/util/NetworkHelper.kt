/*
 * Copyright (c) by Andreas (oentoro.andreas@gmail.com)
 * created at 25 - 7 - 2020.
 */

/**
 * Created by Andreas on 13/10/2019.
 */

package starbright.com.projectegg.util

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import retrofit2.HttpException
import starbright.com.projectegg.data.model.response.NetworkError
import java.io.IOException
import java.net.ConnectException

class NetworkHelper constructor(private val context: Context) {
    companion object {
        private const val TAG = "NetworkHelper"
    }

    fun isConnectedWithNetwork(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork?.isConnected ?: false
    }

    fun castToNetworkError(throwable: Throwable): NetworkError {
        val defaultNetworkError = NetworkError()
        try {
            if (throwable is ConnectException) return NetworkError(statusCode = 0)
            if (throwable !is HttpException) return defaultNetworkError
            val error = GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create()
                .fromJson(throwable.response()?.errorBody()?.string(), NetworkError::class.java)
            return NetworkError(error.status, throwable.code(), error.message)
        } catch (e: IOException) {
            Log.e(TAG, e.toString())
        } catch (e: JsonSyntaxException) {
            Log.e(TAG, e.toString())
        } catch (e: NullPointerException) {
            Log.e(TAG, e.toString())
        }
        return defaultNetworkError
    }
}