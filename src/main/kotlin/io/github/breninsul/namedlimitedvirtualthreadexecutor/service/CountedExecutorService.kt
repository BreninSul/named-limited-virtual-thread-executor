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

/**
 * The `CountedExecutorService` interface represents an executor service that provides the ability to track the count of active and total tasks.
 * It extends the `ExecutorService` interface and the `CountedTasksExecutor` interface.
 *
 * The `CountedExecutorService` interface adds two additional methods:
 * - `getActiveTasksCount(): Long` - retrieves the count of active tasks in the executor service.
 * - `getTotalTasksCount(): Long` - retrieves the total count of tasks executed by the executor service.
 *
 * Implementing classes should provide implementations for the methods declared in the parent interfaces:
 * - `execute(task: Runnable)` - submits a task for execution and returns a `Future` representing that task.
 * - `shutdown()` - initiates an orderly shutdown of the executor service.
 * - `shutdownNow(): List<Runnable>` - attempts to stop all actively executing tasks and returns a list of tasks that were awaiting execution.
 * - `isShutdown(): Boolean` - returns `true` if the executor service has been shut down, `false` otherwise.
 * - `isTerminated(): Boolean` - returns `true` if the executor service has been shut down and all tasks have completed after shutdown, `false` otherwise.
 * - `awaitTermination(timeout: Long, unit: TimeUnit): Boolean` - blocks until all tasks have completed execution after a shutdown request, or the timeout occurs.
 *
 * Implementing classes should also provide their own constructors with appropriate parameters, based on their specific requirements.
 *
 * Example usage:
 * ```
 * val executorService: CountedExecutorService = MyCountedExecutorService()
 * executorService.execute(MyTask())
 * val activeTasksCount = executorService.getActiveTasksCount()
 * val totalTasksCount = executorService.getTotalTasksCount()
 * ```
 *
 * @see ExecutorService
 * @see CountedTasksExecutor
 */
interface CountedExecutorService:ExecutorService,CountedTasksExecutor