package com.example.domain.repository

interface IRepository<T> {

    fun addItem(item: T)

    fun removeItem(item: T)

    fun updateItem(item: T)
}