package br.com.fiap.twitter;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

	private Properties properties = new Properties();

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public Config() {
		try {
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("config.properties");
			properties.load(inputStream);
			inputStream.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
