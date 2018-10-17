package org.steamzone.shaked.app.home.list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.steamzone.shaked.box.DeviceBox
import android.view.LayoutInflater
import android.widget.ImageView
import com.orhanobut.logger.Logger
import org.steamzone.shaked.R
import org.steamzone.shaked.utils.JsonUtil


class MainBTListAdapter(var onItemClickListener: MainBTViewHolder.OnItemClickListener?) : RecyclerView.Adapter<MainBTViewHolder>() {

    var list: List<DeviceBox>? = ArrayList()


    fun setupList(listbt: List<DeviceBox>) {
        this.list = listbt
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainBTViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_main_bt_element, parent, false)
        return MainBTViewHolder(v)
    }

    override fun getItemCount(): Int {

        return list?.size!!
    }

    override fun onBindViewHolder(holder: MainBTViewHolder, position: Int) {
        var deviceBox = list?.get(position)
        //Logger.wtf(JsonUtil.gson.toJson(deviceBox))
        holder.bind(deviceBox, onItemClickListener)


    }

}