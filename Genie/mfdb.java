//package dbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;


//import com.ibm.db2.jcc.DB2SimpleDataSource;

public class mfdb {
        String DBCONFIG = "DBConfig";
        String DBUSERNAMEFILE = "DBUserNameFile";
        String DBPASSWORDFILE = "DBPasswordFile";
        String CONFIGPATH = "ConfigPath";
        String qfile;
        String valuepair;

        String dbconfig;
        String dbusernamefile;
        String dbpasswordfile;
        String configpath;

        public mfdb ( String qfile, String valuepair )
        {
            dbconfig = DBCONFIG;
            dbusernamefile = DBUSERNAMEFILE;
            dbpasswordfile = DBPASSWORDFILE;
            configpath=CONFIGPATH;
            this.qfile = qfile;
            this.valuepair = valuepair;
        }
		
		
        public String getdata ( String datafile ) {
            ResultSet rs=null;
            String ctgy=null;
            Connection con=null;
            Statement stmt=null;
            FileWriter owriter=null;
            String msg_rtn = "";
            String query;
            /*Read the query file and prepare the staement to query*/

            try {
                //get the query
                query = getFirstLine ( qfile );

                //Substitute values
                //value pair should be in below format
                //__carrier~QFA @$ __flight~12 @$ __date~12-02-2017
                //here '~' defines '=' symbol
                //'@$' defines '&' symbol
                //each variable in query file to be replaced, should have '^' in its prefix
                //These rules are to byepass default browser URL decoding
                //System.out.println(this.valuepair);
if(valuepair!=null)
for ( String a : this.valuepair.split ( Pattern.quote ( "@$" ) ) )
                {

                    String[] b = a.split ( "~" );
                    //System.out.println(a);
                    //System.out.println(b[0]);
                    //System.out.println(b[1]);
                    query=query.replace ( b[0],b[1] );
                }
            }
            catch ( Exception e )
            {
                msg_rtn = "Could not read file";
                return msg_rtn;
            }

            try {
                //System.out.println(query);
                Class.forName ( "com.ibm.db2.jcc.DB2Driver" );
                con = DriverManager.getConnection ( getFirstLine ( ConfigurationManager.getConfig ( configpath ) +ConfigurationManager.getConfig ( dbconfig ) ),
                                                    getFirstLine ( ConfigurationManager.getConfig ( configpath ) +ConfigurationManager.getConfig ( dbusernamefile ) ),
                                                    getFirstLine ( ConfigurationManager.getConfig ( configpath ) +ConfigurationManager.getConfig ( dbpasswordfile ) ) );
                con.setAutoCommit ( false );
                stmt = con.createStatement();


                rs = stmt.executeQuery ( query );
                owriter = new FileWriter ( datafile );
                String s="";

                int k=0;

                while ( rs.next() ) {
                    //s = "{"; //This is not required as the lines can be splitted with \r\n character in user end for each row
					s="";
                    String s1 = "";

                    if ( k==0 ) {
                        //System.out.println ( "first" );

                        while ( true )
                        {
                            try {
                                k++;
                                s1 = rs.getString ( k );
								s += ( s1==null?"null":s1 ) +"|"; //Seperator can be changed here
                            }
                            catch ( Exception e )
                            {
                                k--;
                                break;
                            }
                        }
					//s+="}";
                        //System.out.println ( k );
                    }
                    else {
                        //System.out.println ( "second" );
                        //System.out.println ( k );

                        for ( int k1=1;k1<=k;k1++ )
                        {
                            s1 = rs.getString ( k1 );
                            s += ( s1==null?"null":s1 ) +"|"; //Seperator can be changed here


                        }
					//s+="}";
                    }

                    owriter.write ( s.substring(0,s.length()-1 ));
                    owriter.write ( System.getProperty ( "line.separator" ) );
                }

                if ( s.equals ( "" ) ) msg_rtn="No flights found";

                owriter.close();

                rs.close();

                stmt.close();

                con.commit();

                con.close();
            } catch ( IOException e ) {
                msg_rtn = "Could not write files";
                e.printStackTrace();

                try {
                    owriter.close();
                    rs.close();
                    stmt.close();
                    con.commit();
                    con.close();
                }
                catch ( Exception e1 )
                {
                    e1.printStackTrace();
                    //System.out.println("Error closing all connections");
                }
            }
            catch ( Exception e ) {
                //System.out.println("Hello ! "+e.getMessage());
                //String a[]=e.getMessage().split(":");
                msg_rtn = e.getMessage().substring ( e.getMessage().indexOf ( " " ) +1 );
                e.printStackTrace();

                try {
                    owriter.close();
                    rs.close();
                    stmt.close();
                    con.commit();
                    con.close();
                }
                catch ( Exception e1 )
                {
                    System.out.println ( "Error closing all connections" );
                }
            }

            return msg_rtn;
        }

        public String getFirstLine ( String filename ) throws Exception

        {
            String line = "";


            BufferedReader ireader=new BufferedReader ( new FileReader ( new File ( filename ) ) );

            line = ireader.readLine();

            ireader.close();

            return line;

        }



}