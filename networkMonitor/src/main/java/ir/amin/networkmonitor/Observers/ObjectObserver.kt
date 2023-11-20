package ir.amin.networkmonitor.Observers;

import ir.amin.networkmonitor.ObserverBase.StatefullObserverHandler


/**
 * Created by Amin on 17/02/2018.
 */

class ObjectObserver<DATA_TYPE> protected constructor() : StatefullObserverHandler<(data: Any?) -> Any, DATA_TYPE>() {

    fun addObserverWithInit(observer: (data: Any?)->Any) {
        super.addObserver(observer)
        observer.invoke(data)

    }

    override fun informObserverInternal(dataObserver: (data: Any?)->Any, data_type: DATA_TYPE) {
        dataObserver.invoke(data_type)
    }

//    interface DataObserver {
//        fun onDataChanged(data: Any)
//    }

    companion object {
        private val observers: ObserverListContainer<ObjectObserver<Any>> =  ObserverListContainer({ ObjectObserver<Any>() } )

        val instance: ObjectObserver<Any>
            get() = observers.instance

        fun getInstance(id: String): ObjectObserver<Any> {

            return observers.getInstance(id)
        }


//        private val ObjectObservers = HashMap()
//
//        val instance: ObjectObserver<*>
//            get() = getInstance("default")
//
//        fun getInstance(id: String): ObjectObserver<*> {
//
//            var objectObserver: ObjectObserver<*>? = ObjectObservers.get(id)
//            if (objectObserver == null) {
//                val newObserver = ObjectObserver()
//                objectObserver = newObserver
//                ObjectObservers.put(id, newObserver)
//            }
//            return objectObserver
//        }

        fun removeObjectObserver(id: String) {
            observers.remove(id)
        }
    }
}