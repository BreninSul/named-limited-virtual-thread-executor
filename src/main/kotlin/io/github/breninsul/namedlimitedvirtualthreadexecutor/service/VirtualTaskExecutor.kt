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

/**
 * VirtualTaskExecutor is an open class that extends the CountedTaskExecutor interface.
 * It is used to execute tasks on virtual threads with limited concurrency.
 * The class takes a thread name prefix and an optional maximum number of parallel jobs as constructor parameters.
 *
 * @param threadNamePrefix The prefix for the name of the created virtual threads.
 * @param maxParallelJobs The maximum number of parallel jobs that can be executed at a time. If null, no concurrency limit is imposed.
 */
open class VirtualTaskExecutor(threadNamePrefix: String, maxParallelJobs: Int? = null) : CountedTaskExecutor {

    protected open val virtualThreadFactory: CountedThreadFactory =
        VirtualNamedLimitedThreadFactory(threadNamePrefix, maxParallelJobs)

    /**
     * Executes the given task on a virtual thread.
     *
     * @param task The task to be executed.
     */
    override fun execute(task: Runnable) {
            virtualThreadFactory.newThread(task).start()
    }

    /**
     * Retrieves the count of active tasks in the current thread factory.
     *
     * @return The count of active tasks.
     */
    override fun getActiveTasksCount() = virtualThreadFactory.getActiveTasksCount()

    /**
     * Retrieves the total count of tasks executed by the VirtualTaskExecutor's thread factory.
     *
     * @return The total count of tasks executed.
     */
    override fun getTotalTasksCount(): Long =virtualThreadFactory.getTotalTasksCount()

}