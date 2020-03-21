/*
 * ConnectionHandler.java    1.0 12Mar2017
 *
 * Copyright 2004 Liju, Inc. All rights reserved.
 * Liju PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.image.*;
import javax.imageio.*;

//import dbc.*;
//import Logger.*;
//import ConfigurationManager.*;

/**
 * This runs as a thread handling a connection at a time
 * Connection to handle will be supplied as a socket from calling class
 * Incase of any error return #error# as a response
 */

class ConnectionHandler implements Runnable
{

        String WWWPATH = "wwwPath";
        String INDEXFILE = "IndexFile";

        String DATAFILE = "DataFile";

        String RESPATH = "resPath";

        String QPATH = "qpath";
        String ERRFILE = "ErrFile";

        String wwwpath;

        String respath;
        String indexfile;

        String datafile;

        String qpath;
        String errfile;


        Socket socket=null;

        PrintWriter      outToClient;
        BufferedReader   inFromClient;

        String clientSentence; //sample: GET /qry.q!__CCCC~QFA@$__NNNN~12@$__FFFF~FLORLOCD@$__DDDD~2017-02-28 HTTP/1.1

        public ConnectionHandler ( Socket sct )
        {
            socket=sct;
            wwwpath = ConfigurationManager.getConfig ( WWWPATH );
            indexfile = ConfigurationManager.getConfig ( INDEXFILE );

            datafile = ConfigurationManager.getConfig ( DATAFILE );

            respath = ConfigurationManager.getConfig ( RESPATH );

            qpath = ConfigurationManager.getConfig ( QPATH );
            errfile = ConfigurationManager.getConfig ( ERRFILE );

        }

        public void run()
        {
            //System.out.println("Thread started");
            try {
                inFromClient=new BufferedReader ( new InputStreamReader ( socket.getInputStream() ) );
                outToClient=new PrintWriter ( socket.getOutputStream(), true );
                clientSentence=inFromClient.readLine();
                Logger.log ( clientSentence,socket );

                //Give user request to respond() function.
                //System.out.println(clientSentence);

                if ( clientSentence!=null )
                    respond ( clientSentence.split ( "\\s" ) [1] );

                //respond ( clientSentence );
                socket.close();

                //System.out.println("Connection closed");

            }

            catch ( Exception e )

            {

                e.printStackTrace();

                try {Logger.log ( e );}catch ( Exception f ) {}

            }


        }




        /**
         * Gets user request, interpret the request and respond

               * If .q files requested, Execute q file with mfdb and return the results

               * Other files, return as it is. only if it present under www folder

               */
        public void respond ( String name )
        {
            //This function plays the role of file server.
            //Special type files like query file will be processed inside RequestProcessor and the response will be stored in a file
            //which will then be served from here
            File fl=null;
            BufferedReader opnScanner;



            if ( name.equals ( "/" ) ) {

                // If the index file requested
                fl=new File ( wwwpath+indexfile );
            }
            else {

			    // If q file requested
                if ( name.indexOf ( ".q!" ) !=-1 )

                {

                    String[] s = name.split ( "!" );

                    if ( new mfdb ( qpath+ ( s[0].substring ( 1 ) ),s[1] ).getdata ( respath+name ).equals ( "" ) )
                    {

                        fl=new File ( respath+name );
                    }
                    else {
                        //This means, Error in connection with mainframe Db2, Set the error
                        fl=new File ( respath+errfile );
                    }

                }

                else {
                    //q file without input
                    if ( name.indexOf ( ".q" ) !=-1 )
                    {
                        if ( new mfdb ( qpath+name.substring ( 1 ),null ).getdata ( respath+name ).equals ( "" ) ) //Passing the result file name same as request to eleiminate ambiguity when multiple threads runs at a time
                        {
                            fl=new File ( respath+name );
                        }
						else {
                        //This means, Error in connection with mainframe Db2, Set the error
                        fl=new File ( respath+errfile );
                    }
                    }
                    else

                        // If other files requested
                        fl=new File ( wwwpath+name.substring ( 1 ) );

                }
            }

            if ( fl.exists() )
            {
                try
                {

				// Image files which needs to be sent as bytes
				//png
			    if ( name.indexOf ( ".png" ) !=-1 )
				{
					//System.out.println("sending png");
					 OutputStream os=socket.getOutputStream();
					BufferedImage image=ImageIO.read(fl);
               // ByteArrayOutputStream baos=new ByteArrayOutputStream();
                //ImageIO.write(image,"png",baos);
                ImageIO.write(image,"png",os);
                //baos.close();
                //ObjectOutputStream oos=new ObjectOutputStream(os);
                //oos.writeObject(baos.size()+"");
                //os.write(baos.toByteArray());
				//System.out.println("done");
				}
				else{
				// Other files with text format
                    opnScanner = new BufferedReader ( new FileReader ( fl ) );
                    String s;

                    while ( ( s = opnScanner.readLine() ) != null )
                    {
                        outToClient.println ( s );
                    }

                    opnScanner.close();

                    outToClient.println ( "\r\n" );
				}
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                    try {Logger.log ( e );}catch ( Exception f ) {}
                }
            }
            else
            {
                outToClient.println ( "#ERROR#" );
                outToClient.println ( "\r\n" );
            }

        }


}