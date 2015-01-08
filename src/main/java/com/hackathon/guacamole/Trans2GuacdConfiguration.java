package com.hackathon.guacamole;

import org.glyptodon.guacamole.protocol.GuacamoleConfiguration;
import org.json.JSONObject;

/**
 * @foundation transfor the jsonString to GuacamoleConfiguration
 * 
 * @author v-bih
 * 
 * @param  jsonString
 * @return GuacamoleConfiguration
 */
public class Trans2GuacdConfiguration {
	
	private GuacamoleConfiguration configuration ;

	public Trans2GuacdConfiguration(String jsonString) {
				
		try {
			JSONObject json = new JSONObject(jsonString);
			
			configuration = new GuacamoleConfiguration();
			configuration.setParameter("id", json.getString("id"));
			configuration.setParameter("username", json.getString("username"));
			configuration.setParameter("password", json.getString("password"));
			configuration.setParameter("hostname", json.getString("hostname"));
			configuration.setParameter("port", json.getString("port"));
			configuration.setProtocol(json.getString("protocol"));	
			
		} catch (Exception e) {
			configuration = new GuacamoleConfiguration();
			e.printStackTrace();			
		}			
	}

	public GuacamoleConfiguration getConfiguration() {
		return configuration;
	}

	
}