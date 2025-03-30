package com.nyang8ja.api.global.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor

@EnableAsync
@Configuration
class AsyncConfig {

    @Bean(name = ["bizNotificationExecutor"])
    fun bizNotificationExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 5
        executor.maxPoolSize = 10
        executor.queueCapacity = 25
        executor.setThreadNamePrefix("AsyncExecutor-")
        executor.initialize()
        return executor
    }

    @Bean(name = ["tarotHistoryExecutor"])
    fun tarotHistoryExecutor(): Executor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = 5
        executor.maxPoolSize = 10
        executor.queueCapacity = 25
        executor.setThreadNamePrefix("TarotHistoryExecutor-")
        executor.initialize()
        return executor
    }
}