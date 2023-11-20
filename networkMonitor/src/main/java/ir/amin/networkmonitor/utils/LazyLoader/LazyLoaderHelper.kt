package ir.amin.networkmonitor.utils.LazyLoader

import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import ir.amin.networkmonitor.Observers.NestedScrollObserver
import ir.amin.networkmonitor.utils.RetryHelper.RetryHelper

/**
 *
 */
class LazyLoaderHelper {

    private var currentIndex = 0
    private var loadingService = false
    private val loadRegion = 4//item count to start loading more for recycler view
    var stepCount = 20//just change it before start
        private set

    private var loaderMethod: ((currentIndex: Int) -> Any)? = null
    private var finished = false
    private var recyclerView: RecyclerView? = null
    private var nestedScrollView: NestedScrollView? = null
    var ProgressBar: View? = null
    var retryHelper: RetryHelper? = null
    var lifecycleOwner: LifecycleOwner? = null

    val nestedListener = NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
        if (finished)
            return@OnScrollChangeListener
        if (scrollY > oldScrollY) {
//                Log.i(TAG, "Scroll DOWN")
        }
        if (scrollY < oldScrollY) {
//                Log.i(TAG, "Scroll UP")
        }

        if (scrollY == 0) {
//                Log.i(TAG, "TOP SCROLL")
        }


        if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
//                Log.i(TAG, "BOTTOM SCROLL")
            loadMoreItems()
        }
    }
    val recyclerListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (finished)
                return


            if (checkBottomScrollState())
                loadMoreItems()


//            if (recyclerView!!.computeVerticalScrollOffset() ==recyclerView!!.getChildAt(0).measuredHeight - recyclerView!!.measuredHeight) {
//                Log.i(TAG, "BOTTOM SCROLL")
//                loadMoreItems()
//            }
//            if (dy > 0) {
//                //check for scroll down
//
//                val visibleItemCount = recyclerView!!.layoutManager.childCount
//                val totalItemCount = recyclerView.layoutManager.itemCount
//                val pastVisiblesItems = (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
//
//                // Log.i(TAG, "onScrolled: " + (totalItemCount) + "");
//
//
//                     if (visibleItemCount + pastVisiblesItems >= totalItemCount - loadRegion)
//                        loadMoreItems()
//
//            }
        }
    }

    private fun checkBottomScrollState(): Boolean {
//        SmartLogger.logDebug("scroll" + (recyclerView!!.computeVerticalScrollRange() -
//                recyclerView!!.computeVerticalScrollOffset()
//                ).toString())
////
////        SmartLogger.logDebug("scroll range:" + (recyclerView!!.computeVerticalScrollRange()).toString())
////
////        SmartLogger.logDebug("scroll offset:" + (recyclerView!!.computeVerticalScrollOffset()).toString())
////
////        SmartLogger.logDebug("scroll Extent:" + (recyclerView!!.computeVerticalScrollExtent()).toString())
//
//        SmartLogger.logDebug("hello " + (recyclerView!!.computeVerticalScrollRange() != 0 &&
//                (recyclerView!!.computeVerticalScrollRange() -
//                        recyclerView!!.computeVerticalScrollOffset() -
//
//                        recyclerView!!.computeVerticalScrollExtent() <= 300)).toString())

        return recyclerView!!.computeVerticalScrollRange() != 0 &&
                (recyclerView!!.computeVerticalScrollRange() -
                        recyclerView!!.computeVerticalScrollOffset() -
                        recyclerView!!.computeVerticalScrollExtent() <= 300)

//        val linearLayoutManager = recyclerView!!.layoutManager as LinearLayoutManager
//        val position = linearLayoutManager.findLastVisibleItemPosition()
//        val rect = Rect()
//        var lastVisibleView = linearLayoutManager.findViewByPosition(position)
//        if (lastVisibleView != null) {
//            lastVisibleView.getGlobalVisibleRect(rect)
//
//            if (rect.bottom - rect.top <= 300) {
////                Log.i(TAG, "BOTTOM SCROLL")
//                return true
//            }
//        }
//        return false
    }

    fun initAndStart(mRecyclerView: RecyclerView, loaderMethod: ((currentIndex: Int) -> Any), progressBar: View? = null, stepCount: Int? = null, lifecycleOwner: LifecycleOwner? = null) {
        stepCount?.let {
            this.stepCount = stepCount
        }
        initFields()
        this.loaderMethod = loaderMethod
        this.recyclerView = mRecyclerView
        retryHelper = RetryHelper(mRecyclerView.context)
        this.lifecycleOwner=lifecycleOwner

        mRecyclerView.addOnScrollListener(recyclerListener)
        loadMoreItems()
        this.ProgressBar = progressBar

    }

    private fun initFields() {
        nestedScrollView = null
        recyclerView = null
        currentIndex = -stepCount
        loadingService = false
        ProgressBar?.visibility = View.GONE
        ProgressBar = null
        finishedLazyLoading()

        finished = false
    }


    fun initAndStart(nestedScrollView: NestedScrollView, loaderMethod: ((currentIndex: Int) -> Any), progressBar: View? = null, lifecycleOwner: LifecycleOwner? = null) {
        initFields()
        this.loaderMethod = loaderMethod
        this.nestedScrollView = nestedScrollView
        retryHelper = RetryHelper(nestedScrollView.context)
        this.lifecycleOwner=lifecycleOwner

        NestedScrollObserver.getInstance(nestedScrollView).addObserver(nestedListener)
        loadMoreItems()
        this.ProgressBar = progressBar

    }

    private fun loadMoreItems(addIndex: Boolean = true) {
        if (!loadingService && !finished) {
            loadingService = true
            ProgressBar?.visibility = View.VISIBLE
            if (addIndex)
                currentIndex += stepCount
            retryHelper!!.initializeAndCall({ loaderMethod?.invoke(currentIndex) },lifecycleOwner)

        }
    }

    fun serviceLoaded() {
        loadingService = false
        ProgressBar?.visibility = View.GONE
        checkIsScrollable()
    }

    fun retryLastSection() {
        if (!finished) {
//            Thread.sleep(delay)
//            loadingService = false
//            loadMoreItems(false)
            retryHelper?.retry()
        }
    }

    fun pauseRetry() {
        retryHelper?.pauseThisRetry()
    }

    fun resumeRetry() {
        retryHelper?.resumeThisRetry()
    }


    fun finishedLazyLoading() {
        finished = true
        ProgressBar?.visibility = View.GONE

        nestedScrollView?.let {
            NestedScrollObserver.getInstance(nestedScrollView!!).removeObserver(nestedListener)
        }
        recyclerView?.removeOnScrollListener(recyclerListener)
        retryHelper?.finished()
    }

    fun removeObservers() {
        finishedLazyLoading()
    }

    private fun checkIsScrollable() {
        recyclerView?.let {
            if (checkBottomScrollState())
                loadMoreItems()
        }
        nestedScrollView?.let {
            nestedScrollView?.post({
                if (measureAllHeights(nestedScrollView!!) <= nestedScrollView!!.measuredHeight)
                    loadMoreItems()
            })
        }

    }

    private fun measureAllHeights(nestedScrollView: NestedScrollView): Int {
        var total = 0
        for (i in 0 until nestedScrollView!!.childCount)
            total += nestedScrollView!!.getChildAt(i).measuredHeight
        return total
    }
}
