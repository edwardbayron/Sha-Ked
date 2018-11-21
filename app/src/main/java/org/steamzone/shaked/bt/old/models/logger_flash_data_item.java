package org.steamzone.shaked.bt.old.models;

import java.text.DecimalFormat;

public class logger_flash_data_item {

    //sys info
    private String sysinfo; //"id:sector_select:frame_id_in_sector"
    private int pkt_type;

    private String timestamp; //2018-07-12T13:24:34.497Z


    private double  gnss_latitude;
    private double  gnss_longitude;

    private double gnss_speed;
    private double gnss_angle;

    private boolean gnss_valid_data;
    private int gnss_fix_type;
    private int gnss_satellites;
    private int gnss_hdop;

    private int battery_level;

    private int trigger_ext_power;
    private int trigger_motion;

    public logger_flash_data_item(){}

    public void logger_flash_data_item_add(String sysinfo, int pkt_type, String timestamp,
                                           double  gnss_latitude, double  gnss_longitude, double gnss_speed, double gnss_angle,
                                           boolean gnss_valid_data, int gnss_fix_type, int gnss_satellites, int gnss_hdop,
                                           int battery_level, int trigger_ext_power, int trigger_motion){

        this.sysinfo = sysinfo;
        this.pkt_type = pkt_type;

        this.timestamp = timestamp;

        this.gnss_latitude = gnss_latitude;
        this.gnss_longitude = gnss_longitude;

        this.gnss_speed = gnss_speed;
        this.gnss_angle = gnss_angle;

        this.gnss_valid_data = gnss_valid_data;
        this.gnss_fix_type = gnss_fix_type;
        this.gnss_satellites = gnss_satellites;
        this.gnss_hdop = gnss_hdop;

        this.battery_level = battery_level;

        this.trigger_ext_power = trigger_ext_power;
        this.trigger_motion = trigger_motion;
    }


    public void sysinfo_set(int id, int sector_select, int frame_id_in_sector){
        this.sysinfo = new  String("" + id + "-" + "" + sector_select + "-" + "" + frame_id_in_sector);
    }
    public void sysinfo_set(String sysinfo){this.sysinfo = sysinfo;}

    public void pkt_type_set(int pkt_type){this.pkt_type = pkt_type;}

    public void timestamp_set(String timestamp){this.timestamp = timestamp;}

    public void timestamp_set(int year, int month, int day, int hours, int minutes, int seconds){
        DecimalFormat isoDate = new DecimalFormat("00");
        this.timestamp = new  String("20" + year + "-" + isoDate.format(month) + "-" + isoDate.format(day) + "T" + isoDate.format(hours) + ":" + isoDate.format(minutes) + ":" + isoDate.format(seconds) + ".000Z");
    }

    public void gnss_latitude_set(double gnss_latitude){this.gnss_latitude = gnss_latitude;}

    public void gnss_longitude_set(double  gnss_longitude){this.gnss_longitude = gnss_longitude;}

    public void gnss_speed_set(double gnss_speed){this.gnss_speed = gnss_speed;}

    public void gnss_angle_set(double gnss_angle){this.gnss_angle = gnss_angle;}

    public void gnss_valid_data_set(boolean gnss_valid_data){this.gnss_valid_data = gnss_valid_data;}

    public void gnss_fix_type_set(int gnss_fix_type){this.gnss_fix_type = gnss_fix_type;}

    public void gnss_satellites_set(int gnss_satellites){this.gnss_satellites = gnss_satellites;}

    public void gnss_hdop_set(int gnss_hdop){this.gnss_hdop = gnss_hdop;}


    public void battery_level_set(int battery_level){this.battery_level = battery_level;}

    public void trigger_ext_power_set(int trigger_ext_power){this.trigger_ext_power = trigger_ext_power;}

    public void trigger_motion_set(int trigger_motion){this.trigger_motion = trigger_motion;}


}




