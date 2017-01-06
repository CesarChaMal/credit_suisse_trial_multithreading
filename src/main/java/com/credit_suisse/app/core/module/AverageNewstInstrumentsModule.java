package com.credit_suisse.app.core.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.InstrumentCalculateBehavior;
import com.credit_suisse.app.util.CommonConstants;

public class AverageNewstInstrumentsModule implements InstrumentCalculateBehavior {

	private List<Instrument> instruments;
	
	private String instrument;

	public String getInstrument() {
		return instrument;
	}

	public void setInstrument(String instrument) {
		this.instrument = instrument;
	}

	public AverageNewstInstrumentsModule(String instrument) {
		instruments = new ArrayList<>(); 
		this.instrument = instrument;
	}
	
	public void addInstruments(List<Instrument> instruments) {
		this.instruments = instruments;
	}

	public List<Instrument> getInstruments() {
		return instruments;
	}
	
	@Override
	public Double calculate() {
		return getSum();
	}

	private Double getSum() {
		double sum = 0;
		int counter = 0;
		int limit = CommonConstants.NEWST;
		
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
