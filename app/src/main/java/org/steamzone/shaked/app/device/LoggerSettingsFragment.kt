package org.steamzone.shaked.app.device

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.trello.rxlifecycle2.components.support.RxFragment
import kotlinx.android.synthetic.main.dialog_system_view.*
import kotlinx.android.synthetic.main.fragment_firmware_update.*
import kotlinx.android.synthetic.main.fragment_logger_settings.*
import org.steamzone.shaked.R

class LoggerSettingsFragment : RxFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_logger_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        default_settings_cont.setOnClickListener {

//            activity?.supportFragmentManager?.beginTransaction()?.hide(this)?.add(R.id.fragment_container, DefaultSettingsDialogFragment(), DefaultSettingsDialogFragment::class.java.name)
//                    ?.addToBackStack(DefaultSettingsDialogFragment::class.java.name)
//                    ?.commit()

            var dialogSystem = AlertDialog.Builder(activity)
            var inflater = activity?.layoutInflater
            dialogSystem.setCancelable(false).setView(inflater?.inflate(R.layout.dialog_system_view, null))
            dialogSystem.show()
        }

        key_settings_cont.setOnClickListener {
            var dialogSystem = AlertDialog.Builder(activity)
            var inflater = activity?.layoutInflater
            dialogSystem.setCancelable(false).setView(inflater?.inflate(R.layout.dialog_big_system_view, null))
            dialogSystem.show()
        }

    }
}
