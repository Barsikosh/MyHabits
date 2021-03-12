package com.example.task3.Adapter

interface ITouchHelperAdapter {

    fun deleteItem(position: Int)

    fun moveItem(startPosition: Int, nextPosition: Int)
}