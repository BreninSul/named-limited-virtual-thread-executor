/*
 * MIT License
 * Copyright (c) 2024 BreninSul
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.github.breninsul.namedlimitedvirtualthreadexecutor.service

import java.util.concurrent.Semaphore
import java.util.concurrent.atomic.AtomicLong

/**
 * VirtualNamedLimitedThreadFactory is an open class that implements the CountedThreadFactory interface.
 * It is used to create new threads for executing tasks with limited concurrency.
 * The class takes the thread name prefix, optional maximum parallel jobs, optional uncaught exception handler, and optional flag to inherit thread locals as constructor parameters
 * .
 *
 * @param threadNamePrefix The prefix for the name of the created threads.
 * @param maxParallelJobs The maximum number of parallel jobs that can be executed at a time. If null, no concurrency limit is imposed.
 * @param uncaughtExceptionHandler The handler for uncaught exceptions in the created threads. If null, no specific exception handler is set.
 * @param inheritThreadLocals Flag that determines whether the created threads should inherit thread locals. If null, the default behavior is used.
 */
open class VirtualNamedLimitedThreadFactory(val threadNamePrefix: String,
                                            maxParallelJobs: Int? = null,
                                            val uncaughtExceptionHandler: Thread.UncaughtExceptionHandler?=null,
                                            val inheritThreadLocals:Boolean?=null
) : CountedThreadFactory {
    /**
     * `threadCounter` is a protected open property of type `AtomicLong`. It is a counter that is used to generate unique thread names when creating new threads in the `VirtualNamed
     * LimitedThreadFactory` class.
     *
     * @see VirtualNamedLimitedThreadFactory
     */
    protected open val threadCounter = AtomicLong(0)

    /**
     * Represents the count of active tasks in the current thread factory.
     * The count is stored in an AtomicLong variable to ensure thread-safe access.
     * This variable is incremented whenever a new task is created and decremented when a task is finished.
     *
     * @property activeTasksCount The current count of active tasks.
     */
    protected open val activeTasksCount = AtomicLong(0)

    /**
     * Represents the total count of tasks executed by the thread factory.
     *
     * This variable is a protected property that holds an instance of AtomicLong, which provides atomic operations on a single value.
     * The initial value of the total tasks count is 0.
     *
     * Example usage:
     *
     * protected open val totalTasksCount = AtomicLong(0)
     *
     * Functiin newThread(task:Runnable) increments the totalTasksCount by one when a new thread is created and executed.
     *
     * Function getTotalTasksCount() returns the current value of totalTasksCount.
     *
     * Classes CountedThreadFactory, LimitedRunnable, and CountedRunnable make use of the totalTasksCount property.
     */
    protected open val totalTasksCount = AtomicLong(0)

    /**
     * The `semaphore` variable is a protected open property of type `Semaphore`.
     * It is initialized with the value of `maxParallelJobs` if it is not null, otherwise it is initialized to null.
     * The `Semaphore` class is used to control the concurrency limit of executing tasks.
     *
     * @see Semaphore
     */
    protected open val semaphore = maxParallelJobs?.let { Semaphore(it) }

    /**
     * LimitedRunnable is an open class that implements the Runnable interface. It is used to execute a task with limited concurrency using a Semaphore.
     * The class takes a Semaphore and a parent task as constructor parameters.
     *
     * @param semaphore The Semaphore to control the concurrency limit.
     * @param parentTask The parent task to be executed.
     */
    open class LimitedRunnable(protected val semaphore: Semaphore, protected val parentTask: Runnable) : Runnable {
        override fun run() {
            semaphore.acquire(1)
            try {
                parentTask.run()
            } catch (e: InterruptedException) {
                Thread.currentThread().interrupt();
            } finally {
                semaphore.release(1)
            }
        }
    }

    /**
     * CountedRunnable is an open class that implements the Runnable interface.
     * It is used to execute a task and keep track of the number of active tasks using an AtomicLong counter.
     * The class takes an AtomicLong counter and a parent task as constructor parameters.
     *
     * @param count The AtomicLong counter to keep track of the number of active tasks.
     * @param parentTask The parent task to be executed.
     */
    open class CountedRunnable(protected val count:AtomicLong ,protected val parentTask: Runnable) : Runnable {
        override fun run() {
            count.incrementAndGet()
            try {
                parentTask.run()
            } finally {
                count.decrementAndGet()
            }
        }
    }

    /**
     * Creates a new thread that executes the given task.
     *
     * @param task The task to be executed by the new thread.
     * @return The newly created thread.
     */
    override fun newThread(task: Runnable): Thread {
        totalTasksCount.incrementAndGet()
        try {
            val decoratedTask = if (semaphore == null) task else LimitedRunnable(semaphore!!, task)
            val countedTask = CountedRunnable(activeTasksCount, decoratedTask)
            val threadBuilder = Thread.ofVirtual().name("$threadNamePrefix-${threadCounter.incrementAndGet()}")
            if (uncaughtExceptionHandler != null) {
                threadBuilder.uncaughtExceptionHandler(uncaughtExceptionHandler)
            }
            if (inheritThreadLocals != null) {
                threadBuilder.inheritInheritableThreadLocals(inheritThreadLocals)
            }
            val thread = threadBuilder.unstarted(countedTask)
            return thread
        }finally {
            totalTasksCount.decrementAndGet()
        }
    }

    /**
     * Retrieves the count of active tasks in the current thread factory.
     *
     * @return The count of active tasks.
     */
    override fun getActiveTasksCount() = activeTasksCount.get()

    /**
     * Retrieves the total count of tasks executed by the thread factory.
     *
     * @return The total count of tasks executed.
     */
    override fun getTotalTasksCount(): Long =totalTasksCount.get()
}