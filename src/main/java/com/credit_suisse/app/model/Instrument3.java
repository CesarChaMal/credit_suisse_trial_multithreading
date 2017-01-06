package com.credit_suisse.app.model;

import java.util.Date;

import com.credit_suisse.app.core.module.OnFlyModule;

public class Instrument3 extends Instrument {

	private static final String INSTRUMENT = "INSTRUMENT3";

	public Instrument3() {
		super(INSTRUMENT);
	}

	public Instrument3(String name) {
		super(name);
	}

	public Instrument3(String name, Double price, Date date) {
		super(name, price, date);
		this.instrumentCalculateBehavior = new OnFlyModule(); 
	}

}
