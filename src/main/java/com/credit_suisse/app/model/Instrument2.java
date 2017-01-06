package com.credit_suisse.app.model;

import java.util.Date;

import com.credit_suisse.app.core.module.AverageMonthModule;

public class Instrument2 extends Instrument {

	private static final Integer YEAR = 2014;
	private static final Integer NOVEMBER = 10;

	private static final String INSTRUMENT = "INSTRUMENT2";

	public Instrument2() {
		super(INSTRUMENT);
	}

	public Instrument2(String name) {
		super(name);
	}
	public Instrument2(String name, Double price, Date date) {
		super(name, price, date);
		this.instrumentCalculateBehavior = new AverageMonthModule(); 
	}

}
