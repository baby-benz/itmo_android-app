package com.example.myapplication.contract.task1

interface Task1ItemDetailsContract {
    data class State(val natural: Int, val fibonacci: Int, val nextFibonacci: Int, val collatz: Int)

    interface View {
        fun displayNatural(value: Int)
        fun displayFibonacci(value: Int)
        fun displayCollatz(value: Int)
        fun showMaxNumberErrorToast()
        fun showLastCollatzMemberErrorToast()
    }

    interface Presenter {
        fun onNaturalButtonPressed()
        fun onFibonacciButtonPressed()
        fun onCollatzButtonPressed()
        fun curState(): State
        fun onDestroy()
    }

    interface Model {
        fun nextNatural(): Int
        fun nextFibonacci(): Int
        fun nextCollatz(): Int
        fun curState(): State
    }
}