package com.credit_suisse.app.core.module;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.InstrumentCalculateBehavior;

public class AverageMonthModule implements InstrumentCalculateBehavior {

	private static final Integer YEAR = 2014;
	private static final Integer NOVEMBER = 10;

	private List<Instrument> instruments;

	public AverageMonthModule() {
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
