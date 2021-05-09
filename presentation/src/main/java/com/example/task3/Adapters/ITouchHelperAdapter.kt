package com.example.task3.Adapters

interface ITouchHelperAdapter {

    fun deleteItem(position: Int)

    fun moveItem(startPosition: Int, nextPosition: Int)
}