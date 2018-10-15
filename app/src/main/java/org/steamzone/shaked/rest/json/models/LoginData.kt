package org.steamzone.shaked.rest.json.models

import org.steamzone.shaked.box.DeviceBox


data class LoginData(
        var deviceAssignments: List<DeviceBox>? = null,

        var fullName: String? = null,

        var userRoles: List<UserRole>? = null,
//                     @field:JsonUtil(name = "userTeams")
//                     var userTeams: List<Any>? = null,

        var username: String? = null)