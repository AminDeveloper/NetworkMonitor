package ir.amin.networkmonitor.Observers;

import android.content.Context;
import ir.amin.networkmonitor.BroadCastReceiver.NetworkChangeReceiver;
import ir.amin.networkmonitor.ObserverBase.StatefullObserverHandler;
import ir.amin.networkmonitor.utils.Utils;


/**
 * Created by Amin on 03/12/2017.
 */

public class NetworkObserverHandler extends StatefullObserverHandler<NetworkObserverHandler.NetworkChangeObserver, Boolean> {
    static NetworkObserverHandler instance;
    static NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();

    public static NetworkObserverHandler getInstance() {
        if (instance == null)
            instance = new NetworkObserverHandler();
        return instance;
    }

    private NetworkObserverHandler() {

    }


    /**
     * notifies listener when network status changed
     * it will immediately notify the current state
     * do not forget to removeObserver when it is not nessessary to avoid memory leak!
     *
     * @param observer
     */
    @Override
    public void addObserver(NetworkChangeObserver observer) {
        super.addObserver(observer);
        networkChangeReceiver.registerService(observer.getContextForNetworkObserver());
            if (getObserversCount() == 1)//for the first time
                networkChangeReceiver.registerService(observer.getContextForNetworkObserver());
        informObservers(Utils.isNetworkAvailable(observer.getContextForNetworkObserver()));
    }

    @Override
    public void removeObserver(NetworkChangeObserver observer) {
        int lastCont = getObserversCount();
        super.removeObserver(observer);
            if (getObserversCount() == 0 && lastCont == 1)//for the last time
                networkChangeReceiver.unRegisterService(observer.getContextForNetworkObserver());

    }

    @Override
    protected void informObserverInternal(NetworkChangeObserver observe, Boolean data) {
        observe.onNetworkStateChange(data);
    }

    public interface NetworkChangeObserver {
        void onNetworkStateChange(Boolean connected);
        Context getContextForNetworkObserver();
    }
}

