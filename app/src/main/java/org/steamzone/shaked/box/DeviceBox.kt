package org.steamzone.shaked.box

import com.polidea.rxandroidble2.RxBleConnection
import com.polidea.rxandroidble2.RxBleDevice
import com.polidea.rxandroidble2.scan.ScanResult
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Index

@Entity
class DeviceBox {


    @Id(assignable = true)
    var id: Long = 0
    @Index
    var hardwareId: String? = null //macaddress
    var name: String? = null
    var type: String? = null

    var connected: Boolean = false
    var rssi: Int = -1
    var distance: Double = -1.0
    var batteryInfo: String? = null
    var batteryInfoVoltage: String? = null


    companion object {
        fun save(list: List<DeviceBox>): List<DeviceBox> {
            SBox.getBox(DeviceBox::class.java).put(list)
            return list
        }

        fun save(model: DeviceBox): DeviceBox {
            SBox.getBox(DeviceBox::class.java).put(model)
            return model
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

        fun getAll(): List<DeviceBox>? {
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


    }

}