package ir.amin.networkmonitor.ObserverBase;

/**
 * Created by Amin on 13/02/2018.
 */

public abstract class StatefullObserverHandler<OBSERVER_TYPE,OBSERVABLE_TYPE> extends ObserverHandlerBase<OBSERVER_TYPE,OBSERVABLE_TYPE> {
    OBSERVABLE_TYPE data;
    @Override
    protected void informObserverListInternal(OBSERVABLE_TYPE data) {
        this.data=data;
        super.informObserverListInternal(data);
    }

    public OBSERVABLE_TYPE getData() {
        return data;
    }
}
