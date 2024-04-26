package com.alcosi.nft.subscription.config

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

object VirtualTreadExecutor : ExecutorService by Executors.newVirtualThreadPerTaskExecutor() {
    fun createNamed(name: String): ExecutorService {
        val count = AtomicInteger(1)
        val factory = ThreadFactory { task: Runnable? ->
            Thread.ofVirtual().name("$name-" + count.getAndIncrement()).start(task)
        }
        return Executors.newThreadPerTaskExecutor(factory)
    }
}
