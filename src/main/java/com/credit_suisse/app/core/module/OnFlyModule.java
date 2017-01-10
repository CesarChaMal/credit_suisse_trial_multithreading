package com.credit_suisse.app.core.module;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

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

	private synchronized Double getAverage() {
		logger.debug("OnFlyModule Instruments: " + getInstruments().size());
		OptionalDouble average = getInstruments().stream().mapToDouble(o -> o.getPrice()).average();
		return average.getAsDouble();
	}
	
	private synchronized Double getAverage2() {
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
		logger.debug(CommonConstants.INSTRUMENT3 + " OnFlyModule Instruments: " + getInstruments().size());
		double sum = instruments.stream().filter(o -> o.getPrice() >= 0).mapToDouble(Instrument::getPrice).sum();
		return sum;
	}
	
	private synchronized Double getSum2() {
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
