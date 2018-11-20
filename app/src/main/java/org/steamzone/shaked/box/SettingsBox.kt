package org.steamzone.shaked.box

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.query.QueryBuilder
import org.steamzone.shaked.bt.new_settings_item
import java.util.*

@Entity
class SettingsBox {

    @Id(assignable = true)
    var id: Long = 0
    var profileName: String? = null
    var dateRead: Date? = null
    var dateWrite: Date? = null

    //GPS settings


    var gnss_time_interval_mode: Int = 0
    var gnss_time_interval_in_sec: Int = 0

    var gnss_distance_interval_mode: Int = 0
    var gnss_distance_interval_val_in_meter: Int = 0

    var gnss_heading_mode: Int = 0
    var gnss_heading_val_in_grad: Int = 0

    var motion_sensor_en: Int = 0
    var motion_sensetivity_lvl: Int = 0
    var stationary_detect_time_val_in_sec: Int = 0
    var motion_detect_time_val_in_sec: Int = 0

    var master_mode: Int = 0
    var gnss_always_power_on: Int = 0

    var distance_interval_filter: Int = 0
    var distance_interval_filter_val_in_meter: Int = 0

    var speed_filter: Int = 0
    var speed_filter_val_in_km_h: Int = 0

    var save_frame_to_flash_every: Int = 0

    var gnss_timeout: Int = 0
    var gnss_timeout_val_in_sec: Int = 0

    var gnss_ant_ctrl: Int = 0

    //BT
    var bt_work_mode: Int = 0
    var bt_time_interval_val_in_sec: Int = 0
    var bt_work_time_val_in_sec: Int = 0
    var bt_enable_in_stop: Int = 0
    var bt_enable_in_geozone: Int = 0
    var bt_ant_ctrl: Int = 0
    var bt_pa_lna_mode: Int = 0
    var bt_hidden_mode: Int = 0

    //Event
    var detect_gnss_jamming: Int = 0
    var detect_low_memory: Int = 0

    //Button/Led
    var button_en: Int = 0
    var led_work_mode: Int = 0

    //Logger settings
    var flash_overwrite_en: Int = 0
    var bt_debug_en: Int = 0
    var device_id: ByteArray? = null

    //Геозоны
    var geozone_val: Int = 0
    //var geozone_item: Array<new_settings_geozone_item>

    //Рассписание
    var schedule_val: Int = 0
    //var schedule_item: Array<new_settings_schedule_item>


    companion object {
        fun save(list: List<SettingsBox>): List<SettingsBox> {
            SBox.getBox(SettingsBox::class.java).put(list)
            return list
        }

        fun save(box: SettingsBox?): SettingsBox? {
            SBox.getBox(SettingsBox::class.java).put(box)
            return box
        }


        fun delete(list: List<SettingsBox>): List<SettingsBox> {
            SBox.getBox(SettingsBox::class.java).remove(list)
            return list
        }

        fun delete(model: SettingsBox): SettingsBox {
            SBox.getBox(SettingsBox::class.java).remove(model)
            return model
        }

        fun get(id: Long): SettingsBox? {

            return SBox.getBox(SettingsBox::class.java).query().equal(SettingsBox_.id, id).build().findFirst()
        }

        fun get(): SettingsBox? {

            return get(1)

        }

        fun getAll(): List<SettingsBox>? {
            return SBox.getBox(SettingsBox::class.java).query().build().find()
        }


        fun query(): QueryBuilder<SettingsBox> {
            return SBox.getBox(SettingsBox::class.java).query()
        }

        fun dataRead(settings: new_settings_item): SettingsBox {
            var box: SettingsBox = SettingsBox()
            box.id = 1
            box.bt_ant_ctrl = settings.bt_ant_ctrl
            box.bt_debug_en = settings.bt_debug_en
            box.bt_enable_in_geozone = settings.bt_enable_in_geozone
            box.bt_enable_in_stop = settings.bt_enable_in_stop
            box.bt_hidden_mode = settings.bt_hidden_mode
            box.bt_pa_lna_mode = settings.bt_pa_lna_mode
            box.bt_time_interval_val_in_sec = settings.bt_time_interval_val_in_sec
            box.bt_work_mode = settings.bt_work_mode
            box.bt_work_time_val_in_sec = settings.bt_work_time_val_in_sec



            box.gnss_time_interval_mode = settings.gnss_time_interval_mode
            box.gnss_time_interval_in_sec = settings.gnss_time_interval_in_sec

            box.gnss_distance_interval_mode = settings.gnss_distance_interval_mode
            box.gnss_distance_interval_val_in_meter = settings.gnss_distance_interval_val_in_meter

            box.gnss_heading_mode = settings.gnss_heading_mode
            box.gnss_heading_val_in_grad = settings.gnss_heading_val_in_grad

            box.motion_sensor_en = settings.motion_sensor_en
            box.motion_sensetivity_lvl = settings.motion_sensetivity_lvl
            box.stationary_detect_time_val_in_sec = settings.stationary_detect_time_val_in_sec
            box.motion_detect_time_val_in_sec = settings.motion_detect_time_val_in_sec

            box.master_mode = settings.master_mode
            box.gnss_always_power_on = settings.gnss_always_power_on

            box.distance_interval_filter = settings.distance_interval_filter
            box.distance_interval_filter_val_in_meter = settings.distance_interval_filter_val_in_meter

            box.speed_filter = settings.speed_filter
            box.speed_filter_val_in_km_h = settings.speed_filter_val_in_km_h

            box.save_frame_to_flash_every = settings.save_frame_to_flash_every

            box.gnss_timeout = settings.gnss_timeout
            box.gnss_timeout_val_in_sec = settings.gnss_timeout_val_in_sec

            box.gnss_ant_ctrl = settings.gnss_ant_ctrl

            //BT
            box.bt_work_mode = settings.bt_work_mode
            box.bt_time_interval_val_in_sec = settings.bt_time_interval_val_in_sec
            box.bt_work_time_val_in_sec = settings.bt_work_time_val_in_sec
            box.bt_enable_in_stop = settings.bt_enable_in_stop
            box.bt_enable_in_geozone = settings.bt_enable_in_geozone
            box.bt_ant_ctrl = settings.bt_ant_ctrl
            box.bt_pa_lna_mode = settings.bt_pa_lna_mode
            box.bt_hidden_mode = settings.bt_hidden_mode

            //Event
            box.detect_gnss_jamming = settings.detect_gnss_jamming
            box.detect_low_memory = settings.detect_low_memory

            //Button/Led
            box.button_en = settings.button_en
            box.led_work_mode = settings.led_work_mode

            //Logger settings
            box.flash_overwrite_en = settings.flash_overwrite_en
            box.bt_debug_en = settings.bt_debug_en
            box.device_id = settings.device_id

            //Геозоны
            box.geozone_val = settings.geozone_val
            //var geozone_item: Array<new_settings_geozone_item>

            //Рассписание
            box.schedule_val = settings.schedule_val
            //var schedule_item: Array<new_settings_schedule_item>


            return box
        }

        fun converBoxToSettings(box: SettingsBox?): new_settings_item {
            if (box != null) {
                val settingsBt = new_settings_item()
                settingsBt.bt_ant_ctrl = box.bt_ant_ctrl
                settingsBt.bt_debug_en = box.bt_debug_en
                settingsBt.bt_enable_in_geozone = box.bt_enable_in_geozone
                settingsBt.bt_enable_in_stop = box.bt_enable_in_stop
                settingsBt.bt_hidden_mode = box.bt_hidden_mode
                settingsBt.bt_pa_lna_mode = box.bt_pa_lna_mode
                settingsBt.bt_time_interval_val_in_sec = box.bt_time_interval_val_in_sec
                settingsBt.bt_work_mode = box.bt_work_mode
                settingsBt.bt_work_time_val_in_sec = box.bt_work_time_val_in_sec



                settingsBt.gnss_time_interval_mode = box.gnss_time_interval_mode
                settingsBt.gnss_time_interval_in_sec = box.gnss_time_interval_in_sec

                settingsBt.gnss_distance_interval_mode = box.gnss_distance_interval_mode
                settingsBt.gnss_distance_interval_val_in_meter = box.gnss_distance_interval_val_in_meter

                settingsBt.gnss_heading_mode = box.gnss_heading_mode
                settingsBt.gnss_heading_val_in_grad = box.gnss_heading_val_in_grad

                settingsBt.motion_sensor_en = box.motion_sensor_en
                settingsBt.motion_sensetivity_lvl = box.motion_sensetivity_lvl
                settingsBt.stationary_detect_time_val_in_sec = box.stationary_detect_time_val_in_sec
                settingsBt.motion_detect_time_val_in_sec = box.motion_detect_time_val_in_sec

                settingsBt.master_mode = box.master_mode
                settingsBt.gnss_always_power_on = box.gnss_always_power_on

                settingsBt.distance_interval_filter = box.distance_interval_filter
                settingsBt.distance_interval_filter_val_in_meter = box.distance_interval_filter_val_in_meter

                settingsBt.speed_filter = box.speed_filter
                settingsBt.speed_filter_val_in_km_h = box.speed_filter_val_in_km_h

                settingsBt.save_frame_to_flash_every = box.save_frame_to_flash_every

                settingsBt.gnss_timeout = box.gnss_timeout
                settingsBt.gnss_timeout_val_in_sec = box.gnss_timeout_val_in_sec

                settingsBt.gnss_ant_ctrl = box.gnss_ant_ctrl

                //BT
                settingsBt.bt_work_mode = box.bt_work_mode
                settingsBt.bt_time_interval_val_in_sec = box.bt_time_interval_val_in_sec
                settingsBt.bt_work_time_val_in_sec = box.bt_work_time_val_in_sec
                settingsBt.bt_enable_in_stop = box.bt_enable_in_stop
                settingsBt.bt_enable_in_geozone = box.bt_enable_in_geozone
                settingsBt.bt_ant_ctrl = box.bt_ant_ctrl
                settingsBt.bt_pa_lna_mode = box.bt_pa_lna_mode
                settingsBt.bt_hidden_mode = box.bt_hidden_mode

                //Event
                settingsBt.detect_gnss_jamming = box.detect_gnss_jamming
                settingsBt.detect_low_memory = box.detect_low_memory

                //Button/Led
                settingsBt.button_en = box.button_en
                settingsBt.led_work_mode = box.led_work_mode

                //Logger settings
                settingsBt.flash_overwrite_en = box.flash_overwrite_en
                settingsBt.bt_debug_en = box.bt_debug_en
                settingsBt.device_id = box.device_id

                //Геозоны
                settingsBt.geozone_val = box.geozone_val
                //var geozone_item: Array<new_settings_geozone_item>

                //Рассписание
                settingsBt.schedule_val = box.schedule_val
                return settingsBt
            } else {
                return new_settings_item()
            }
        }


    }

}