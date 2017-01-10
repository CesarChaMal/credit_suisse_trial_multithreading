package com.credit_suisse.app.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.credit_suisse.app.core.CalculatorEngine;
import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.Instrument1;
import com.credit_suisse.app.model.Instrument2;
import com.credit_suisse.app.model.Instrument3;
import com.credit_suisse.app.model.newInstrument;

public class InstrumentUtil {

	private static final Logger logger = LoggerFactory.getLogger(InstrumentUtil.class);

	public static final List<Integer> WEEKEND = new ArrayList<Integer>() {
		private static final long serialVersionUID = 1L;
		{
			add(1);
			add(7);
		}
	};

	public synchronized static Instrument defineOf(String line) {
		Instrument instrument = null;
		String[] arr = line.split("[,]");
		if (arr.length == 3) {
			Date date = getDate(arr[1]);
			if (isWorkDay(date)) {
				String name = arr[0];
				Double price = Double.parseDouble(arr[2]);
				if (name.equalsIgnoreCase(CommonConstants.INSTRUMENT1))
					instrument = new Instrument1(name, price, date);
				else if (name.equalsIgnoreCase(CommonConstants.INSTRUMENT2))
					instrument = new Instrument2(name, price, date);
				else if (name.equalsIgnoreCase(CommonConstants.INSTRUMENT3))
					instrument = new Instrument3(name, price, date);
				else
					instrument = new newInstrument(name, price, date);
				logger.info("Instrument " + instrument.toString() + " in memory");
			}
		}
		return instrument;
	}

	public static boolean isWorkDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return !WEEKEND.contains(calendar.get(Calendar.DAY_OF_WEEK));
	}

	public static Date getDate(String txtDate) {
		DateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
		Date date = null;
		try {
			date = df.parse(txtDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	public static double generateRandomNumberDouble(int modifier_min, int modifier_max) {
		return modifier_min + (Math.random() * ((modifier_max - modifier_min)));
	} 
	
	public static int generateRandomNumberInteger(int modifier_min, int modifier_max) {
		return modifier_min + (int)(Math.random() * ((modifier_max - modifier_min) + 1));
	}
	
	public static String generateRandomDateRange(Date from, Date to) {
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long offset = Timestamp.valueOf(sdf.format(from)).getTime();
		long end = Timestamp.valueOf(sdf.format(to)).getTime();
		long diff = end - offset + 1;
		Timestamp rand = new Timestamp(offset + (long)(Math.random() * diff));

		DateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
//		DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		Date today = (Date) Calendar.getInstance().getTime();        
		String todayDate = df.format(today);
		String date = df.format(rand);

//		System.out.println(date);
//		System.out.println(today);
		return date;
	} 
	
}
