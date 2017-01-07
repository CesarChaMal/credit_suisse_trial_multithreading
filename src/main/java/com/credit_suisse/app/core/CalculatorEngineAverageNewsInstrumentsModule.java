package com.credit_suisse.app.core;

import com.credit_suisse.app.core.module.AverageModule;
import com.credit_suisse.app.core.module.AverageNewsInstrumentsModule;
import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.InstrumentCalculateBehavior;
import com.credit_suisse.app.util.CommonConstants;

public class CalculatorEngineAverageNewsInstrumentsModule implements CalculatorEngineStrategy {

	@Override
	public InstrumentCalculateBehavior add(String name, Instrument module, Instrument instrument) {
		AverageNewsInstrumentsModule behavior = null;
		if (!(CommonConstants.INSTRUMENT1.equalsIgnoreCase(name) ||
			CommonConstants.INSTRUMENT2.equalsIgnoreCase(name) ||
			CommonConstants.INSTRUMENT3.equalsIgnoreCase(name) )) {
			behavior = (AverageNewsInstrumentsModule) module.getInstrumentCalculateBehavior();
			behavior.getInstruments().add(instrument);
		}
		return behavior;
	}

	@Override
	public InstrumentCalculateBehavior addEngineModule(String name, Instrument module, Instrument instrument) {
		AverageNewsInstrumentsModule behavior = (AverageNewsInstrumentsModule) this.add(name, module, instrument);
		return behavior;
	}

}
