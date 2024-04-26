import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

open class NamedVirtualThreadFactory(val threadNamePrefix: String, maxParallelJobs: Int? = null) : ThreadFactory {
    protected val count = AtomicInteger(0)
    protected val semaphore = maxParallelJobs?.let { Semaphore(it) }

    override fun newThread(task: Runnable): Thread {
       val decoratedTask= if (semaphore != null) {
            Runnable {
                semaphore.acquire(1)
                try {
                    task.run()
                } finally {
                    semaphore.release(1)
                }
            }
        } else {
            task
        }
        return Thread.ofVirtual().name("$threadNamePrefix-${count.incrementAndGet()}").start(decoratedTask)
    }

    companion object {
        fun createNamed(threadNamePrefix: String, maxParallelJobs: Int? = null): ExecutorService {
            return Executors.newThreadPerTaskExecutor(NamedVirtualThreadFactory(threadNamePrefix,maxParallelJobs))
        }
    }
}