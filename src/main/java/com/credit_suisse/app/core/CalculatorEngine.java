package com.credit_suisse.app.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.credit_suisse.app.config.SpringRootConfig;
import com.credit_suisse.app.config.SpringWebConfig;
import com.credit_suisse.app.config.db.DerbyDataSource;
import com.credit_suisse.app.config.db.H2DataSource;
import com.credit_suisse.app.config.db.HsqlDataSource;
import com.credit_suisse.app.core.module.AverageModule;
import com.credit_suisse.app.core.module.AverageMonthModule;
import com.credit_suisse.app.core.module.AverageNewstInstrumentsModule;
import com.credit_suisse.app.core.module.OnFlyModule;
import com.credit_suisse.app.dao.InstrumentPriceModifierDao;
import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.Instrument1;
import com.credit_suisse.app.model.Instrument2;
import com.credit_suisse.app.model.Instrument3;
import com.credit_suisse.app.model.InstrumentPriceModifier;
import com.credit_suisse.app.model.newInstrument;
import com.credit_suisse.app.util.CommonConstants;
import com.credit_suisse.app.util.InstrumentUtil;

@Service
//@Controller
@ImportResource(value = {"file:src/main/**/db-derby-config.xml","file:src/main/**/db-h2-config.xml","file:src/main/**/db-hsqldb-config.xml","file:src/main/**/spring-bean-config.xml"})
//@Import( {DerbyDataSource.class, H2DataSource.class, HsqlDataSource.class, SpringRootConfig.class, SpringWebConfig.class} )
//@ContextConfiguration(locations = {"file:src/main/**/db-derby-config.xml","file:src/main/**/db-h2-config.xml","file:src/main/**/db-hsqldb-config.xml","file:src/main/**/spring-bean-config.xml"})
//@DependsOn("h2")
public class CalculatorEngine extends Thread {
	
    @Autowired
    public ApplicationContext ctx;

	@Autowired
	private WebApplicationContext wctx;
    
//	@Autowired
//	DataSource dataSource;

//	@Bean
//	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
//		return new NamedParameterJdbcTemplate(dataSource);
//	}

	public CalculatorEngine() {
	}

	private static final Logger logger = LoggerFactory.getLogger(CalculatorEngine.class);

	private static final Map<String, List<Instrument>> INSTRUMENTS = new TreeMap<>();
//	private static volatile Map<String, List<Instrument>> INSTRUMENTS = new ConcurrentHashMap<>();

	private static final Map<String, Instrument> MODULES = new TreeMap<>();
//	private static volatile Map<String, Instrument> MODULES = new ConcurrentHashMap<>();

	private String inputPath = null;
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	
	@Autowired
	InstrumentPriceModifierDao instrumentPriceModifierDao;
	
	// commented out because causes synchronization issue
	static {
//		init();
	}

	private synchronized static void init() {
		logger.debug("Init instruments");

		for (int i = 1; i <= CommonConstants.INSTRUMENTS_COUNT; i++) {
			String name = "INSTRUMENT" + i;
			INSTRUMENTS.put(name, new ArrayList<Instrument>());
			
			if (name.equals(CommonConstants.INSTRUMENT1)){
				Instrument instrument1 = new Instrument1(CommonConstants.INSTRUMENT1);
				instrument1.setInstrumentCalculateBehavior(new AverageModule());
				MODULES.put(CommonConstants.INSTRUMENT1, instrument1);
			} else if (name.equals(CommonConstants.INSTRUMENT2)){
				Instrument instrument2 = new Instrument2(CommonConstants.INSTRUMENT2);
				instrument2.setInstrumentCalculateBehavior(new AverageMonthModule());
				MODULES.put(CommonConstants.INSTRUMENT2, instrument2);
			} else if (name.equals(CommonConstants.INSTRUMENT3)){
				Instrument instrument3 = new Instrument3(CommonConstants.INSTRUMENT3);
				instrument3.setInstrumentCalculateBehavior(new OnFlyModule());
				MODULES.put(CommonConstants.INSTRUMENT3, instrument3);
			} else{
				Instrument newInstrument = new newInstrument(CommonConstants.NEW_INSTRUMENT);
				newInstrument.setInstrumentCalculateBehavior(new AverageNewstInstrumentsModule(name));
				MODULES.put(name, newInstrument);
			}
		}

	}

	public CalculatorEngine(String inputPath) {
		logger.debug(String.format("Input file path: %s", inputPath));
		this.inputPath = inputPath;
		init();
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
			if (CommonConstants.INSTRUMENT1.equalsIgnoreCase(name)) {
				AverageModule module = (AverageModule) MODULES.get(name).getInstrumentCalculateBehavior();
				module.getInstruments().add(instrument);
			} else if (CommonConstants.INSTRUMENT2.equalsIgnoreCase(name)) {
				AverageMonthModule module = (AverageMonthModule) MODULES.get(name).getInstrumentCalculateBehavior();
				module.getInstruments().add(instrument);
			} else if (CommonConstants.INSTRUMENT3.equalsIgnoreCase(name)) {
				Instrument instrumentOri = MODULES.get(name);
				OnFlyModule module = (OnFlyModule) instrumentOri.getInstrumentCalculateBehavior();
				module.getInstruments().add(instrument);
				OnFlyModule moduleDest = (OnFlyModule) instrument.getInstrumentCalculateBehavior();
				moduleDest.addInstruments(module.getInstruments());
				instrumentOri.setInstrumentCalculateBehavior(moduleDest);
//				System.out.println(Arrays.deepToString(module.getInstruments().toArray()));
			} else {
				AverageNewstInstrumentsModule module = (AverageNewstInstrumentsModule) MODULES.get(name).getInstrumentCalculateBehavior();
				module.getInstruments().add(instrument);
			}
		}
	}

	public synchronized Map<String, Double> calculate(InstrumentPriceModifierDao multiplier) {
		Map<String, Double> result = new TreeMap<>();
		parseFile();
		Double multiplierValue = 1.0;
		
		logger.debug("MODULES.size: " + MODULES.size());
		
		for (Entry<String, Instrument> instrumentModule : MODULES.entrySet()) {
			if (multiplier != null){
				List<InstrumentPriceModifier> instrumentPriceModifier = multiplier.findByNameList(instrumentModule.getKey());
				multiplierValue = instrumentPriceModifier != null ? instrumentPriceModifier.get(0).getModifier() : 1;
				System.out.println(instrumentPriceModifier);
			}
			System.out.println(instrumentModule.getKey() + ":" + instrumentModule.getValue().calculate());
			System.out.println(instrumentModule.getKey() + " multiplier:" + multiplierValue);
//			result.put(instrumentModule.getKey(), instrumentModule.getValue().calculate());
			result.put(instrumentModule.getKey(), instrumentModule.getValue().calculate() * multiplierValue);
		}
		return result;
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
			if (CommonConstants.INSTRUMENT1.equalsIgnoreCase(name)) {
				AverageModule module = (AverageModule) MODULES.get(name).getInstrumentCalculateBehavior();
				module.getInstruments().add(instrument);
			} else if (CommonConstants.INSTRUMENT2.equalsIgnoreCase(name)) {
				AverageMonthModule module = (AverageMonthModule) MODULES.get(name).getInstrumentCalculateBehavior();
				module.getInstruments().add(instrument);
			} else if (CommonConstants.INSTRUMENT3.equalsIgnoreCase(name)) {
				OnFlyModule module = (OnFlyModule) MODULES.get(name).getInstrumentCalculateBehavior();
				module.getInstruments().add(instrument);
			} else {
				AverageNewstInstrumentsModule module = (AverageNewstInstrumentsModule) MODULES.get(name).getInstrumentCalculateBehavior();
				module.getInstruments().add(instrument);
			}
		}
	}

	@Override
	public void run() {
		logger.debug("Calculator Engine calculate");
		
//		instrumentPriceModifierDao = (InstrumentPriceModifierDao) ctx.getBean(InstrumentPriceModifierDao.class);
//		instrumentPriceModifierDao = ctx.getBean(InstrumentPriceModifierDao.class);
//		List<InstrumentPriceModifier> modifiers = instrumentPriceModifierDao.findAll();
//		modifiers.forEach(System.out::println);

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
		
//		CalculatorEngine calculator = new CalculatorEngine(CommonConstants.INPUT_FILE);
//		CalculatorEngine calculator = CalculatorEngine.getInstance(CommonConstants.INPUT_FILE);
//		this.addModule(newInstrument);
		this.calculate(instrumentPriceModifierDao);
	}

}
