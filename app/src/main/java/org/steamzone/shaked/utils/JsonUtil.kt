package org.steamzone.shaked.utils

import com.google.gson.GsonBuilder


object JsonUtil {
   val gson = GsonBuilder().create()

   fun longHash(string: String): Long {
      var h = 98764321261L
      val l = string.length
      val chars = string.toCharArray()

      for (i in 0 until l) {
         h = 31 * h + chars[i].toLong()
      }
      return h
   }
}