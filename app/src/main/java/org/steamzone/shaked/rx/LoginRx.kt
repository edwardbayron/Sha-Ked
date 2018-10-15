package org.steamzone.shaked.rx

import io.reactivex.Single
import org.steamzone.shaked.box.DeviceBox
import org.steamzone.shaked.box.LoginBox
import org.steamzone.shaked.rest.Rest
import org.steamzone.shaked.rest.json.models.LoginData

object LoginRx {
    //login to get all the userData
    fun login(userName: String?, password: String?): Single<LoginData> {
        return Rest.create(userName, password).login().map {

            var loginBox = LoginBox()
            loginBox.id = 1
            loginBox.email = userName
            loginBox.password = password
            LoginBox.save(loginBox)
            it.deviceAssignments?.let { it1 -> DeviceBox.save(it1) }
            return@map it
        }
    }
}