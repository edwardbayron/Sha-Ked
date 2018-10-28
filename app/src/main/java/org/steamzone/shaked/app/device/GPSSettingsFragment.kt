package org.steamzone.shaked.app.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.components.support.RxFragment
import kotlinx.android.synthetic.main.fragment_bluetooth_settings.*
import kotlinx.android.synthetic.main.fragment_gps_settings.*
import org.steamzone.shaked.R
import org.steamzone.shaked.app.device.forms.GeozoneSettingsFormEditFragment

class GPSSettingsFragment:RxFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gps_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        master_mode_checkbox.setOnClickListener {

            if(master_mode_checkbox.isChecked)
                master_mode_container.visibility = View.VISIBLE
            else
                master_mode_container.visibility = View.GONE

        }


        time_interval_checkbox.setOnClickListener {
            if(time_interval_checkbox.isChecked)
                time_interval_cont_hideable.visibility = View.VISIBLE
            else
                time_interval_cont_hideable.visibility = View.GONE
        }


        distance_mode_checkbox.setOnClickListener {
            if(distance_mode_checkbox.isChecked)
                distance_mode_cont_hideable.visibility = View.VISIBLE
            else
                distance_mode_cont_hideable.visibility = View.GONE
        }

        heading_mode_checkbox.setOnClickListener {
            if(heading_mode_checkbox.isChecked)
                heading_mode_cont_hideable.visibility = View.VISIBLE
            else
                heading_mode_cont_hideable.visibility = View.GONE
        }


        motion_sensor_checkbox.setOnClickListener {
            if(motion_sensor_checkbox.isChecked) {
                hmotion_sensor_cont_1_hideable.visibility = View.VISIBLE
                hmotion_sensor_cont_2_hideable.visibility = View.VISIBLE
                hmotion_sensor_cont_3_hideable.visibility = View.VISIBLE
            }
            else{
                hmotion_sensor_cont_1_hideable.visibility = View.GONE
                hmotion_sensor_cont_2_hideable.visibility = View.GONE
                hmotion_sensor_cont_3_hideable.visibility = View.GONE
            }
        }



    }

}