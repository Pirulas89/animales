package com.hibernate.util;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import com.hibernate.model.Cliente;
import com.hibernate.model.Animal;
import com.hibernate.model.Medicina;
import com.hibernate.model.Adopcion;

public class HibernateUtil {
	private static SessionFactory sessionFactory;

	public static SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			try {
				Configuration configuration = new Configuration();

				Properties settings = new Properties();
				settings.put(AvailableSettings.JAKARTA_JDBC_DRIVER, "com.mysql.cj.jdbc.Driver");
				settings.put(AvailableSettings.JAKARTA_JDBC_URL, "jdbc:mysql://127.0.0.1:3307/refugio_animales?useSSL=false");
				settings.put(AvailableSettings.JAKARTA_JDBC_USER, "alumno");
				settings.put(AvailableSettings.JAKARTA_JDBC_PASSWORD, "alumno");
				settings.put(AvailableSettings.SHOW_SQL, "true");
				settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
				settings.put(AvailableSettings.HBM2DDL_AUTO, "update");

				configuration.setProperties(settings);

				configuration.addAnnotatedClass(Medicina.class);
				configuration.addAnnotatedClass(Animal.class);
				configuration.addAnnotatedClass(Cliente.class);
				configuration.addAnnotatedClass(Adopcion.class);

				ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
						.applySettings(configuration.getProperties()).build();

				sessionFactory = configuration.buildSessionFactory(serviceRegistry);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sessionFactory;
	}
}
