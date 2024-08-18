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

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * VirtualNamedLimitedExecutorService is an open class that extends the CountedExecutorService interface and implements the ExecutorService interface.
 * It is used to create a thread pool for executing tasks with limited concurrency.
 * The class takes a thread name prefix and an optional maximum parallel jobs as constructor parameters.
 *
 * @param threadNamePrefix The prefix for the name of the created threads.
 * @param maxParallelJobs The maximum number of parallel jobs that can be executed at a time. If null, no concurrency limit is imposed.
 * @param threadFactory The thread factory used to create new threads. If not specified, a default implementation, VirtualNamedLimitedThreadFactory, is used.
 */
open class VirtualNamedLimitedExecutorService(
    threadNamePrefix: String,
    maxParallelJobs: Int? = null,
    protected open val threadFactory: CountedThreadFactory =
        VirtualNamedLimitedThreadFactory(
            threadNamePrefix,
            maxParallelJobs,
        )
) : CountedExecutorService,
    ExecutorService by Executors.newThreadPerTaskExecutor(threadFactory) {
    /**
     * Retrieves the count of active tasks in the current thread factory.
     *
     * @return The count of active tasks as a Long value.
     */
    override fun getActiveTasksCount(): Long {
        return threadFactory.getActiveTasksCount()
    }

    /**
     * Retrieves the total count of tasks executed by the thread factory.
     *
     * This method returns the total count of tasks executed by the thread factory. The count is stored in an AtomicLong
     * variable to ensure thread-safe access. The total count is incremented whenever a new thread is created and executed.
     *
     * @return The total count of tasks executed as a Long value.
     */
    override fun getTotalTasksCount(): Long {
        return threadFactory.getTotalTasksCount()
    }
}
