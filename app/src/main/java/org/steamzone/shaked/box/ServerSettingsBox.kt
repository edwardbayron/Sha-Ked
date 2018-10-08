package org.steamzone.shaked.box

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id

@Entity
class ServerSettingsBox  {

    @Id(assignable = true)
    var id: Long = 0
    var serverUrl: String? = null



}