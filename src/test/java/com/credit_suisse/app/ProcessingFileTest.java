package com.credit_suisse.app;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.junit.Test;

import com.credit_suisse.app.core.CalculatorEngine;
import com.credit_suisse.app.core.module.AverageModule;
import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.util.CommonConstants;
import com.credit_suisse.app.util.InstrumentUtil;
import com.credit_suisse.app.util.PartitioningSpliterator;

public class ProcessingFileTest {

	private static final Map<String, List<Instrument>> INSTRUMENTS = new TreeMap<>();

	private static final String INSTRUMENT = "INSTRUMENT1";

//	@Test
	public void averageProcessingTest() {

		INSTRUMENTS.put(INSTRUMENT, new ArrayList<Instrument>());

		InputStream is = ProcessingFileTest.class.getResourceAsStream("src/main/resources/input.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		String line;
		AverageModule averageModule = new AverageModule();

		try {
			while ((line = br.readLine()) != null) {
				Instrument instrument = InstrumentUtil.defineOf(line);
				if (instrument != null) {
					if (INSTRUMENT.equals(instrument.getName())) {
						INSTRUMENTS.get(INSTRUMENT).add(instrument);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

//		INSTRUMENTS.get(INSTRUMENT).forEach(System.out::println);
//		System.out.println(INSTRUMENTS.get(INSTRUMENT).size());
//		System.out.println(averageModule.calculate());
	
		averageModule.addInstruments(INSTRUMENTS.get(INSTRUMENT));
		assertEquals(4, INSTRUMENTS.get(INSTRUMENT).size());
//    	assertEquals(0.0, averageModule.calculate(), 0.001);
	}
	
	@Test
	public void averageProcessingTesInChunks() {
		
		INSTRUMENTS.put(INSTRUMENT, new ArrayList<Instrument>());
		AverageModule averageModule = new AverageModule();
		
    	try (Stream<String> stream = Files.lines(Paths.get("src/main/resources/huge_input.txt"))) {
    		
    		Stream<List<String>> partitioned = PartitioningSpliterator.partition(stream, 100, 1);
    		partitioned.forEach(chunk -> {
					chunk.stream()
					.filter(instrument-> InstrumentUtil.isWorkDay(InstrumentUtil.getDate(instrument.split(",")[1])) )
					.forEach(instrument -> CalculatorEngine.add(InstrumentUtil.defineOf(instrument)));
	//				chunk.forEach(System.out::println);
					chunk.size();
				}
    		);
//    		stream.forEach(System.out::println);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
	}

}
