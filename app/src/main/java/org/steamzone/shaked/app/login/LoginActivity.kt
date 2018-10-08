package org.steamzone.shaked.app.login

import android.content.Intent
import android.os.Bundle
import org.steamzone.shaked.R
import org.steamzone.shaked.app.SActivity
import org.steamzone.shaked.app.home.HomeActivity
import org.steamzone.shaked.box.LoginBox

class LoginActivity:SActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLoginFirst()
        setContentView(R.layout.activity_login)

    }

    private fun checkLoginFirst() {

        var loginBox = LoginBox.get()
        //if there are already saved data about user, send user to next screen and close current activity
        if (loginBox != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            this.finish()
        }

    }

    private fun startForgotPassword()
    {
        startActivity(Intent(this, ForgotPassword::class.java))
    }
}