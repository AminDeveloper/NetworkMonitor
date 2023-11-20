package ir.amin.networkmonitor.utils.RetryHelper

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import ir.amin.networkmonitor.utils.RetryHelper.RetryHelper

class RetryHelperLive(val retryHelper: RetryHelper) : LiveData<String>() {
    override fun onActive() {
        super.onActive()
        retryHelper.resumeThisRetry()

    }

    override fun removeObserver(observer: Observer<in String>) {
        super.removeObserver(observer)
        if (!hasObservers()) {
            retryHelper.disable()
//            SmartLogger.logDebug("disabled")
        }
    }

    override fun onInactive() {
        super.onInactive()
        if(!hasActiveObservers())
            retryHelper.pauseThisRetry()
    }


    fun invoke() {
        value = "call"

    }
}