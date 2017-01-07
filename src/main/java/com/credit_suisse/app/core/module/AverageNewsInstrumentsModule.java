package com.credit_suisse.app.core.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.InstrumentCalculateBehavior;
import com.credit_suisse.app.util.CommonConstants;

public class AverageNewsInstrumentsModule implements InstrumentCalculateBehavior {

	private static final Logger logger = LoggerFactory.getLogger(AverageNewsInstrumentsModule.class);

	private List<Instrument> instruments;
	
	private String instrument;

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public AverageNewsInstrumentsModule(String instrument) {
		instruments = new ArrayList<>(); 
		this.instrument = instrument;
	}
	
	public synchronized void addInstruments(List<Instrument> instruments) {
		this.instruments = instruments;
	}

	public synchronized List<Instrument> getInstruments() {
		return instruments;
	}
	
	@Override
	public synchronized Double calculate() {
		return getSum();
	}

	private synchronized Double getSum() {
		double sum = 0;
		int counter = 0;
		int limit = CommonConstants.NEWST;
		
		logger.debug(instrument + " AverageNewstInstrumentsModule Instruments: " + getInstruments().size());

		List<Instrument> instruments = getInstruments();
		Collections.sort(instruments);
		
		for (Instrument i : instruments) {
			sum += i.getPrice();
			counter++;
			if (counter>=limit)
				break;
		}
		return sum;
	}

}
