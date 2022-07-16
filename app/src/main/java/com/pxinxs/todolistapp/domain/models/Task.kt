package com.pxinxs.todolistapp.domain.models

data class Task(
    val id: String,
    val title: String,
    val description: String,
    var dateInMillis: Long
)