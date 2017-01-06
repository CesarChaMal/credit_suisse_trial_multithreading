package com.credit_suisse.app.core.module;

import java.util.ArrayList;
import java.util.List;

import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.InstrumentCalculateBehavior;

public class OnFlyModule implements InstrumentCalculateBehavior {

	private double result;

	private List<Instrument> instruments;

	public OnFlyModule(){
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
//		return result;
//		return getAverage(multiplier);
		return getSum();
	}

	public void refresh() {
		double sum = 0;
		int counter = 0;
		for (Instrument i : getInstruments()) {
			sum += i.getPrice();
			counter++;
		}
		if (sum == 0 && counter==0)
			result = 0d;
		result = (sum / counter);
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

	private Double getSum() {
		double sum = 0;
		int counter = 0;
		for (Instrument i : getInstruments()) {
			sum += i.getPrice();
			counter++;
		}
		return sum;
	}
	
	

}
