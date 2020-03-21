/*
 * ConfigurationManager.java    1.0 12Mar2017
 *
 * Copyright 2004 Liju, Inc. All rights reserved.
 * Liju PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

// package ConfigurationManager;
 
import java.sql.*;
import java.io.*;
import java.util.*;

/**
 * Controls the configurations of an application
 * This initially reads the configuration from the specified file.
 * Allows calling classes to read and update the configurations.
 * This may use different ReaderClasses for different file formats(XML, Json etc..). or its own for plain text files.
 * Methods in this class are defined static to be called from any place without instantiating.
 */
public final class ConfigurationManager
{
        /**
         * Constants
         */
        private static String COMMA = ",";
        private static String DEFAULT_SEPERATOR = COMMA;
        private static String SEPERATOR;

        /*
         * Properties
         */
        private static String configurationfile; // Filename which contains all the configurations related with this application
        private static String[] names = new String[30];           // Configuration names
        private static String[] values = new String[30];          // Configuration values
        private static int count;                // No of configurations set so far


        /**
         * Running Variables
         */
        private static BufferedReader ireader;       // File reader
        private static String line;                             // Running variable to hold current line


        /**
         * Sets the configurations file, reads and fills the name, value combinations
         * This function has to be called initially before calling any other functions
		 * This can throw ioException
         */
        public static void setConfigurationFile ( String filename ) throws Exception
        {
            /**
             * Seperate ReaderClasses has to be invoked here to read corresponding file types such XML and JSON
             * Default configuration file type is comma seperated plain text
             * file can have comments starting with *
             * each configuration has to be in its own line
             * file can be in below format
             * hostip, 127.0.0.1
             * *above is host ip
             * logfile, log.txt
             */

            /*
             * set the coinfiguration file name first
             */
            configurationfile = filename;
            count = 0;

            //Set default values before reading the file
            SEPERATOR = DEFAULT_SEPERATOR;

            //Read the file lines one by one
            ireader=new BufferedReader ( new FileReader ( new File ( configurationfile ) ) );
            while ( ( line = ireader.readLine() ) != null )
				if(
				( !line.trim().equals("") ) &&   //If this is an empty line
                ( line.charAt ( 0 ) !='*' )     //If this is a comment
            )
            {
				
				//System.out.println(line);
				//System.out.println(line.charAt ( 0 ));
				//System.out.println(line.trim().equals(""));
                //As per default format, one line must contain one name and value pair with comman as seperator
                String[] pair = line.split ( SEPERATOR );				//Split the line with comma
				//System.out.println(SEPERATOR);
				//System.out.println(pair[0]);
                names[count] = pair[0].trim();                //Before comma is the name of configuration
                values[count] = pair[1].trim();               //After comma is the value of configuration
                count++;
            }
            ireader.close();

        }

        /**
         * gets the configuration value for given name
         * returns null if not found, no configuration available
         */
        public static String getConfig ( String inname )
        {
            try
            {
                for ( int i=0; i<count; i++ )
                {
                    if ( names[i].equals ( inname ) )
                    {
                        return values[i];
                    }
                }
            }
            catch ( Exception e )
            {
                //Configuration array is not set (properly) which may lead to arrayindexoutofbound exception
				return null;
            }
            return null; //no such configuration found
        }

        /**
         * sets/updates the configuration value for given name
         * reuturns false if error
		 * this function will not save configuration permanently in file. This update temporary for current execution
         */
		public static boolean setConfig(String inname, String invalue)
		{
			try
            {
                for ( int i=0; i<count; i++ )
                {
                    if ( names[i].equals ( inname ) )
                    {
                        values[i] = invalue;
						return true;
                    }
                }
            }
            catch ( Exception e )
            {
                //if Configuration array is not set (properly) this may lead to arrayindexoutofbound exception
				return false;
            }
            return false; // no such configuration found
		}

}