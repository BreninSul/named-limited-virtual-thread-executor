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

open class VirtualNamedLimitedThreadFactory(val threadNamePrefix: String,
                                            maxParallelJobs: Int? = null,
                                            val uncaughtExceptionHandler: Thread.UncaughtExceptionHandler?=null,
                                            val inheritThreadLocals:Boolean?=null
) : CountedThreadFactory {

    protected open val activeTasksCount = AtomicLong(0)
    protected open val totalTasksCount = AtomicLong(0)
    protected open val semaphore = maxParallelJobs?.let { Semaphore(it) }

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
    override fun newThread(task: Runnable): Thread {
        totalTasksCount.incrementAndGet()
        try {
            val decoratedTask = if (semaphore == null) task else LimitedRunnable(semaphore!!, task)
            val countedTask = CountedRunnable(activeTasksCount, decoratedTask)
            val threadBuilder = Thread.ofVirtual().name("$threadNamePrefix-${activeTasksCount.incrementAndGet()}")
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
    override fun getActiveTasksCount() = activeTasksCount.get()
    override fun getTotalTasksCount(): Long =totalTasksCount.get()
}