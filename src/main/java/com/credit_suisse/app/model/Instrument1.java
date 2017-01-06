package com.credit_suisse.app.model;

import java.util.Date;

import com.credit_suisse.app.core.module.AverageModule;

public class Instrument1 extends Instrument {

	private static final String INSTRUMENT = "INSTRUMENT1";
	
	public Instrument1() {
		super(INSTRUMENT);
	}
	
	public Instrument1(String name) {
		super(name);
	}
	
	public Instrument1(String name, Double price, Date date) {
		super(name, price, date);
		this.instrumentCalculateBehavior = new AverageModule(); 
	}

}
