package ir.amin.networkmonitor.Observers;

import ir.amin.networkmonitor.ObserverBase.StatefullObserverHandler


class StringObserver : StatefullObserverHandler<StringObserver.StringDataObserver, String>() {


    override fun informObserverInternal(intDataObserver: StringDataObserver, string: String) {
        intDataObserver.onDataChanged(string)
    }

    interface StringDataObserver {
        fun onDataChanged(stringData: String)
    }

    companion object {
        private val observers: ObserverListContainer<StringObserver> =  ObserverListContainer({ StringObserver() } )

        val instance: StringObserver
            get() = observers.instance

        fun getInstance(id: String): StringObserver {

            return observers.getInstance(id)
        }

//        private val IntObservers = HashMap()
//
//        val instance: StringObserver
//            get() = getInstance("default")
//
//        fun getInstance(id: String): StringObserver {
//            var intObserver: StringObserver? = IntObservers.get(id)
//            if (intObserver == null) {
//                val newObserver = StringObserver()
//                intObserver = newObserver
//                IntObservers.put(id, newObserver)
//            }
//            return intObserver
//        }

        fun removeStringObserver(id: String) {
            observers.remove(id)
        }
    }

}



