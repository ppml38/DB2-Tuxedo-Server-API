//package Logger;

import java.io.*;
import java.net.*;
import java.util.*;

public final class Logger
{
	//private static String Remote = new String("");
	private static File fl;

public static void setLogFile(String filename) throws Exception
{
	fl = new File(filename);
	if(fl==null){
		throw new GeneralException("Null file name");
	}
}

public static void log(String msg, Socket Sckt) throws Exception
{
		String Remote = "";
		FileWriter wrtlog = new FileWriter(fl,true);
		Remote+=Sckt==null?" ":Sckt.getInetAddress().toString()+":"+Sckt.getPort();
		wrtlog.write(new Date().toString()+"\t"+Remote+"\t\""+msg+"\"");
		wrtlog.write(System.getProperty("line.separator"));
		wrtlog.close();
}

public static void log(String msg) throws Exception
{
		FileWriter wrtlog = new FileWriter(fl,true);
		wrtlog.write(new Date().toString()+"\t \t"+msg);
		wrtlog.write(System.getProperty("line.separator"));
		wrtlog.close();
}

public static void log(Exception e) throws Exception
{
		FileWriter wrtlog = new FileWriter(fl,true);
		wrtlog.write(new Date().toString()+"\t \t"+e.toString());
		wrtlog.write(System.getProperty("line.separator"));
		wrtlog.close();
}


}