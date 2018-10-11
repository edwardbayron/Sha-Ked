package org.steamzone.shaked.app.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent.getActivity
import android.app.ProgressDialog.show
import android.bluetooth.BluetoothAdapter
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.steamzone.shaked.R
import org.steamzone.shaked.app.SActivity
import org.steamzone.shaked.bt.BTClient

class HomeActivity : SActivity(), NavigationView.OnNavigationItemSelectedListener {


    var btClient: BTClient? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupActionBar()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }


        setupNavigationView()
        setupPermissions()
    }

    @SuppressLint("CheckResult")
    private fun setupPermissions() {
        RxPermissions(this)
                .request(Manifest.permission.BLUETOOTH,
                        Manifest.permission.BLUETOOTH_ADMIN,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(
                        {
                            if (it) {
                                initBT()
                                initList()
                            } else {
                                Snackbar.make(root_view, R.string.no_permission, Snackbar.LENGTH_LONG).show()
                            }
                        },
                        {
                            it.printStackTrace()
                        }
                )
    }

    private fun initBT() {
        btClient = BTClient(btClientInerface, this)


        when (btClient?.bt_adapter_init()) {
            BTClient.BT_init_status.BT_OFF -> {
                AlertDialog.Builder(this)
                        .setTitle(R.string.bt_is_off)
                        .setMessage(R.string.need_enable_bt)
                        .setPositiveButton(R.string.enable) { dialog, which ->
                            run {
                                val REQUEST_ENABLE_BT = 1
                                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)

                            }
                        }
                        .setNegativeButton(R.string.cancel) { dialog, which ->
                            run {

                            }
                        }
                        .show()


            }

            BTClient.BT_init_status.BT_INIT_OK ->
                //FIXME: Добавить проверку из настроек: нужен ли broadcast и его содержание
                //FIXME: Добавить фильтр по содержанию широковещательного пакета
                //FIXME: Фильтр из настроек!

                //Настраиваем широковещательную отправку
                //Настраиваем поиск
                //Включаем широковещательную рассылку
                //Запускаем поиск
                btClient?.scan_start()

            BTClient.BT_init_status.BT_FATAL_ERROR_BT_NULL -> {

                Snackbar.make(root_view, R.string.bluetooth_not_found, Snackbar.LENGTH_LONG).show()

            }
        }

    }

    private fun initList() {


    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar)
    }

    private fun setupNavigationView() {

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }


    var btClientInerface: BTClient.BTMessageListener = object : BTClient.BTMessageListener {
        override fun ScanCallBack() {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun device_connect_status(connect: Boolean) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun get_data(data: ByteArray?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

}
