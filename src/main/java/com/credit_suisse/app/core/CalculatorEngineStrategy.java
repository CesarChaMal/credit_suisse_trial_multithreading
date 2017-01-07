package com.credit_suisse.app.core;

import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.InstrumentCalculateBehavior;

public interface CalculatorEngineStrategy {
	InstrumentCalculateBehavior add(String name, Instrument module, Instrument instrument);
	InstrumentCalculateBehavior addEngineModule(String name, Instrument module, Instrument instrument);
}
