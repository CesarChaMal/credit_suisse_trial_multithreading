package com.credit_suisse.app.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.credit_suisse.app.core.CalculatorEngine;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InstrumentFileGenerator {
	
	private static final Logger logger = LoggerFactory.getLogger(InstrumentFileGenerator.class);
	
	public static void GenegerateInstrument() throws IOException {
		File fout = new File("src/main/resources/very_huge_input.txt");
		FileOutputStream fos = new FileOutputStream(fout);

		try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
			
//			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
			DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String from = "2000-01-01 00:00:00";
			String to = "2017-01-01 00:00:00";
			Date fromDate = null;
			Date toDate = null;
			NumberFormat formatter = new DecimalFormat("#0.00000");     

			try {
				fromDate = sdf.parse(from);
				toDate = sdf.parse(to);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}

			int counter = 1;
			String instrument = ""; 
//			for (int i = 1; i <= 10; i++) {
//			for (int i = 1; i <= 200; i++) {
//			for (int i = 1; i <= 5000; i++) {
//			for (int i = 1; i <= 1000; i++) {
			for (long i = 1; i <= 1000000000; i++) {
				instrument = "INSTRUMENT" + counter;
				
				if (counter > 3){
					if (i % 100 == 0){
						counter = 0;
					}
					if (i % 5 == 0){
						instrument = "INSTRUMENT" + InstrumentUtil.generateRandomNumberInteger(1, 100);
					}
					counter++;
				} else {
					if (i % 100 == 0){
						counter++;
					}
				}
					
				String date = InstrumentUtil.generateRandomDateRange(fromDate, toDate);
				Double number = Double.parseDouble(formatter.format(InstrumentUtil.generateRandomNumberDouble(0, 100)));
				String instrumentGenerated = instrument + "," + date + "," + number;
//				logger.info(instrumentGenerated);				
				System.out.println("\n" + instrumentGenerated + "\n");				
				showFileSize(fout);
				
				bw.write(instrumentGenerated);
				bw.newLine();
			}
		}
	}
	
	public static void showFileSize(File file){
		if(file.exists()){

			double bytes = file.length();
			double kilobytes = (bytes / 1024);
			double megabytes = (kilobytes / 1024);
			double gigabytes = (megabytes / 1024);
			double terabytes = (gigabytes / 1024);
			double petabytes = (terabytes / 1024);
			double exabytes = (petabytes / 1024);
			double zettabytes = (exabytes / 1024);
			double yottabytes = (zettabytes / 1024);

			System.out.println("bytes : " + bytes);
			System.out.println("kilobytes : " + kilobytes);
			System.out.println("megabytes : " + megabytes);
			System.out.println("gigabytes : " + gigabytes);
			System.out.println("terabytes : " + terabytes);
			System.out.println("petabytes : " + petabytes);
			System.out.println("exabytes : " + exabytes);
			System.out.println("zettabytes : " + zettabytes);
			System.out.println("yottabytes : " + yottabytes);
		}else{
			 System.out.println("File does not exists!");
		}
	}
	
	public static void main(String[] args) {
		try {
			InstrumentFileGenerator.GenegerateInstrument();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
