package org.steamzone.shaked.bt;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.util.Log;

import com.orhanobut.logger.Logger;

import org.steamzone.shaked.bt.models.cus_mydev_item;
import org.steamzone.shaked.bt.models.cus_search_item;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static org.steamzone.shaked.bt.BTClient.Scan_type.SCAN_NEW_DEVICES;
import static org.steamzone.shaked.bt.BTClient.Scan_type.SCAN_NONE;
import static org.steamzone.shaked.bt.BTClient.Scan_type.SCAN_UPDATE_DATA;
import static org.steamzone.shaked.bt.models.cus_mydev_item.devType.shaKedTypeDev;

//FIXME: Добавить удаление метода. Для закрытия соедениения. При закрытии формы bt_close
//FIXME: перенести в сервис?

public class BTClient {


    public static final UUID DESCRIPTOR_CONFIG_UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

    public static final UUID SERVICE_GPS_ONLINE_DATA_UUID = UUID.fromString("F3641400-00B0-4240-BA50-05CA45BF8ABC");
    public static final UUID CHARACTERESTIC_GPS_ONLINE_DATA_UUID = UUID.fromString("F3641401-00B0-4240-BA50-05CA45BF8ABC");

    public static final UUID LOGGER_SETTINGS_SERVICE_UUID = UUID.fromString("F364A0C0-00B0-4240-BA50-05CA45Bf8ABC");
    public static final UUID LOGGER_SETTINGS_CHAR_UUID = UUID.fromString("F364A0C1-00B0-4240-BA50-05CA45Bf8ABC");
    public static final UUID RTC_TIME_CHAR_UUID = UUID.fromString("F364A0C2-00B0-4240-BA50-05CA45Bf8ABC");
    public static final UUID ERASE_FLASH_CHAR_UUID = UUID.fromString("F364A0C3-00B0-4240-BA50-05CA45Bf8ABC");

    public static final UUID SERVICE_TRACKER_STATUS_UUID = UUID.fromString("F364A0E0-00B0-4240-BA50-05CA45Bf8ABC");
    public static final UUID CHARACTERESTIC_TRACKER_ERROR_STATUS_UUID = UUID.fromString("F364A0EE-00B0-4240-BA50-05CA45Bf8ABC");
    public static final UUID CHARACTERESTIC_GPS_POWER_STATUS_UUID = UUID.fromString("F364A0E1-00B0-4240-BA50-05CA45Bf8ABC");
    public static final UUID CHARACTERESTIC_GPS_FIX_TYPE_CHAR_UUID = UUID.fromString("F364A0E2-00B0-4240-BA50-05CA45Bf8ABC");
    public static final UUID CHARACTERESTIC_FLASH_ID_CHAR_UUID = UUID.fromString("F364A0E3-00B0-4240-BA50-05CA45Bf8ABC");
    public static final UUID CHARACTERESTIC_FLASH_VALL_STATUS_CHAR_UUID = UUID.fromString("F364A0E4-00B0-4240-BA50-05CA45Bf8ABC");
    public static final UUID CHARACTERESTIC_RAM_VALL_STATUS_CHAR_UUID = UUID.fromString("F364A0E5-00B0-4240-BA50-05CA45Bf8ABC");
    public static final UUID CHARACTERESTIC_GPS_MSG_NOT_VALID_CYCLE_UUID = UUID.fromString("F364A0E6-00B0-4240-BA50-05CA45Bf8ABC");
    public static final UUID CHARACTERESTIC_GPS_SAT_ONLINE_UUID = UUID.fromString("F364A0E7-00B0-4240-BA50-05CA45Bf8ABC");

    public static final UUID SERVICE_DATA_TRANSFER_UUID = UUID.fromString("F364ADD0-00B0-4240-BA50-05CA45Bf8ABC");
    public static final UUID CHARACTERESTIC_DATA_TRANSFER_UUID = UUID.fromString("F364ADDD-00B0-4240-BA50-05CA45Bf8ABC");
    public static final UUID CHARACTERESTIC_DATA_TRANSFER_GET_INFO_UUID = UUID.fromString("F364ADDA-00B0-4240-BA50-05CA45Bf8ABC");


    private final static int REQUEST_ENABLE_BT = 1;

    private BTMessageListener listener;
    private Context context;

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothLeAdvertiser mBluetoothLeAdvertiser;
    private BluetoothGatt mBluetoothGatt;

    public ArrayList<cus_search_item> BtSearchDevArrayList = new ArrayList<cus_search_item>();
    public int CounterDevSearch;

    ArrayList<cus_mydev_item> BTmyDevArrayList = new ArrayList<cus_mydev_item>();

    private Timer mTimer;
    private TimerTask mMyTimerTask;

    private Timer ConnectmTimer;
    private TimerTask ConnectmMyTimerTask;


    //bt dev
    private boolean mGattDeviceConnectStatus;   //Статус подключения к устройству

    //debug
    private static final boolean DEBUG_OUT_CHARACTERISTICS_AND_SERVICES = true;


    public enum BT_init_status {
        BT_INIT_OK,
        BT_FATAL_ERROR_BT_NULL,
        BT_OFF
    }

    public enum Scan_type {
        SCAN_NONE,
        SCAN_NEW_DEVICES,
        SCAN_UPDATE_DATA
    }

    public Scan_type scan_type = SCAN_NONE;


    public interface BTMessageListener {
        void ScanCallBack();

        void device_connect_status(boolean connect);

        void get_data(byte[] data);
    }


    public BTClient(BTMessageListener listener, Context context) {
        this.listener = listener;
        this.context = context;
    }

    public ArrayList<cus_search_item> get_scan_list() {
        return BtSearchDevArrayList;
    }

    public ArrayList<cus_mydev_item> get_update_list() {
        return BTmyDevArrayList;
    }

    public cus_mydev_item get_mydev_item() {
        return BTmyDevArrayList.get(0);
    }

    public Scan_type get_scan_mode() {
        return scan_type;
    }

    //FIXME: Добавить аргументы сканирования, фильтр, время, возможность включать адверт даты
    public void scan_start() {
        scan_type = SCAN_NEW_DEVICES;
        BtSearchDevArrayList.clear();
        CounterDevSearch = 0;
        mBluetoothLeScanner.startScan(mScanCallback);
    }

    public void scan_start(ArrayList<cus_mydev_item> myDevArrayList) {
        scan_type = SCAN_UPDATE_DATA;
        BTmyDevArrayList = new ArrayList<cus_mydev_item>(myDevArrayList);
        mBluetoothLeScanner.startScan(mScanCallback);
        MyDevUpdateTimerInit();
    }

    public void scan_start(cus_mydev_item MyDev) {
        scan_type = SCAN_UPDATE_DATA;
        BTmyDevArrayList.clear();
        BTmyDevArrayList.add(MyDev);
        mBluetoothLeScanner.startScan(mScanCallback);
        MyDevUpdateTimerInit();
    }

    public void scan_stop() {
        MyDevUpdateTimerStop();
        mBluetoothLeScanner.stopScan(mScanCallback);
    }

    //FIXME: Разнести на подключение, определение сервисов, чтение сервиса, запись, автоматическое получение данных, потоковая передача. Сейчас костыль для онлайн отображения
    public void connect_device(BluetoothDevice myBtDevice) {
        //mBluetoothGatt = myBtDevice.connectGatt(context, false, mBluetoothGattCallback, TRANSPORT_AUTO, PHY_LE_2M);
        mBluetoothGatt = myBtDevice.connectGatt(context, false, mBluetoothGattCallback);
    }

    public void disconnect_device() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
        }
    }


    public double calculateDistance(int rssi) {

        double txPower = -59; //hard coded power value. Usually ranges between -59 to -65

        if (rssi == 0) {
            return -1;
        }

        double ratio = (rssi * 1.0) / txPower;

        if (ratio < 1.0) {
            return Math.pow(ratio, 10);
        } else {
            double distance = ((0.89976) * Math.pow(ratio, 7.7095)) + 0.111;
            return distance;
        }
    }


    public BT_init_status bt_adapter_init() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //FIXME: проверить BT адаптер на возможность работы с BT5 и выставить определенный флаг для отправки на устройство для перестройки PHY
        //FIXME: Делать вывод параметров BT в меню! для информирования пользователя о его возможностях BT
        //FIXME: Проверять на возможность отправки широковещательных пакетов
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isEnabled()) {

                bt_le_scanner_init();
                bt_le_advertiser();
                return BT_init_status.BT_INIT_OK;
            } else {
                return BT_init_status.BT_OFF;
            }
        } else {
            //Ошибка BT адаптера
            return BT_init_status.BT_FATAL_ERROR_BT_NULL;
        }
    }

    public void online_data_service(boolean status) {
        //FIXME добавить проверку на подключение
        //FIXME добавить обработку ошибок
        //FIXME: костыль, пока просто если объект существует. добавить нормальную обработку ошибок
        if (mBluetoothGatt != null) {
            BluetoothGattService service = mBluetoothGatt.getService(SERVICE_GPS_ONLINE_DATA_UUID);
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(CHARACTERESTIC_GPS_ONLINE_DATA_UUID);

            mBluetoothGatt.setCharacteristicNotification(characteristic, status);

            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(DESCRIPTOR_CONFIG_UUID);
            if (status) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            } else {
                descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
            }
            mBluetoothGatt.writeDescriptor(descriptor);
        }
    }

    public void get_log_data_service(boolean status) {
        //FIXME добавить проверку на подключение
        //FIXME добавить обработку ошибок
        //FIXME: костыль, пока просто если объект существует. добавить нормальную обработку ошибок
        if (mBluetoothGatt != null) {
            BluetoothGattService service = mBluetoothGatt.getService(SERVICE_DATA_TRANSFER_UUID);
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(CHARACTERESTIC_DATA_TRANSFER_UUID);

            mBluetoothGatt.setCharacteristicNotification(characteristic, status);

            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(DESCRIPTOR_CONFIG_UUID);

            if (status) {
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            } else {
                descriptor.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
            }

            mBluetoothGatt.writeDescriptor(descriptor);

        }
    }

    public boolean get_log_data_info() {
        BluetoothGattCharacteristic mBtLoggerSettingsCharacteristic = mBluetoothGatt.getService(SERVICE_DATA_TRANSFER_UUID)
                .getCharacteristic(CHARACTERESTIC_DATA_TRANSFER_GET_INFO_UUID);
        return mBluetoothGatt.readCharacteristic(mBtLoggerSettingsCharacteristic);
    }

    public boolean get_settings_frame() {
        BluetoothGattCharacteristic mBtLoggerSettingsCharacteristic = mBluetoothGatt.getService(LOGGER_SETTINGS_SERVICE_UUID)
                .getCharacteristic(LOGGER_SETTINGS_CHAR_UUID);
        return mBluetoothGatt.readCharacteristic(mBtLoggerSettingsCharacteristic);
    }

    public boolean set_settings_frame(byte[] settings_buff) {
        BluetoothGattCharacteristic mBtLoggerSettingsCharacteristic = mBluetoothGatt.getService(LOGGER_SETTINGS_SERVICE_UUID)
                .getCharacteristic(LOGGER_SETTINGS_CHAR_UUID);

        mBtLoggerSettingsCharacteristic.setValue(settings_buff);
        return mBluetoothGatt.writeCharacteristic(mBtLoggerSettingsCharacteristic);
    }

    private void bt_le_scanner_init() {
        mBluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
    }

    private void bt_le_advertiser() {
        mBluetoothLeAdvertiser = BluetoothAdapter.getDefaultAdapter().getBluetoothLeAdvertiser();
    }

    //Call Back
    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            //FIXME: свитч режима
            switch (scan_type) {
                case SCAN_NEW_DEVICES:
                    ScanlistDevInfo(result);
                    break;
                case SCAN_UPDATE_DATA:
                    ScanlistDevInfoUpdate(result);
                    break;
            }
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);
        }

        @Override
        public void onScanFailed(int errorCode) {
            Logger.wtf( "Discovery onScanFailed: " + errorCode);
            super.onScanFailed(errorCode);
        }
    };

    private BluetoothGattCallback mBluetoothGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices();
                ConnectMyDevUpdateTimerInit();
                if (DEBUG_OUT_CHARACTERISTICS_AND_SERVICES) {
                    Logger.wtf( "discoverServices: [START] ");
                }

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {

                mGattDeviceConnectStatus = false;
                listener.device_connect_status(false);
                ConnectMyDevUpdateTimerStop();
                try {
                    gatt.close();
                } catch (Exception e) {
                }
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status != BluetoothGatt.GATT_SUCCESS) {
                return;
            }//FIXME: Обрабатывать ошибку подключения! Handle the error

            if (DEBUG_OUT_CHARACTERISTICS_AND_SERVICES) {
                Logger.wtf("discoverServices: [DONE] ");
                for (BluetoothGattService s : gatt.getServices()) {
                    Logger.wtf("discoverServices: found " + s.getUuid());
                    for (BluetoothGattCharacteristic c : s.getCharacteristics()) {
                        Logger.wtf("--> characteristic: " + c.getUuid() + ":" + String.format("%x", c.getInstanceId()));
                    }
                }
            }

            //FIXME: Проверка на доступность сервисов

            //FIXME: Если сервисы доступны и устройство подключено - вызываем call back
            mGattDeviceConnectStatus = true;
            listener.device_connect_status(true);
            //FIXME: иначе отключаемся от устройства


        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
           /* if (DESCRIPTOR_CONFIG_UUID.equals(descriptor.getUuid())) {
                BluetoothGattCharacteristic characteristic = gatt
                        .getService(SERVICE_NORDIC_UART_UUID)
                        .getCharacteristic(CHARACTERESTIC_NORDIC_UART_TX_UUID);
                gatt.readCharacteristic(characteristic);
            } ??????????????????????????? что это?*/
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            readCounterCharacteristic(characteristic);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            readCounterCharacteristic(characteristic);
        }

        private void readCounterCharacteristic(BluetoothGattCharacteristic characteristic) {
            if (CHARACTERESTIC_GPS_ONLINE_DATA_UUID.equals(characteristic.getUuid())) {
                byte[] data = characteristic.getValue();
                // Update UI
                listener.get_data(data);
            } else if (LOGGER_SETTINGS_CHAR_UUID.equals(characteristic.getUuid())) {
                byte[] data = characteristic.getValue();
                // Update UI
                listener.get_data(data);
            } else if (CHARACTERESTIC_DATA_TRANSFER_UUID.equals(characteristic.getUuid())) {
                byte[] data = characteristic.getValue();
                // Update UI
                listener.get_data(data);
            } else if (CHARACTERESTIC_DATA_TRANSFER_GET_INFO_UUID.equals(characteristic.getUuid())) {
                byte[] data = characteristic.getValue();
                // Update UI
                listener.get_data(data);
            }
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.d(TAG, String.format("BluetoothGatt ReadRssi[%d]", rssi));
                BTmyDevArrayList.get(0).RSSI = rssi;
                BTmyDevArrayList.get(0).distance = calculateDistance(rssi);
                BTmyDevArrayList.get(0).rssi_valid = true;
                listener.ScanCallBack();
            }
        }
    };


    //FIXME: минимальная прверка устройства по advertdata. Выставлять флаг принадлежности! и типа устройства
    //Поиск и добавление устройств
    public void ScanlistDevInfo(ScanResult result) {
        if (result.getDevice().getAddress() != "") {
            String srcStrMAC = result.getDevice().getAddress();
            int flag_create = 1;

            //FIXME: ПРоверять по буферу на возможность добавления advertdata, сейчас костыль, проверяем по имени
            //FIXME: Отображать иначе уже добавленные устройства и заменить кнопку добавления на ОК?
            boolean allow_add;
            String NameAdd = new String("GPS TRACKER");

            for (int i = 0; i < CounterDevSearch; i++) {

                if (srcStrMAC.equals(BtSearchDevArrayList.get(i).bleDev.getAddress())) {

                    if (NameAdd.equals(BtSearchDevArrayList.get(i).bleDev.getName())) {
                        allow_add = true;
                    } else {
                        allow_add = false;
                    }

                    BtSearchDevArrayList.set(i, new cus_search_item(result.getDevice(), result.getRssi(), calculateDistance(result.getRssi()), allow_add, 1));
                    flag_create = 0;
                    break;
                }
            }

            if (flag_create == 1) {

                if (NameAdd.equals(result.getDevice().getName())) {
                    allow_add = true;
                } else {
                    allow_add = false;
                }

                BtSearchDevArrayList.add(new cus_search_item(result.getDevice(), result.getRssi(), calculateDistance(result.getRssi()), allow_add, 1));
                CounterDevSearch++;
            }
        }
        listener.ScanCallBack();
    }

    //Обновление информации об устройстве
    public void ScanlistDevInfoUpdate(ScanResult result) {
        //FIXME: перенести в отдельный класс? потом изменить поиск с мака на advert data. Если мы будем постоянно менять мак на устройстве!
        if (result.getDevice().getAddress() != "") {
            String srcStrMAC = result.getDevice().getAddress();

            for (int i = 0; i < BTmyDevArrayList.size(); i++) {
                if (srcStrMAC.equals(BTmyDevArrayList.get(i).bleDev.getAddress())) {
                    //FIXME: Не нужно каждый раз обновлять устройство. Достаточно обновлять только RSSI и advert data
                    cus_mydev_item update_item = new cus_mydev_item(result.getDevice(), result.getRssi(), calculateDistance(result.getRssi()), shaKedTypeDev);
                    update_item.set_rsii_valid(true);
                    update_item.set_update_flag(true);
                    BTmyDevArrayList.set(i, update_item);
                }
            }
        }
        listener.ScanCallBack();
    }


    //таймер для обновления инфорсмации BT RSSI
    private void MyDevUpdateTimerInit() {
        mTimer = new Timer();
        mMyTimerTask = new MyDevUpdateTimerTask();

        // delay 500ms, repeat in 1000ms
        mTimer.schedule(mMyTimerTask, 2000, 2000);

    }

    private void MyDevUpdateTimerStop() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }


    class MyDevUpdateTimerTask extends TimerTask {
        @Override
        public void run() {
            //MainActivity.this.getAcitivity().runOnUiThread(new Runnable() { }
            Activity activity = (Activity) context;

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < BTmyDevArrayList.size(); i++) {
                        if (BTmyDevArrayList.get(i).get_update_flag()) {
                            BTmyDevArrayList.get(i).set_update_flag(false);
                        } else {
                            BTmyDevArrayList.get(i).set_rsii_valid(false);
                            listener.ScanCallBack();
                        }
                    }
                }
            });
        }
    }


    //таймер для обновления инфорсмации BT RSSI
    private void ConnectMyDevUpdateTimerInit() {
        ConnectmTimer = new Timer();
        ConnectmMyTimerTask = new ConnectMyDevUpdateTimerTask();

        // delay 500ms, repeat in 1000ms
        ConnectmTimer.schedule(ConnectmMyTimerTask, 500, 500);
    }

    private void ConnectMyDevUpdateTimerStop() {
        if (ConnectmTimer != null) {
            ConnectmTimer.cancel();
        }
    }

    class ConnectMyDevUpdateTimerTask extends TimerTask {
        @Override
        public void run() {
            //MainActivity.this.getAcitivity().runOnUiThread(new Runnable() { }
            Activity activity = (Activity) context;

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mBluetoothGatt.readRemoteRssi();
                }
            });
        }
    }


}
