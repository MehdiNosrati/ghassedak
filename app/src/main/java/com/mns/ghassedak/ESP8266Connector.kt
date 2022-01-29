package com.mns.ghassedak

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class ESP8266Connector(private val ctx: Context, ip: String, port: String) {
    private val root: String = "http://$ip:$port"
    private var mRequestQueue: RequestQueue? = null
    fun moveForward() {
        sendRequest("N")
    }

    fun moveBackward() {
        sendRequest("S")
    }

    fun turnRight() {
        sendRequest("E")
    }

    fun turnLeft() {
        sendRequest("W")
    }

    fun stopMoving() {
        sendRequest("stop")
    }

    fun moveNE() {
        sendRequest("NE")
    }

    fun moveNW() {
        sendRequest("NW")
    }

    fun moveSE() {
        sendRequest("SE")
    }

    fun moveSW() {
        sendRequest("SW")
    }

    fun sendVip() {
        sendRequest("Vip")
    }

    fun setSound() {
        sendRequest("sound")
    }

    private fun sendRequest(stateValue: String) {
        val request = this.root + "/?State=" + stateValue
        val stringRequest = StringRequest(
            Request.Method.GET, request,
            { response ->
                Log.i(
                    "send Request Response",
                    response!!
                )
            }) { error -> Log.i("send Request Error", error.toString()) }
        stringRequest.setShouldCache(false)
        requestQueue.add(stringRequest)
    }

    private val requestQueue: RequestQueue
        get() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(ctx)
            }
            return mRequestQueue!!
        }

    fun clearRequestQueue() {
        requestQueue.cancelAll { true }
    }

}