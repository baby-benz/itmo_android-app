package com.example.myapplication.contract.task1.presenter

import com.example.myapplication.contract.task1.Task1IncrementerContract
import com.example.myapplication.contract.task1.data.IncrementerPersistentStorageModel

class Task1IncrementerPresenter(
    private var view: Task1IncrementerContract.View?,
    private val model: IncrementerPersistentStorageModel
) : Task1IncrementerContract.Presenter {
    init {
        view?.updateIncrementerText(model.getNum().toString())
    }

    override fun onIncrementButtonPressed() {
        var num = model.getNum()
        if (num < 100) {
            num++
            model.updateNum(num)
            view?.updateIncrementerText(num.toString())
        }
    }

    override fun onDecrementButtonPressed() {
        var num = model.getNum()
        if (num > 0) {
            num--
            model.updateNum(num)
            view?.updateIncrementerText(num.toString())
        }
    }

    override fun onDestroy() {
        view = null
    }
}