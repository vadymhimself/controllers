package com.controllers.coroutines

import com.controllers.Controller
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.withContext

import kotlinx.coroutines.experimental.CommonPool as BG_CONTEXT
import kotlinx.coroutines.experimental.android.UI as UI_CONTEXT

inline fun <T : Controller<*>> T.async(crossinline block: suspend AsyncHandle.() -> Unit) {
    launch(UI_CONTEXT) {
        val asyncHandle = AsyncHandle()
        block(asyncHandle)
    }
}

class AsyncHandle {
    suspend fun <R> await(block: suspend () -> R) : R {
        return withContext(BG_CONTEXT) {
            block()
        }
    }
}