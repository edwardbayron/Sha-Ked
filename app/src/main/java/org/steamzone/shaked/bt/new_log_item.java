package org.steamzone.shaked.bt;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class new_log_item {

    //IDX BT_SEND_BUFF

    public final static int FRAME_SIZE = 27;
    public final static int FRAME_VALL_IN_LOCAL_BUFF = 151;

    public final static int IDX_SECTOR_ID_1 = 0;	//ID Сектора из которога сейчас идет отправка
    public final static int IDX_SECTOR_ID_2 = 1;
    public final static int IDX_SECTOR_ID_3 = 2;
    public final static int IDX_SECTOR_ID_4 = 3;

    public final static int IDX_FRAME_IN_SECTOR = 4;	//Количество фреймов в секторе
    public final static int IDX_FRAME_IN_BT_BUFF = 5;	//Количество фреймов в буффере

    public final static int IDX_BT_PCK_VAL_IN_SECTOR = 6;	//Всего BT пакетов (по сектору, каждый новый сектор имеет новый номер пакета)
    public final static int IDX_BT_PCK_ID_IN_SECTOR	= 7;	//Номер BT пакета

    public final static int IDX_BT_BUFF_RESERVED_1 = 8;
    public final static int IDX_BT_BUFF_RESERVED_2 = 9;

    public final static int IDX_FRAME_1_ID_IN_SECTOR = 10;		//10 -> 36
    public final static int IDX_FRAME_2_ID_IN_SECTOR = IDX_FRAME_1_ID_IN_SECTOR + FRAME_SIZE; 	//37 -> 63
    public final static int IDX_FRAME_3_ID_IN_SECTOR = IDX_FRAME_2_ID_IN_SECTOR + FRAME_SIZE; 	//64 -> 90
    public final static int IDX_FRAME_4_ID_IN_SECTOR = IDX_FRAME_3_ID_IN_SECTOR + FRAME_SIZE; 	//91 -> 117
    public final static int IDX_FRAME_5_ID_IN_SECTOR = IDX_FRAME_4_ID_IN_SECTOR + FRAME_SIZE; 	//118 -> 144
    public final static int IDX_FRAME_6_ID_IN_SECTOR = IDX_FRAME_5_ID_IN_SECTOR + FRAME_SIZE;	//145 -> 171

    //Информация о загруженной информации
    boolean flag_downloading_start;

    public int pkt_length;
    public int pkt_val;

    public float size_of;
    public float sizeB;
    public float sizeKB;
    public float sizeMB;

    public float mean_speed;
    public float instant_speed;


    public int frame_download;                  //Сколько фреймов скачали

    public boolean frame_download_complete;

    //flash info
    public int flash_data_start_page;          //Начальная страница с данными
    public int flash_data_end_page;            //Конечная страница с данными
    public int flash_data_last_page;           //Последняя загруженная страница с данными
    public int frame_in_flash;                 //Количество фреймов в флеш памяти
    public int pkt_length_info;                //размер БТ пакета
    public int frame_in_pkt_info;              //Количество фреймов в БТ пакете

    public int frame_download_vall;            //Сколько фреймов скачали

    //tmp
    public int transfCount;
    public long mStartTimeFileTransfer;
    public long instantTime;


    //app frame
    public ArrayList<new_log_item_data_struct> data_item_mass;


    public new_log_item() {
        data_item_mass = new ArrayList<new_log_item_data_struct>();
        frame_download_vall = 0;
        frame_download_complete = false;
    }

    public void update_item_info(byte[] bt_buff)
    {

    }

    public void download_f(byte[] bt_buff) {
        speed_control(bt_buff.length);
        parse_pkt(bt_buff);
    }

    private void speed_control(int buff_len) {
        pkt_val++;

        pkt_length = buff_len;

        if ((flag_downloading_start == false) && (pkt_val > 40)) {
            flag_downloading_start = true;
            mStartTimeFileTransfer = System.currentTimeMillis();
            instantTime = System.currentTimeMillis();
        }

        long elapsedTime = System.currentTimeMillis() - mStartTimeFileTransfer;
        float elapsedSeconds = (float) elapsedTime / 1000.0f;

        //Средняя скорость (с момента начала загрузки)
        mean_speed = ((((buff_len * pkt_val) * 8.0f) / elapsedSeconds) / 1000.0f);

        //Информация о загруженной информации
        sizeB = buff_len * pkt_val;
        sizeKB = sizeB / 1024;
        sizeMB = sizeKB / 1024;

        if (transfCount > 60) {
            long INSTANTelapsedTime = System.currentTimeMillis() - instantTime;
            float INSTANTelapsedSeconds = (float) INSTANTelapsedTime / 1000.0f;

            instant_speed = ((((buff_len * transfCount) * 8.0f) / INSTANTelapsedSeconds) / 1000.0f);

            instantTime = System.currentTimeMillis();
            transfCount = 0;
        }
        transfCount++;
    }

    private void parse_pkt(byte[] buff) {

        byte[] tmp4 = new byte[4];
        byte[] tmp2 = new byte[2];

        //разбор шапки пакета
        if ((buff[IDX_BT_BUFF_RESERVED_1] == (-54)) && (buff[IDX_BT_BUFF_RESERVED_2] == (-84)))
        {
            frame_download_complete = true;
        }
        else if ((buff[IDX_BT_BUFF_RESERVED_1] == (-52)) && (buff[IDX_BT_BUFF_RESERVED_2] == (-52)))
        {
            //Сектор из которго сейчас идет чтение
            tmp4[3] = buff[IDX_SECTOR_ID_1];
            tmp4[2] = buff[IDX_SECTOR_ID_2];
            tmp4[1] = buff[IDX_SECTOR_ID_3];
            tmp4[0] = buff[IDX_SECTOR_ID_4];
            int sector_select = BytesToInt4(tmp4);

            //Количество фреймов в секторе
            int frame_vall_in_sector = ByteToUint(buff[IDX_FRAME_IN_SECTOR]);
            //Количество фреймов в BT пакете
            int frame_in_pkt = ByteToUint(buff[IDX_FRAME_IN_BT_BUFF]);

            //Всего BT пакетов (по сектору, каждый новый сектор имеет новый номер пакета)
            int bt_pkt_val_in_sector = ByteToUint(buff[IDX_BT_PCK_VAL_IN_SECTOR]);
            //Номер BT пакета
            int pkt_id_in_sector = ByteToUint(buff[IDX_BT_PCK_ID_IN_SECTOR]);


            //FIXME проверка на количество фреймов и длину буффера + Добавить поддержку CRC
            //Разбор фреймов
            //Перебираем фреймы в пакете

            int idx_buffer = IDX_FRAME_1_ID_IN_SECTOR;

            int frame_counter_in_bt_pkt = 0; // счетчик фреймов внутри BT пакета

            for (int i = 0; i < frame_in_pkt; i++) {

                frame_download_vall++;

                new_log_item_data_struct data_item = new new_log_item_data_struct();

                //Добавим для отладки служебную информацию в фрейи
                data_item.sysinfo_set(sector_select, frame_vall_in_sector, bt_pkt_val_in_sector, pkt_id_in_sector, frame_counter_in_bt_pkt);
                frame_counter_in_bt_pkt++;

                //Тип пакета
                data_item.pkt_type_set(buff[idx_buffer++]);

                //TimeStamp
                int day = ByteToUint(buff[idx_buffer++]);
                int month = ByteToUint(buff[idx_buffer++]);
                int year = ByteToUint(buff[idx_buffer++]);
                int seconds = ByteToUint(buff[idx_buffer++]);
                int minutes = ByteToUint(buff[idx_buffer++]);
                int hours = ByteToUint(buff[idx_buffer++]);
                data_item.timestamp_set(year, month, day, hours, minutes, seconds);

                //Latitude
                tmp4[0] = buff[idx_buffer++];
                tmp4[1] = buff[idx_buffer++];
                tmp4[2] = buff[idx_buffer++];
                tmp4[3] = buff[idx_buffer++];
                float latitude = byteArrayToFloat(tmp4);
                //if(Double.isNaN(latitude)){latitude = 0;}
                data_item.latitude_set(latitude);


                //Longitude
                tmp4[0] = buff[idx_buffer++];
                tmp4[1] = buff[idx_buffer++];
                tmp4[2] = buff[idx_buffer++];
                tmp4[3] = buff[idx_buffer++];
                float longitude = byteArrayToFloat(tmp4);
                //if(Double.isNaN(latitude)){longitude = 0;}
                data_item.longitude_set(longitude);

                //Speed
                tmp2[1] = buff[idx_buffer++];
                tmp2[0] = buff[idx_buffer++];
                data_item.speed_set(BytesToInt2(tmp2));

                //Angle
                tmp2[1] = buff[idx_buffer++];
                tmp2[0] = buff[idx_buffer++];
                data_item.angle_set(BytesToInt2(tmp2));

                //Satellites
                data_item.satellites_set(ByteToUint(buff[idx_buffer++]));
                //Fix_type
                data_item.fix_type_set(ByteToUint(buff[idx_buffer++]));
                //HDOP
                data_item.hdop_set(ByteToUint(buff[idx_buffer++]));

                //Battery voltage
                tmp2[1] = buff[idx_buffer++];
                tmp2[0] = buff[idx_buffer++];
                data_item.battery_voltage_set(BytesToInt2(tmp2));

                //Ext power
                data_item.ext_power_set(buff[idx_buffer++]);
                //CH power
                data_item.ch_power_set(buff[idx_buffer++]);

                //motion status
                data_item.motion_status_set(buff[idx_buffer++]);

                data_item_mass.add(data_item);
            }
        }
    }

    public int get_val_frame_download(){
        return data_item_mass.size();
        //return frame_download_vall;
    }

    // packing an array of 4 bytes to an int, big endian
    private int BytesToInt4(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }

    // packing an array of 2 bytes to an int, big endian
    private int BytesToInt2(byte[] bytes) {
        return (bytes[0] & 0xFF) << 8 | (bytes[1] & 0xFF);
    }

    private int ByteToUint(byte byte_i){
        byte[] bytes = new byte[2];
        bytes[0] = 0;
        bytes[1] = byte_i;
        return (bytes[0] & 0xFF) << 8 | (bytes[1] & 0xFF);
    }


    private static float byteArrayToFloat(byte[] bytes)
    {
        float f = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getFloat();
        return f;
    }

    String jsonString;

    public void test()
    {
      /*  logger_flash_data_items = new ArrayList<logger_flash_data_item>();
        logger_flash_data_item local_data_item;

        for(int i=0; i<100; i++) {
            local_data_item = new logger_flash_data_item();
            local_data_item.sysinfo_set(0, 1, 5);
            local_data_item.timestamp_set(18, 8, 14, 18, 45, 10);
            local_data_item.gnss_latitude_set(56.832883);
            local_data_item.gnss_longitude_set(60.583770);
            local_data_item.gnss_speed_set(0);
            local_data_item.gnss_angle_set(180);
            local_data_item.gnss_valid_data_set(true);
            local_data_item.gnss_fix_type_set(1);
            local_data_item.gnss_satellites_set(7);
            local_data_item.gnss_hdop_set(1);
            local_data_item.battery_level_set(37);
            local_data_item.trigger_ext_power_set(0);
            local_data_item.trigger_motion_set(0);
            logger_flash_data_items.add(local_data_item);
        }


        //FIXME: костыль. переполняеться если больше 100к фреймов.
        jsonString = log_item_to_JSON_String(logger_flash_data_items, "test_dev_id");*/
    }



    public void convert_data(Context context, String device_id)
    {
        String json_string;

        json_string = log_item_to_JSON_String(data_item_mass, device_id);

        create_log_file(context, json_string);
    }


    public String log_item_to_JSON_String(ArrayList<new_log_item_data_struct> log_item_array, String device_id)
    {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        new_logfile_item logfile_item = new new_logfile_item();

        logfile_item.set_data_item_array(log_item_array);
        logfile_item.set_device_id(device_id);

        return gson.toJson(logfile_item);
    }


    public void create_log_file(Context context, String inp) {
        final String FILE_NAME = "SHAKED_LogFile " + getDate() + ".json";

        FileOutputStream fileOutputStream = null;

        try {
            File myFile = new File(Environment.getExternalStorageDirectory().toString() + "/" + FILE_NAME);
            myFile.createNewFile();                                         // Создается файл, если он не был создан
            FileOutputStream outputStream = new FileOutputStream(myFile);   // После чего создаем поток для записи
            outputStream.write(inp.getBytes());                            // и производим непосредственно запись
            outputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH-mm");
        return dateFormat.format(new Date());
    }
}
