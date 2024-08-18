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

import java.util.concurrent.ThreadFactory

/**
 * CountedThreadFactory is an interface that extends the ThreadFactory interface.
 * It represents a factory for creating threads with the ability to track the count of active and total tasks.
 * The interface provides methods to retrieve the count of active tasks and the total count of tasks executed by the thread factory.
 *
 * Implementing classes should provide an implementation for the newThread method to create a new thread that executes the given task.
 *
 * The CountedThreadFactory interface extends the ThreadFactory interface and adds two additional methods:
 * - getActiveTasksCount(): Long - Retrieves the count of active tasks in the current thread factory.
 * - getTotalTasksCount(): Long - Retrieves the total count of tasks executed by the thread factory.
 *
 * Example usage:
 * The CountedThreadFactory interface is used by classes such as VirtualTaskExecutor and VirtualNamedLimitedExecutorService
 * to create threads and track the count of active and total tasks.
 *
 * @see ThreadFactory
 */
interface CountedThreadFactory:ThreadFactory,CountedTasksExecutor {
}