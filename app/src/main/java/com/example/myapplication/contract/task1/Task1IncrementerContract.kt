package com.example.myapplication.contract.task1

interface Task1IncrementerContract {
    interface View {
        fun updateIncrementerText(text: String)
    }

    interface Presenter {
        fun onIncrementButtonPressed()
        fun onDecrementButtonPressed()
        fun onDestroy()
    }

    interface Model {
        fun updateNum(value: Int)
        fun getNum(): Int
    }
}