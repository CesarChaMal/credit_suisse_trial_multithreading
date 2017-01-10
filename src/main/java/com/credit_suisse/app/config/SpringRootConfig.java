package com.credit_suisse.app.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.hsqldb.util.DatabaseManagerSwing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.credit_suisse.app.config.db.DerbyDataSource;
import com.credit_suisse.app.config.db.H2DataSource;
import com.credit_suisse.app.config.db.HsqlDataSource;
import com.credit_suisse.app.core.TaskManager;
import com.credit_suisse.app.dao.InstrumentPriceModifierDao;
import com.credit_suisse.app.util.CommonConstants;

@Configuration
@ComponentScan({ "com.credit_suisse.app" })
//@ImportResource(value = {"file:src/main/**/db-derby-config.xml","file:src/main/**/db-h2-config.xml","file:src/main/**/db-hsqldb-config.xml","file:src/main/**/spring-bean-config.xml"})
@Import( {DerbyDataSource.class, H2DataSource.class, HsqlDataSource.class, SpringWebConfig.class} )
public class SpringRootConfig {

	private static final Logger logger = LoggerFactory.getLogger(SpringRootConfig.class);

	@Autowired
	DataSource dataSource;

	@Bean
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return new NamedParameterJdbcTemplate(dataSource);
	}

	@Bean
	public JdbcTemplate getJdbcTemplate() {
		return new JdbcTemplate(dataSource);
	}	

	@Autowired
	InstrumentPriceModifierDao instrumentPriceModifierDao;


	@Value("${ThreadPoolSize}")
    private int threadPoolSize;
//    private int threadPoolSize = CommonConstants.THREAD_POOL_SIZE;
    
    @Bean
    public Bootstrap bootstrap() {
        return new Bootstrap();
    }
    
//    @Bean
    public TaskManager taskManager(InstrumentPriceModifierDao instrumentPriceModifierDao) {
    	
        return new TaskManager(instrumentPriceModifierDao);
    }
    
    @Bean
    public ExecutorService executorService() {
    	CommonConstants.THREAD_POOL_SIZE = threadPoolSize;
        return Executors.newFixedThreadPool(threadPoolSize);
    }
    
	@PostConstruct
	public void startDBManager() {
		//hsqldb
//		DatabaseManagerSwing.main(new String[] { "--url", "jdbc:hsqldb:mem:testdb", "--user", "sa", "--password", "" });

		//derby
//		DatabaseManagerSwing.main(new String[] { "--url", "jdbc:derby:memory:testdb", "--user", "", "--password", "" });

		//h2
		DatabaseManagerSwing.main(new String[] { "--url", "jdbc:h2:mem:testdb", "--user", "sa", "--password", "" });
	}
	
//	//MethodInvokingBean example
//	@PostConstruct
//	public void startDBM() {
//		MethodInvokingBean mBean = new MethodInvokingBean();
//
//		mBean.setTargetClass(DatabaseManagerSwing.class);
//		mBean.setTargetMethod("main");
//		String[] args = new String[] { "--url", "jdbc:hsqldb:mem:testdb", "--user", "sa", "--password", "" };
//		mBean.setArguments(args);
//		try {
//			mBean.prepare();
//			mBean.invoke();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

}