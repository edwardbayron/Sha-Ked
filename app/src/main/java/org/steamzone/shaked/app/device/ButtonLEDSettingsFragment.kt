package org.steamzone.shaked.app.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.components.support.RxFragment
import kotlinx.android.synthetic.main.fragment_button_led_settings.*
import org.steamzone.shaked.R

class ButtonLEDSettingsFragment:RxFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_button_led_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        time_interval_checkbox.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                time_interval_cont_hideable.visibility = View.VISIBLE
            }
            else{
                time_interval_cont_hideable.visibility = View.GONE
            }
        }


    }
}