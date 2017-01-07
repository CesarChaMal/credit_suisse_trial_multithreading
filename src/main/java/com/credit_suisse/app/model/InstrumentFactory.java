package com.credit_suisse.app.model;

import com.credit_suisse.app.core.module.AverageModule;
import com.credit_suisse.app.core.module.AverageMonthModule;
import com.credit_suisse.app.core.module.AverageNewsInstrumentsModule;
import com.credit_suisse.app.core.module.OnFlyModule;
import com.credit_suisse.app.util.CommonConstants;

public class InstrumentFactory {

	public static Instrument createInstrument(String name){
		
		if (name.equals(CommonConstants.INSTRUMENT1)){
			Instrument instrument1 = new Instrument1(CommonConstants.INSTRUMENT1);
			instrument1.setInstrumentCalculateBehavior(new AverageModule());
			return instrument1;
		} else if (name.equals(CommonConstants.INSTRUMENT2)){
			Instrument instrument2 = new Instrument2(CommonConstants.INSTRUMENT2);
			instrument2.setInstrumentCalculateBehavior(new AverageMonthModule());
			return instrument2;
		} else if (name.equals(CommonConstants.INSTRUMENT3)){
			Instrument instrument3 = new Instrument3(CommonConstants.INSTRUMENT3);
			instrument3.setInstrumentCalculateBehavior(new OnFlyModule());
			return instrument3;
		} else{
			Instrument newInstrument = new newInstrument(CommonConstants.NEW_INSTRUMENT);
			newInstrument.setInstrumentCalculateBehavior(new AverageNewsInstrumentsModule(name));
			return newInstrument;
		}
		
	}
}