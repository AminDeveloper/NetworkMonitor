package ir.amin.networkmonitor.Observers

import androidx.core.widget.NestedScrollView

class NestedScrollObserver : CommandObserver<NestedScrollView.OnScrollChangeListener>() {
    companion object {

        private val observers: ObserverListContainer<NestedScrollObserver> = ObserverListContainer({ NestedScrollObserver() })

        fun getInstance(nestedScrollView: NestedScrollView): NestedScrollObserver {
            val nestedScrollObserver = observers.getInstance(nestedScrollView.hashCode().toString())
            nestedScrollObserver.setNestedScroll(nestedScrollView)
            return nestedScrollObserver
        }

        fun removeNestedScrollObserver(nestedScrollView: NestedScrollView) {
            observers.remove(nestedScrollView.hashCode().toString())
        }
    }

    lateinit var nestedScrollView: NestedScrollView
    private fun setNestedScroll(nestedScrollView: NestedScrollView) {
        this.nestedScrollView = nestedScrollView
        nestedScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            informObservers { it.onScrollChange(v, scrollX, scrollY, oldScrollX, oldScrollY) }
        })
    }

    override fun removeObserver(observer: NestedScrollView.OnScrollChangeListener?) {
        super.removeObserver(observer)
        if(observersCount==0)
            removeNestedScrollObserver(nestedScrollView)
    }
}
