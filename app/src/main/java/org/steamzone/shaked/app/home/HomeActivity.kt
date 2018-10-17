package org.steamzone.shaked.app.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import io.objectbox.android.AndroidScheduler
import io.objectbox.reactive.DataSubscription
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.steamzone.shaked.R
import org.steamzone.shaked.app.SActivity
import org.steamzone.shaked.app.add.AddDeviceActivity
import org.steamzone.shaked.app.device.DeviceActivity
import org.steamzone.shaked.app.device.DeviceActivity.Companion.MAC_ADDRESS_EXTRA
import org.steamzone.shaked.app.home.list.MainBTListAdapter
import org.steamzone.shaked.app.home.list.MainBTViewHolder
import org.steamzone.shaked.box.DeviceBox


class HomeActivity : SActivity(), NavigationView.OnNavigationItemSelectedListener {

    var adapterBt: MainBTListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setupActionBar()
        initList()
        setupDeviceBoxQuery()

        fab.setOnClickListener { view ->
            startActivity(Intent(this@HomeActivity, AddDeviceActivity::class.java))
        }


        setupNavigationView()

    }


    var scanSubjectSubscanSubjectSub: Disposable? = null

    override fun onResume() {
        super.onResume()
        setupPublishListener()
    }


    @SuppressLint("CheckResult")
    private fun setupPublishListener() {
        if (scanSubjectSubscanSubjectSub != null) {
            scanSubjectSubscanSubjectSub?.dispose()
        }

        scanSubjectSubscanSubjectSub = scanResultBehaviorSubject.observeOn(AndroidSchedulers.mainThread()).subscribe(
                {

                    checkDevices(it)
                },
                {
                    it.printStackTrace()
                }
        )

    }

    private fun checkDevices(list: MutableCollection<DeviceBox>?) {

        for (i in adapterBt?.list!!.indices) {
            val deviceBox = adapterBt?.list!![i]
            for (scannedDevicesBoxes in list?.toList()!!) {
                if (scannedDevicesBoxes.hardwareId.equals(deviceBox.hardwareId)) {
                    deviceBox.rssi = scannedDevicesBoxes.rssi
                    deviceBox.distance = scannedDevicesBoxes.distance
                    deviceBox.batteryInfo = scannedDevicesBoxes.batteryInfo
                    deviceBox.batteryInfoVoltage = scannedDevicesBoxes.batteryInfoVoltage
                    deviceBox.connected = scannedDevicesBoxes.connected
                    adapterBt?.notifyItemChanged(i)

                    break
                }
            }
        }

    }


    private fun initList() {

        var viewManager = LinearLayoutManager(this)

        adapterBt = MainBTListAdapter(object : MainBTViewHolder.OnItemClickListener {
            override fun onItemClick(item: DeviceBox) {
                var intent = Intent(this@HomeActivity, DeviceActivity::class.java)
                intent.putExtra(MAC_ADDRESS_EXTRA, item.hardwareId)
                startActivity(intent)
            }

        })

        adapterBt?.setupList(DeviceBox.getAll()!!)


        home_bt_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = adapterBt
        }

    }

    var deviceBoxChangesListener: DataSubscription? = null
    private fun setupDeviceBoxQuery() {

        deviceBoxChangesListener = DeviceBox.query().build().subscribe()
                .on(AndroidScheduler.mainThread())
                .observer { updateData(it) }

    }

    private fun updateData(list: List<DeviceBox>) {
        adapterBt?.setupList(list)
        checkDevices(scanMap.values)

    }

    override fun onDestroy() {
        super.onDestroy()

        if (!deviceBoxChangesListener?.isCanceled!!) {
            deviceBoxChangesListener?.cancel()
        }

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


}
