package org.steamzone.shaked.app.add.list

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_main_bt_element.*
import org.steamzone.shaked.R
import org.steamzone.shaked.app.home.list.MainBTViewHolder
import org.steamzone.shaked.box.DeviceBox

class AddDeviceBTViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {


    var onItemClickListener: MainBTViewHolder.OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(item: DeviceBox)
    }


    @SuppressLint("SetTextI18n")
    fun bind(item: DeviceBox?, onItemClickListener: OnItemClickListener?) {

        if (item?.connected!!) {
            bluetooth_status_icon.setImageResource(R.drawable.ic_bluetooth_connected_black_24dp)
        } else {
            bluetooth_status_icon.setImageResource(R.drawable.ic_bluetooth_black_24dp)
        }

        battery_text.text =""+Math.floor(item.distance) + " M"
        rssi_text.text ="" +item.rssi + " dBM"
        type_text.text = item.type
        name_text.text = item.name
        mac_text.text = item.hardwareId



        containerView.setOnClickListener {
            onItemClickListener?.onItemClick(item)
        }


    }


}