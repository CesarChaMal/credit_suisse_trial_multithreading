package com.credit_suisse.app.dao;

import java.util.List;

import com.credit_suisse.app.model.InstrumentPriceModifier;

public interface InstrumentPriceModifierDao {

	InstrumentPriceModifier findById(Long id);

	InstrumentPriceModifier findByName(String name);
	
	List<InstrumentPriceModifier> findByNameList(String name);
	
	List<InstrumentPriceModifier> findAll();

}