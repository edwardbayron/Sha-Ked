package org.steamzone.shaked.bt;

import java.text.DecimalFormat;

public class new_log_item_data_struct {

    //data struct
    //sys info
    private String sysinfo; //"sector_select_id:frame_vall_in_sector:bt_pkt_vall:bt_pkt_id:frame_in_bt_pkt_id"

    private int pkt_type;

    private String timestamp; //2018-07-12T13:24:34.497Z

    private float  latitude;
    private float  longitude;

    private int speed;
    private int angle;

    private int satellites;
    private int fix_type;
    private int hdop;

    private int battery_voltage;

    private int ext_power;
    private int ch_power;

    private int motion_status;


    public new_log_item_data_struct(){}

    //Функция формирования слежебной информации
    public void sysinfo_set(int sector_select_id, int frame_vall_in_sector, int bt_pkt_vall, int bt_pkt_id, int frame_in_bt_pkt_id){
        this.sysinfo = new  String(""
                                                + sector_select_id                  //ИД сектора из которго идет загрузка
                                                + ":" +  frame_vall_in_sector       //Количество фреймов в этом секторе
                                                + ":" +  bt_pkt_vall                //Количество БТ пакетов для передачи информации из сектора
                                                + ":" +  bt_pkt_id                  //ID BT пакета
                                                + ":" +  frame_in_bt_pkt_id);       //ID фрейма в BT в пакете

    }

    public void pkt_type_set(int pkt_type){this.pkt_type = pkt_type;}

    public void timestamp_set(int year, int month, int day, int hours, int minutes, int seconds){
        DecimalFormat isoDate = new DecimalFormat("00");
        this.timestamp = new  String("20" + year + "-" + isoDate.format(month) + "-" + isoDate.format(day) + "T" + isoDate.format(hours) + ":" + isoDate.format(minutes) + ":" + isoDate.format(seconds) + ".000Z");
    }

    public void latitude_set(float  latitude){this.latitude = latitude;}

    public void longitude_set(float  longitude){this.longitude = longitude;}

    public void speed_set(int speed){this.speed = speed;}

    public void angle_set(int angle){this.angle = angle;}

    public void satellites_set(int satellites){this.satellites = satellites;}

    public void fix_type_set(int fix_type){this.fix_type = fix_type;}

    public void hdop_set(int hdop){this.hdop = hdop;}

    public void battery_voltage_set(int battery_voltage){this.battery_voltage = battery_voltage;}

    public void ext_power_set(int ext_power){this.ext_power = ext_power;}

    public void ch_power_set(int ch_power){this.ch_power = ch_power;}

    public void motion_status_set(int motion_status){this.motion_status = motion_status;}


}
