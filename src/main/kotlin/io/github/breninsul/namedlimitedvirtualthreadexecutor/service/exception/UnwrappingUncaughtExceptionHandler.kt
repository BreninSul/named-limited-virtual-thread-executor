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
import java.util.concurrent.ExecutionException

/**
 * The `UnwrappingUncaughtExceptionHandler` class is an implementation of the `Thread.UncaughtExceptionHandler` interface.
 * This class is responsible for handling uncaught exceptions in threads.
 * It specifically handles exceptions of type `ExecutionException` by unwrapping and re-throwing the cause of the exception if it exists.
 *
 * This class is open for further customization via subclassing.
 *
 * @constructor Creates a new instance of `UnwrappingUncaughtExceptionHandler`.
 *
 * @implements Thread.UncaughtExceptionHandler This class can handle uncaught exceptions in threads.
 * @implements Ordered This class can be ordered.
 *
 * @property order The order in which the handler handles the exceptions. Defaults to 10.
 *
 * @see Thread.UncaughtExceptionHandler
 */
open class UnwrappingUncaughtExceptionHandler (): Thread.UncaughtExceptionHandler, Ordered {
    override val order: Int = 10

    /**
     * This property represents the order of the exception handler.
     * The order determines the sequence in which the handlers will be executed in case of an uncaught exception.
     * The handler with a lower order value will be executed before a handler with a higher order value.
     * @return an Int value representing the order of the exception handler.
     */
    override fun uncaughtException(
        t: Thread?,
        e: Throwable?,
    ) {
        if (e is ExecutionException){
            if (e.cause !=null){
                throw e.cause!!
            }
        }
    }
}
