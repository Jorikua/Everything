package com.example.everything

import com.example.everything.base.TestCoroutineRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class MainViewModelTest {

  @get:Rule
  val rule = TestCoroutineRule()

  @Test
  fun `get item list`() = runBlocking {
    val repo = mock<Repository>()

    val vm = MainViewModel(repo)
    val data = listOf("a", "b")

    whenever(repo.getList()).thenReturn(data)

    val list = vm.data.value!!

    assertEquals(list.size, data.size)
  }
}