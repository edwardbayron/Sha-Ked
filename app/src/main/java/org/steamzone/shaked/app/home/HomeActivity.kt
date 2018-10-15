package org.steamzone.shaked.app.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.google.android.material.navigation.NavigationView
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.steamzone.shaked.R
import org.steamzone.shaked.app.SActivity
import org.steamzone.shaked.app.add.AddDeviceActivity
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

        fab.setOnClickListener { view ->
            startActivity(Intent(this@HomeActivity, AddDeviceActivity::class.java))
        }


        setupNavigationView()

    }

    override fun onResume() {
        super.onResume()
        setupPublishListener()
    }


    @SuppressLint("CheckResult")
    private fun setupPublishListener() {


      var scanSubjectSubscanSubjectSub = scanResultConnectedBehaviorSubject.observeOn(AndroidSchedulers.mainThread()).subscribe(
                {

                    val combinedList = ArrayList<DeviceBox>()
                    combinedList.addAll(DeviceBox.getAll()!!)
                    combinedList.addAll(it)

                    adapterBt?.setupList(combinedList)
                },
                {
                    it.printStackTrace()
                }
        )

    }


    private fun initList() {

        var viewManager = LinearLayoutManager(this)

        adapterBt = MainBTListAdapter(object : MainBTViewHolder.OnItemClickListener {
            override fun onItemClick(item: DeviceBox) {
            }

        })

        adapterBt?.setupList(DeviceBox.getAll()!!)


        home_bt_list.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = adapterBt
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
