package org.steamzone.shaked.utils

import com.google.gson.GsonBuilder


object JsonUtil {
   val gson = GsonBuilder().setPrettyPrinting().create()
}