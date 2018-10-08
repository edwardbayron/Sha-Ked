package org.steamzone.shaked.bt.models;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class gps_data_frame {
    //FIXME: РАзделить на класс парсера и на item данных sha-ked data item

    //ONLINE SERVICE LOGGER IDX
    private final static int   IDX_OSL_LOGGER_SERVICE_STATUS = 0;	    //Состояние сервиса логгера
    private final static int   IDX_OSL_BT_SERVICE_STATUS = 1;			//Состояние сервиса БТ

    private final static int   IDX_OSL_GPS_WORK_MODE = 2;		        //Режим работы основного режима
    private final static int   IDX_OSL_EXT_POWER_TRIGGER_MODE = 3;		//Режим работы триггера внешнего питания
    private final static int   IDX_OSL_MOTION_TRIGGER_MODE = 4; 		//Режим работы триггира движения
    private final static int   IDX_OSL_BT_MODE = 5;			            //Режим работы БТ сервиса

    private final static int   IDX_OSL_GPS_POWER = 6;			//Питание GPS модуля
    private final static int   IDX_OSL_BUTTON_TRIGGER = 7;		//последнее значение call back с кнопки (пока подключение активно она не выполняет действия)

    //RTC																						//Значения RTC
    private final static int   IDX_OSL_RTC_DATA_YEAR = 8;
    private final static int   IDX_OSL_RTC_DATA_MONTH = 9;
    private final static int   IDX_OSL_RTC_DATA_DAY = 10;
    private final static int   IDX_OSL_RTC_TIME_HOURS = 11;
    private final static int   IDX_OSL_RTC_TIME_MINUTES = 12;
    private final static int   IDX_OSL_RTC_TIME_SECONDS = 13;

    //gnss data
    private final static int   IDX_OSL_PCK_TYPE	= 14;
    private final static int   IDX_OSL_DATA_DAY = 15;
    private final static int   IDX_OSL_DATA_MONTH = 16;
    private final static int   IDX_OSL_DATA_YEAR = 17;
    private final static int   IDX_OSL_TIME_HOURS = 18;
    private final static int   IDX_OSL_TIME_MINUTES = 19;
    private final static int   IDX_OSL_TIME_SECONDS = 20;
    private final static int   IDX_OSL_LATITUDE_VAL1 = 21;
    private final static int   IDX_OSL_LATITUDE_VAL2 = 22;
    private final static int   IDX_OSL_LATITUDE_VAL3 = 23;
    private final static int   IDX_OSL_LATITUDE_VAL4 = 24;
    private final static int   IDX_OSL_LATITUDE_SCALE1 = 25;
    private final static int   IDX_OSL_LATITUDE_SCALE2 = 26;
    private final static int   IDX_OSL_LATITUDE_SCALE3 = 27;
    private final static int   IDX_OSL_LATITUDE_SCALE4 = 28;
    private final static int   IDX_OSL_LONGITUDE_VAL1 = 29;
    private final static int   IDX_OSL_LONGITUDE_VAL2 = 30;
    private final static int   IDX_OSL_LONGITUDE_VAL3 = 31;
    private final static int   IDX_OSL_LONGITUDE_VAL4 = 32;
    private final static int   IDX_OSL_LONGITUDE_SCALE1 = 33;
    private final static int   IDX_OSL_LONGITUDE_SCALE2 = 34;
    private final static int   IDX_OSL_LONGITUDE_SCALE3 = 35;
    private final static int   IDX_OSL_LONGITUDE_SCALE4 = 36;
    private final static int   IDX_OSL_SPEED_VAL1 = 37;
    private final static int   IDX_OSL_SPEED_VAL2 = 38;
    private final static int   IDX_OSL_ANGLE_VAL1 = 39;
    private final static int   IDX_OSL_ANGLE_VAL2 = 40;
    private final static int   IDX_OSL_DATA_VALID = 41;
    private final static int   IDX_OSL_GNSS_FIX_TYPE = 42;
    private final static int   IDX_OSL_GNSS_SATELLITES = 43;
    private final static int   IDX_OSL_GNSS_HDOP = 44;

    private final static int   IDX_OSL_BATTERY_VAL = 45;			//Уровень АКБ

    private final static int   IDX_OSL_TRIGGER_EXT_POW = 46;			//Значение триггера внешнего питания
    private final static int   IDX_OSL_TRIGGER_MOION = 47;			//Значение триггера движения

    private final static int   ONLINE_SERVICE_LOGGER_BUFF_LEN = 48;


    //services info
    public int logger_service_status;
    public int bt_service_status;
    //work mode
    public int gps_work_mode;
    public int ext_power_trigger_mode;
    public int motion_trigger_mode;
    public int bt_mode;
    //Status info
    public int gps_power;
    public int button_last_trig;
    //rtc
    public int rtc_data_year;
    public int rtc_data_month;
    public int rtc_data_day;
    public int rtc_time_hours;
    public int rtc_time_minutes;
    public int rtc_time_seconds;
    //pck type
    public int last_pck_type;
    //date
    public int day;
    public int month;
    public int year;
    //time
    public int hours;
    public int minutes;
    public int seconds;
    //latitude
    public int latitude_value;
    public int latitude_scale;
    public double  latitude;
    //longitude
    public int longitude_value;
    public int longitude_scale;
    public double  longitude;

    public double speed;
    public double angle;

    public int valid_data;
    public int gnss_fix_type;
    public int gnss_satellites;
    public int gnss_hdop;

    //null
    public int battery_level;
    public String device_id;
    public int fix_quality;


    public int trigger_ext_power;
    public int trigger_motion;

    public String motion_trigger_status_inString;

    public gps_data_frame()
    {

    }


    //FIXME добавить проверку на длину и возвращать enum если ошибка || возвращать null  в случае ошибки а возвращать item данных gps
    public void parse_data (byte[] data_src) {
        byte[] tmp = new byte[4];
        byte[] tmp2 = new byte[2];

        //services info
        logger_service_status = data_src[IDX_OSL_LOGGER_SERVICE_STATUS];
        bt_service_status = data_src[IDX_OSL_BT_SERVICE_STATUS];
        //work mode
        gps_work_mode = data_src[IDX_OSL_GPS_WORK_MODE];
        ext_power_trigger_mode = data_src[IDX_OSL_EXT_POWER_TRIGGER_MODE];
        motion_trigger_mode = data_src[IDX_OSL_MOTION_TRIGGER_MODE];
        bt_mode = data_src[IDX_OSL_BT_MODE];
        //Status info
        gps_power = data_src[IDX_OSL_GPS_POWER];
        button_last_trig = data_src[IDX_OSL_BUTTON_TRIGGER];
        //rtc
        rtc_data_year = data_src[IDX_OSL_RTC_DATA_YEAR];
        rtc_data_month = data_src[IDX_OSL_RTC_DATA_MONTH];
        rtc_data_day = data_src[IDX_OSL_RTC_DATA_DAY];
        rtc_time_hours = data_src[IDX_OSL_RTC_TIME_HOURS];
        rtc_time_minutes = data_src[IDX_OSL_RTC_TIME_MINUTES];
        rtc_time_seconds = data_src[IDX_OSL_RTC_TIME_SECONDS];
        //pck type
        last_pck_type = data_src[IDX_OSL_PCK_TYPE];
        //gnss data
        day   = data_src[IDX_OSL_DATA_DAY];
        month = data_src[IDX_OSL_DATA_MONTH];
        year  = data_src[IDX_OSL_DATA_YEAR];

        hours   = data_src[IDX_OSL_TIME_HOURS];
        minutes = data_src[IDX_OSL_TIME_MINUTES];
        seconds = data_src[IDX_OSL_TIME_SECONDS];

        tmp[0] = data_src[IDX_OSL_LATITUDE_VAL1];
        tmp[1] = data_src[IDX_OSL_LATITUDE_VAL2];
        tmp[2] = data_src[IDX_OSL_LATITUDE_VAL3];
        tmp[3] = data_src[IDX_OSL_LATITUDE_VAL4];
        latitude_value = fromByteArray(tmp);
        tmp[0] = data_src[IDX_OSL_LATITUDE_SCALE1];
        tmp[1] = data_src[IDX_OSL_LATITUDE_SCALE2];
        tmp[2] = data_src[IDX_OSL_LATITUDE_SCALE3];
        tmp[3] = data_src[IDX_OSL_LATITUDE_SCALE4];
        latitude_scale = fromByteArray(tmp);
        latitude = (double )latitude_value / (double ) latitude_scale;

        tmp[0] = data_src[IDX_OSL_LONGITUDE_VAL1];
        tmp[1] = data_src[IDX_OSL_LONGITUDE_VAL2];
        tmp[2] = data_src[IDX_OSL_LONGITUDE_VAL3];
        tmp[3] = data_src[IDX_OSL_LONGITUDE_VAL4];
        longitude_value = fromByteArray(tmp);
        tmp[0] = data_src[IDX_OSL_LONGITUDE_SCALE1];
        tmp[1] = data_src[IDX_OSL_LONGITUDE_SCALE2];
        tmp[2] = data_src[IDX_OSL_LONGITUDE_SCALE3];
        tmp[3] = data_src[IDX_OSL_LONGITUDE_SCALE4];
        longitude_scale = fromByteArray(tmp);
        longitude = (double )longitude_value / (double ) longitude_scale;

        tmp2[0] = data_src[IDX_OSL_SPEED_VAL1];
        tmp2[1] = data_src[IDX_OSL_SPEED_VAL2];
        speed = BytesToInt2(tmp2);

        tmp2[0] = data_src[IDX_OSL_ANGLE_VAL1];
        tmp2[1] = data_src[IDX_OSL_ANGLE_VAL2];
        angle = BytesToInt2(tmp2);

        valid_data      = data_src[IDX_OSL_DATA_VALID];
        gnss_fix_type   = data_src[IDX_OSL_GNSS_FIX_TYPE];
        gnss_satellites = data_src[IDX_OSL_GNSS_SATELLITES];
        gnss_hdop       = data_src[IDX_OSL_GNSS_HDOP];

        trigger_ext_power = data_src[IDX_OSL_TRIGGER_EXT_POW];
        trigger_motion = data_src[IDX_OSL_TRIGGER_MOION];

        device_id = "Gedalij_GPS";

        switch(trigger_motion)
        {
            case 0://Нет обнаружения
                motion_trigger_status_inString = "MOTION_NA";
                break;
            case 1://Обнаружено начало движения
                motion_trigger_status_inString = "MOTION_START";
                break;
            case 2://Обнаружена остановка
                motion_trigger_status_inString = "MOTION_STOP";
                break;
            case 3://Продолжение движения
                motion_trigger_status_inString = "MOTION_ACTIVE";
                break;
            case 4://Продолжение остановки
                motion_trigger_status_inString = "MOTION_STOPED";
                break;
        }
    }

    public int get_battery_Level(){return battery_level;}
    public String get_device_id(){return device_id;}
    public int get_fix_quality(){return fix_quality;}
    public double get_latitude(){return latitude;}
    public double get_longitude(){return longitude;}
    public int get_satellites_tracked(){return gnss_satellites;}
    public String get_timestamp()
    {//timestamp : 2018-06-27T17-33-33+03:00 // String representation of data in ISO 8601 format
        DecimalFormat isoDate = new DecimalFormat("00");
        String out = new  String("20" + year + "-" + isoDate.format(month) + "-" + isoDate.format(day) + "T" + isoDate.format(hours) + ":" + isoDate.format(minutes) + ":" + isoDate.format(seconds) + ".000Z");
        return out;
    }
    public boolean get_valid_data(){if (valid_data == 1)return true; else return false;}

    public String get_str_json()
    {
        final char dm = (char) 34;

        DecimalFormatSymbols dfs = new DecimalFormatSymbols(Locale.US);
        DecimalFormat ld = new DecimalFormat("00.00000", dfs);
        String strJson =
                "{"
                +dm+"timestamp"+dm + ":" + dm+get_timestamp()+dm + ","
                +dm+"latitude"+dm + ":" + ld.format(latitude) + ","
                +dm+"longitude"+dm + ":" + ld.format(longitude) + ","
                +dm+"valid"+dm +  ":" + get_valid_data() + ","
                +dm+"fixQuality"+dm + ":" + fix_quality + ","
                +dm+"satellitesTracked"+dm + ":" + gnss_satellites + ","
                +dm+"positioningAccuracy"+dm + ":" + gnss_hdop + ","
                +dm+"batteryLevel"+dm + ":" + battery_level + ","
                +dm+"deviceId"+dm + ":" + dm+device_id+dm
                +"}";

        return strJson;
    }



    // packing an array of 4 bytes to an int, big endian
    private int fromByteArray(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }

    // packing an array of 2 bytes to an int, big endian
    private int BytesToInt2(byte[] bytes) {
        return (bytes[0] & 0xFF) << 8 | (bytes[1] & 0xFF);
    }
}
