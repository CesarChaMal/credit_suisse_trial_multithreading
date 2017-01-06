package com.credit_suisse.app.core.module;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

import com.credit_suisse.app.core.CalculatorEngine;
import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.InstrumentCalculateBehavior;
import com.credit_suisse.app.util.CommonConstants;

public class AverageModule implements InstrumentCalculateBehavior {
	
	private static final Logger logger = LoggerFactory.getLogger(AverageModule.class);

	private List<Instrument> instruments;

	public AverageModule() {
		instruments = new ArrayList<>(); 
	}
	
	public synchronized void addInstruments(List<Instrument> instruments) {
		this.instruments = instruments;
	}

	public synchronized List<Instrument> getInstruments() {
		return instruments;
	}
	
	@Override
	public synchronized Double calculate() {
		return getAverage();
	}

	private synchronized Double getAverage() {
		double sum = 0;
		int counter = 0;
		
		logger.debug(CommonConstants.INSTRUMENT1 + " AverageModule Instruments: " + getInstruments().size());

		for (Instrument i : getInstruments()) {
			sum += i.getPrice();
			counter++;
		}
		if (sum == 0 && counter==0)
			return 0d;
		return (sum / counter);
	}

}
