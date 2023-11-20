package ir.amin.networkmonitor.utils.RetryHelper

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import ir.amin.networkmonitor.Observers.NetworkStateLiveData

object DefaultNetworkErrorDialog {

    fun startShowingDefaultNetworkErrorDialog(context: Context?, owner: LifecycleOwner) {
        NetworkStateLiveData.getInstance().registerService(context)
        NetworkStateLiveData.getInstance().observe(owner, Observer {
            handleNetworkEvent(it, context)

        })
    }

    fun startShowingDefaultNetworkErrorDialog(context: Context?) {
        if(context is LifecycleOwner)
            startShowingDefaultNetworkErrorDialog(context,context as LifecycleOwner)
        else {
            NetworkStateLiveData.getInstance().registerService(context)
            NetworkStateLiveData.getInstance().observeForever {
                handleNetworkEvent(it, context)
            }
        }
    }

    fun stopShowingDefaultNetworkErrorDialog(context: Context?) {
        RetryHelper.dismisDialog()
        NetworkStateLiveData.getInstance().unRegisterService(context)

    }

    private fun handleNetworkEvent(networkState: NetworkStateLiveData.NetworkState?, context: Context?) {
        networkState?.let {
            if (it.isConnected)
                RetryHelper.dismisDialog()
            else
                context?.let { RetryHelper.showDialog(it) }

        }
    }

}