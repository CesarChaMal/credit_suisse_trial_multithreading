package com.credit_suisse.app.core.module;

import java.util.ArrayList;
import java.util.List;

import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.InstrumentCalculateBehavior;

public class AverageModule implements InstrumentCalculateBehavior {
	
	private List<Instrument> instruments;

	public AverageModule() {
		instruments = new ArrayList<>(); 
	}
	
	public void addInstruments(List<Instrument> instruments) {
		this.instruments = instruments;
	}

	public List<Instrument> getInstruments() {
		return instruments;
	}
	
	@Override
	public Double calculate() {
		return getAverage();
	}

	private Double getAverage() {
		double sum = 0;
		int counter = 0;
		for (Instrument i : getInstruments()) {
			sum += i.getPrice();
			counter++;
		}
		if (sum == 0 && counter==0)
			return 0d;
		return (sum / counter);
	}

}
