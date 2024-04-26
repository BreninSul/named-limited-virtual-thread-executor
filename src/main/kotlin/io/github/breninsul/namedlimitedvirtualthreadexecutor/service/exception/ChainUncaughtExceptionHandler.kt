/*
 * Copyright (c) 2023 Alcosi Group Ltd. and affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.github.breninsul.namedlimitedvirtualthreadexecutor.service.blocking

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
