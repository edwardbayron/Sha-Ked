package org.steamzone.shaked.app.add.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.steamzone.shaked.R
import org.steamzone.shaked.box.DeviceBox
import org.steamzone.shaked.utils.JsonUtil

class AddDeviceBtAdapter(var onItemClickListener: AddDeviceBTViewHolder.OnItemClickListener?) : RecyclerView.Adapter<AddDeviceBTViewHolder>() {

    var list: List<DeviceBox>? = ArrayList()


    fun setupList(listbt: List<DeviceBox>) {
        this.list = listbt
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return JsonUtil.longHash(list!![position].hardwareId!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddDeviceBTViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_add_main_bt_element, parent, false)
        return AddDeviceBTViewHolder(v)
    }

    override fun getItemCount(): Int {

        return list?.size!!
    }

    override fun onBindViewHolder(holder: AddDeviceBTViewHolder, position: Int) {
        var deviceBox = list?.get(position)
        //Logger.wtf(JsonUtil.gson.toJson(deviceBox))
        holder.bind(deviceBox, onItemClickListener)


    }

}