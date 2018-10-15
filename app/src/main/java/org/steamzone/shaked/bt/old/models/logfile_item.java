package org.steamzone.shaked.bt.old.models;

import java.util.ArrayList;

public class logfile_item {

    private ArrayList<logger_flash_data_item> logger_flash_data_item;
    private int log_size;
    private String device_id;

    public logfile_item(){}

    void set_data_item_array (ArrayList<logger_flash_data_item> logger_flash_data_item) {
        this.logger_flash_data_item = logger_flash_data_item;
        this.log_size = logger_flash_data_item.size();
    }

    void set_device_id(String device_id)
    {
        this.device_id = device_id;
    }
}
