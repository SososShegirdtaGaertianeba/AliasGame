package com.example.alias.util

data class PagingEvent<T>(var data: T, val position: Int, val isHandled: Boolean = false)