package org.steamzone.shaked.bt.old.models;

import android.bluetooth.BluetoothDevice;

public class cus_search_item
{
    //FIXME: Переписать класс! наследовать от mydev

    public BluetoothDevice bleDev;
    public int RSSI;
    public int coonected;
    public double distance;
    public boolean allow_add;
    public int dev_type;


    public cus_search_item(BluetoothDevice bleDev, int RSSI, double distance, boolean allow_add, int dev_type)
    {
        this.bleDev = bleDev;
        this.RSSI = RSSI;
        this.distance = distance;
        this.allow_add = allow_add;
        this.dev_type = dev_type;
    }
}