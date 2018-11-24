package org.steamzone.shaked.bt;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static org.steamzone.shaked.bt.new_settings_idx.*;



public class new_settings_item {

    //GNSS
    public int gnss_time_interval_mode;
    public int gnss_time_interval_in_sec;

    public int gnss_distance_interval_mode;
    public int gnss_distance_interval_val_in_meter;

    public int gnss_heading_mode;
    public int gnss_heading_val_in_grad;

    public int motion_sensor_en;
    public int motion_sensetivity_lvl;
    public int stationary_detect_time_val_in_sec;
    public int motion_detect_time_val_in_sec;

    public int master_mode;
    public int gnss_always_power_on;

    public int distance_interval_filter;
    public int distance_interval_filter_val_in_meter;

    public int speed_filter;
    public int speed_filter_val_in_km_h;

    public int save_frame_to_flash_every;

    public int gnss_timeout;
    public int gnss_timeout_val_in_sec;

    public int gnss_ant_ctrl;

    //BT
    public int bt_work_mode;
    public int bt_time_interval_val_in_sec;
    public int bt_work_time_val_in_sec;
    public int bt_enable_in_stop;
    public int bt_enable_in_geozone;
    public int bt_ant_ctrl;
    public int bt_pa_lna_mode;
    public int bt_hidden_mode;

    //Event
    public int detect_gnss_jamming;
    public int detect_low_memory;

    //Button/Led
    public int button_en;
    public int led_work_mode;

    //Logger settings
    public int flash_overwrite_en;
    public int bt_debug_en;
    public byte[] device_id;

    //Геозоны
    public int geozone_val;
    public new_settings_geozone_item[] geozone_item;

    //Рассписание
    public int schedule_val;
    public new_settings_schedule_item[] schedule_item;

    //F
    public new_settings_item(){
        geozone_item = new new_settings_geozone_item[MAX_GEOZONE_VAL];
        for(int i=0; i<MAX_GEOZONE_VAL; i++){geozone_item[i] = new new_settings_geozone_item();}

        schedule_item = new new_settings_schedule_item[SHEDULE_MAX_VAL];
        for(int i=0; i<SHEDULE_MAX_VAL; i++){schedule_item[i] = new new_settings_schedule_item();}

        device_id = new byte[10];
    }

    public int parse_data (byte[] data_src) {

        if(data_src.length != SETTINGS_FLASH_SIZE)
        {
            return 0;
        }

        byte[] tmp2 = new byte[2];
        byte[] tmp4 = new byte[4];

        //GNSS
        gnss_time_interval_mode                     = data_src[IGS_TIME_INTERVAL_MODE_EN];
            tmp2[1] = data_src[IGS_TIME_INTERVAL_IN_SEC_1];
            tmp2[0] = data_src[IGS_TIME_INTERVAL_IN_SEC_2];
        gnss_time_interval_in_sec = BytesToInt2(tmp2);

        gnss_distance_interval_mode                 = data_src[IGS_DISTANCE_INTERVAL_EN];
            tmp2[1] = data_src[IGS_DISTANCE_INTERVAL_IN_M_1];
            tmp2[0] = data_src[IGS_DISTANCE_INTERVAL_IN_M_2];
        gnss_distance_interval_val_in_meter = BytesToInt2(tmp2);


        gnss_heading_mode                           = data_src[IGS_HEADING_MODE_EN];
            tmp2[1] = data_src[IGS_HEADING_IN_GRAD_1];
            tmp2[0] = data_src[IGS_HEADING_IN_GRAD_2];
        gnss_heading_val_in_grad = BytesToInt2(tmp2);


        motion_sensor_en                            = data_src[IGS_MOTION_SENSOR_EN];
        motion_sensetivity_lvl                      = data_src[IGS_MOTION_SENSETIVE_LVL];

        tmp2[1] = data_src[IGS_STATIONARY_DETECT_TIME_IN_SEC];
        tmp2[0] = 0;
        stationary_detect_time_val_in_sec = BytesToInt2(tmp2);

        tmp2[1] = data_src[IGS_MOTION_DETCT_TIME_IN_SEC];
        tmp2[0] = 0;
        motion_detect_time_val_in_sec = BytesToInt2(tmp2);


        master_mode                                 = data_src[IGS_MASTER_MODE];
        gnss_always_power_on                        = data_src[IGS_GNSS_POWER_ALWAYS_ON];

        distance_interval_filter                    = data_src[IGS_DISTANCE_INTERVAL_FILTER_EN];
            tmp2[1] = data_src[IGS_DISTANCE_INTERVAL_FILTER_IN_M_1];
            tmp2[0] = data_src[IGS_DISTANCE_INTERVAL_FILTER_IN_M_2];
        distance_interval_filter_val_in_meter = BytesToInt2(tmp2);

        speed_filter                                = data_src[IGS_SPEED_FILTER_EN];

        tmp2[1] = data_src[IGS_SPEED_FILTER_VAL_IN_KMH];
        tmp2[0] = 0;
        speed_filter_val_in_km_h = BytesToInt2(tmp2);

        tmp2[1] = data_src[IGS_SAVE_FRAME_TO_FLASH_VAL];
        tmp2[0] = 0;
        save_frame_to_flash_every = BytesToInt2(tmp2);

        gnss_timeout                                = data_src[IGS_GNSS_TIMEOUT_EN];

        tmp2[1] = data_src[IGS_GNSS_TIMEOUT_IN_SEC];
        tmp2[0] = 0;
        gnss_timeout_val_in_sec = BytesToInt2(tmp2);

        gnss_ant_ctrl                               = data_src[IGS_GNSS_ANT_CTRL];

        //BT
        bt_work_mode                                = data_src[IBTS_BT_WORK_MODE];
            tmp2[1] = data_src[IBTS_BT_TIME_INTERVAL_IN_SEC_1];
            tmp2[0] = data_src[IBTS_BT_TIME_INTERVAL_IN_SEC_2];
        bt_time_interval_val_in_sec = BytesToInt2(tmp2);

        tmp2[1] = data_src[IBTS_BT_WORK_TIME_IN_SEC_1];
        tmp2[0] = data_src[IBTS_BT_WORK_TIME_IN_SEC_2];
        bt_work_time_val_in_sec = BytesToInt2(tmp2);

        bt_enable_in_stop                           = data_src[IBTS_ENABLE_BT_ON_STOP];
        bt_enable_in_geozone                        = data_src[IBTS_ENABLE_BT_IN_GEOZONE];
        bt_ant_ctrl                                 = data_src[IBTS_ANT_CTRL];
        bt_pa_lna_mode                              = data_src[IBTS_PA_LNA_MODE];
        bt_hidden_mode                              = data_src[IBTS_HIDDEN_MODE];

        detect_gnss_jamming                         = data_src[IES_DETECT_GNSS_JAMMING];
        detect_low_memory                           = data_src[IES_DETECT_LOW_MEMORY];

        button_en                                   = data_src[IBLS_BUTTON_EN];
        led_work_mode                               = data_src[IBLS_LED_WORK_MODE];

        flash_overwrite_en                          = data_src[ILS_FLASH_OVERWRITE_EN];
        bt_debug_en                                 = data_src[ILS_BT_DEBUG_EN];

        //dev id
        for(int i=0; i<10; i++){device_id[i] = data_src[ILS_DEVICE_ID_1 + i];}

        //Геозоны
        geozone_val                                 = data_src[IDX_GEOZONE_ARRAY_VAL];
        int geozone_index = IDX_GEOZONE_ARRAY;
        for(int i=0; i<MAX_GEOZONE_VAL; i++)
        {
            geozone_item[i].id = data_src[geozone_index++];

                tmp2[1] = data_src[geozone_index++];
                tmp2[0] = data_src[geozone_index++];
            geozone_item[i].radius = BytesToInt2(tmp2);

                tmp4[3] = data_src[geozone_index++];
                tmp4[2] = data_src[geozone_index++];
                tmp4[1] = data_src[geozone_index++];
                tmp4[0] = data_src[geozone_index++];
            //geozone_item[i].latitude = BytesToInt4(tmp4);
            geozone_item[i].latitude = byteArrayToFloat(tmp4);

                tmp4[3] = data_src[geozone_index++];
                tmp4[2] = data_src[geozone_index++];
                tmp4[1] = data_src[geozone_index++];
                tmp4[0] = data_src[geozone_index++];
            //geozone_item[i].longitude = BytesToInt4(tmp4);
            geozone_item[i].longitude = byteArrayToFloat(tmp4);
        }

        //рассписание
        schedule_val                                = data_src[IDX_SCHEDULE_ARRAY_VAL];
        int schedule_index = ISA_ITEM_TYPE;

        for(int i=0; i<SHEDULE_MAX_VAL; i++)
        {
            schedule_item[i].item_type = data_src[schedule_index++];

            schedule_item[i].ts_year = data_src[schedule_index++];
            schedule_item[i].ts_mon = data_src[schedule_index++];
            schedule_item[i].ts_day = data_src[schedule_index++];

            schedule_item[i].ts_hour = data_src[schedule_index++];
            schedule_item[i].ts_min = data_src[schedule_index++];
            schedule_item[i].ts_sec = data_src[schedule_index++];

            schedule_item[i].te_year = data_src[schedule_index++];
            schedule_item[i].te_mon = data_src[schedule_index++];
            schedule_item[i].te_day = data_src[schedule_index++];

            schedule_item[i].te_hour = data_src[schedule_index++];
            schedule_item[i].te_min = data_src[schedule_index++];
            schedule_item[i].te_sec = data_src[schedule_index++];
        }

        return 1;
    }

    public byte[] create_settings_bt_buff ()
    {
        byte[] OutBuff = new byte[SETTINGS_FLASH_SIZE];
        byte[] tmp2 = new byte[2];
        byte[] tmp4 = new byte[4];

        //GNSS
        OutBuff[IGS_TIME_INTERVAL_MODE_EN]  = (byte) gnss_time_interval_mode;
        tmp2 = intToBytes2(gnss_time_interval_in_sec);
        OutBuff[IGS_TIME_INTERVAL_IN_SEC_1] = tmp2[1];
        OutBuff[IGS_TIME_INTERVAL_IN_SEC_2] = tmp2[0];

        OutBuff[IGS_DISTANCE_INTERVAL_EN]  = (byte) gnss_distance_interval_mode;
        tmp2 = intToBytes2(gnss_distance_interval_val_in_meter);
        OutBuff[IGS_DISTANCE_INTERVAL_IN_M_1] = tmp2[1];
        OutBuff[IGS_DISTANCE_INTERVAL_IN_M_2] = tmp2[0];

        OutBuff[IGS_HEADING_MODE_EN]  = (byte) gnss_heading_mode;
        tmp2 = intToBytes2(gnss_heading_val_in_grad);
        OutBuff[IGS_HEADING_IN_GRAD_1] = tmp2[1];
        OutBuff[IGS_HEADING_IN_GRAD_2] = tmp2[0];

        OutBuff[IGS_MOTION_SENSOR_EN]  = (byte) motion_sensor_en;
        OutBuff[IGS_MOTION_SENSETIVE_LVL]  = (byte) motion_sensetivity_lvl;
        OutBuff[IGS_STATIONARY_DETECT_TIME_IN_SEC]  = (byte) stationary_detect_time_val_in_sec;
        OutBuff[IGS_MOTION_DETCT_TIME_IN_SEC]  = (byte) motion_detect_time_val_in_sec;
        OutBuff[IGS_MASTER_MODE]  = (byte) master_mode;
        OutBuff[IGS_GNSS_POWER_ALWAYS_ON]  = (byte) gnss_always_power_on;

        OutBuff[IGS_DISTANCE_INTERVAL_FILTER_EN]  = (byte) distance_interval_filter;
        tmp2 = intToBytes2(distance_interval_filter_val_in_meter);
        OutBuff[IGS_DISTANCE_INTERVAL_FILTER_IN_M_1] = tmp2[1];
        OutBuff[IGS_DISTANCE_INTERVAL_FILTER_IN_M_2] = tmp2[0];

        OutBuff[IGS_SPEED_FILTER_EN]  = (byte) speed_filter;
        OutBuff[IGS_SPEED_FILTER_VAL_IN_KMH]  = (byte) speed_filter_val_in_km_h;
        OutBuff[IGS_SAVE_FRAME_TO_FLASH_VAL]  = (byte) save_frame_to_flash_every;
        OutBuff[IGS_GNSS_TIMEOUT_EN]  = (byte) gnss_timeout;
        OutBuff[IGS_GNSS_TIMEOUT_IN_SEC]  = (byte) gnss_timeout_val_in_sec;
        OutBuff[IGS_GNSS_ANT_CTRL]  = (byte) gnss_ant_ctrl;

        //BT
        OutBuff[IBTS_BT_WORK_MODE]  = (byte) bt_work_mode;

        tmp2 = intToBytes2(bt_time_interval_val_in_sec);
        OutBuff[IBTS_BT_TIME_INTERVAL_IN_SEC_1] = tmp2[1];
        OutBuff[IBTS_BT_TIME_INTERVAL_IN_SEC_2] = tmp2[0];

        tmp2 = intToBytes2(bt_work_time_val_in_sec);
        OutBuff[IBTS_BT_WORK_TIME_IN_SEC_1] = tmp2[1];
        OutBuff[IBTS_BT_WORK_TIME_IN_SEC_2] = tmp2[0];

        OutBuff[IBTS_ENABLE_BT_ON_STOP]  = (byte) bt_enable_in_stop;
        OutBuff[IBTS_ENABLE_BT_IN_GEOZONE]  = (byte) bt_enable_in_geozone;
        OutBuff[IBTS_ANT_CTRL]  = (byte) bt_ant_ctrl;
        OutBuff[IBTS_PA_LNA_MODE]  = (byte) bt_pa_lna_mode;
        OutBuff[IBTS_HIDDEN_MODE]  = (byte) bt_hidden_mode;

        //EVENT
        OutBuff[IES_DETECT_GNSS_JAMMING]  = (byte) detect_gnss_jamming;
        OutBuff[IES_DETECT_LOW_MEMORY]  = (byte) detect_low_memory;

        //BUTTON / LED
        OutBuff[IBLS_BUTTON_EN]  = (byte) button_en;
        OutBuff[IBLS_LED_WORK_MODE]  = (byte) led_work_mode;

        //LOGGER SETTINGS
        OutBuff[ILS_FLASH_OVERWRITE_EN]  = (byte) flash_overwrite_en;
        OutBuff[ILS_BT_DEBUG_EN]  = (byte) bt_debug_en;
        //ИД
        for(int i=0; i<10; i++){OutBuff[ILS_DEVICE_ID_1 + i] = device_id[i];}

        //Геозоны
        OutBuff[IDX_GEOZONE_ARRAY_VAL] = (byte) geozone_val;

        int geozone_index = IDX_GEOZONE_ARRAY;

        for(int i=0; i<MAX_GEOZONE_VAL; i++)
        {
            OutBuff[geozone_index++] = (byte) geozone_item[i].id;

            tmp2 = intToBytes2(geozone_item[i].radius);
            OutBuff[geozone_index++] = tmp2[1];
            OutBuff[geozone_index++] = tmp2[0];

            tmp4 = float2ByteArray(geozone_item[i].latitude);
            OutBuff[geozone_index++] = tmp4[3];
            OutBuff[geozone_index++] = tmp4[2];
            OutBuff[geozone_index++] = tmp4[1];
            OutBuff[geozone_index++] = tmp4[0];

            tmp4 = float2ByteArray(geozone_item[i].longitude);
            OutBuff[geozone_index++] = tmp4[3];
            OutBuff[geozone_index++] = tmp4[2];
            OutBuff[geozone_index++] = tmp4[1];
            OutBuff[geozone_index++] = tmp4[0];
        }

        //Рассписание
        OutBuff[IDX_SCHEDULE_ARRAY_VAL] = (byte) schedule_val;

        int schedule_index = ISA_ITEM_TYPE;

        for(int i=0; i<SHEDULE_MAX_VAL; i++)
        {
            OutBuff[schedule_index++] = (byte) schedule_item[i].item_type;

            OutBuff[schedule_index++] = (byte) schedule_item[i].ts_year;
            OutBuff[schedule_index++] = (byte) schedule_item[i].ts_mon;
            OutBuff[schedule_index++] = (byte) schedule_item[i].ts_day;

            OutBuff[schedule_index++] = (byte) schedule_item[i].ts_hour;
            OutBuff[schedule_index++] = (byte) schedule_item[i].ts_min;
            OutBuff[schedule_index++] = (byte) schedule_item[i].ts_sec;

            OutBuff[schedule_index++] = (byte) schedule_item[i].te_year;
            OutBuff[schedule_index++] = (byte) schedule_item[i].te_mon;
            OutBuff[schedule_index++] = (byte) schedule_item[i].te_day;

            OutBuff[schedule_index++] = (byte) schedule_item[i].te_hour;
            OutBuff[schedule_index++] = (byte) schedule_item[i].te_min;
            OutBuff[schedule_index++] = (byte) schedule_item[i].te_sec;

        }

        return OutBuff;
    }


    // packing an array of 4 bytes to an int, big endian
    private int BytesToInt4(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }

    // packing an array of 2 bytes to an int, big endian
    private int BytesToInt2(byte[] bytes) {
        return (bytes[0] & 0xFF) << 8 | (bytes[1] & 0xFF);
    }

    private static float byteArrayToFloat(byte[] bytes)
    {
        float f = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        return f;
    }




    private static byte [] long2ByteArray (long value)
    {
        return ByteBuffer.allocate(8).putLong(value).array();
    }

    private static byte [] float2ByteArray (float value)
    {
        return ByteBuffer.allocate(4).putFloat(value).array();
    }

    private static byte[] intToBytes4(final int a) {
        return new byte[] {
                (byte)((a >> 24) & 0xff),
                (byte)((a >> 16) & 0xff),
                (byte)((a >> 8) & 0xff),
                (byte)((a >> 0) & 0xff),
        };
    }

    private static byte[] intToBytes2(final int a) {
        return new byte[] {
                (byte)((a >> 8) & 0xff),
                (byte)((a >> 0) & 0xff),
        };
    }

}
