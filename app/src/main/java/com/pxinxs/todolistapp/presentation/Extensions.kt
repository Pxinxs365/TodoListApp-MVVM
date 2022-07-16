package com.pxinxs.todolistapp.presentation

import android.util.Log
import android.view.View
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

fun View.visible() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}

fun View.gone() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

fun View.enable() {
    if (!isEnabled) {
        isEnabled = true
    }
}

fun View.disable() {
    if (isEnabled) {
        isEnabled = false
    }
}

@Suppress("FunctionName")
fun Any.TodoListScope(
    dispatcher: CoroutineDispatcher,
    name: CoroutineName = CoroutineName(this@TodoListScope::class.java.simpleName),
    exceptionHandler: CoroutineExceptionHandler = onError()
) = object : CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + name + dispatcher + exceptionHandler
}

private fun onError() = CoroutineExceptionHandler { _, throwable ->
    Log.d("CoroutineException", throwable.localizedMessage ?: throwable.toString())
}