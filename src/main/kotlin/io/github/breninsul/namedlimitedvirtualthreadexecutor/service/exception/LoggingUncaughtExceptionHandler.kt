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
import java.util.logging.Level
import java.util.logging.Logger

open class LoggingUncaughtExceptionHandler (
    val exceptionLoggingLevel: Level?=Level.SEVERE,
): Thread.UncaughtExceptionHandler,Ordered {
    override val order: Int = 0
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
