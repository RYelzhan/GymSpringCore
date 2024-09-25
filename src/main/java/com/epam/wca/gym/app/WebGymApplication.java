package com.epam.wca.gym.app;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class WebGymApplication {
    public static void main(String[] args) throws Exception {
        // Create Tomcat instance
        Tomcat tomcat = new Tomcat();

        // Create a Connector
        final Connector connector = new Connector();
        connector.setPort(8080);
        connector.setScheme("http");
        connector.setSecure(false);
        tomcat.setConnector(connector);

        // Create a temporary directory for Tomcat to unpack
        String webappDir = new File("src/main/webapp").getAbsolutePath();
        tomcat.setBaseDir(webappDir);

        // Add a web application context
        Context context = tomcat.addWebapp("", webappDir);

        // Disable JSP
        context.setJspConfigDescriptor(null); // not working :(

        // Start Tomcat
        tomcat.start();
        tomcat.getServer().await();
    }
}
