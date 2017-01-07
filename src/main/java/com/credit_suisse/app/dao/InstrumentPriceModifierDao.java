package com.credit_suisse.app.dao;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import com.credit_suisse.app.model.InstrumentPriceModifier;

@Configuration
@Repository
//@Repository(value="InstrumentPriceModifierDao")
public interface InstrumentPriceModifierDao {

	InstrumentPriceModifier findById(Long id);

	InstrumentPriceModifier findByName(String name);
	
	List<InstrumentPriceModifier> findByNameList(String name);
	
	List<InstrumentPriceModifier> findAll();
	
	void setMultiplier(String instrumentName, double multiplier);
}