package com.credit_suisse.app.web.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.credit_suisse.app.core.CalculatorEngine;
import com.credit_suisse.app.core.module.AverageNewsInstrumentsModule;
import com.credit_suisse.app.core.module.OnFlyModule;
import com.credit_suisse.app.dao.InstrumentPriceModifierDao;
import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.InstrumentPriceModifier;
import com.credit_suisse.app.model.newInstrument;
import com.credit_suisse.app.util.CommonConstants;

@Controller
public class WelcomeController {

	private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);

	@Autowired
	InstrumentPriceModifierDao instrumentPriceModifierDao;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String welcome(Model model) {

		List<InstrumentPriceModifier> modifiers = instrumentPriceModifierDao.findAll();

		logger.debug(Arrays.toString(modifiers.toArray()));

		String inputPath = "src/main/resources/input.txt";

		Instrument newInstrument1 = new newInstrument("INSTRUMENT3", 4.0d, new Date());
		newInstrument1.setInstrumentCalculateBehavior(new OnFlyModule(){
			@Override
			public Double calculate() {
				double sum = 0;
				int counter = 0;
				for (Instrument i : getInstruments()) {
					logger.debug(CommonConstants.INSTRUMENT3 + " OnFlyModule Instruments: " + getInstruments().size());
					sum += i.getPrice();
					counter++;
				}
				return sum*2;
			}
		});
		
		CalculatorEngine calculator = new CalculatorEngine(inputPath);
		calculator.addModule(newInstrument1);

		model.addAttribute("modifiers", modifiers);
		model.addAttribute("instruments", calculator.calculate(instrumentPriceModifierDao));

		return "welcome";

	}

}