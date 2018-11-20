package org.steamzone.shaked.app.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.trello.rxlifecycle2.components.support.RxFragment
import org.steamzone.shaked.R
import kotlinx.android.synthetic.main.fragment_date_time_settings.*
import java.util.*
import java.util.Arrays.asList
import android.widget.ArrayAdapter
import java.text.SimpleDateFormat
import java.util.Arrays.asList








class DateTimeSettingsFragment : RxFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_date_time_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentTime = Calendar.getInstance().getTime()
        val plants = arrayOf(toSimpleString(currentTime))
        val aa = ArrayAdapter(context, android.R.layout.simple_spinner_item, plants)

        aa.setDropDownViewResource(android.R.layout.simple_spinner_item)
        date_interval_spinner.adapter = aa

    }

    fun toSimpleString(date: Date?) = with(date ?: Date()) {
        SimpleDateFormat("dd-MM-yyy").format(this)
    }


}
