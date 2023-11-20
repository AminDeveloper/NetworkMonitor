package ir.amin.networkmonitor.Observers;

import ir.amin.networkmonitor.ObserverBase.StatefullObserverHandler


/**
 * Useful for multi method interfaces
 * send command to observers by informing lamda method
 * data will be last command
 */
open class CommandObserver<COMMAND_INTERFACE> : StatefullObserverHandler<COMMAND_INTERFACE, (listener: COMMAND_INTERFACE) -> Unit>() {
    override fun informObserverInternal(listener: COMMAND_INTERFACE, data: ((listener: COMMAND_INTERFACE) -> Unit)?) {
        data?.invoke(listener)
    }
}
