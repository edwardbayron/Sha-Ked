package org.steamzone.shaked.bt;

public class new_log_item_idx {

    //BT info buff len
    public final static int BT_INFO_BUFF_LEN        = 10;			//Размер шапки BT буффера

    //FRAME IDX (EFI)
    public final static int  FI_PCK_TYPE            = 0 + BT_INFO_BUFF_LEN;			//Тип EVENT'A

    public final static int  FI_DATA_DAY            = 1 + BT_INFO_BUFF_LEN;
    public final static int  FI_DATA_MONTH          = 2 + BT_INFO_BUFF_LEN;
    public final static int  FI_DATA_YEAR           = 3 + BT_INFO_BUFF_LEN;

    public final static int  FI_TIME_SECONDS        = 4 + BT_INFO_BUFF_LEN;
    public final static int  FI_TIME_MINUTES        = 5 + BT_INFO_BUFF_LEN;
    public final static int  FI_TIME_HOURS          = 6 + BT_INFO_BUFF_LEN;

    public final static int  FI_LATITUDE_1          = 7 + BT_INFO_BUFF_LEN;
    public final static int  FI_LATITUDE_2          = 8 + BT_INFO_BUFF_LEN;
    public final static int  FI_LATITUDE_3          = 9 + BT_INFO_BUFF_LEN;
    public final static int  FI_LATITUDE_4          = 10 + BT_INFO_BUFF_LEN;

    public final static int  FI_LONGITUDE_1         = 11 + BT_INFO_BUFF_LEN;
    public final static int  FI_LONGITUDE_2         = 12 + BT_INFO_BUFF_LEN;
    public final static int  FI_LONGITUDE_3         = 13 + BT_INFO_BUFF_LEN;
    public final static int  FI_LONGITUDE_4         = 14 + BT_INFO_BUFF_LEN;

    public final static int  FI_SPEED_1             = 15 + BT_INFO_BUFF_LEN;
    public final static int  FI_SPEED_2             = 16 + BT_INFO_BUFF_LEN;

    public final static int  FI_COURSE_1            = 17 + BT_INFO_BUFF_LEN;
    public final static int  FI_COURSE_2            = 18 + BT_INFO_BUFF_LEN;

    public final static int  FI_SATELLITES          = 19 + BT_INFO_BUFF_LEN;
    public final static int  FI_FIX_TYPE            = 20 + BT_INFO_BUFF_LEN;
    public final static int  FI_HDOP                = 21 + BT_INFO_BUFF_LEN;

    public final static int  FI_BATTERY_VOLTAGE1    = 22 + BT_INFO_BUFF_LEN;
    public final static int  FI_BATTERY_VOLTAGE2    = 23 + BT_INFO_BUFF_LEN;

    public final static int  FI_EXT_POWER_STATUS    = 24 + BT_INFO_BUFF_LEN;
    public final static int  FI_CH_POWER_STATUS     = 25 + BT_INFO_BUFF_LEN;

    public final static int  FI_MOTION_STATUS       = 26 + BT_INFO_BUFF_LEN;
}
