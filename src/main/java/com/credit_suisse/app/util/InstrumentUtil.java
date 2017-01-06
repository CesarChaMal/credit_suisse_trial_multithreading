package com.credit_suisse.app.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.credit_suisse.app.model.Instrument;
import com.credit_suisse.app.model.Instrument1;
import com.credit_suisse.app.model.Instrument2;
import com.credit_suisse.app.model.Instrument3;
import com.credit_suisse.app.model.newInstrument;

public class InstrumentUtil {

	public static final List<Integer> WEEKEND = new ArrayList<Integer>() {
		/**
		* 
		*/
		private static final long serialVersionUID = 1L;

		{
			add(1);
			add(7);
		}
	};

	public static Instrument defineOf(String line) {
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
			}
		}
		return instrument;
	}

	private static boolean isWorkDay(Date date) {
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
}
