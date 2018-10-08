package org.steamzone.shaked.rx

import io.reactivex.Single
import org.steamzone.shaked.rest.Rest
import org.steamzone.shaked.rest.json.models.LoginData

object LoginRx {
    //login to get all the userData
    fun login(userName: String?, password: String?): Single<LoginData> {
        return Rest.create(userName, password).login()
    }
}