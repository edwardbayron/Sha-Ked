package org.steamzone.shaked.app.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.components.support.RxFragment
import kotlinx.android.synthetic.main.fragment_device.*
import kotlinx.android.synthetic.main.fragment_device_settings.*
import org.steamzone.shaked.R

class DeviceSettingsFragment : RxFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_device_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gps_settings_cont.setOnClickListener {

            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.hide(this)
                    ?.add(R.id.fragment_container, GPSSettingsFragment(), GPSSettingsFragment::class.java.name)
                    ?.addToBackStack(GPSSettingsFragment::class.java.name)
                    ?.commit()

        }

        bt_settings_cont.setOnClickListener {

            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.hide(this)
                    ?.add(R.id.fragment_container, BTSettingsFragment(), BTSettingsFragment::class.java.name)
                    ?.addToBackStack(BTSettingsFragment::class.java.name)
                    ?.commit()

        }



    }
}