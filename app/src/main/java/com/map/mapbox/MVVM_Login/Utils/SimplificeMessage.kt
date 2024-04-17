package com.map.mapbox.MVVM_Login.Utils

import org.json.JSONException
import org.json.JSONObject

object SimplificeMessage {
    fun get(stringMessage: String): HashMap<String, String> {
        val message = HashMap<String, String>()
        val jsonObject = JSONObject(stringMessage)
        try {
            val jsonMessages = jsonObject.optJSONObject("message")
            jsonMessages.keys().forEach { message[it] = jsonMessages.getString(it) }
        } catch (e: JSONException) {
            message["message"] = jsonObject.getString("message")
        }
        return message
    }
}