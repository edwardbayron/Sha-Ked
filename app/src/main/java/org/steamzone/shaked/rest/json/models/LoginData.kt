package org.steamzone.shaked.rest.json.models

import com.squareup.moshi.Json


data class LoginData(@Json(name = "deviceAssignments")
                     var deviceAssignments: List<DeviceAssignment>? = null,
                     @Json(name = "fullName")
                     var fullName: String? = null,
                     @Json(name = "userRoles")
                     var userRoles: List<UserRole>? = null,
                     @Json(name = "userTeams")
                     var userTeams: List<Any>? = null,
                     @Json(name = "username")
                     var username: String? = null)