package ir.amin.networkmonitor.Observers;


import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import ir.amin.networkmonitor.BroadCastReceiver.NetworkChangeReceiver;

public class NetworkStateLiveData extends MutableLiveData<NetworkStateLiveData.NetworkState> {

    static NetworkStateLiveData instance;
    static NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver();


    public static NetworkStateLiveData getInstance() {
        if (instance == null)
            instance = new NetworkStateLiveData();
        return instance;
    }

    public NetworkStateLiveData() {
        super();
    }


    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super NetworkState> observer) {
        super.observe(owner, observer);
    }

    @Override
    public void observeForever(@NonNull Observer<? super NetworkState> observer) {
        super.observeForever(observer);
    }

    public void registerService(Context context) {
        networkChangeReceiver.registerService(context);
        //inform initial value
        NetworkChangeReceiver.Companion.checkNetworkState(context);

    }

    public void unRegisterService(Context context) {
        networkChangeReceiver.unRegisterService(context);
    }


    public static class NetworkState {
        boolean isConnected;

        public NetworkState(boolean isConnected) {
            this.isConnected = isConnected;
        }

        public boolean getIsConnected() {
            return isConnected;
        }
    }
}


