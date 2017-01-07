package com.credit_suisse.app.core;

import com.credit_suisse.app.core.module.AverageModule;
import com.credit_suisse.app.core.module.AverageMonthModule;
import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.InstrumentCalculateBehavior;
import com.credit_suisse.app.util.CommonConstants;

public class CalculatorEngineAverageMonthModule implements CalculatorEngineStrategy {

	@Override
	public InstrumentCalculateBehavior add(String name, Instrument module, Instrument instrument) {
		AverageMonthModule behavior = null;
		if (CommonConstants.INSTRUMENT2.equalsIgnoreCase(name)) {
			behavior = (AverageMonthModule) module.getInstrumentCalculateBehavior();
			behavior.getInstruments().add(instrument);
		}
		return behavior;
	}

	@Override
	public InstrumentCalculateBehavior addEngineModule(String name, Instrument module, Instrument instrument) {
		AverageMonthModule behavior = (AverageMonthModule) this.add(name, module, instrument);
		return behavior;
	}

}
