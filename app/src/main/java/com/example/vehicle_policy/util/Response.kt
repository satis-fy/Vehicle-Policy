package com.example.vehicle_policy.util

sealed class Response {
    object Valid : Response()
    data class NotValid(val isEmpty: Boolean = false, val higherPurchaseDate: Boolean = false) :
        Response()
}