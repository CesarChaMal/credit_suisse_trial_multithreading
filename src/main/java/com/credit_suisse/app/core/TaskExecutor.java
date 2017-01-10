package com.credit_suisse.app.core;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.credit_suisse.app.dao.InstrumentPriceModifierDao;
import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.util.CommonConstants;
import com.credit_suisse.app.util.InstrumentUtil;
import com.credit_suisse.app.util.emWorkerProfile;

public class TaskExecutor implements Runnable {
	
	private static final Logger logger = LoggerFactory.getLogger(TaskExecutor.class);
	
	String workername;
	
	emWorkerProfile eWorkerProfile;

	@Autowired
	TaskManager taskManager;
	
	private InstrumentPriceModifierDao instrumentPriceModifierDao;

	private ExecutorService executorService = Executors.newFixedThreadPool(CommonConstants.THREAD_POOL_SIZE);

	public TaskExecutor(String name, emWorkerProfile profile, InstrumentPriceModifierDao instrumentPriceModifierDao) {
		this.eWorkerProfile = profile;
		this.workername = name;
		this.instrumentPriceModifierDao=instrumentPriceModifierDao;
	}

	public void run() {
		String[] status = new String[2];  //return status from worker service.

		while (true) 
		{
			try
			{
				if(CommonConstants.WORKER_ON){
					logger.info(workername + " Worker on");
					updateModifiers();
					logger.info(workername + " Worker off");
					
					synchronized(this){
						try {
							this.wait(CommonConstants.SLEEP_MILLIS);
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
					}
				}
			}catch(Exception e){
				logger.error("Exception caught while processing work",e);
			}
		}

	}

	public synchronized void updateModifiers() {
		String name = "";
		double multiplier = 0;
		NumberFormat formatter = new DecimalFormat("#0.00000");     
		for (int i = 1; i <= CommonConstants.INSTRUMENTS_COUNT; i++) {
			name = "INSTRUMENT" + i;
			if(CommonConstants.MODIFIER_DOUBLE)
				multiplier = Double.parseDouble(formatter.format(InstrumentUtil.generateRandomNumberDouble(CommonConstants.MODIFIER_MIN, CommonConstants.MODIFIER_MAX)));
			else
				multiplier = InstrumentUtil.generateRandomNumberInteger(CommonConstants.MODIFIER_MIN, CommonConstants.MODIFIER_MAX);
			instrumentPriceModifierDao.setMultiplier(name, multiplier);
		}
	}

}

