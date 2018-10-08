package org.steamzone.shaked.box

import io.objectbox.Box
import io.objectbox.BoxStore



object SBox {

    fun <T> getBox(t: Class<T>): Box<T> {
        return BoxStore.getDefault().boxFor(t)
    }
}