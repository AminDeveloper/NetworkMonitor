package ir.amin.networkmonitor.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun doAsync(job: () -> Any): Job {
    return GlobalScope.launch {
        job.invoke()
    }
}

fun uiThread(job: () -> Any): Job {
    return GlobalScope.launch(Dispatchers.Main)
    {
        job.invoke()
    }
}