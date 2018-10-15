package org.steamzone.shaked.bt.old.models;

import android.content.Context;
import android.os.Environment;


import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class logger_flash_data {

    //DATA TRANSFER
    public static int LOCAL_DUMP_SIZE = 34;            //Размер дампа

    public static int DATA_TRANSFER_BUFF_LEN = 180;
    public static int DATA_IN_BT_PCK = 5;
    public static int FRAME5_DATA_IN_BT_BUFF = LOCAL_DUMP_SIZE * DATA_IN_BT_PCK;

    //IDX BT_SEND_BUFF
    public static int IDX_SECTOR_ID_1 = 0;    //ID Сектора из которога сейчас идет отправка
    public static int IDX_SECTOR_ID_2 = 1;
    public static int IDX_SECTOR_ID_3 = 2;
    public static int IDX_SECTOR_ID_4 = 3;

    public static int IDX_FRAME_IN_BT_SECTOR = 4;    //Количество фреймов в секторе
    public static int IDX_FRAME_IN_BT_BUFF = 5;    //Количество фреймов в буффере

    public static int IDX_BT_PCK_VAL_IN_SECTOR = 6;    //Всего BT пакетов (по сектору, каждый новый сектор имеет новый номер пакета)
    public static int IDX_BT_PCK_ID_IN_SECTOR = 7;    //Номер BT пакета

    public static int IDX_BT_BUFF_RESERVED_1 = 8;
    public static int IDX_BT_BUFF_RESERVED_2 = 9;

    public static int IDX_FRAME_1_ID_IN_SECTOR = 10;

    //PCKT DATA IDX
    public static int IDX_PCK_TYPE = 0;

    public static int IDX_DATA_DAY = 1;
    public static int IDX_DATA_MONTH = 2;
    public static int IDX_DATA_YEAR = 3;

    public static int IDX_TIME_HOURS = 4;
    public static int IDX_TIME_MINUTES = 5;
    public static int IDX_TIME_SECONDS = 6;

    public static int IDX_LATITUDE_VAL1 = 7;
    public static int IDX_LATITUDE_VAL2 = 8;
    public static int IDX_LATITUDE_VAL3 = 9;
    public static int IDX_LATITUDE_VAL4 = 10;
    public static int IDX_LATITUDE_SCALE1 = 11;
    public static int IDX_LATITUDE_SCALE2 = 12;
    public static int IDX_LATITUDE_SCALE3 = 13;
    public static int IDX_LATITUDE_SCALE4 = 14;

    public static int IDX_LONGITUDE_VAL1 = 15;
    public static int IDX_LONGITUDE_VAL2 = 16;
    public static int IDX_LONGITUDE_VAL3 = 17;
    public static int IDX_LONGITUDE_VAL4 = 18;
    public static int IDX_LONGITUDE_SCALE1 = 19;
    public static int IDX_LONGITUDE_SCALE2 = 20;
    public static int IDX_LONGITUDE_SCALE3 = 21;
    public static int IDX_LONGITUDE_SCALE4 = 22;

    public static int IDX_SPEED_VAL1 = 23;
    public static int IDX_SPEED_VAL2 = 24;

    public static int IDX_ANGLE_VAL1 = 25;
    public static int IDX_ANGLE_VAL2 = 26;

    public static int IDX_DATA_VALID = 27;
    public static int IDX_GNSS_FIX_TYPE = 28;
    public static int IDX_GNSS_SATELLITES = 29;
    public static int IDX_GNSS_HDOP = 30;

    public static int IDX_BATTERY_VAL = 31;

    public static int IDX_TRIGGER_EXT_POW = 32;
    public static int IDX_TRIGGER_MOION = 33;


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

    //Status logger buf
    public int frame_in_flash;                 //Количество фреймов в флеш памяти

    //flash info
    public int flash_size;                     //Размер памяти
    public int val_frame_in_one_page;          //Количество фреймов на 1 странице
    public int frame_size;                     //Размер 1го фрейма

    public int flash_data_start_page;          //Начальная страница с данными
    public int flash_data_end_page;            //Конечная страница с данными


    //tmp
    public int transfCount;
    public long mStartTimeFileTransfer;
    public long instantTime;


    //app frame
    public ArrayList<logger_flash_data_item> logger_flash_data_items;


    public logger_flash_data() {
        logger_flash_data_items = new ArrayList<logger_flash_data_item>();
    }

    public void update_flash_info(byte[] bt_buff) {

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

        //logger_flash_data_items = new ArrayList<logger_flash_data_item>();

        int day, month, year, hours, minutes, seconds, sector_select, frame_id_in_sector;
        int ids = 0;

        byte[] tmp4 = new byte[4];
        byte[] tmp2 = new byte[2];
        int frame_in_pkt;
        int idx_buffer;

        int xyz_val;
        int xyz_scale;
        double xyz;

        //разбор шапки пакета
        tmp4[0] = buff[IDX_SECTOR_ID_1];
        tmp4[1] = buff[IDX_SECTOR_ID_2];
        tmp4[2] = buff[IDX_SECTOR_ID_3];
        tmp4[3] = buff[IDX_SECTOR_ID_4];
        sector_select = BytesToInt4(tmp4);

        frame_id_in_sector = buff[IDX_BT_PCK_ID_IN_SECTOR];

        frame_in_pkt = buff[IDX_FRAME_IN_BT_BUFF];

        idx_buffer = IDX_FRAME_1_ID_IN_SECTOR;


        //FIXME проверка на количество фреймов и длину буффера
        //Разбор фреймов
        for (int i = 0; i < frame_in_pkt; i++) {

            logger_flash_data_item local_data_item = new logger_flash_data_item();

            local_data_item.sysinfo_set(ids, sector_select, frame_id_in_sector);
            ids++;

            local_data_item.pkt_type_set(buff[idx_buffer++]);

            day = buff[idx_buffer++];
            month = buff[idx_buffer++];
            year = buff[idx_buffer++];
            hours = buff[idx_buffer++];
            minutes = buff[idx_buffer++];
            seconds = buff[idx_buffer++];
            local_data_item.timestamp_set(year, month, day, hours, minutes, seconds);

            tmp4[0] = buff[idx_buffer++];
            tmp4[1] = buff[idx_buffer++];
            tmp4[2] = buff[idx_buffer++];
            tmp4[3] = buff[idx_buffer++];
            xyz_val = BytesToInt4(tmp4);

            tmp4[0] = buff[idx_buffer++];
            tmp4[1] = buff[idx_buffer++];
            tmp4[2] = buff[idx_buffer++];
            tmp4[3] = buff[idx_buffer++];
            xyz_scale = BytesToInt4(tmp4);

            xyz = (double) xyz_val / (double) xyz_scale;
            local_data_item.gnss_latitude_set(xyz);


            tmp4[0] = buff[idx_buffer++];
            tmp4[1] = buff[idx_buffer++];
            tmp4[2] = buff[idx_buffer++];
            tmp4[3] = buff[idx_buffer++];
            xyz_val = BytesToInt4(tmp4);

            tmp4[0] = buff[idx_buffer++];
            tmp4[1] = buff[idx_buffer++];
            tmp4[2] = buff[idx_buffer++];
            tmp4[3] = buff[idx_buffer++];
            xyz_scale = BytesToInt4(tmp4);

            xyz = (double) xyz_val / (double) xyz_scale;

            local_data_item.gnss_longitude_set(xyz);

            tmp2[0] = buff[idx_buffer++];
            tmp2[1] = buff[idx_buffer++];
            local_data_item.gnss_speed_set(BytesToInt2(tmp2));

            tmp2[0] = buff[idx_buffer++];
            tmp2[1] = buff[idx_buffer++];
            local_data_item.gnss_angle_set(BytesToInt2(tmp2));

            if (buff[idx_buffer++] == 1) {
                local_data_item.gnss_valid_data_set(true);
            } else {
                local_data_item.gnss_valid_data_set(false);
            }
            local_data_item.gnss_fix_type_set(buff[idx_buffer++]);
            local_data_item.gnss_satellites_set(buff[idx_buffer++]);
            local_data_item.gnss_hdop_set(buff[idx_buffer++]);

            local_data_item.battery_level_set(buff[idx_buffer++]);

            local_data_item.trigger_ext_power_set(buff[idx_buffer++]);
            local_data_item.trigger_motion_set(buff[idx_buffer++]);

            logger_flash_data_items.add(local_data_item);
        }
    }

    // packing an array of 4 bytes to an int, big endian
    private int BytesToInt4(byte[] bytes) {
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }

    // packing an array of 2 bytes to an int, big endian
    private int BytesToInt2(byte[] bytes) {
        return (bytes[0] & 0xFF) << 8 | (bytes[1] & 0xFF);
    }


    String jsonString;

    public void test() {
        logger_flash_data_items = new ArrayList<logger_flash_data_item>();
        logger_flash_data_item local_data_item;

        for (int i = 0; i < 100; i++) {
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
        jsonString = log_item_to_JSON_String(logger_flash_data_items, "test_dev_id");
    }


    public void convert_data(Context context, String device_id) {
        String json_string;

        json_string = log_item_to_JSON_String(logger_flash_data_items, device_id);

        create_log_file(context, json_string);
    }


    public String log_item_to_JSON_String(ArrayList<logger_flash_data_item> log_item_array, String device_id) {
//        GsonBuilder builder = new GsonBuilder();
//        Gson gson = builder.create();
//
//        logfile_item logfile_item = new logfile_item();
//
//        logfile_item.set_data_item_array(log_item_array);
//        logfile_item.set_device_id(device_id);
//
//         return gson.toJson(logfile_item);
        return null;
    }


    public void create_log_file(Context context, String inp) {
        final String FILE_NAME = "data.json";

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
}


