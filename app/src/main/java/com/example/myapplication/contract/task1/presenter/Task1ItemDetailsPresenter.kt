package com.example.myapplication.contract.task1.presenter

import com.example.myapplication.contract.task1.Task1ItemDetailsContract

class Task1ItemDetailsPresenter(
    private var view: Task1ItemDetailsContract.View?,
    private val model: Task1ItemDetailsContract.Model
) : Task1ItemDetailsContract.Presenter {
    override fun onNaturalButtonPressed() {
        try {
            view?.displayNatural(model.nextNatural())
        } catch (ex: RuntimeException) {
            view?.showMaxNumberErrorToast()
        }
    }

    override fun onFibonacciButtonPressed() {
        try {
            view?.displayFibonacci(model.nextFibonacci())
        } catch (ex: RuntimeException) {
            view?.showMaxNumberErrorToast()
        }
    }

    override fun onCollatzButtonPressed() {
        try {
            view?.displayCollatz(model.nextCollatz())
        } catch (ex: RuntimeException) {
            view?.showLastCollatzMemberErrorToast()
        }
    }

    override fun curState(): Task1ItemDetailsContract.State {
        return model.curState()
    }

    override fun onDestroy() {
        this.view = null
    }
}