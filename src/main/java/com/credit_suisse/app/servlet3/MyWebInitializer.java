package com.credit_suisse.app.servlet3;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.credit_suisse.app.config.Bootstrap;
import com.credit_suisse.app.config.PropertyConfig;
import com.credit_suisse.app.config.SpringRootConfig;
import com.credit_suisse.app.config.SpringWebConfig;
import com.credit_suisse.app.config.db.DerbyDataSource;
import com.credit_suisse.app.config.db.H2DataSource;
import com.credit_suisse.app.config.db.HsqlDataSource;
import com.credit_suisse.app.dao.InstrumentPriceModifierDao;
import com.credit_suisse.app.model.InstrumentPriceModifier;

public class MyWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Autowired
	DataSource dataSource;

	@Bean
	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return new NamedParameterJdbcTemplate(dataSource);
	}

	@Autowired
	InstrumentPriceModifierDao instrumentPriceModifierDao;


	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] { SpringRootConfig.class };
//		return new Class[] { SpringRootConfig.class, H2DataSource.class, DerbyDataSource.class, HsqlDataSource.class };
//		return new Class[] { SpringRootConfig.class, SpringWebConfig.class, Bootstrap.class, PropertyConfig.class, H2DataSource.class, DerbyDataSource.class, HsqlDataSource.class };
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return new Class[] { SpringWebConfig.class };
//		return new Class[] { SpringWebConfig.class, H2DataSource.class, DerbyDataSource.class, HsqlDataSource.class };
//		return new Class[] { SpringRootConfig.class, SpringWebConfig.class, Bootstrap.class, PropertyConfig.class, H2DataSource.class, DerbyDataSource.class, HsqlDataSource.class };
	}
//
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}

//	@Override
//	public void onStartup(ServletContext servletContext) throws ServletException {
//		super.onStartup(servletContext);
//		servletContext.setInitParameter("spring.profiles.active", "hsql");
//		servletContext.setInitParameter("spring.profiles.active", "derby");
//		servletContext.setInitParameter("spring.profiles.active", "h2");
//	}

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
    	servletContext.setInitParameter("spring.profiles.active", "h2");
        WebApplicationContext context = getContext();
        servletContext.addListener(new ContextLoaderListener(context));
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

    private AnnotationConfigWebApplicationContext getContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation("com.credit_suisse.app.config");
        return context;
    }

    @PostConstruct
	public void startDBManager() {
		System.out.println(instrumentPriceModifierDao);
		List<InstrumentPriceModifier> modifiers = instrumentPriceModifierDao.findAll();
		modifiers.forEach(System.out::println);
	}

}