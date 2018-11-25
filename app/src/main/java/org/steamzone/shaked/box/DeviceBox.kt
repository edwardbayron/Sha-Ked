package org.steamzone.shaked.box

import com.polidea.rxandroidble2.RxBleConnection
import com.polidea.rxandroidble2.RxBleDevice
import com.polidea.rxandroidble2.scan.ScanResult
import io.objectbox.annotation.*
import io.objectbox.query.Query
import io.objectbox.query.QueryBuilder

@Entity
class DeviceBox {


    @Id(assignable = true)
    var id: Long = 0
    @Unique
    @Index(type = IndexType.VALUE)
    var hardwareId: String? = null //macaddress
    var name: String? = null
    var type: String? = null

    var connected: Boolean = false
    var rssi: Int = -1
    var distance: Double = -1.0
    var batteryInfo: String? = null
    var batteryInfoVoltage: String? = null

    var autoConnect: Boolean = true
    var liveDataEnable: Boolean = false
    var connectionStatus: String? = null


    companion object {
        fun save(list: List<DeviceBox>): List<DeviceBox> {
            SBox.getBox(DeviceBox::class.java).put(list)
            return list
        }

        fun save(model: DeviceBox): DeviceBox {

            var deviceBox = getDeviceBoxByMac(model.hardwareId)
            if (deviceBox != null) {
                model.id = deviceBox.id
            }
            model.rssi = 0
            model.distance = 0.0


            SBox.getBox(DeviceBox::class.java).put(model)
            return model
        }

        fun getDeviceBoxByMac(hardwareId: String?): DeviceBox? {
            return query().equal(DeviceBox_.hardwareId, hardwareId).build().findFirst()
        }

        fun delete(list: List<DeviceBox>): List<DeviceBox> {
            SBox.getBox(DeviceBox::class.java).remove(list)
            return list
        }

        fun delete(model: DeviceBox): DeviceBox {
            SBox.getBox(DeviceBox::class.java).remove(model)
            return model
        }

        fun get(id: Long): DeviceBox? {

            return SBox.getBox(DeviceBox::class.java).query().equal(DeviceBox_.id, id).build().findFirst()
        }

        fun get(): DeviceBox? {

            return get(1)

        }

        fun getAll(): List<DeviceBox> {
            return SBox.getBox(DeviceBox::class.java).query().build().find()
        }

        fun calculateDistance(rssi: Int): Double {

            val txPower = -59.0 //hard coded power value. Usually ranges between -59 to -65

            if (rssi == 0) {
                return -1.0
            }

            val ratio = rssi * 1.0 / txPower

            return if (ratio < 1.0) {
                Math.pow(ratio, 10.0)
            } else {
                0.89976 * Math.pow(ratio, 7.7095) + 0.111
            }
        }

        fun createDevivceBoxFromScanResult(ble: ScanResult): DeviceBox {

            val deviceBox = DeviceBox()
            deviceBox.name = ble.bleDevice.name
            deviceBox.hardwareId = ble.bleDevice.macAddress
            deviceBox.connected = ble.bleDevice.connectionState == RxBleConnection.RxBleConnectionState.CONNECTED
            deviceBox.rssi = ble.rssi
            deviceBox.distance = calculateDistance(deviceBox.rssi)


            return deviceBox
        }

        fun query(): QueryBuilder<DeviceBox> {
            return SBox.getBox(DeviceBox::class.java).query()
        }


    }

}