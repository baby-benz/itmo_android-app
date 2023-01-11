package com.example.myapplication.contract.task1.data

import com.example.myapplication.contract.task1.Task1ItemDetailsContract

private const val FIBONACCI_MAX_INT_VALUE = 1836311903

class Task1DetailedModel(state: Task1ItemDetailsContract.State) :
    Task1ItemDetailsContract.Model {
    private var natural: Int = state.natural
    private var fibonacci: Int = state.fibonacci
    private var nextFibonacci: Int = state.nextFibonacci
    private var collatz: Int = state.collatz

    override fun nextNatural(): Int {
        return if (natural < Int.MAX_VALUE) {
            natural++
            natural
        } else {
            throw RuntimeException()
        }
    }

    override fun nextFibonacci(): Int {
        return if (fibonacci < FIBONACCI_MAX_INT_VALUE) {
            val tmp = fibonacci + nextFibonacci
            fibonacci = nextFibonacci
            nextFibonacci = tmp
            fibonacci
        } else {
            throw RuntimeException()
        }
    }

    override fun nextCollatz(): Int {
        return if (collatz != 1) {
            if (collatz and 1 == 0) {
                collatz /= 2
            } else {
                collatz = collatz * 3 + 1
            }
            collatz
        } else {
            throw RuntimeException()
        }
    }

    override fun curState(): Task1ItemDetailsContract.State {
        return Task1ItemDetailsContract.State(natural, fibonacci, nextFibonacci, collatz)
    }
}