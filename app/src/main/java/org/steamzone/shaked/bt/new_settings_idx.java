package org.steamzone.shaked.bt;

public class new_settings_idx {
    public final static int SETTINGS_FLASH_SIZE = 230; //Размер настроек

    //IDX
    public final static int IDX_GNSS_SETTINGS = 0 ;  //IGS

    public final static int IGS_TIME_INTERVAL_MODE_EN              = IDX_GNSS_SETTINGS + 0;
    public final static int IGS_TIME_INTERVAL_IN_SEC_1             = IDX_GNSS_SETTINGS + 1;
    public final static int IGS_TIME_INTERVAL_IN_SEC_2             = IDX_GNSS_SETTINGS + 2;
    public final static int IGS_DISTANCE_INTERVAL_EN               = IDX_GNSS_SETTINGS + 3;
    public final static int IGS_DISTANCE_INTERVAL_IN_M_1           = IDX_GNSS_SETTINGS + 4;
    public final static int IGS_DISTANCE_INTERVAL_IN_M_2           = IDX_GNSS_SETTINGS + 5;
    public final static int IGS_HEADING_MODE_EN                    = IDX_GNSS_SETTINGS + 6;
    public final static int IGS_HEADING_IN_GRAD_1                  = IDX_GNSS_SETTINGS + 7;
    public final static int IGS_HEADING_IN_GRAD_2                  = IDX_GNSS_SETTINGS + 8;
    public final static int IGS_MOTION_SENSOR_EN                   = IDX_GNSS_SETTINGS + 9;
    public final static int IGS_MOTION_SENSETIVE_LVL               = IDX_GNSS_SETTINGS + 10;
    public final static int IGS_STATIONARY_DETECT_TIME_IN_SEC      = IDX_GNSS_SETTINGS + 11;
    public final static int IGS_MOTION_DETCT_TIME_IN_SEC           = IDX_GNSS_SETTINGS + 12;
    public final static int IGS_MASTER_MODE                        = IDX_GNSS_SETTINGS + 13;
    public final static int IGS_GNSS_POWER_ALWAYS_ON               = IDX_GNSS_SETTINGS + 14;
    public final static int IGS_DISTANCE_INTERVAL_FILTER_EN        = IDX_GNSS_SETTINGS + 15;
    public final static int IGS_DISTANCE_INTERVAL_FILTER_IN_M_1    = IDX_GNSS_SETTINGS + 16;
    public final static int IGS_DISTANCE_INTERVAL_FILTER_IN_M_2    = IDX_GNSS_SETTINGS + 17;
    public final static int IGS_SPEED_FILTER_EN                    = IDX_GNSS_SETTINGS + 18;
    public final static int IGS_SPEED_FILTER_VAL_IN_KMH            = IDX_GNSS_SETTINGS + 19;
    public final static int IGS_SAVE_FRAME_TO_FLASH_VAL            = IDX_GNSS_SETTINGS + 20;
    public final static int IGS_GNSS_TIMEOUT_EN                    = IDX_GNSS_SETTINGS + 21;
    public final static int IGS_GNSS_TIMEOUT_IN_SEC                = IDX_GNSS_SETTINGS + 22;
    public final static int IGS_GNSS_ANT_CTRL                      = IDX_GNSS_SETTINGS + 23;



    public final static int IDX_BT_SETTINGS = 24;   //IBTS

    public final static int IBTS_BT_WORK_MODE                      = IDX_BT_SETTINGS + 0;
    public final static int IBTS_BT_TIME_INTERVAL_IN_SEC_1         = IDX_BT_SETTINGS + 1;
    public final static int IBTS_BT_TIME_INTERVAL_IN_SEC_2         = IDX_BT_SETTINGS + 2;
    public final static int IBTS_BT_WORK_TIME_IN_SEC_1             = IDX_BT_SETTINGS + 3;
    public final static int IBTS_BT_WORK_TIME_IN_SEC_2             = IDX_BT_SETTINGS + 4;
    public final static int IBTS_ENABLE_BT_ON_STOP                 = IDX_BT_SETTINGS + 5;
    public final static int IBTS_ENABLE_BT_IN_GEOZONE              = IDX_BT_SETTINGS + 6;
    public final static int IBTS_ANT_CTRL                          = IDX_BT_SETTINGS + 7;	//new
    public final static int IBTS_PA_LNA_MODE                       = IDX_BT_SETTINGS + 8;	//new
    public final static int IBTS_HIDDEN_MODE                       = IDX_BT_SETTINGS + 9;	//new



    public final static int IDX_EVENT_SETTINGS = 34;   //IES

    public final static int IES_DETECT_GNSS_JAMMING                = IDX_EVENT_SETTINGS + 0;
    public final static int IES_DETECT_LOW_MEMORY                  = IDX_EVENT_SETTINGS + 1;



    public final static int IDX_BUTTON_LED_SETTINGS = 36 ;  //IBLS

    public final static int IBLS_BUTTON_EN                         = IDX_BUTTON_LED_SETTINGS + 0;
    public final static int IBLS_LED_WORK_MODE                     = IDX_BUTTON_LED_SETTINGS + 1;



    public final static int IDX_LOGGER_SETTINGS = 38;   //ILS

    public final static int ILS_FLASH_OVERWRITE_EN                 = IDX_LOGGER_SETTINGS + 0;
    public final static int ILS_BT_DEBUG_EN                        = IDX_LOGGER_SETTINGS + 1;
    public final static int ILS_DEVICE_ID_1                        = IDX_LOGGER_SETTINGS + 2;
    public final static int ILS_DEVICE_ID_2                        = IDX_LOGGER_SETTINGS + 3;
    public final static int ILS_DEVICE_ID_3                        = IDX_LOGGER_SETTINGS + 4;
    public final static int ILS_DEVICE_ID_4                        = IDX_LOGGER_SETTINGS + 5;
    public final static int ILS_DEVICE_ID_5                        = IDX_LOGGER_SETTINGS + 6;
    public final static int ILS_DEVICE_ID_6                        = IDX_LOGGER_SETTINGS + 7;
    public final static int ILS_DEVICE_ID_7                        = IDX_LOGGER_SETTINGS + 8;
    public final static int ILS_DEVICE_ID_8                        = IDX_LOGGER_SETTINGS + 9;
    public final static int ILS_DEVICE_ID_9                        = IDX_LOGGER_SETTINGS + 10;
    public final static int ILS_DEVICE_ID_10                       = IDX_LOGGER_SETTINGS + 11;



    public final static int IDX_RTC_SETTINGS = 50 ;  	//IRTCS

    public final static int IRTCS_TIMEZONE                         = IDX_RTC_SETTINGS + 0;
    public final static int IRTCS_UPDATE_WITH_GPS                  = IDX_RTC_SETTINGS + 1;


    //Геозоны
    public final static int IDX_GEOZONE_ARRAY_VAL = 52;	 //количесвто активных геозон

    public final static int IDX_GEOZONE_ARRAY = 53;		 //idx начала записии геозон
    public final static int GEOZONE_SIZE = 11;		     //Размер 1 записи геозоны
    public final static int MAX_GEOZONE_VAL = 10;	     //Максимальное количество записей

    //Динамическая часть
    public final static int IGA_ID                                 = IDX_GEOZONE_ARRAY + 0;

    public final static int IGA_RADIUS_1                           = IDX_GEOZONE_ARRAY + 1;
    public final static int IGA_RADIUS_2                           = IDX_GEOZONE_ARRAY + 2;

    public final static int IGA_LAT_1                              = IDX_GEOZONE_ARRAY + 3;
    public final static int IGA_LAT_2                              = IDX_GEOZONE_ARRAY + 4;
    public final static int IGA_LAT_3                              = IDX_GEOZONE_ARRAY + 5;
    public final static int IGA_LAT_4                              = IDX_GEOZONE_ARRAY + 6;

    public final static int IGA_LONG_1                             = IDX_GEOZONE_ARRAY + 7;
    public final static int IGA_LONG_2                             = IDX_GEOZONE_ARRAY + 8;
    public final static int IGA_LONG_3                             = IDX_GEOZONE_ARRAY + 9;
    public final static int IGA_LONG_4                             = IDX_GEOZONE_ARRAY + 10;

    //Рассписание
    public final static int IDX_SCHEDULE_ARRAY = 163;
    public final static int SHEDULE_MAX_VAL = 5;
    public final static int IDX_SCHEDULE_ARRAY_VAL                 = IDX_SCHEDULE_ARRAY + 0;	//Количество рассписаний
    //Динамическая часть
    public final static int ISA_ITEM_TYPE                          = IDX_SCHEDULE_ARRAY + 1;

    public final static int ISA_TS_YEAR                            = IDX_SCHEDULE_ARRAY + 2;
    public final static int ISA_TS_MON                             = IDX_SCHEDULE_ARRAY + 3;
    public final static int ISA_TS_DAY                             = IDX_SCHEDULE_ARRAY + 4;

    public final static int ISA_TS_HOUR                            = IDX_SCHEDULE_ARRAY + 5;
    public final static int ISA_TS_MIN                             = IDX_SCHEDULE_ARRAY + 6;
    public final static int ISA_TS_SEC                             = IDX_SCHEDULE_ARRAY + 7;

    public final static int ISA_TE_YEAR                            = IDX_SCHEDULE_ARRAY + 8;
    public final static int ISA_TE_MON	                           = IDX_SCHEDULE_ARRAY + 9;
    public final static int ISA_TE_DAY	                           = IDX_SCHEDULE_ARRAY + 10;

    public final static int ISA_TE_HOUR                            = IDX_SCHEDULE_ARRAY + 11;
    public final static int ISA_TE_MIN	                           = IDX_SCHEDULE_ARRAY + 12;
    public final static int ISA_TE_SEC                             = IDX_SCHEDULE_ARRAY + 13;

    //
    public final static int IDX_FLASH_SETTINGS_CRC_A = 228;
    public final static int IDX_FLASH_SETTINGS_CRC_B = 229;
}
