package com.credit_suisse.app.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.credit_suisse.app.dao.InstrumentPriceModifierDao;
import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.InstrumentFactory;
import com.credit_suisse.app.model.InstrumentPriceModifier;
import com.credit_suisse.app.util.CommonConstants;
import com.credit_suisse.app.util.InstrumentUtil;
import com.credit_suisse.app.util.PartitioningSpliterator;

public class CalculatorEngine extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(CalculatorEngine.class);

	private static final Map<String, List<Instrument>> INSTRUMENTS = new TreeMap<>();

	private static final Map<String, Instrument> MODULES = new TreeMap<>();

	private String inputPath = null;

	public static Map<String, Instrument> getModules() {
		return MODULES;
	}

	@Autowired
	private DataSource dataSource;

	@Bean
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return new NamedParameterJdbcTemplate(dataSource);
	}

	private InstrumentPriceModifierDao instrumentPriceModifierDao;

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public InstrumentPriceModifierDao getInstrumentPriceModifierDao() {
		return instrumentPriceModifierDao;
	}

	public void setInstrumentPriceModifierDao(InstrumentPriceModifierDao instrumentPriceModifierDao) {
		this.instrumentPriceModifierDao = instrumentPriceModifierDao;
	}

	public CalculatorEngine(InstrumentPriceModifierDao instrumentPriceModifierDao) {
		this.inputPath = CommonConstants.INPUT_FILE;
		logger.debug(String.format("Input file path: %s", inputPath));
		this.instrumentPriceModifierDao = instrumentPriceModifierDao;
		init();
	}

	public CalculatorEngine(String inputPath) {
		this.inputPath = inputPath;
		logger.debug(String.format("Input file path: %s", inputPath));
		init();
	}

	public CalculatorEngine() {
	}

	// commented out because causes synchronization issue
	static {
		// init();
	}

	private synchronized static void init() {
		logger.debug("Init instruments");

		for (int i = 1; i <= CommonConstants.INSTRUMENTS_COUNT; i++) {
			String name = "INSTRUMENT" + i;
			INSTRUMENTS.put(name, new ArrayList<Instrument>());

			Instrument instrument = InstrumentFactory.createInstrument(name);
			MODULES.put(name, instrument);
		}

	}

	public synchronized void addModule(Instrument instrument) {
		logger.info(String.format("Add module %s for instrument %s", instrument.getClass().getName(),
				instrument.getName()));
		String name = instrument.getName();
		INSTRUMENTS.get(name).add(instrument);
		this.addEngineModule(instrument);
	}

	private synchronized void addEngineModule(Instrument instrument) {
		String name = instrument.getName();
		if (MODULES.containsKey(name) && INSTRUMENTS.containsKey(name)) {
			CalculatorEngineFactory.create(name, MODULES.get(name), instrument, true);
		}
	}

	public synchronized Map<String, Double> calculate(InstrumentPriceModifierDao multiplier) {
		Map<String, Double> result = new TreeMap<>();
		try {
			parseFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Double multiplierValue = 1.0;
		Double compute = 0.0;
		Double instrumentValue = 0.0;
		NumberFormat formatter = new DecimalFormat("#0.00000");

		logger.debug("MODULES.size: " + MODULES.size());

		for (Entry<String, Instrument> instrumentModule : MODULES.entrySet()) {
			instrumentValue = instrumentModule.getValue().calculate();
			multiplierValue = getModifier(multiplier, instrumentModule);

			if (CommonConstants.MODIFIERS) {
				compute = instrumentValue * multiplierValue;
			} else {
				compute = instrumentValue;
			}

			logger.info(instrumentModule.getKey() + ":" + instrumentValue);
			logger.info("Multiplier:" + multiplierValue);
			logger.info("Result: " + compute + "\n");
			result.put(instrumentModule.getKey(), compute);
		}
		return result;
	}

	public synchronized Double getModifier(InstrumentPriceModifierDao multiplier,
			Entry<String, Instrument> instrumentModule) {
		double multiplierValue = 1.0;
		if (multiplier != null) {
			List<InstrumentPriceModifier> instrumentPriceModifier = multiplier
					.findByNameList(instrumentModule.getKey());
			multiplierValue = instrumentPriceModifier != null ? instrumentPriceModifier.get(0).getModifier() : 1;
		}
		return multiplierValue;
	}

	private synchronized void parseFile() throws IOException {
    	try (Stream<String> stream = Files.lines(Paths.get(inputPath))) {
    		
    		Stream<List<String>> partitioned = PartitioningSpliterator.partition(stream, CommonConstants.BATCH_SIZE, 1);
    		partitioned.forEach(chunk -> 
	    		chunk.stream()
	    		.filter(instrument-> InstrumentUtil.isWorkDay(InstrumentUtil.getDate(instrument.split(",")[1])) )
	    		.forEach(instrument -> add(InstrumentUtil.defineOf(instrument)))
    		);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
	}

	public static void add(Instrument instrument) {
		String name = instrument.getName();

		if (MODULES.containsKey(name) && INSTRUMENTS.containsKey(name)) {
			INSTRUMENTS.get(name).add(instrument);
			CalculatorEngineFactory.create(name, MODULES.get(name), instrument, false);
		}
	}

	@Override
	@PostConstruct
	public void run() {
		logger.debug("Calculator Engine calculate");

		List<InstrumentPriceModifier> modifiers = instrumentPriceModifierDao.findAll();
		modifiers.forEach(System.out::println);

//		Instrument newInstrument = new newInstrument("INSTRUMENT3", 4.0d, new Date());
//		newInstrument.setInstrumentCalculateBehavior(new OnFlyModule() {
//			@Override
//			public synchronized Double calculate() {
//				double sum = 0;
//				int counter = 0;
//				for (Instrument i : getInstruments()) {
//					System.out.println(i.getName());
//					System.out.println(i.getPrice());
//					sum += i.getPrice();
//					counter++;
//				}
//				return sum * 2;
//			}
//		});
//
//		this.addModule(newInstrument);
		this.calculate(instrumentPriceModifierDao);
		Map<String, Double> prices = new TreeMap<>();
		prices = this.calculate(instrumentPriceModifierDao);
		prices.entrySet().stream().forEach(System.out::println);
	}

}
