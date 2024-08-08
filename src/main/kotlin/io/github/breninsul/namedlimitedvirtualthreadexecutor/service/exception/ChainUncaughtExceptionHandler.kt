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

package io.github.breninsul.namedlimitedvirtualthreadexecutor.service.exception

import io.github.breninsul.namedlimitedvirtualthreadexecutor.service.Ordered

/**
 * This class implements a chain of uncaught exception handlers.
 * It is used to handle uncaught exceptions in a chain-like manner
 * allowing multiple handlers to process any given uncaught exception.
 *
 * @property handlers List of uncaught exception handlers
 * @property sortedHandlers Sorted list of the uncaught exception handlers
 * @constructor  constructs a new `ChainUncaughtExceptionHandler` instance sorting the
 * handler list by their order if they implement Ordered interface.
 */
open class ChainUncaughtExceptionHandler(handlers: List<Thread.UncaughtExceptionHandler>) : Thread.UncaughtExceptionHandler {
   protected val sortedHandlers = handlers.sortedWith(Ordered.OrderedComparator)

    override fun uncaughtException(
        t: Thread?,
        e: Throwable?,
    ) {
        sortedHandlers.forEach { it.uncaughtException(t, e) }
    }
}
