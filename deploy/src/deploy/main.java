package deploy;



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingQueue;


public class main {
  public static void main(String[] paramArrayOfString) throws IOException {
	
	  LinkedBlockingQueue<Thread> queue = new LinkedBlockingQueue<Thread>() ;
	  ArrayList<Connection> cons = new ArrayList<Connection>();
	  
	  
	  for(int i=0; i<11 ; i++)
	  {
	  Connection c = new Connection(i) ;
	  
	  c.start();
	  
	  cons.add(c) ;
	  }
	  
	  
	  cons.forEach( connection ->
	  { 
		  if( connection != null)
		  {
		  		  if(connection .isAlive())
		  		  {
		  			  connection.checkconn() ;
		  			  
				  try {
					connection.join(10000);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		  		  }
			 }
	  		}
			  );
	  
	  Connection.resetsplit() ;
	  
		cons.forEach( machine ->
		
		{if(machine != null)
		{
			if(machine.isconnected() == true)
			{
				machine.map();
				machine.shuffle();
				
			}
		}}
				
				
				);
			  
			  
			  
	
}
} 
	  
	  
	  
	  
/*	  
	  
	  arrayList.forEach(p -> {
	    	  String s ;
	    	  byte[] buffer;
	  	  
	    	 
	 
	    	  try {
				//while ((s = br.readLine()) != null)
	    		  buffer = new byte[20000];
	    		  p.getInputStream().read(buffer) ;
	    		  file.createNewFile() ;

	    		  OutputStream os = new FileOutputStream(file) ;
	    		  os.write(buffer);
	    		  os.close();

	    		  
	    		  
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
	        PrintStream out = new PrintStream(p.getOutputStream()) ;
	        out.println("exit");
	        
	  
	        });
}

	  */