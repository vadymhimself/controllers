import kotlinx.coroutines.experimental.CoroutineStart.ATOMIC
import kotlinx.coroutines.experimental.NonCancellable
import kotlinx.coroutines.experimental.cancelAndJoin
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlinx.coroutines.experimental.withContext
import org.junit.Test
import java.io.IOException

class LifecycleConsumerTest {

  @Test(expected = IOException::class)
  fun testExceptionNotSwallowedInCancelledCoroutine() = runBlocking {

    val job = launch(start = ATOMIC) {
      withContext(NonCancellable) {
        delay(100)
        println("2. Cancelled coroutine throws an exception that is not swallowed")
        throw IOException()
      }
    }

    println("1. Coroutine cancelled")
    job.cancelAndJoin()
  }

}