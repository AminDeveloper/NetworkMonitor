package ir.amin.networkmonitor.Observers;

import java.util.HashMap

/**
 * helper for retriving observer instance by id
 */
class ObserverListContainer<OBSERVER_TYPE>(var creator: () -> OBSERVER_TYPE) {

    private val observers = HashMap<String, OBSERVER_TYPE>()

    val instance: OBSERVER_TYPE
        get() = getInstance("default")

    fun getInstance(id: String): OBSERVER_TYPE {

        var observer: OBSERVER_TYPE? = observers[id]
        if (observer == null) {
            val newObserver = creator.invoke()
            observer = newObserver
            observers[id] = newObserver
        }
        return observer!!
    }

    fun remove(id: String) {
        observers.remove(id)
    }
}
