package com.controllers

import com.controllers.core.RouterStack
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = StubApp::class)
class RouterStackTest {

  @Test
  fun testIsInTransactionDuringTransaction() {
    val stack = RouterStack<Controller<*>>()
    assert(!stack.isInTransaction)

    stack.transaction {
      assert(stack.isInTransaction)
    }

    assert(!stack.isInTransaction)
  }

  @Test
  fun testAdd() {
    val stack = RouterStack<Controller<*>>()
    val ctrl = TestController()

    stack.transaction {
      it.add(ctrl)
    }

    assertThat(stack).containsExactly(ctrl)
  }

  @Test
  fun testPop() {
    val stack = MockRouterStack<Controller<*>>()
    stack.populate(TestController())

    stack.transaction {
      it.pop()
    }

    assertThat(stack).isEmpty()
  }

  @Test
  fun testPopCount() {
    val stack = MockRouterStack<Controller<*>>()
    val firstCtrl = TestController()

    stack.populate(firstCtrl, TestController(), TestController())

    stack.transaction {
      it.pop(2)
    }

    assertThat(stack).containsExactly(firstCtrl)
  }

  @Test
  fun testExceptionalRollback() {
    val stack = RouterStack<Controller<*>>()
    val ctrl = TestController()

    try {
      stack.transaction {
        it.add(ctrl)
        throw Exception()
      }
      assert(false, { "Didn't rethrow" })
    } catch (e: Exception) {
      assertThat(stack).isEmpty()
    }

  }

  @Test
  fun testStackNotifications() {
    val stack = MockRouterStack<Controller<*>>()

    val first = TestController()
    val second = TestController()
    val third = TestController()

    val ctrls = arrayOf(
        first,
        second,
        third
    )

    stack.populate(*ctrls)

    ctrls.forEach { assertThat(it.isAttachedToRouter) }

    val fourth = TestController()

    stack.transaction {
      it.pop(2)
      it.pop()
      it.add(fourth)
      it.add(first)
    }

    assertThat(stack).containsExactly(fourth, first)
    assert(first.isAttachedToRouter)
    assert(fourth.isAttachedToRouter)
    assert(!second.isAttachedToRouter)
    assert(!third.isAttachedToRouter)
  }
}