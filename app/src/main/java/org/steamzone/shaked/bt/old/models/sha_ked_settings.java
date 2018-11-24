package org.steamzone.shaked.bt.old.models;

public class sha_ked_settings {
    //Const
    //settings IDX
    private final static int   IDX_FS_GPS_WORK_MODE_TYPE = 0;		        //Work Mode
    private final static int   IDX_FS_PERIOD_BEETWEN_GPS_CYCLE_0 = 1;		//X time
    private final static int   IDX_FS_PERIOD_BEETWEN_GPS_CYCLE_1 = 2;		//X time
    private final static int   IDX_FS_GPS_WRITE_VALL = 3;                   //Frame val
    private final static int   IDX_FS_GPS_TIMEOUT_0 = 4;
    private final static int   IDX_FS_GPS_TIMEOUT_1 = 5;
    //Ext power trigger
    private final static int   IDX_FS_EXT_POWER_TRIGGER = 6;
    //Motion trigger settings
    private final static int   IDX_FS_MOTION_TRIGGER = 7;
    private final static int   IDX_FS_MOTION_TIME_IMP_1 = 8;
    private final static int   IDX_FS_MOTION_TIME_IMP_2 = 9;
    private final static int   IDX_FS_MOTION_TIME_DETECT_1 = 10;
    private final static int   IDX_FS_MOTION_TIME_DETECT_2 = 11;
    private final static int   IDX_FS_IMP_MOTION_DETECT = 12;
    private final static int   IDX_FS_MIN_IMP_MOTION_DETECT = 13;
    //RTC Settings
    private final static int   IDX_FS_RTC_AUTO_UPD_FLAG = 14;
    //BT Settings
    private final static int   IDX_FS_BT_WORK_MODE = 15;
    private final static int   IDX_FS_BT_TIME_EVERY_X_1 = 16;
    private final static int   IDX_FS_BT_TIME_EVERY_X_2 = 17;
    private final static int   IDX_FS_BT_TIME_BT_ACTIVITY_1 = 18;
    private final static int   IDX_FS_BT_TIME_BT_ACTIVITY_2 = 19;

    private final static int   LOGGER_SETTINGS_BT_BUFF_LEN = 20;


    //Variable
    //GPS Settings
    public int work_mode_type;   					//Тип работы и сохранения данных во флеш
    //Настройки GPS
    public int 	period_between_gps_cycle;	        //Через сколько запускать следующий цикл сканирования GPS в секундах
    public int 	GPS_WriteWall;						//Количество записей за 1 цикл включения. После превышения этого значения выключить питание GNSS
    public int 	GPS_TimeOut;				  		//Таймаут на отключение модуля //если долго не может найти спутники
    //Ext power trigger settings
    public int ext_power_triger;					//Событие при подключении внешнего питания // если стоит флаг то пишем всегда
    //Motion trigger settings
    public int motion_trigger;						//Событие от датчика движения
    //Настройки датчика движения
    public int time_imp;							//Минимальное время импульса
    public int time_detector;						//Время детектирования
    public int imp_motion_detect;					//Количество импульсов для обнаружения движения
    public int min_imp_motion_detect;			    //Минимальное количесвто импульсов (если счетчик не превышает это значение = нет движения
    //RTC settings
    public int rtc_auto_correct_witch_gps;
    //BT Settings
    public int bt_work_mode;						//Тип включения BT
    //Настройки BT
    public int time_every_x;				        //Время Х (включать каждые Х секунд)
    public int time_bt_activity;		            //Время, сколько слушать эфир до того как уйти в сон






    public sha_ked_settings()
    {

    }

    //FIXME добавить проверку на длину и возвращать enum если ошибка || возвращать null  в случае ошибки а возвращать item данных gps
    public void parse_data (byte[] data_src) {
        byte[] tmp = new byte[2];

        work_mode_type = data_src[IDX_FS_GPS_WORK_MODE_TYPE];

        tmp[0] = data_src[IDX_FS_PERIOD_BEETWEN_GPS_CYCLE_0];
        tmp[1] = data_src[IDX_FS_PERIOD_BEETWEN_GPS_CYCLE_1];
        period_between_gps_cycle = BytesToInt2(tmp);

        GPS_WriteWall = data_src[IDX_FS_GPS_WRITE_VALL];

        tmp[0] = data_src[IDX_FS_GPS_TIMEOUT_0];
        tmp[1] = data_src[IDX_FS_GPS_TIMEOUT_1];
        GPS_TimeOut = BytesToInt2(tmp);

        ext_power_triger = data_src[IDX_FS_EXT_POWER_TRIGGER];

        motion_trigger = data_src[IDX_FS_MOTION_TRIGGER];

        tmp[0] =  data_src[IDX_FS_MOTION_TIME_IMP_1];
        tmp[1] =  data_src[IDX_FS_MOTION_TIME_IMP_2];
        time_imp = BytesToInt2(tmp);

        tmp[0] = data_src[IDX_FS_MOTION_TIME_DETECT_1];
        tmp[1] = data_src[IDX_FS_MOTION_TIME_DETECT_2];
        time_detector = BytesToInt2(tmp);

        imp_motion_detect= data_src[IDX_FS_IMP_MOTION_DETECT];

        min_imp_motion_detect = data_src[IDX_FS_MIN_IMP_MOTION_DETECT];

        rtc_auto_correct_witch_gps = data_src[IDX_FS_RTC_AUTO_UPD_FLAG];

        bt_work_mode = data_src[IDX_FS_BT_WORK_MODE];

        tmp[0] = data_src[IDX_FS_BT_TIME_EVERY_X_1];
        tmp[1] = data_src[IDX_FS_BT_TIME_EVERY_X_2];
        time_every_x = BytesToInt2(tmp);

        tmp[0] = data_src[IDX_FS_BT_TIME_BT_ACTIVITY_1];
        tmp[1] = data_src[IDX_FS_BT_TIME_BT_ACTIVITY_2];
        time_bt_activity = BytesToInt2(tmp);
    }


    public byte[] create_settings_bt_buff ()
    {
        byte[] OutBuff = new byte[LOGGER_SETTINGS_BT_BUFF_LEN];
        byte[] tmp = new byte[2];

        OutBuff[IDX_FS_GPS_WORK_MODE_TYPE] = (byte) work_mode_type;

        tmp = intToBytes2(period_between_gps_cycle);
        OutBuff[IDX_FS_PERIOD_BEETWEN_GPS_CYCLE_0] = tmp[0];
        OutBuff[IDX_FS_PERIOD_BEETWEN_GPS_CYCLE_1] = tmp[1];

        OutBuff[IDX_FS_GPS_WRITE_VALL] = (byte) GPS_WriteWall;

        tmp = intToBytes2(GPS_TimeOut);
        OutBuff[IDX_FS_GPS_TIMEOUT_0] = tmp[0];
        OutBuff[IDX_FS_GPS_TIMEOUT_1] = tmp[1];

        OutBuff[IDX_FS_EXT_POWER_TRIGGER] = (byte) ext_power_triger;

        OutBuff[IDX_FS_MOTION_TRIGGER] = (byte) motion_trigger;

        tmp = intToBytes2(time_imp);
        OutBuff[IDX_FS_MOTION_TIME_IMP_1] = tmp[0];
        OutBuff[IDX_FS_MOTION_TIME_IMP_2] = tmp[1];

        tmp = intToBytes2(time_detector);
        OutBuff[IDX_FS_MOTION_TIME_DETECT_1] = tmp[0];
        OutBuff[IDX_FS_MOTION_TIME_DETECT_2] = tmp[1];

        OutBuff[IDX_FS_IMP_MOTION_DETECT] = (byte) imp_motion_detect;

        OutBuff[IDX_FS_MIN_IMP_MOTION_DETECT] = (byte) min_imp_motion_detect;

        OutBuff[IDX_FS_RTC_AUTO_UPD_FLAG] = (byte) rtc_auto_correct_witch_gps;

        OutBuff[IDX_FS_BT_WORK_MODE] = (byte) bt_work_mode;

        tmp = intToBytes2(time_every_x);
        OutBuff[IDX_FS_BT_TIME_EVERY_X_1] = tmp[0];
        OutBuff[IDX_FS_BT_TIME_EVERY_X_2] = tmp[1];

        tmp = intToBytes2(time_bt_activity);
        OutBuff[IDX_FS_BT_TIME_BT_ACTIVITY_1] = tmp[0];
        OutBuff[IDX_FS_BT_TIME_BT_ACTIVITY_2] = tmp[1];

        return OutBuff;
    }


    public void set_v_work_mode_type(int work_mode_type)
    {
        this.work_mode_type = work_mode_type;
    }

    public void set_v_bt_work_mode(int bt_work_mode)
    {
        this.bt_work_mode = bt_work_mode;
    }

    public void set_ext_power_triger_type(int ext_power_triger)
    {
        this.ext_power_triger = ext_power_triger;
    }

    public void set_motion_trigger_type(int motion_trigger)
    {
        this.motion_trigger = motion_trigger;
    }



    // packing an array of 4 bytes to an int, big endian
    private int BytesToInt4(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }

    // packing an array of 2 bytes to an int, big endian
    private int BytesToInt2(byte[] bytes) {
        return (bytes[0] & 0xFF) << 8 | (bytes[1] & 0xFF);
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
