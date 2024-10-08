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

import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ThreadPoolExecutor
import java.util.logging.Level
import java.util.logging.Logger

/**
 * The `WaitingRejectedExecutor` is an implementation of the `RejectedExecutionHandler` interface for a `ThreadPoolExecutor`.
 * If a task submission is rejected, this handler will try to put the task back into the executor's task queue,
 * causing the submitter to block until space becomes available.
 *
 * This class is open, allowing for further customization via subclassing.
 *
 * @property logger An instance of `Logger` that is used for logging exceptions that may occur during the execution.
 */
open class WaitingRejectedExecutor : RejectedExecutionHandler {

    /**
     * Handles a task that cannot be accepted by the executor.
     * If the task is unable to start because of execution rejection by the `ThreadPoolExecutor`,
     * this method attempts to put the task back into the executor's task queue, causing the task submission
     * to block until space becomes available.
     *
     * @param r the runnable task requested to execute
     * @param executor the executor attempting to execute this task
     * @throws InterruptedException if interrupted while waiting
     */
    override fun rejectedExecution(
        r: Runnable,
        executor: ThreadPoolExecutor,
    ) {
        try {
            executor.queue.put(r)
        } catch (t: Throwable) {
            logger.log(Level.SEVERE, "Exception in executor $r", t)
        }
    }

    companion object {
        val logger = Logger.getLogger(this::class.java.name)
    }
}
