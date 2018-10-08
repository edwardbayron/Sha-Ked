package org.steamzone.shaked.rest.json.models

import com.squareup.moshi.Json


data class DeviceAssignment(@Json(name = "hardwareId")
                            var hardwareId: String? = null,
                            @Json(name = "id")
                            var id: Int? = null,
                            @Json(name = "name")
                            var name: String? = null,
                            @Json(name = "type")
                            var type: String? = null)