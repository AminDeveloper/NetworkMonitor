package ir.amin.networkmonitor.ObserverBase;


import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * to add a new observer jus make your class singletone and extend this class
 * use informObserverListInternal fun and customize global informs in your class
 *
 * @param <OBSERVER_TYPE>
 */
abstract public class ObserverHandlerBase<OBSERVER_TYPE, OBSERVABLE_TYPE> {

    ConcurrentHashMap<OBSERVER_TYPE, Boolean> observerList = new ConcurrentHashMap();
    LinkedList<ObserverListChangeListener<OBSERVER_TYPE>> observerListChangeListeners = new LinkedList<>();

    public int getObserversCount() {
        return observerList.size();
    }

    public void addObserver(LifecycleOwner owner, OBSERVER_TYPE observer) {
        synchronized (this) {
            setOwner(owner, observer);
            observerList.put(observer, true);
            callBackObserverListChangeListeners(true, observer);
//            SmartLogger.logDebug("observerList size:" + observerList.size());
        }
    }

    public void addObserver(OBSERVER_TYPE observer) {
        addObserver(null, observer);
    }

    public void removeObserver(OBSERVER_TYPE observer) {
        synchronized (this) {
            observerList.remove(observer);
            callBackObserverListChangeListeners(false, observer);
//            SmartLogger.logDebug("observerList size:" + observerList.size());
        }
    }

    protected void informObserverListInternal(OBSERVABLE_TYPE data) {
        synchronized (this) {
//            SmartLogger.logDebug("observerList size:" + observerList.size());

            for (Map.Entry<OBSERVER_TYPE, Boolean> entry : observerList.entrySet()) {
                informObserverInternal(entry.getKey(), data);
            }
        }
    }

    public void informObservers(OBSERVABLE_TYPE data) {
        informObserverListInternal(data);
    }


//    protected void informObserverListInternal(OBSERVABLE_TYPE data) {
//        List<Object> tempList = new LinkedList<>();
//        tempList.add(data);
//        informObserverListInternal(tempList);
//    }

    private void setOwner(LifecycleOwner owner, final OBSERVER_TYPE observer) {
        if (owner == null && observer instanceof LifecycleOwner) {
            owner = (LifecycleOwner) observer;
        }

        if (owner != null)
            owner.getLifecycle().addObserver(new GenericLifecycleObserver() {
                @Override
                public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
                    switch (event) {
//                        case ON_CREATE:
//                            break;
//                        case ON_START:
//                            break;
//                        case ON_RESUME:
//                            break;
//                        case ON_PAUSE:
//                            break;
//                        case ON_STOP:
//                            break;
                        case ON_DESTROY:
                            removeObserver(observer);
                            break;
//                        case ON_ANY:
//                            throw new IllegalArgumentException("ON_ANY must not been send by anybody");
                    }
                }
            });
    }

    private void callBackObserverListChangeListeners(boolean added, OBSERVER_TYPE observer) {
        for (ObserverListChangeListener observerListChangeListener : observerListChangeListeners)
            if (added)
                observerListChangeListener.observeAdded(observer, getObserversCount());
            else
                observerListChangeListener.observerRemoved(observer, getObserversCount());
    }

    public void addObserverChangeListener
            (ObserverListChangeListener<OBSERVER_TYPE> observerListChangeListener) {
        observerListChangeListeners.add(observerListChangeListener);
    }

    public void removeObserverChangeListener
            (ObserverListChangeListener<OBSERVER_TYPE> observerListChangeListener) {
        observerListChangeListeners.remove(observerListChangeListener);
    }

    /**
     * data is list of parameters needed for calling observer
     *
     * @param observe
     * @param data
     */
    abstract protected void informObserverInternal(OBSERVER_TYPE observe, OBSERVABLE_TYPE data)
    ;


    public interface ObserverListChangeListener<OBSERVER_TYPE> {
        void observeAdded(OBSERVER_TYPE observer, int observersCount);

        void observerRemoved(OBSERVER_TYPE observer, int observersCount);
    }

}
