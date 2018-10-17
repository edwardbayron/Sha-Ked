package org.steamzone.shaked.rx

import org.steamzone.shaked.box.DeviceBox
import org.steamzone.shaked.box.LoginBox
import org.steamzone.shaked.box.SBox

object DbUtil {

    fun dropDatabase()
    {
        SBox.getBox(LoginBox::class.java).removeAll()
        SBox.getBox(DeviceBox::class.java).removeAll()

    }
}