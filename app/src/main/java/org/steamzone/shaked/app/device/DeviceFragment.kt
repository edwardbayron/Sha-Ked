package org.steamzone.shaked.app.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_device.*
import org.steamzone.shaked.R


class DeviceFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        get_log_cont.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()
                    ?.hide(this)
                    ?.add(R.id.fragment_container, DeviceGetLogFragment(), DeviceGetLogFragment::class.java.name)
                    ?.addToBackStack(DeviceGetLogFragment::class.java.name)
                    ?.commit()
        }

    }





}
