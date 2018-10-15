package org.steamzone.shaked.rest.api

import io.reactivex.Flowable
import io.reactivex.Single
import org.steamzone.shaked.rest.json.models.LoginData
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST




interface API
{

    @Headers("Content-Type: application/json;charset=UTF-8")
    @GET("user/login")
    fun login(): Single<LoginData>

}
