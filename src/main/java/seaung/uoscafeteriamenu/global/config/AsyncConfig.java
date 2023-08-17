package seaung.uoscafeteriamenu.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean("crawlingAsyncExecutor")
    public Executor customAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // 기본적으로 실행 대기중인 스레드 갯수
        executor.setMaxPoolSize(5); // 동시에 동작하는 최대 스레드 갯수
        // executor.setQueueCapacity(500); // core pool을 넘어서면 큐에 저장하는 큐의 최대 용량
        executor.setThreadNamePrefix("crawlingThread");
        executor.initialize();

        return executor;
    }
}
