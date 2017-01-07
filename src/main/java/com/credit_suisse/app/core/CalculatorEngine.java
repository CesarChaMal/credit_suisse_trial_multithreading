package com.credit_suisse.app.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.credit_suisse.app.core.module.AverageModule;
import com.credit_suisse.app.core.module.AverageMonthModule;
import com.credit_suisse.app.core.module.AverageNewsInstrumentsModule;
import com.credit_suisse.app.core.module.OnFlyModule;
import com.credit_suisse.app.dao.InstrumentPriceModifierDao;
import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.InstrumentFactory;
import com.credit_suisse.app.model.InstrumentPriceModifier;
import com.credit_suisse.app.servlet3.MyWebInitializer;
import com.credit_suisse.app.util.CommonConstants;
import com.credit_suisse.app.util.InstrumentUtil;

//@Configuration
//@ComponentScan({ "com.credit_suisse.app" })
//@ComponentScan({ "com.credit_suisse.app.config", "com.credit_suisse.app.config.db", "com.credit_suisse.app.dao" })
//@ImportResource(value = {"file:src/main/**/db-derby-config.xml","file:src/main/**/db-h2-config.xml","file:src/main/**/db-hsqldb-config.xml","file:src/main/**/spring-bean-config.xml"})
//@Import( {DerbyDataSource.class, H2DataSource.class, HsqlDataSource.class, SpringRootConfig.class, SpringWebConfig.class} )
//@Import( {DerbyDataSource.class, H2DataSource.class, HsqlDataSource.class, InstrumentPriceModifierDao.class, InstrumentPriceModifierDaoImpl.class} )
//@ContextConfiguration(locations = {"file:src/main/**/db-derby-config.xml","file:src/main/**/db-h2-config.xml","file:src/main/**/db-hsqldb-config.xml","file:src/main/**/spring-bean-config.xml"})
public class CalculatorEngine extends Thread {
	
	private static final Logger logger = LoggerFactory.getLogger(CalculatorEngine.class);

	private static final Map<String, List<Instrument>> INSTRUMENTS = new TreeMap<>();
//	private static volatile Map<String, List<Instrument>> INSTRUMENTS = new ConcurrentHashMap<>();

	private static final Map<String, Instrument> MODULES = new TreeMap<>();
//	private static volatile Map<String, Instrument> MODULES = new ConcurrentHashMap<>();

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
		logger.debug(String.format("Input file path: %s", inputPath));
		this.inputPath = CommonConstants.INPUT_FILE;
		this.instrumentPriceModifierDao = instrumentPriceModifierDao;
		init();
	}

	public CalculatorEngine(String inputPath) {
		logger.debug(String.format("Input file path: %s", inputPath));
		this.inputPath = inputPath;
		init();
	}
	
	public CalculatorEngine() {
	}
	
	// commented out because causes synchronization issue
	static {
//		init();
	}

	private synchronized static void init() {
		logger.debug("Init instruments");

		for (int i = 1; i <= CommonConstants.INSTRUMENTS_COUNT; i++) {
			String name = "INSTRUMENT" + i;
			INSTRUMENTS.put(name, new ArrayList<Instrument>());
			
			Instrument instrument = InstrumentFactory.createInstrument(name);
			MODULES.put(name, instrument);

//			if (name.equals(CommonConstants.INSTRUMENT1)){
//				Instrument instrument1 = new Instrument1(CommonConstants.INSTRUMENT1);
//				instrument1.setInstrumentCalculateBehavior(new AverageModule());
//				MODULES.put(CommonConstants.INSTRUMENT1, instrument1);
//			} else if (name.equals(CommonConstants.INSTRUMENT2)){
//				Instrument instrument2 = new Instrument2(CommonConstants.INSTRUMENT2);
//				instrument2.setInstrumentCalculateBehavior(new AverageMonthModule());
//				MODULES.put(CommonConstants.INSTRUMENT2, instrument2);
//			} else if (name.equals(CommonConstants.INSTRUMENT3)){
//				Instrument instrument3 = new Instrument3(CommonConstants.INSTRUMENT3);
//				instrument3.setInstrumentCalculateBehavior(new OnFlyModule());
//				MODULES.put(CommonConstants.INSTRUMENT3, instrument3);
//			} else{
//				Instrument newInstrument = new newInstrument(CommonConstants.NEW_INSTRUMENT);
//				newInstrument.setInstrumentCalculateBehavior(new AverageNewstInstrumentsModule(name));
//				MODULES.put(name, newInstrument);
//			}
			
		}

	}

	public synchronized void addModule(Instrument instrument) {
		logger.info(String.format("Add module %s for instrument %s", instrument.getClass().getName(),instrument.getName()));
		String name = instrument.getName();
		INSTRUMENTS.get(name).add(instrument);
		this.addEngineModule(instrument);
	}

	private synchronized void addEngineModule(Instrument instrument) {
		String name = instrument.getName();
		if (MODULES.containsKey(name) && INSTRUMENTS.containsKey(name)) {
			CalculatorEngineFactory.create(name, MODULES.get(name), instrument);
			
//			if (CommonConstants.INSTRUMENT1.equalsIgnoreCase(name)) {
//				AverageModule module = (AverageModule) MODULES.get(name).getInstrumentCalculateBehavior();
//				module.getInstruments().add(instrument);
//			} else if (CommonConstants.INSTRUMENT2.equalsIgnoreCase(name)) {
//				AverageMonthModule module = (AverageMonthModule) MODULES.get(name).getInstrumentCalculateBehavior();
//				module.getInstruments().add(instrument);
//			} else if (CommonConstants.INSTRUMENT3.equalsIgnoreCase(name)) {
//				Instrument instrumentOri = MODULES.get(name);
//				OnFlyModule module = (OnFlyModule) instrumentOri.getInstrumentCalculateBehavior();
//				module.getInstruments().add(instrument);
//				OnFlyModule moduleDest = (OnFlyModule) instrument.getInstrumentCalculateBehavior();
//				moduleDest.addInstruments(module.getInstruments());
//				instrumentOri.setInstrumentCalculateBehavior(moduleDest);
////				System.out.println(Arrays.deepToString(module.getInstruments().toArray()));
//			} else {
//				AverageNewsInstrumentsModule module = (AverageNewsInstrumentsModule) MODULES.get(name).getInstrumentCalculateBehavior();
//				module.getInstruments().add(instrument);
//			}
		}
	}

	public synchronized Map<String, Double> calculate(InstrumentPriceModifierDao multiplier) {
		Map<String, Double> result = new TreeMap<>();
		parseFile();
		Double multiplierValue = 1.0;
		Double compute = 0.0;
		Double instrumentValue = 0.0;
		NumberFormat formatter = new DecimalFormat("#0.00000");     
		
		logger.debug("MODULES.size: " + MODULES.size());
		
		for (Entry<String, Instrument> instrumentModule : MODULES.entrySet()) {
			instrumentValue = instrumentModule.getValue().calculate();
			multiplierValue = getModifier(multiplier, instrumentModule);

			if (CommonConstants.MODIFIERS) {
				compute = instrumentModule.getValue().calculate() * multiplierValue;
//				compute = Double.parseDouble(formatter.format(instrumentModule.getValue().calculate() * multiplierValue));
			} else {
				compute = instrumentModule.getValue().calculate();
			}
			
			logger.info(instrumentModule.getKey() + ":" + instrumentValue);
			logger.info("Multiplier:" + multiplierValue);
			logger.info("Result: " + compute + "\n");
//			result.put(instrumentModule.getKey(), );
			result.put(instrumentModule.getKey(), compute);
		}
		return result;
	}

	public synchronized Double getModifier(InstrumentPriceModifierDao multiplier, Entry<String, Instrument> instrumentModule) {
		double multiplierValue = 1.0;
		if (multiplier != null){
			List<InstrumentPriceModifier> instrumentPriceModifier = multiplier.findByNameList(instrumentModule.getKey());
			multiplierValue = instrumentPriceModifier != null ? instrumentPriceModifier.get(0).getModifier() : 1;
		}
		return multiplierValue;
	}

	private synchronized void parseFile() {
		String line = null;
		try (BufferedReader reader = Files.newBufferedReader(new File(inputPath).toPath(), Charset.defaultCharset())) {
			while ((line = reader.readLine()) != null) {
				Instrument instrument = InstrumentUtil.defineOf(line);
				if (instrument != null) {
					add(instrument);
				}
			}
		} catch (IOException e) {
			logger.error("InputFile exception : ", e);
		}
	}

	private static void add(Instrument instrument) {
		String name = instrument.getName();
		
		if (MODULES.containsKey(name) && INSTRUMENTS.containsKey(name)) {
			INSTRUMENTS.get(name).add(instrument);
			CalculatorEngineFactory.create(name, MODULES.get(name), instrument);
			
//			manager.setStrategy(new CalculatorEngineAverageModule());
//			manager.add(name, MODULES.get(name), instrument);
//			
//			manager.setStrategy(new CalculatorEngineAverageMonthModule());
//			manager.add(name, MODULES.get(name), instrument);
//			
//			manager.setStrategy(new CalculatorEngineOnFlyModule());
//			manager.add(name, MODULES.get(name), instrument);
//			
//			manager.setStrategy(new CalculatorEngineAverageNewsInstrumentsModule());
//			manager.add(name, MODULES.get(name), instrument);
		}
		
		
		
//		if (MODULES.containsKey(name) && INSTRUMENTS.containsKey(name)) {
//			INSTRUMENTS.get(name).add(instrument);
//			if (CommonConstants.INSTRUMENT1.equalsIgnoreCase(name)) {
//				AverageModule module = (AverageModule) MODULES.get(name).getInstrumentCalculateBehavior();
//				module.getInstruments().add(instrument);
//			} else if (CommonConstants.INSTRUMENT2.equalsIgnoreCase(name)) {
//				AverageMonthModule module = (AverageMonthModule) MODULES.get(name).getInstrumentCalculateBehavior();
//				module.getInstruments().add(instrument);
//			} else if (CommonConstants.INSTRUMENT3.equalsIgnoreCase(name)) {
//				OnFlyModule module = (OnFlyModule) MODULES.get(name).getInstrumentCalculateBehavior();
//				module.getInstruments().add(instrument);
//			} else {
//				AverageNewstInstrumentsModule module = (AverageNewstInstrumentsModule) MODULES.get(name).getInstrumentCalculateBehavior();
//				module.getInstruments().add(instrument);
//			}
//		}
	}

	@Override
	@PostConstruct
	public void run() {
		logger.debug("Calculator Engine calculate");

		MyWebInitializer initializer = new MyWebInitializer();
		
		List<InstrumentPriceModifier> modifiers = instrumentPriceModifierDao.findAll();
		modifiers.forEach(System.out::println);

//		Instrument newInstrument = new newInstrument("INSTRUMENT3", 4.0d, new Date());
//		newInstrument.setInstrumentCalculateBehavior(new OnFlyModule(){
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
//				return sum*2;
//			}
//		});
		
//		this.addModule(newInstrument);
		this.calculate(instrumentPriceModifierDao);
	}

}
