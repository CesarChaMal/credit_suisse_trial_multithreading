package com.credit_suisse.app.model;

import java.util.Date;

public abstract class Instrument implements Comparable<Instrument> {

	private String name;
	private Double price;
	private Date date;
	InstrumentCalculateBehavior instrumentCalculateBehavior;

	public InstrumentCalculateBehavior getInstrumentCalculateBehavior() {
		return this.instrumentCalculateBehavior;
	}

	public void setInstrumentCalculateBehavior(InstrumentCalculateBehavior instrumentCalculateBehavior) {
		this.instrumentCalculateBehavior = instrumentCalculateBehavior;
	}
	
	public Instrument(String name) {
		super();
		this.name = name;
	}

	public Instrument(String name, double price, Date date) {
		super();
		this.name = name;
		this.price = price;
		this.date = date;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Double calculate() {
		return instrumentCalculateBehavior.calculate();
	}

	@Override
	public String toString() {
		return "Instrument [name=" + name + ", price=" + price + ", date=" + date + "]";
	}

	@Override
	public int compareTo(Instrument i) {
		Instrument instrument = i;
		if (this.date.getTime() == instrument.date.getTime()) {
			return 0;
		} else {
			return (this.date.getTime() < instrument.date.getTime() ? 1 : -1);
		}
	}

}
