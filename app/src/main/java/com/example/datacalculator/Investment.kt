package com.example.datacalculator

class Investment(var name: String, var quantity: Int, var amount: Double) {

    val stringAmount: String
        get() = String.format("R$ %,.2f", amount)
}