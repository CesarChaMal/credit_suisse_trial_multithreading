package com.credit_suisse.app;

import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.credit_suisse.app.config.SpringRootConfig;
import com.credit_suisse.app.core.CalculatorEngine;
import com.credit_suisse.app.core.module.OnFlyModule;
import com.credit_suisse.app.dao.InstrumentPriceModifierDao;
import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.InstrumentPriceModifier;
import com.credit_suisse.app.model.newInstrument;
import com.credit_suisse.app.util.InstrumentUtil;

public class Application {

    @Autowired
    public ApplicationContext ctx;

	@Autowired
	DataSource dataSource;

	@Bean
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return new NamedParameterJdbcTemplate(dataSource);
	}

	public static void main(String[] args) {

//		String inputPath = "c:\\temp\\input.txt";
//		String inputPath = "c:\\temp\\big_input.txt";
//		String inputPath = "c:\\temp\\huge_input.txt";
		
		String inputPath = "src/main/resources/input.txt";
//		String inputPath = "src/main/resources/big_input.txt";
//		String inputPath = "src/main/resources/huge_input.txt";

		InstrumentPriceModifierDao instrumentPriceModifierDao = null;

//		Instrument newInstrument = new newInstrument("INSTRUMENT3", 4.0d, new Date());
//		Instrument newInstrument = new newInstrument("INSTRUMENT3", 6.0d, DefinerInstrument.getDate("03-Jan-2017"));
//		newInstrument.setInstrumentCalculateBehavior(new OnFlyModule(){
//			@Override
//			public synchronized Double calculate() {
//				double sum = 0;
//				int counter = 0;
//				for (Instrument i : getInstruments()) {
////					System.out.println(i.getName());
////					System.out.println(i.getPrice());
//					sum += i.getPrice();
//					counter++;
//				}
//				return sum*2;
//			}
//		});
		
		CalculatorEngine calculator = new CalculatorEngine(inputPath);
//		CalculatorEngine calculator = CalculatorEngine.getInstance(inputPath);
//		calculator.addModule(newInstrument);
		calculator.calculate(instrumentPriceModifierDao);

	}
}
