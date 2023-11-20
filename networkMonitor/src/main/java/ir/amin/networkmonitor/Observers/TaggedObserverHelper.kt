package ir.amin.networkmonitor.Observers;

import ir.amin.networkmonitor.ObserverBase.ObserverHandlerBase
import java.util.*

/**
 * Created by Amin on 05/03/2018.
 */
class TaggedObserverHelper {

    private val observers = HashMap<String, ObserverHandlerBase<Any, Any>>()


    fun getInstance(creator: () -> ObserverHandlerBase<Any, Any>): ObserverHandlerBase<Any, Any> {
        return getInstance("default", creator)
    }

    fun getInstance(id: String, creator: () -> ObserverHandlerBase<Any, Any>): ObserverHandlerBase<Any, Any> {

        var objectObserver: ObserverHandlerBase<Any, Any>? = observers[id]
        if (objectObserver == null) {
            objectObserver = creator.invoke()
            observers.put(id, objectObserver)
        }
        return objectObserver!!
    }
    fun retriveInctance(id:String): ObserverHandlerBase<Any, Any>? {
        var objectObserver: ObserverHandlerBase<Any, Any>? = observers[id]
        return objectObserver

    }

    fun getAllInctance() = observers.values


//    protected  abstract fun createObserver(): ObserverHandlerBase<OBSERVER_TYPE, OBSERVABLE_TYPE>
//    interface Creator{
//        fun createObserver(): ObserverHandlerBase<Any, Any>
//    }

    //SINGLETONE
    //    companion object {
//         var instance: DownloadObserver?=null
//            private set
//        get() {
//            if(field==null)
//                instance= DownloadObserver()
//            return field
//        }
//    }


}