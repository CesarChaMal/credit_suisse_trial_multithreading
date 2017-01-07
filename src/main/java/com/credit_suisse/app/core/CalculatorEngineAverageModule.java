package com.credit_suisse.app.core;

import com.credit_suisse.app.core.module.AverageModule;
import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.InstrumentCalculateBehavior;
import com.credit_suisse.app.util.CommonConstants;

public class CalculatorEngineAverageModule implements CalculatorEngineStrategy {

	@Override
	public InstrumentCalculateBehavior add(String name, Instrument module, Instrument instrument) {
		AverageModule behavior = null;
		if (CommonConstants.INSTRUMENT1.equalsIgnoreCase(name)) {
			behavior = (AverageModule) module.getInstrumentCalculateBehavior();
			behavior.getInstruments().add(instrument);
		}
		return behavior;
	}

	@Override
	public InstrumentCalculateBehavior addEngineModule(String name, Instrument module, Instrument instrument) {
		AverageModule behavior = (AverageModule) this.add(name, module, instrument);
		return behavior;
	}

}
