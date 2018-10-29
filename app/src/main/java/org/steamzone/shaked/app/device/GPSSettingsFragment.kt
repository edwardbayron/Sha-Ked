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
import org.steamzone.shaked.box.SettingsBox

class GPSSettingsFragment : RxFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gps_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        master_mode_checkbox.setOnClickListener {

            if (master_mode_checkbox.isChecked)
                master_mode_container.visibility = View.VISIBLE
            else
                master_mode_container.visibility = View.GONE

        }


        time_interval_checkbox.setOnClickListener {
            if (time_interval_checkbox.isChecked)
                time_interval_cont_hideable.visibility = View.VISIBLE
            else
                time_interval_cont_hideable.visibility = View.GONE
        }


        distance_mode_checkbox.setOnClickListener {
            if (distance_mode_checkbox.isChecked)
                distance_mode_cont_hideable.visibility = View.VISIBLE
            else
                distance_mode_cont_hideable.visibility = View.GONE
        }

        heading_mode_checkbox.setOnClickListener {
            if (heading_mode_checkbox.isChecked)
                heading_mode_cont_hideable.visibility = View.VISIBLE
            else
                heading_mode_cont_hideable.visibility = View.GONE
        }


        motion_sensor_checkbox.setOnClickListener {
            if (motion_sensor_checkbox.isChecked) {
                hmotion_sensor_cont_1_hideable.visibility = View.VISIBLE
                hmotion_sensor_cont_2_hideable.visibility = View.VISIBLE
                hmotion_sensor_cont_3_hideable.visibility = View.VISIBLE
            } else {
                hmotion_sensor_cont_1_hideable.visibility = View.GONE
                hmotion_sensor_cont_2_hideable.visibility = View.GONE
                hmotion_sensor_cont_3_hideable.visibility = View.GONE
            }
        }

        time_interval_spinner.selectedItem

        setData(SettingsBox.get())

    }

    private fun setData(box: SettingsBox?) {
        box?.let {

            var settings = it
            setupTimeInterval(settings)
            setupDistance(settings)
            setupHeadingMode(settings)
            setupMotionMode(settings)
            setupMasterMode(settings)
            setupSpeedFilter(settings)

        }
    }

    private fun setupSpeedFilter(settings: SettingsBox) {
        speed_filter_checkbox.isChecked = settings.speed_filter ==1

        var array = resources.getStringArray(R.array.speed_interval)
        var index = 0
        for (i in array.indices) {
            var value = array[i]
            if (settings.speed_filter_val_in_km_h == value.toInt()) {
                index = i
            }
        }
        speed_spinner.setSelection(index, true)


    }

    private fun setupMasterMode(settings: SettingsBox)
    {
        master_mode_checkbox.isChecked = settings.master_mode == 1
        gps_always_on_checkbox.isChecked = settings.gnss_always_power_on ==1
        distance_interval_filter_checkbox.isChecked = settings.distance_interval_filter ==1


        var array = resources.getStringArray(R.array.distance_interval)
        var index = 0
        for (i in array.indices) {
            var value = array[i]
            if (settings.distance_interval_filter_val_in_meter == value.toInt()) {
                index = i
            }
        }
        distance_interval_spinner.setSelection(index, true)


    }

    private fun setupMotionMode(settings: SettingsBox) {
        motion_sensor_checkbox.isChecked = settings.motion_sensor_en == 1


        var array = resources.getStringArray(R.array.sensetivityl_array)
        var index = 0
        for (i in array.indices) {
            var value = array[i]
            if (settings.motion_sensetivity_lvl == value.toInt()) {
                index = i
            }
        }
        sensiv_spinner.setSelection(index, true)


        array = resources.getStringArray(R.array.stationary_detect_time)
        index = 0
        for (i in array.indices) {
            var value = array[i]
            if (settings.motion_detect_time_val_in_sec == value.toInt() * 60 * 60) {
                index = i
            }
        }
        stationary_detect_time_spinner.setSelection(index, true)

        array = resources.getStringArray(R.array.motion_detect_time)
        index = 0
        for (i in array.indices) {
            var value = array[i]
            if (settings.motion_detect_time_val_in_sec == value.toInt() ) {
                index = i
            }
        }
        motion_detect_time_spinner.setSelection(index, true)


    }

    private fun setupHeadingMode(settings: SettingsBox) {
        heading_mode_checkbox.isChecked = settings.gnss_heading_mode == 1


//        val distanceInterval = resources.getStringArray(R.array.distance_interval)
//        var distanceIntervalIndex = 0
//        for (i in distanceInterval.indices) {
//            var value = distanceInterval[i]
//            if (settings.gnss_distance_interval_val_in_meter == value.toInt()) {
//                distanceIntervalIndex = i
//            }
//        }
//        distance_interval_spinner.setSelection(distanceIntervalIndex, true)
    }

    private fun setupDistance(settings: SettingsBox) {
        distance_mode_checkbox.isChecked = settings.gnss_distance_interval_mode == 1


        val distanceInterval = resources.getStringArray(R.array.distance_interval)
        var distanceIntervalIndex = 0
        for (i in distanceInterval.indices) {
            var value = distanceInterval[i]
            if (settings.gnss_distance_interval_val_in_meter == value.toInt()) {
                distanceIntervalIndex = i
            }
        }
        distance_interval_spinner.setSelection(distanceIntervalIndex, true)
    }

    private fun setupTimeInterval(settings: SettingsBox) {
        time_interval_checkbox.isChecked = settings.gnss_time_interval_mode == 1

        val timeInterval = resources.getStringArray(R.array.array_time_interval)
        var timeIntervalIndex = 0
        for (i in timeInterval.indices) {
            var value = timeInterval[i]
            if (settings.gnss_time_interval_in_sec == value.toInt()) {
                timeIntervalIndex = i
            }
        }
        time_interval_spinner.setSelection(timeIntervalIndex, true)
    }

}