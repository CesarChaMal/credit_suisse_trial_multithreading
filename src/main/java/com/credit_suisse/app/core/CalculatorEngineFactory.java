package com.credit_suisse.app.core;

import com.credit_suisse.app.model.Instrument;

public class CalculatorEngineFactory {

	public static void create(String name, Instrument module, Instrument instrument, boolean addModule) {
		CalculatorEngineManager manager = new CalculatorEngineManager();
		manager.setStrategy(new CalculatorEngineAverageModule());
		executeStrategy(name, module, instrument, addModule, manager);

		manager.setStrategy(new CalculatorEngineAverageMonthModule());
		executeStrategy(name, module, instrument, addModule, manager);

		manager.setStrategy(new CalculatorEngineOnFlyModule());
		executeStrategy(name, module, instrument, addModule, manager);

		manager.setStrategy(new CalculatorEngineAverageNewsInstrumentsModule());
		executeStrategy(name, module, instrument, addModule, manager);
	}

	public static void executeStrategy(String name, Instrument module, Instrument instrument, boolean addModule,
			CalculatorEngineManager manager) {
		if (addModule)
			manager.addEngineModule(name, module, instrument);
		else
			manager.add(name, module, instrument);
	}
}
