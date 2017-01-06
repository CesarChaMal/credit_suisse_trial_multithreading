package com.credit_suisse.app.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.credit_suisse.app.core.TaskManager;
import com.credit_suisse.app.util.CommonConstants;

@ComponentScan({ "com.credit_suisse.app" })
@Configuration
public class SpringRootConfig {

//    @Value("${threadPoolSize}")
    private int threadPoolSize;
//    private int threadPoolSize = CommonConstants.THREAD_POOL_SIZE;
    
    @Bean
    public Bootstrap bootstrap() {
        return new Bootstrap();
    }
    
    @Bean
    public TaskManager taskManager() {
        return new TaskManager();
    }
    
    @Bean
    public ExecutorService executorService() {
    	System.out.println("threadPoolSize:" + threadPoolSize);
//        return Executors.newFixedThreadPool(threadPoolSize);
        return Executors.newFixedThreadPool(CommonConstants.THREAD_POOL_SIZE);
    }
    
	@PostConstruct
	public void startDBManager() {
		//hsqldb
//		DatabaseManagerSwing.main(new String[] { "--url", "jdbc:hsqldb:mem:testdb", "--user", "sa", "--password", "" });

		//derby
//		DatabaseManagerSwing.main(new String[] { "--url", "jdbc:derby:memory:testdb", "--user", "", "--password", "" });

		//h2
//		DatabaseManagerSwing.main(new String[] { "--url", "jdbc:h2:mem:testdb", "--user", "sa", "--password", "" });
	}

}