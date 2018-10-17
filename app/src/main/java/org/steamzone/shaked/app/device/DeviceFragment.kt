package org.steamzone.shaked.app.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.steamzone.shaked.R


class DeviceFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_device, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }


    fun gotoDownloadLog(view: View) {
        activity?.supportFragmentManager?.beginTransaction()
                ?.add(R.id.fragment_container, DeviceGetLogFragment(), DeviceGetLogFragment::class.java.name)
                ?.addToBackStack(DeviceGetLogFragment::class.java.name)
                ?.commit()
    }


}
