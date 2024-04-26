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

open class ChainUncaughtExceptionHandler(handlers: List<Thread.UncaughtExceptionHandler>) : Thread.UncaughtExceptionHandler {
   protected val sortedHandlers = handlers.sortedWith(Comparator { o1, o2 ->
        if (o1 is Ordered) {
            if (o2 is Ordered) {
                return@Comparator o1.order.compareTo(o2.order)
            } else {
                return@Comparator -1
            }
        } else {
            if (o2 is Ordered) {
                return@Comparator 1
            } else {
                return@Comparator 0
            }
        }
    })

    override fun uncaughtException(
        t: Thread?,
        e: Throwable?,
    ) {
        sortedHandlers.forEach { it.uncaughtException(t, e) }
    }
}
