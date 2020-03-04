package com.example.everything

import kotlinx.coroutines.delay

open class Repository {

  open suspend fun getList(): List<String> {
    delay(1000)
    return listOf("a", "b", "c")
  }
}