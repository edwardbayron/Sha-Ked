package org.steamzone.shaked.app.login

import android.content.Intent
import android.os.Bundle
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.*
import org.steamzone.shaked.R
import org.steamzone.shaked.app.SActivity
import org.steamzone.shaked.app.home.HomeActivity
import org.steamzone.shaked.box.LoginBox
import org.steamzone.shaked.rx.LoginRx

class LoginActivity : SActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLoginFirst()
        setContentView(R.layout.activity_login)
        setClickListeners()
    }

    private fun setClickListeners() {

        login_bt.setOnClickListener {

            LoginRx.login(login_username_et.editText?.text.toString(), login_password_et.editText?.text.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        //go to main screen yo
                        //
                        startHomeActivity()
                    }, {
                        it.printStackTrace()
                    })

        }

        without_login_bt.setOnClickListener {
        }

        server_settings.setOnClickListener {

        }

    }

    private fun startHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
    }

    private fun checkLoginFirst() {

        var loginBox = LoginBox.get()
        //if there are already saved data about user, send user to next screen and close current activity
        if (loginBox != null) {
            startActivity(Intent(this, HomeActivity::class.java))
            this.finish()
        }

    }

    private fun startForgotPassword() {
        startActivity(Intent(this, ForgotPassword::class.java))
    }
}