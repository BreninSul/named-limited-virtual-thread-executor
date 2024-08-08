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
import java.util.logging.Level
import java.util.logging.Logger

/**
 * This class represents a handler for uncaught exceptions in threads,
 * which logs these exceptions.
 *
 * @property exceptionLoggingLevel The logging level for exceptions. Defaults to severe.
 * @property order The order in which the handler handles the exceptions.
 *
 * @constructor Creates a new instance of LoggingUncaughtExceptionHandler.
 *
 * @param exceptionLoggingLevel The logging level for exceptions. If not specified, defaults to severe.
 *
 * @implements Thread.UncaughtExceptionHandler This class can handle uncaught exceptions in threads.
 * @implements Ordered This class can be ordered.
 */
open class LoggingUncaughtExceptionHandler (
   protected open val exceptionLoggingLevel: Level?=Level.SEVERE,
): Thread.UncaughtExceptionHandler,Ordered {
    override val order: Int = 0

    /**
     * Overrides the default uncaughtException function from the 'Thread.UncaughtExceptionHandler' Interface. Writes the details of an unhandled exception in a certain Thread to a logger.
     *
     * @param t Thread where the exception happened. Can be null if not specified.
     * @param e Throwable object containing details about the exception.
     */
    override fun uncaughtException(
        t: Thread?,
        e: Throwable?,
    ) {
        logger.log(exceptionLoggingLevel, "Exception in thread ${t?.name}", e)
    }

    companion object {
        val logger = Logger.getLogger(this::class.java.name)
    }
}
