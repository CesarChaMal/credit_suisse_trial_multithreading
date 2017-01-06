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
import com.credit_suisse.app.core.module.OnFlyModule;
import com.credit_suisse.app.dao.InstrumentPriceModifierDao;
import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.InstrumentPriceModifier;
import com.credit_suisse.app.model.newInstrument;

@Controller
public class WelcomeController {

	private static final Logger logger = LoggerFactory.getLogger(WelcomeController.class);

	@Autowired
	InstrumentPriceModifierDao instrumentPriceModifierDao;
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String welcome(Model model) {

		InstrumentPriceModifier instrument = instrumentPriceModifierDao.findByName("INSTRUMENT1");
		logger.debug("instrument:" + instrument);
		
		List<InstrumentPriceModifier> instrumentList = instrumentPriceModifierDao.findByNameList("INSTRUMENT1");
		logger.debug("instrument:" + instrumentList.get(0));
		
		List<InstrumentPriceModifier> modifiers = instrumentPriceModifierDao.findAll();

		logger.debug(Arrays.toString(modifiers.toArray()));

//		String inputPath = "c:\\temp\\input.txt";
//		String inputPath = "c:\\temp\\big_input.txt";
//		String inputPath = "c:\\temp\\huge_input.txt";
		
		String inputPath = "src/main/resources/input.txt";
//		String inputPath = "src/main/resources/big_input.txt";
//		String inputPath = "src/main/resources/huge_input.txt";

//		ctx = new AnnotationConfigApplicationContext(SpringRootConfig.class);
//		ctx = new ClassPathXmlApplicationContext("file:src/main/**/spring-bean-config.xml");

		Instrument newInstrument = new newInstrument("INSTRUMENT3", 4.0d, new Date());
		newInstrument.setInstrumentCalculateBehavior(new OnFlyModule(){
			@Override
			public Double calculate() {
				double sum = 0;
				int counter = 0;
				for (Instrument i : getInstruments()) {
					System.out.println(i.getName());
					System.out.println(i.getPrice());
					sum += i.getPrice();
					counter++;
				}
				return sum*2;
			}
		});
		
//		CalculatorEngine calculator = new CalculatorEngine(inputPath);
		CalculatorEngine calculator = CalculatorEngine.getInstance(inputPath);
//		calculator.addModule(newInstrument);

		model.addAttribute("modifiers", modifiers);
		model.addAttribute("instruments", calculator.calculate(instrumentPriceModifierDao));

		return "welcome";

	}

}