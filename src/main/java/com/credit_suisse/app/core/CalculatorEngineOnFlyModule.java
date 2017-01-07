package com.credit_suisse.app.core;

import com.credit_suisse.app.core.module.AverageNewsInstrumentsModule;
import com.credit_suisse.app.core.module.OnFlyModule;
import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.InstrumentCalculateBehavior;
import com.credit_suisse.app.util.CommonConstants;

public class CalculatorEngineOnFlyModule implements CalculatorEngineStrategy {

	@Override
	public InstrumentCalculateBehavior add(String name, Instrument module, Instrument instrument) {
		OnFlyModule behavior = null;
		if (CommonConstants.INSTRUMENT3.equalsIgnoreCase(name)) {
			behavior = (OnFlyModule) module.getInstrumentCalculateBehavior();
			behavior.getInstruments().add(instrument);
		}
		return behavior;
	}

	@Override
	public InstrumentCalculateBehavior addEngineModule(String name, Instrument module, Instrument instrument) {
		OnFlyModule moduleOri = null;
		if (CommonConstants.INSTRUMENT3.equalsIgnoreCase(name)) {
			Instrument instrumentOri = module;
			moduleOri = (OnFlyModule) instrumentOri.getInstrumentCalculateBehavior();
			moduleOri.getInstruments().add(instrument);
			OnFlyModule moduleDest = (OnFlyModule) instrument.getInstrumentCalculateBehavior();
			moduleDest.addInstruments(moduleOri.getInstruments());
			instrumentOri.setInstrumentCalculateBehavior(moduleDest);
		}
		return moduleOri;
	}

}
