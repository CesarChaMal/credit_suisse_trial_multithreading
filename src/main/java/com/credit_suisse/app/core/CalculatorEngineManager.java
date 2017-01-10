package com.credit_suisse.app.core;

import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.InstrumentCalculateBehavior;

public class CalculatorEngineManager implements CalculatorEngineStrategy {

	private CalculatorEngineStrategy strategy;
	
	public CalculatorEngineStrategy getStrategy() {
		return strategy;
	}

	public void setStrategy(CalculatorEngineStrategy strategy){
		this.strategy = strategy;
	}

	@Override
	public InstrumentCalculateBehavior add(String name, Instrument module, Instrument instrument) {
		return this.strategy.add(name, module, instrument);
	}

	@Override
	public InstrumentCalculateBehavior addEngineModule(String name, Instrument module, Instrument instrument) {
		return this.strategy.addEngineModule(name, module, instrument);
	}

}
