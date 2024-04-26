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

open class LimitedBackpressureBlockingExecutor(
     maxParallelJobs: Int,
     workQueue: BlockingQueue<Runnable?>,
     factory: ThreadFactory,
     handler: RejectedExecutionHandler,
) : ExecutorService by ThreadPoolExecutor(maxParallelJobs, maxParallelJobs, 0L, TimeUnit.MILLISECONDS, workQueue, factory, handler) {

    companion object {
        protected val WAITING_REJECTED_EXECUTOR = WaitingRejectedExecutor()

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
