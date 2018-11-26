package org.steamzone.shaked.app.device

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.support.RxFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_gps_settings.*
import org.steamzone.shaked.R
import org.steamzone.shaked.bt.old.BTClient
import android.app.ProgressDialog
import org.steamzone.shaked.bt.new_settings_item
import org.steamzone.shaked.services.BTService


class GPSSettingsFragment : RxFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_gps_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        master_mode_checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                master_mode_container.visibility = View.VISIBLE
            else
                master_mode_container.visibility = View.GONE

        }

        time_interval_checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                time_interval_cont.visibility = View.VISIBLE
            else
                time_interval_cont.visibility = View.GONE
        }


        distance_mode_checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                distance_mode_cont.visibility = View.VISIBLE
            else
                distance_mode_cont.visibility = View.GONE
        }

        heading_mode_checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                heading_mode_cont.visibility = View.VISIBLE
            else
                heading_mode_cont.visibility = View.GONE
        }

        gps_timeout_checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                gps_timeout_cont.visibility = View.VISIBLE
            else
                gps_timeout_cont.visibility = View.GONE
        }


        motion_sensor_checkbox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                hmotion_sensor_cont_1.visibility = View.VISIBLE
                hmotion_sensor_cont_2.visibility = View.VISIBLE
                hmotion_sensor_cont_3.visibility = View.VISIBLE
            } else {
                hmotion_sensor_cont_1.visibility = View.GONE
                hmotion_sensor_cont_2.visibility = View.GONE
                hmotion_sensor_cont_3.visibility = View.GONE
            }
        }



        loadData()

        save_profile_bt.setOnClickListener {
            sendBtSettingsToDevice()
        }

    }

    var settings: new_settings_item = new_settings_item()
    var dialog: ProgressDialog? = null
    @SuppressLint("CheckResult")
    private fun loadData() {
        if (dialog != null) {
            dialog?.dismiss()
        }
        dialog = ProgressDialog.show(activity, "",
                "Loading. Please wait...", true)

        if ((activity as DeviceActivity)?.isConnected() && BTService.deviceConnectedMap[(activity as DeviceActivity)?.deviceMac] != null) {


            BTService.deviceConnectedMap[(activity as DeviceActivity)?.deviceMac]?.readCharacteristic(BTClient.CHAR_LOGGER_SETTINGS_UUID)
                    ?.compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({

                        settings.parse_data(it)
                        setData(settings)
                        dialog?.dismiss()
                    }, {
                        dialog?.dismiss()
                        it.printStackTrace()
                    })


        } else {
            dialog?.dismiss()
        }


    }

    @SuppressLint("CheckResult")
    private fun sendBtSettingsToDevice() {

        if ((activity as DeviceActivity)?.isConnected() && BTService.deviceConnectedMap[(activity as DeviceActivity)?.deviceMac] != null) {

            var settings: new_settings_item = getBTSettingsUI()

            (activity as DeviceActivity)?.connectionBle?.writeCharacteristic(BTClient.CHAR_LOGGER_SETTINGS_UUID, settings.create_settings_bt_buff())
                    ?.compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe({
                        Snackbar.make(activity?.findViewById<View>(android.R.id.content)!!, getString(R.string.settings_writed), Snackbar.LENGTH_SHORT).show()
                    }, {
                        Snackbar.make(activity?.findViewById<View>(android.R.id.content)!!, "Error: " + it.message, Snackbar.LENGTH_SHORT).show()

                        it.printStackTrace()
                    })


        }


    }


    fun getBTSettingsUI(): new_settings_item {


        settings.gnss_time_interval_mode = time_interval_checkbox.isChecked.toInt()
        Logger.wtf("" + time_interval_spinner.selectedItem.toString().toInt())
        settings.gnss_time_interval_in_sec = time_interval_spinner.selectedItem.toString().toInt()

        settings.gnss_distance_interval_mode = distance_mode_checkbox.isChecked.toInt()
        settings.gnss_distance_interval_val_in_meter = distance_interval_spinner.selectedItem.toString().toInt()

        settings.gnss_heading_mode = heading_mode_checkbox.isChecked.toInt()


        settings.motion_sensor_en = motion_sensor_checkbox.isChecked.toInt()
        settings.motion_sensetivity_lvl = sensiv_spinner.selectedItem.toString().toInt()
        settings.motion_detect_time_val_in_sec = motion_detect_time_spinner.selectedItem.toString().toInt()
        settings.stationary_detect_time_val_in_sec = stationary_detect_time_spinner.selectedItem.toString().toInt()

        settings.master_mode = master_mode_checkbox.isChecked.toInt()
        settings.gnss_always_power_on = gps_always_on_checkbox.isChecked.toInt()
        settings.distance_interval_filter = distance_interval_filter_checkbox.isChecked.toInt()
        settings.distance_interval_filter_val_in_meter = distance_interval_spinner.selectedItem.toString().toInt()

        settings.speed_filter = speed_filter_checkbox.isChecked.toInt()
        settings.speed_filter_val_in_km_h = speed_spinner.selectedItem.toString().toInt()

        settings.gnss_timeout = gps_timeout_checkbox.isChecked.toInt()
        settings.gnss_timeout_val_in_sec = timeout_spinner.selectedItem.toString().toInt() * 60;

        settings.gnss_ant_ctrl = ant_select_spinner.selectedItemPosition

        return settings

    }

    private fun setData(box: new_settings_item) {
        box?.let {

            var settings = it
            setupTimeInterval(settings)
            setupDistance(settings)
            setupHeadingMode(settings)
            setupMotionMode(settings)
            setupMasterMode(settings)
            setupSpeedFilter(settings)
            setupGPSTimeOut(settings)
            setupAntenna(settings)

        }
    }

    private fun setupAntenna(settings: new_settings_item) {
        ant_select_spinner.setSelection(settings.gnss_ant_ctrl)
    }

    private fun setupGPSTimeOut(settings: new_settings_item) {
        gps_timeout_checkbox.isChecked = settings.gnss_timeout == 1

        var array = resources.getStringArray(R.array.speed_interval)
        var index = 0
        for (i in array.indices) {
            var value = array[i]
            if (settings.gnss_timeout_val_in_sec / 60 == value.toInt()) {
                index = i
            }
        }
        timeout_spinner.setSelection(index, true)
    }

    private fun setupSpeedFilter(settings: new_settings_item) {
        speed_filter_checkbox.isChecked = settings.speed_filter == 1

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

    private fun setupMasterMode(settings: new_settings_item) {
        master_mode_checkbox.isChecked = settings.master_mode == 1
        gps_always_on_checkbox.isChecked = settings.gnss_always_power_on == 1
        distance_interval_filter_checkbox.isChecked = settings.distance_interval_filter == 1


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

    private fun setupMotionMode(settings: new_settings_item) {
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
            if (settings.stationary_detect_time_val_in_sec == value.toInt()) {
                index = i
            }
        }
        stationary_detect_time_spinner.setSelection(index, true)

        array = resources.getStringArray(R.array.motion_detect_time)
        index = 0
        for (i in array.indices) {
            var value = array[i]
            if (settings.motion_detect_time_val_in_sec == value.toInt()) {
                index = i
            }
        }
        motion_detect_time_spinner.setSelection(index, true)


    }

    private fun setupHeadingMode(settings: new_settings_item) {
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

    private fun setupDistance(settings: new_settings_item) {
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

    private fun setupTimeInterval(settings: new_settings_item) {
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

fun Boolean.toInt() = if (this) 1 else 0
