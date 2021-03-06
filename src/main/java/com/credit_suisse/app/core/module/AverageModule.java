package com.credit_suisse.app.core.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		logger.debug(CommonConstants.INSTRUMENT1 + " AverageModule Instruments: " + getInstruments().size());
		OptionalDouble average = getInstruments().stream().filter(Objects::nonNull).filter(o -> o.getPrice()!=null).mapToDouble(o -> o.getPrice()).average();
		return average.getAsDouble();
	}
	
	private synchronized Double getAverage2() {
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

