package faang.school.postservice.threadpool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class PostServiceThreadPool {

    @Value("${postServiceThreadPool.poolAmount}")
    private int nThreads;

    @Bean
    public ExecutorService postServiceThreadPool() {
        return Executors.newFixedThreadPool(nThreads);
    }
}
