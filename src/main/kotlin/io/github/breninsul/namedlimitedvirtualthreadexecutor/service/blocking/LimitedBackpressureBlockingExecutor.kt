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

package io.github.breninsul.namedlimitedvirtualthreadexecutor.service.blocking

import io.github.breninsul.namedlimitedvirtualthreadexecutor.service.VirtualNamedLimitedThreadFactory
import io.github.breninsul.namedlimitedvirtualthreadexecutor.service.exception.ChainUncaughtExceptionHandler
import io.github.breninsul.namedlimitedvirtualthreadexecutor.service.exception.LoggingUncaughtExceptionHandler
import io.github.breninsul.namedlimitedvirtualthreadexecutor.service.exception.UnwrappingUncaughtExceptionHandler
import java.lang.Thread.UncaughtExceptionHandler
import java.util.concurrent.*

/**
 * LimitedBackpressureBlockingExecutor is an implementation of ExecutorService that provides thread pooling,
 * limited parallel execution, and backpressure handling. It extends ThreadPoolExecutor class.
 *
 * @property maxParallelJobs The maximum number of parallel jobs that can be executed at a time.
 * @property workQueue The blocking queue that holds the jobs to be executed.
 * @property factory The thread factory used to create new threads.
 * @property handler The handler for rejected execution of jobs.
 */
open class LimitedBackpressureBlockingExecutor(
     maxParallelJobs: Int,
     workQueue: BlockingQueue<Runnable?>,
     factory: ThreadFactory,
     handler: RejectedExecutionHandler,
) : ExecutorService by ThreadPoolExecutor(maxParallelJobs, maxParallelJobs, 0L, TimeUnit.MILLISECONDS, workQueue, factory, handler) {

    /**
     * The `Companion` class provides a static method to build a `LimitedBackpressureBlockingExecutor`.
     * It also contains a protected property `WAITING_REJECTED_EXECUTOR` of type `WaitingRejectedExecutor`.
     */
    companion object {
        protected val WAITING_REJECTED_EXECUTOR = WaitingRejectedExecutor()

        /**
         * Builds a virtual `LimitedBackpressureBlockingExecutor` with specified arguments.
         *
         * @param threadNamePrefix The prefix for the name of the thread.
         * @param maxParallelJobs The maximum number of parallel jobs that can be executed at a time.
         * @param uncaughtExceptionHandler The handler for uncaught exceptions. By default, it chains `LoggingUncaughtExceptionHandler` and `UnwrappingUncaughtExceptionHandler`.
         * @param inheritThreadLocals Flag that determines whether the created threads should inherit thread locals. By default, it is `null`.
         *
         * @return A `LimitedBackpressureBlockingExecutor` with the specified settings.
         */
        fun buildVirtual(
            threadNamePrefix: String,
            maxParallelJobs: Int,
            uncaughtExceptionHandler:UncaughtExceptionHandler = ChainUncaughtExceptionHandler(listOf(LoggingUncaughtExceptionHandler(), UnwrappingUncaughtExceptionHandler())),
            inheritThreadLocals:Boolean?=null
        ): LimitedBackpressureBlockingExecutor {
            return LimitedBackpressureBlockingExecutor(
                maxParallelJobs,
                LinkedBlockingQueue(maxParallelJobs),
                VirtualNamedLimitedThreadFactory(threadNamePrefix, null,uncaughtExceptionHandler,inheritThreadLocals),
                WAITING_REJECTED_EXECUTOR,
            )
        }
    }
}
