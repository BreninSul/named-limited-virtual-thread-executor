This is small lib to make using Virtual threads easier

It provides:
- Named threads in executor service
- Limited by semaphore parallel jobs executor service
- Main thread blocking limited by queue executor service (parent thread will wait  till queue will have empty space for task)


````kotlin
dependencies {
//Other dependencies
    implementation("io.github.breninsul:named-limited-virtual-thread-executor:${version}")
//Other dependencies
}

````
# Example of usage
````kotlin
//Create executor service with name prefix test
val executor = VirtualNamedLimitedExecutorService("test")

//Create limited parallel jobs executor service with name prefix test
val executor = VirtualNamedLimitedExecutorService("test", maxParallelJobs)

//Create blocking limited by queue executor service with name prefix test
val executor = LimitedBackpressureBlockingExecutor.buildVirtual("test",maxQueue)

````
