package com.credit_suisse.app.core;

import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.credit_suisse.app.util.CommonConstants;

public class CalculatorEngineRefresh extends TimerTask {
	
	private volatile static CalculatorEngineRefresh INSTANCE;
	
	private volatile CalculatorEngine calculatorEngine;
	
	private CalculatorEngineRefresh() {
//		calculatorEngine = CalculatorEngine.getInstance(CommonConstants.INPUT_FILE);
		calculatorEngine = new CalculatorEngine(CommonConstants.INPUT_FILE);
	}

	private static final Logger logger = LoggerFactory.getLogger(CalculatorEngineRefresh.class);

	@Override
	public void run() {
		logger.info("Started CalculatorEngine refreshing");
		reloadCalculatorEngine();
	}

	public static CalculatorEngineRefresh getInstance() {
		if (INSTANCE == null) {
			synchronized (CalculatorEngineRefresh.class) {
				if (INSTANCE == null) {
					INSTANCE = new CalculatorEngineRefresh();
				}
			}
		}
		return INSTANCE;
	}

	public void reloadCalculatorEngine(){
		ThreadManager cachemanager = new ThreadManager();
		
		Thread thread1 = new Thread(){
			public void run(){
				System.out.println("Thread Running");
			}
		};
//		thread1.start();
		
		Runnable myRunnable = new Runnable(){
		     public void run(){
		        System.out.println("Runnable running");
		     }
		};

		Thread thread2 = new Thread(myRunnable);
//		thread2.start();
		   
//		Thread t[] = new Thread[3];
		Thread t[] = new Thread[1];
		t[0] = new CalculatorEngine(CommonConstants.INPUT_FILE);
//		t[1] = thread1;
//		t[2] = thread2;
		cachemanager.setTask(t);
		cachemanager.start();
	}
}
