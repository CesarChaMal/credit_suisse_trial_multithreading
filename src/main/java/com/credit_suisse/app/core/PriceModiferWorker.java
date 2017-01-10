package com.credit_suisse.app.core;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import com.credit_suisse.app.dao.InstrumentPriceModifierDao;
import com.credit_suisse.app.util.CommonConstants;
import com.credit_suisse.app.util.InstrumentUtil;

public class PriceModiferWorker {
	
	public PriceModiferWorker() {
	};

	public synchronized void updateModifiers(InstrumentPriceModifierDao instrumentPriceModifierDao) {
		String name = "";
		double multiplier = 0;
		NumberFormat formatter = new DecimalFormat("#0.00000");     
		for (int i = 1; i <= CommonConstants.INSTRUMENTS_COUNT; i++) {
			name = "INSTRUMENT" + i;
			if(CommonConstants.MODIFIER_DOUBLE)
				multiplier = Double.parseDouble(formatter.format(InstrumentUtil.generateRandomNumberDouble(CommonConstants.MODIFIER_MIN, CommonConstants.MODIFIER_MAX)));
			else
				multiplier = InstrumentUtil.generateRandomNumberInteger(CommonConstants.MODIFIER_MIN, CommonConstants.MODIFIER_MAX);
			instrumentPriceModifierDao.setMultiplier(name, multiplier);
		}
	}
	
}