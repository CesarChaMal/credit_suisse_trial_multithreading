package com.credit_suisse.app.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.credit_suisse.app.model.InstrumentPriceModifier;

@Configuration
@Repository
//@Repository(value="InstrumentPriceModifierDaoImpl")
public class InstrumentPriceModifierDaoImpl implements InstrumentPriceModifierDao {

	private static final Logger logger = LoggerFactory.getLogger(InstrumentPriceModifierDaoImpl.class);

	NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	
	public void setdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public InstrumentPriceModifier findById(Long id) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("id", id);
        
		String sql = "SELECT * FROM INSTRUMENT_PRICE_MODIFIER WHERE id=:id";
		
		InstrumentPriceModifier result = namedParameterJdbcTemplate.queryForObject(
                    sql,
                    params,
                    new InstrumentPriceModifierMapper());
                    
        return result;
	}

	
	@Override
	public InstrumentPriceModifier findByName(String name) {
		
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", name);
        
		String sql = "SELECT * FROM INSTRUMENT_PRICE_MODIFIER WHERE name=:name";
		
		InstrumentPriceModifier result = namedParameterJdbcTemplate.queryForObject(sql, params, new InstrumentPriceModifierMapper());
        return result;
	}
	
	@Override
	public List<InstrumentPriceModifier> findByNameList(String name) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		
		String sql = "SELECT * FROM INSTRUMENT_PRICE_MODIFIER WHERE name=:name";
		
//		InstrumentPriceModifier result = namedParameterJdbcTemplate.queryForObject(sql, params, new InstrumentPriceModifierMapper());
		List<InstrumentPriceModifier> result = namedParameterJdbcTemplate.query(sql, params, new InstrumentPriceModifierMapper());
		
		return result.size() == 0 ? null : result;
	}
	
	@Override
	public List<InstrumentPriceModifier> findAll() {
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		String sql = "SELECT * FROM INSTRUMENT_PRICE_MODIFIER";
		
        List<InstrumentPriceModifier> result = namedParameterJdbcTemplate.query(sql, params, new InstrumentPriceModifierMapper());
        
        return result.size() == 0 ? null : result;
	}

	@Override
	public void setMultiplier(String instrumentName, double multiplier) {
		Map<String, Object> params = new HashMap<String, Object>();
        params.put("name", instrumentName);
        params.put("multiplier", multiplier);

        String sql = "UPDATE INSTRUMENT_PRICE_MODIFIER set multiplier=:multiplier where name=:name";

        int status = namedParameterJdbcTemplate.update(sql, params);	
        
        if(status != 0){
            logger.info("Multiplier data updated for Instrument " + params.get("name") + " with value: "+ params.get("multiplier"));
        } else {
        	logger.debug("No instrument found with name " + params.get("name"));
        }	
	}

	private static final class InstrumentPriceModifierMapper implements RowMapper<InstrumentPriceModifier> {

		public InstrumentPriceModifier mapRow(ResultSet rs, int rowNum) throws SQLException {
			InstrumentPriceModifier instrument = new InstrumentPriceModifier();
			instrument.setId(rs.getLong("id"));
			instrument.setName(rs.getString("name"));
			instrument.setModifier(rs.getDouble("multiplier"));
			return instrument;
		}
	}

}