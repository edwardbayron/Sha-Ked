package org.steamzone.shaked.bt.models;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;

public class cus_mydev_item implements Parcelable
{
    public enum devType
    {
        shaKedTypeDev
    }

    public BluetoothDevice bleDev;  //BT Структура устройства
    public int RSSI;                //Последний принятый RSSI
    public double distance;         //Последнее измерение рссии и из него высчитанное расстояние
    public devType dev_type;        //Тип устройства
    public boolean coonected;           //Статус подключения к устройству
    public boolean listUpdateFlag;  //Флаг обновления данных
    public boolean rssi_valid;


    public cus_mydev_item(BluetoothDevice bleDev, int RSSI, double distance, devType dev_type)
    {
        this.bleDev = bleDev;
        this.RSSI = RSSI;
        this.distance = distance;
        this.dev_type = dev_type;
    }

    public cus_mydev_item(cus_mydev_item item)
    {
        this.bleDev = item.bleDev;
        this.RSSI = item.RSSI;
        this.distance = item.distance;
        this.dev_type = item.dev_type;
        this.coonected = item.coonected;
        this.listUpdateFlag = item.listUpdateFlag;
        this.rssi_valid = item.rssi_valid;
    }

    // конструктор, считывающий данные из Parcel
    private cus_mydev_item(Parcel parcel) {

        bleDev = (BluetoothDevice) parcel.readValue( null);
        RSSI = parcel.readInt();
        //coonected = parcel.readInt();
        distance = parcel.readDouble();
        dev_type = (devType) parcel.readValue( null);
    }

    // упаковываем объект в Parcel
    public void writeToParcel(Parcel parcel, int flags) {

        parcel.writeValue(bleDev);
        parcel.writeInt(RSSI);
        //parcel.writeInt(coonected);
        parcel.writeDouble(distance);
        parcel.writeValue(dev_type);
    }

    public static final Creator<cus_mydev_item> CREATOR = new Creator<cus_mydev_item>() {
        // распаковываем объект из Parcel
        public cus_mydev_item createFromParcel(Parcel in) {
            return new cus_mydev_item(in);
        }

        public cus_mydev_item[] newArray(int size) {
            return new cus_mydev_item[size];
        }
    };

    //return hashcode of object
    public int describeContents() {
        return hashCode();
    }

    public void set_update_flag(boolean flag){listUpdateFlag = flag;}
    public boolean get_update_flag(){return listUpdateFlag;}

    public void set_rsii_valid (boolean valid){rssi_valid = valid;}
    public boolean get_rsii_valid(){return rssi_valid;}

    public void setConnect(boolean coonected)
    {
        this.coonected = coonected;
    }

    public boolean getConnect()
    {
        return coonected;
    }

    public void update_item(cus_mydev_item item)
    {
        this.bleDev = item.bleDev;
        this.RSSI = item.RSSI;
        this.distance = item.distance;
        this.dev_type = item.dev_type;
       // this.coonected = item.coonected;
        this.listUpdateFlag = item.listUpdateFlag;
        this.rssi_valid = item.rssi_valid;
    }
}