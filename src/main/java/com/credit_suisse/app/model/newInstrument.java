package com.credit_suisse.app.model;

import java.util.Date;

import com.credit_suisse.app.core.module.AverageNewstInstrumentsModule;

public class newInstrument extends Instrument {

	public newInstrument(String instrument) {
		super(instrument);
	}

	public newInstrument(String name, Double price, Date date) {
		super(name, price, date);
		this.instrumentCalculateBehavior = new AverageNewstInstrumentsModule(name); 
	}

}
