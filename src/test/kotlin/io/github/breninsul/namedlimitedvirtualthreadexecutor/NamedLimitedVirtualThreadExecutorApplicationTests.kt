package io.github.breninsul.namedlimitedvirtualthreadexecutor

import io.github.breninsul.namedlimitedvirtualthreadexecutor.service.VirtualNamedLimitedExecutorService
import io.github.breninsul.namedlimitedvirtualthreadexecutor.service.blocking.LimitedBackpressureBlockingExecutor
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime
import java.util.concurrent.Callable


class NamedLimitedVirtualThreadExecutorApplicationTests {
    class WaitingCallable(val waitDuration: Duration) : Callable<Pair< LocalDateTime,LocalDateTime>> {
        override fun call(): Pair< LocalDateTime,LocalDateTime> {
            val start = LocalDateTime.now()
            Thread.sleep(waitDuration)
            val end = LocalDateTime.now()
            return start to end
        }
    }

    @Test
    fun testLimited() {
        val maxParallelJobs = 100
        val taskMillis = 1000L
        val executor = VirtualNamedLimitedExecutorService("test", maxParallelJobs)
        val waitDuration = Duration.ofMillis(taskMillis)
        val startTime = LocalDateTime.now()
        val allTasks = (1..maxParallelJobs * 2).map { executor.submit ( WaitingCallable(waitDuration) ) }
        val processed = allTasks.map { it.get() }
        val timePassed = Duration.between(startTime, LocalDateTime.now())
        assertTrue(timePassed.toMillis() >= taskMillis * 2)
        assertTrue(timePassed.toMillis() <= taskMillis * 3)
    }

    @Test
    fun testUnlimited() {
        val taskMillis = 1000L
        val executor= VirtualNamedLimitedExecutorService("test")
        val waitDuration = Duration.ofMillis(taskMillis)
        val startTime = LocalDateTime.now()
        val allTasks = (1..200).map { executor.submit(WaitingCallable(waitDuration)) }
        val processed = allTasks.map { it.get() }
        val timePassed = Duration.between(startTime, LocalDateTime.now())
        assertTrue(timePassed.toMillis() >= taskMillis)
        assertTrue(timePassed.toMillis() < taskMillis * 2)
    }


    @Test
    fun testBlocking() {
        val taskMillis = 1000L
        val executor= LimitedBackpressureBlockingExecutor.buildVirtual("test",5)
        val waitDuration = Duration.ofMillis(taskMillis)
        val startTime = LocalDateTime.now()
        val allTasks = (1..15).map { executor.submit(WaitingCallable(waitDuration)) }
        val timePassedAfterSubmit = Duration.between(startTime, LocalDateTime.now())
        val processed = allTasks.map { it.get() }
        val timePassed = Duration.between(startTime, LocalDateTime.now())
        assertTrue(timePassed.toMillis() >= taskMillis*3)
        assertTrue(timePassed.toMillis() < taskMillis * 4)
    }
}
