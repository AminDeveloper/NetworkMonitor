package ir.amin.networkmonitor.Observers;

import ir.amin.networkmonitor.ObserverBase.StatefullObserverHandler


/**
 *
 */

open class IntObserver private constructor() : StatefullObserverHandler<IntObserver.IntDataObserver, Int>() {

    override fun informObserverInternal(intDataObserver: IntDataObserver, integer: Int?) {
        intDataObserver.onDataChanged(integer!!)
    }

    interface IntDataObserver {
        fun onDataChanged(intData: Int)
    }

    companion object {

        private val observers: ObserverListContainer<IntObserver> =  ObserverListContainer({ IntObserver() } )

        val instance: IntObserver
            get() = observers.instance

        fun getInstance(id: String): IntObserver {

            return observers.getInstance(id)
        }

//        private val IntObservers = HashMap()
//
//        val instance: IntObserver
//            get() = getInstance("default")
//
//        fun getInstance(id: String): IntObserver {
//            var intObserver: IntObserver? = IntObservers.get(id)
//            if (intObserver == null) {
//                val newObserver = IntObserver()
//                intObserver = newObserver
//                IntObservers.put(id, newObserver)
//            }
//            return intObserver
//        }

        fun removeIntObserver(id: String) {
            observers.remove(id)
        }
    }

}
