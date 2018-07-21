package com.controllers

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = StubApp::class)
class StackTest {

  @Test
  fun testIsInTransactionDuringTransaction() {
    val stack = RouterStack<Controller<*>>(StubRouter())
    assertThat(!stack.isInTransaction)

    stack.beginTransaction {
      assertThat(stack.isInTransaction)
      it.commit()
    }

    assertThat(!stack.isInTransaction)
  }

  @Test
  fun testAdd() {
    val stack = RouterStack<Controller<*>>(StubRouter())
    val ctrl = StubController()

    stack.beginTransaction {
      it.add(ctrl)
      it.commit()
    }

    assertThat(stack).containsExactly(ctrl)
  }

  @Test
  fun testPop() {
    val stack = MockRouterStack<Controller<*>>()
    stack.populate(StubController())

    stack.beginTransaction {
      it.pop()
      it.commit()
    }

    assertThat(stack).isEmpty()
  }

  @Test
  fun testPopCount() {
    val stack = MockRouterStack<Controller<*>>()
    val firstCtrl = StubController()

    stack.populate(firstCtrl, StubController(), StubController())

    stack.beginTransaction {
      it.pop(2)
      it.commit()
    }

    assertThat(stack).containsExactly(firstCtrl)
  }

  @Test
  fun testRollBack() {
    val stack = MockRouterStack<Controller<*>>()

    val ctrls = arrayOf(
        StubController(),
        StubController(),
        StubController()
    )

    stack.populate(*ctrls)

    stack.beginTransaction {
      it.pop(2)
      it.rollBack()
    }

    assertThat(stack).containsExactly(*ctrls)
  }

  @Test
  fun testExceptionalRollback() {
    val stack = RouterStack<Controller<*>>(StubRouter())
    val ctrl = StubController()

    try {
      stack.beginTransaction {
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

    val first = StubController()
    val second = StubController()
    val third = StubController()

    val ctrls = arrayOf(
        first,
        second,
        third
    )

    stack.populate(*ctrls)

    ctrls.forEach { assertThat(it.isAttachedToStack) }

    val fourth = StubController()

    stack.beginTransaction {
      it.pop(2)
      it.pop()
      it.add(fourth)
      it.add(first)
      it.commit()
    }

    assertThat(stack).containsExactly(fourth, first)
    assertThat(first.isAttachedToStack)
    assertThat(fourth.isAttachedToStack)
    assertThat(!second.isAttachedToStack)
    assertThat(!second.isAttachedToStack)
  }
}