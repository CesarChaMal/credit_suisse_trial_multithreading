package com.credit_suisse.app.core.module;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.InstrumentCalculateBehavior;
import com.credit_suisse.app.util.CommonConstants;

public class OnFlyModule implements InstrumentCalculateBehavior {

	private static final Logger logger = LoggerFactory.getLogger(OnFlyModule.class);

	private double result;

	private List<Instrument> instruments;

	public OnFlyModule(){
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
//		return result;
//		return getAverage(multiplier);
		return getSum();
	}

	public synchronized void refresh() {
		double sum = 0;
		int counter = 0;
		
		logger.debug("OnFlyModule Instruments: " + getInstruments().size());
		
		for (Instrument i : getInstruments()) {
			sum += i.getPrice();
			counter++;
		}
		if (sum == 0 && counter==0)
			result = 0d;
		result = (sum / counter);
	}

	private synchronized Double getAverage() {
		double sum = 0;
		int counter = 0;
		
		logger.debug("OnFlyModule Instruments: " + getInstruments().size());

		for (Instrument i : getInstruments()) {
			sum += i.getPrice();
			counter++;
		}
		if (sum == 0 && counter==0)
			return 0d;
		return (sum / counter);
	}

	private synchronized Double getSum() {
		double sum = 0;
		int counter = 0;
		
		logger.debug(CommonConstants.INSTRUMENT3 + " OnFlyModule Instruments: " + getInstruments().size());
		
		for (Instrument i : getInstruments()) {
			sum += i.getPrice();
			counter++;
		}
		return sum;
	}
	
	

}
