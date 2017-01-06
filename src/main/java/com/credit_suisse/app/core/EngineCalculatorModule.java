package com.credit_suisse.app.core;

import java.util.List;
import java.util.Map;

import com.credit_suisse.app.model.Instrument;


public class EngineCalculatorModule {
	
	private Map<String, List<Instrument>> instruments;
	private String instrument;
	
	public EngineCalculatorModule(String instrument) {
		this.instrument = instrument;
	}
	
	public void addInstruments(Map<String, List<Instrument>> instruments) {
		this.instruments = instruments;
	}

	protected String getInstrumentName() {
		return this.instrument;
	}
	
	public List<Instrument> getInstruments() {
		return instruments.get(this.instrument);
	}
	
}
