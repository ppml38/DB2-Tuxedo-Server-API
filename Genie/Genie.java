/*
 * Genie.java    1.0 12Mar2017
 *
 * Copyright 2004 Liju, Inc. All rights reserved.
 * Liju PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
 
import java.io.*;
import java.net.*;
import java.util.*;
//import Logger.*;
//import ConfigurationManager.*;

/**
 * Genie is a webserver with Mainframe DB2 connection facility.
 * It plays below 2 rols
 * 1. HTTP server
 * 2. Mainframe DB2 connector
 */
class Genie
{
	
	/**
	 * Constants
	 */
	 static String CONFIGURATION_FILE_NAME = "Manifest.dat";
	 static String LOGFILE = "LogFile";
	 
        public static void main ( String[] args )
        {
			try
			{
			//Get coinfigurations
			//Setup logger files
			//All the configurations of Genie has to be in the manifest.dat fil
			ConfigurationManager.setConfigurationFile(CONFIGURATION_FILE_NAME);
			Logger.setLogFile(ConfigurationManager.getConfig(LOGFILE));
			//Get an instance of the server and start it.
            Server srvr=new Server();
            srvr.startServer();
			}
			catch(Exception e)
			{
				//There may be exceptions thrown from configuration manager
				//System.out.println(e.getMessage());
				e.printStackTrace();
                //Errors in this area are not toleratable. Just abort.
			}

        }

}


/*
Class Name : Server
Accepts the connection from user browsers and invokes Connection handler to handle those.
This is just an interface class to accept the connetion.
Connection conditions such No of connections to accept, list of valid IPs to accept connections from can be given here.
Its assumed here ConnectionHandler has all the functionalitites to handle the request and respond.
*/
class Server
{

        ServerSocket Srvrsckt =null;
        Socket       Sckt     =null;


        public void startServer() throws Exception
        {
			/**
			 * Get all server configurations.
			 */
			String SERVERPORT = "ServerPort";
			int serverport = Integer.parseInt(ConfigurationManager.getConfig(SERVERPORT));
			
            
                Srvrsckt=new ServerSocket ( serverport );
                while ( true )
                {
                    Sckt    =Srvrsckt.accept();
                    new Thread ( new ConnectionHandler ( Sckt ) ).start();
                }
            
            


        }
}


