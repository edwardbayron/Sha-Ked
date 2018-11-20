package org.steamzone.shaked.app.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.trello.rxlifecycle2.components.support.RxFragment
import kotlinx.android.synthetic.main.fragment_bluetooth_settings.*
import org.steamzone.shaked.R
import org.steamzone.shaked.app.device.forms.BTSettingsScheduleFormFragment
import org.steamzone.shaked.app.device.forms.GeozoneSettingsFormEditFragment
import org.steamzone.shaked.app.device.forms.GeozoneSettingsFormFragment
import java.util.*


class BTSettingsFragment: RxFragment(), AdapterView.OnItemSelectedListener {
    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | SettingsBox | File Templates.
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("not implemented") //To change body of created functions use File | SettingsBox | File Templates.
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_bluetooth_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btActivityAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, resources.getStringArray(R.array.bt_activity_interval))
        btActivityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bt_activity_interval_spinner.adapter = btActivityAdapter
        bt_activity_interval_spinner.onItemSelectedListener


        bt_activity_interval_spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent!!.getItemAtPosition(position).toString()
                mainModesInit(selectedItem)
                scheduleContainerNextForm(selectedItem)
            }
        }

        geozoneContainerNextForm()
        geozoneContainerEditForm()
        scheduleContainerEditForm()

    }

    fun scheduleContainerEditForm(){
        bt_schedule_cont_hideable.setOnClickListener {
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.hide(this)
                    ?.add(R.id.fragment_container, BTSettingsScheduleFormFragment(), BTSettingsScheduleFormFragment::class.java.name)
                    ?.addToBackStack(BTSettingsScheduleFormFragment::class.java.name)
                    ?.commit()
        }
    }

    fun geozoneContainerEditForm(){
        geozone_edit_tv.setOnClickListener {
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.hide(this)
                    ?.add(R.id.fragment_container, GeozoneSettingsFormEditFragment(), GeozoneSettingsFormEditFragment::class.java.name)
                    ?.addToBackStack(GeozoneSettingsFormEditFragment::class.java.name)
                    ?.commit()
        }

    }

    fun geozoneContainerNextForm(){
        geozone_ic_next_form.setOnClickListener {
            activity?.supportFragmentManager
                    ?.beginTransaction()
                    ?.hide(this)
                    ?.add(R.id.fragment_container, GeozoneSettingsFormFragment(), GeozoneSettingsFormFragment::class.java.name)
                    ?.addToBackStack(GeozoneSettingsFormFragment::class.java.name)
                    ?.commit()
        }
    }

    fun scheduleContainerNextForm(selectedItem: String){
        if(selectedItem == "Schedule"){
            geozone_edit_tv.visibility = View.VISIBLE
            bt_schedule_cont_hideable.visibility = View.VISIBLE
            bt_time_interval_cont_hideable.visibility = View.GONE
            pa_mode_cont_hideable.visibility = View.GONE
            ant_select_cont_hideable.visibility = View.GONE
        }
//        else{
//            geozone_edit_tv.visibility = View.GONE
//            bt_schedule_cont_hideable.visibility = View.GONE
//            bt_time_interval_cont_hideable.visibility = View.VISIBLE
//            pa_mode_cont_hideable.visibility = View.VISIBLE
//            ant_select_cont_hideable.visibility = View.VISIBLE
//        }
    }

    fun mainModesInit(selectedItem: String){
        if(selectedItem == "Always ON" || selectedItem == "Button") {
            bt_grouped_cont.visibility = View.GONE
            geozone_cont_hideable.visibility = View.GONE
            pa_mode_cont_hideable.visibility = View.GONE
            ant_select_cont_hideable.visibility = View.GONE
        }
        else{
            bt_grouped_cont.visibility = View.VISIBLE
            geozone_cont_hideable.visibility = View.VISIBLE
            pa_mode_cont_hideable.visibility = View.VISIBLE
            ant_select_cont_hideable.visibility = View.VISIBLE
        }
    }


}