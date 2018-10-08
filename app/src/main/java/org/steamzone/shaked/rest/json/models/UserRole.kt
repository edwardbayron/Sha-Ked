package org.steamzone.shaked.rest.json.models

import com.squareup.moshi.Json


data class UserRole(@Json(name = "description")
                    var description: String? = null,
                    @Json(name = "id")
                    var id: Int? = null,
                    @Json(name = "rolename")
                    var rolename: String? = null)