package com.credit_suisse.app.model;

public class InstrumentPriceModifier {

	private Long id;
	private String name;
	private Double modifier;

	public InstrumentPriceModifier(Long id, String name, Double modifier) {
		super();
		this.id = id;
		this.name = name;
		this.modifier = modifier;
	}

	public InstrumentPriceModifier() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getModifier() {
		return modifier;
	}

	public void setModifier(Double modifier) {
		this.modifier = modifier;
	}

	@Override
	public String toString() {
		return "Instrument [id=" + id + ", name=" + name + ", modifier=" + modifier + "]";
	}

}
