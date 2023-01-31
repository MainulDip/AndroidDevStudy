## Overview:
All about Networking and concurrency usages.

### Threads:
* Don't Use Thread Directly:
    - Unpredictable : direct use of thread can return inconsistance/different/unpredictable results when ran multiple time, As the processor switches between sets of instructions on different threads, the exact time a thread is executed and when a thread is paused is beyond your control.
    -  Low-Performance/resource-hungry: Creating, switching, and managing threads takes up system resources
    - Race-condition: when multiple threads try to access the same value in memory at the same time. Race conditions can result in hard to reproduce, random looking bugs, which may cause your the to crash, often unpredictably.

### Kotlin Coroutine: