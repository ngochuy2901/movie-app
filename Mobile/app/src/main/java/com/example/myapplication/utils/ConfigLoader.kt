package com.example.myapplication.utils

import android.content.Context
import com.example.myapplication.R
import java.util.Properties

object ConfigLoader {
    private var properties: Properties? = null

    fun load(context: Context): Properties {
        if (properties == null) {
            properties = Properties()
            context.resources.openRawResource(R.raw.resource).use { input ->
                properties!!.load(input)
            }
        }
        return properties!!
    }

    fun get(context: Context, key: String): String {
        return load(context).getProperty(key) ?: ""
    }
}