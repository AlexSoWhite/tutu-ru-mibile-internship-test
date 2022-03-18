package com.nafanya.tuturutest.model

import android.content.Context
import com.orhanobut.hawk.Hawk

class LocalStorageProvider(private val context: Context) {

    init {
        Hawk.init(context).build()
    }

    fun put(value: List<Anime>) {
        Hawk.put("cached", value)
    }

    fun get(): List<Anime> {
        return Hawk.get("cached", listOf())
    }
}
