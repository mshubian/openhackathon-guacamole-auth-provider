package com.hackathon.guacamole;

import org.glyptodon.guacamole.GuacamoleException;
import org.glyptodon.guacamole.net.auth.Credentials;
import org.glyptodon.guacamole.net.auth.UserContext;
import org.glyptodon.guacamole.net.auth.simple.SimpleAuthenticationProvider;
import org.glyptodon.guacamole.net.auth.simple.SimpleConnection;
import org.glyptodon.guacamole.net.auth.simple.SimpleConnectionDirectory;
import org.glyptodon.guacamole.protocol.GuacamoleConfiguration;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import java.util.*;

public class HackathonAuthenticationProvider extends SimpleAuthenticationProvider {
	
	public static final Logger logger = Logger.getLogger(HackathonAuthenticationProvider.class.getClass());

    /* two constructed functions */
    public HackathonAuthenticationProvider() {
    	PropertyConfigurator.configure("/etc/guacamole/logger.properties");
    	logger.info("log init sucess ~~~");

    }

    @Override
    public Map<String, GuacamoleConfiguration> getAuthorizedConfigurations(Credentials credentials) throws GuacamoleException {

        GuacamoleConfiguration config = getGuacamoleConfiguration(credentials.getRequest());

        if (config == null) {
            return null;
        }

        Map<String, GuacamoleConfiguration> configs = new HashMap<String, GuacamoleConfiguration>();
        configs.put(config.getParameter("id"), config);
        return configs;
    }

    @Override
    public UserContext updateUserContext(UserContext context, Credentials credentials) throws GuacamoleException {
        HttpServletRequest request = credentials.getRequest();
        GuacamoleConfiguration config = getGuacamoleConfiguration(request);
      
        if (config == null) {
            return null;
        }
        String id = config.getParameter("id");
        SimpleConnectionDirectory connections = (SimpleConnectionDirectory) context.getRootConnectionGroup().getConnectionDirectory();
        SimpleConnection connection = new SimpleConnection(id, id, config);
        connections.putConnection(connection);
        return context;
    }

    private GuacamoleConfiguration getGuacamoleConfiguration(HttpServletRequest request) throws GuacamoleException {
    	
    	GuacamoleConfiguration config ;
    	String jsonString = null;
    	   	
        /*get cookies */
    	String cookieString = "";
        Cookie cookies[]= request.getCookies();
        for (int i = 0; i < cookies.length; i++) {
			Cookie cookie = cookies[i];		
			cookieString = cookie.getName() + "=" + cookie.getValue();
		}
        logger.info("cookieString is : |" + cookieString);
               
        /*check user valid or not*/
		try {
			Connect2OpenHackathon conn = new Connect2OpenHackathon();
			
			jsonString = conn.getGuacamoleJSONString(cookieString);
			logger.info("get guacamole json String :" + jsonString);
			Trans2GuacdConfiguration trans = new Trans2GuacdConfiguration(jsonString);
			config = trans.getConfiguration();
			return config ;
			
		} catch (Exception e) {
			logger.error("Exception when connect with OSSLAB to check User Cookies AAA");
			e.printStackTrace();
			return null;
		}

    }
}

