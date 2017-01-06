package com.credit_suisse.app.config;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.credit_suisse.app.core.CalculatorEngine;
import com.credit_suisse.app.util.CommonConstants;

@Configuration
@Component("bootstrap")
@DependsOn("commonConstants")
public class Bootstrap implements InitializingBean, ApplicationContextAware, ApplicationListener<ApplicationEvent> {

	private static final Logger logger = LoggerFactory.getLogger(Bootstrap.class);

    @Autowired
    ApplicationContext ctx;

    public ApplicationContext getCtx() {
		return ctx;
	}

	public void setCtx(ApplicationContext ctx) {
		this.ctx = ctx;
	}

//	@Value("{classpath:app.properties}")
	@Value("{appProperties}")
    Properties appProperties;

    @Value("#{systemProperties}")
    Properties systemProperties;

    @Value("${Manager_Startup}")
    private boolean manager_Startup;

    @Value("${Worker_Startup}")
    private boolean worker_Startup;
    
    @Value("${RefreshMillis}")
    private long refreshMillis;
    
    @Value("${SleepMillis}")
    private long sleepMillis;
    
    @Value("${ThreadPoolSize}")
    private int threadPoolSize;
    
    @Value("${MaxThreads}")
    private int maxThreads;
    
    @Value("${WorkerProfile}")
    private String workerProfile;
    
    @Value("${InputFile}")
    private String inputFile;
    
    public Bootstrap() { }
    
    public void afterPropertiesSet() throws Exception {
        
        if (appProperties == null) {
            logger.error("The properties was not found, cannot bootstrap!!");
        }
        CommonConstants.MANAGER_ON = manager_Startup;
        CommonConstants.WORKER_ON = worker_Startup;
        CommonConstants.REFRESH_MILLIS = refreshMillis;
        CommonConstants.SLEEP_MILLIS = sleepMillis;
        CommonConstants.THREAD_POOL_SIZE = threadPoolSize;
        CommonConstants.MAX_THREADS = maxThreads;
        CommonConstants.WORKER_PROFILE = workerProfile;
        CommonConstants.INPUT_FILE = inputFile;
    }

    public void setApplicationContext(ApplicationContext context)
            throws BeansException {
        this.ctx = context;
    }

    public void onApplicationEvent(ApplicationEvent event) {
        logger.debug("ApplicationEvent::" + event.getClass().getName());

        if (event instanceof ContextRefreshedEvent) {
            logger.debug("ContextRefreshedEvent received...");
            
            // we would want to fire an event to start various things
        }
    }

}