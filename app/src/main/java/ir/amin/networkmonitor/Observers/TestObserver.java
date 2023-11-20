package ir.amin.networkmonitor.Observers;


import java.util.List;

import ir.amin.networkmonitor.ObserverBase.ObserverHandlerBase;

/**
 * Created by Amin on 13/02/2018.
 */

public class TestObserver extends ObserverHandlerBase<TestObserver.ObserverTest, List<String>> {
    private static TestObserver instance;

    public static TestObserver getInstance() {
        if (instance == null)
            instance = new TestObserver();
        return instance;
    }

    @Override
    protected void informObserverInternal(ObserverTest observe, List<String> data) {
        observe.observeChanges(data);
    }

    public interface ObserverTest {
        void observeChanges(List<String> list);
    }
}
