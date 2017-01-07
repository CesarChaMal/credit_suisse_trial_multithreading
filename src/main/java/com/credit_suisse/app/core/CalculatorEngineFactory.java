package com.credit_suisse.app.core;

import com.credit_suisse.app.model.Instrument;

public class CalculatorEngineFactory {

	public static void create(String name, Instrument module, Instrument instrument) {
		CalculatorEngineManager manager = new CalculatorEngineManager();
		manager.setStrategy(new CalculatorEngineAverageModule());
		manager.add(name, module, instrument);
		manager.addEngineModule(name, module, instrument);

		manager.setStrategy(new CalculatorEngineAverageMonthModule());
		manager.add(name, module, instrument);
		manager.addEngineModule(name, module, instrument);

		manager.setStrategy(new CalculatorEngineOnFlyModule());
		manager.add(name, module, instrument);
		manager.addEngineModule(name, module, instrument);

		manager.setStrategy(new CalculatorEngineAverageNewsInstrumentsModule());
		manager.add(name, module, instrument);
		manager.addEngineModule(name, module, instrument);
	}
}
