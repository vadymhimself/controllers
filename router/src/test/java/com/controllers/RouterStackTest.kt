package com.controllers

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
    val stack = RouterStack<Controller<*>>(StubRouter())
    assert(!stack.isInTransaction)

    stack.transaction {
      assert(stack.isInTransaction)
      it.commit()
    }

    assert(!stack.isInTransaction)
  }

  @Test
  fun testAdd() {
    val stack = RouterStack<Controller<*>>(StubRouter())
    val ctrl = TestController()

    stack.transaction {
      it.add(ctrl)
      it.commit()
    }

    assertThat(stack).containsExactly(ctrl)
  }

  @Test
  fun testPop() {
    val stack = MockRouterStack<Controller<*>>()
    stack.populate(TestController())

    stack.transaction {
      it.pop()
      it.commit()
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
      it.commit()
    }

    assertThat(stack).containsExactly(firstCtrl)
  }

  @Test
  fun testRollBack() {
    val stack = MockRouterStack<Controller<*>>()

    val ctrls = arrayOf(
        TestController(),
        TestController(),
        TestController()
    )

    stack.populate(*ctrls)

    stack.transaction {
      it.pop(2)
      it.rollBack()
    }

    assertThat(stack).containsExactly(*ctrls)
  }

  @Test
  fun testExceptionalRollback() {
    val stack = RouterStack<Controller<*>>(StubRouter())
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

    ctrls.forEach { assertThat(it.isAttachedToStack) }

    val fourth = TestController()

    stack.transaction {
      it.pop(2)
      it.pop()
      it.add(fourth)
      it.add(first)
      it.commit()
    }

    assertThat(stack).containsExactly(fourth, first)
    assert(first.isAttachedToStack)
    assert(fourth.isAttachedToStack)
    assert(!second.isAttachedToStack)
    assert(!third.isAttachedToStack)
  }
}