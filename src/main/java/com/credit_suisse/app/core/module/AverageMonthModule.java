package com.credit_suisse.app.core.module;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.OptionalDouble;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.InstrumentCalculateBehavior;
import com.credit_suisse.app.util.CommonConstants;

public class AverageMonthModule implements InstrumentCalculateBehavior {

	private static final Logger logger = LoggerFactory.getLogger(AverageMonthModule.class);

	private static final Integer YEAR = 2014;
	private static final Integer NOVEMBER = 10;

	private List<Instrument> instruments;

	public AverageMonthModule() {
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
		logger.debug(CommonConstants.INSTRUMENT2 + " AverageMonthModule Instruments: " + getInstruments().size());
		OptionalDouble average = getInstruments().stream().filter(o -> isNovember(o.getDate()) ).mapToDouble(o -> o.getPrice()).average();
		return average.getAsDouble();
	}
	
	private synchronized Double getAverage2() {
		double sum = 0;
		int counter = 0;
		
		logger.debug(CommonConstants.INSTRUMENT2 + " AverageMonthModule Instruments: " + getInstruments().size());
		
		for (Instrument i : getInstruments()) {
			if (isNovember(i.getDate())) {
				sum += i.getPrice();
				counter++;
			}
		}
		if (sum == 0 && counter==0)
			return 0d;
		return (sum / counter);
	}
	
	private boolean isNovember(Date data) {
		return (data.getMonth() == NOVEMBER && (data.getYear() + 1900) == YEAR);
	}

}
