package com.hackathon.guacamole;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import javax.naming.spi.DirStateFactory.Result;
import javax.servlet.http.Cookie;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


public class Connect2OpenHackathon {

	private Logger logger = Logger.getLogger(Connect2OpenHackathon.class.getClass());
	private URL url = null ;
    private BufferedReader in = null;
		
	public Connect2OpenHackathon() throws Exception{		
		PropertyConfigurator.configure("/etc/guacamole/logger.properties");       
	}
	
	/*check user withn cookies */
	public String checkUser(String cookieString) {
		
		String URLstring = "http://osslab.msopentech.cn/getguacdconfiguration";
        String result = "";
     
        try {
        	 url = new URL(URLstring);

        	 HttpURLConnection.setFollowRedirects(false);
        	 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        	 
             conn.setRequestMethod("GET");  
             conn.setUseCaches(false);
             conn.setRequestProperty("Cookie", cookieString);
             conn.connect();
             
             int status = conn.getResponseCode();
             
             if (status != 200) {
            	 logger.debug("OpenHackathon" + conn.getResponseCode());
            	 logger.debug("user have not login , please do it before your request !!!");
            	 return result;
             }
           
             in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
             String line;
             while ((line = in.readLine()) != null) {
                 result += line;
             }
            
        } catch (Exception e) {
        	logger.error("Exception when connect with OSSLAB to check User Cookies BBB");
            e.printStackTrace();
        }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (conn != null) {
                    conn.disconnect();
				}
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}

	
