package com.pxinxs.todolistapp.presentation.base

abstract class BasePresenter<V : BaseView, R : BaseRouter> {

    var view: V? = null; private set
    var router: R? = null; private set

    open fun onTakeView(view: V) {
        this.view = view
    }

    open fun onTakeRouter(router: R) {
        this.router = router
    }

    open fun onDropView() {
        view = null
    }

    open fun onDropRouter() {
        router = null
    }
}