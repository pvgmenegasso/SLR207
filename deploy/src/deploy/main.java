package deploy;



import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
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
	
	public static void startconns(ArrayList<Connection> threads)
	{
		Connection.resetsplit();
		Connection.resetmachines() ;
		for(int i=1; i<10 ; i++)
		  {
			  Connection c = new Connection(i) ;
			  c.start();
			  try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 
			  threads.add(c) ;
		  }
		
		
		
	};
	
  public static void main(String[] Args) throws IOException, InterruptedException {
	  
	  
	  //int splitsize = Integer.parseInt(Args[0]) ;
	  //String file = Args[1] ;
	  
	 // File f = new File(file) ;
	  
	  /*Process p = new ProcessBuilder("split", "-b 600m", "/Users/pvgmenegasso/eclipse-workspace/Wiki",  "/Users/pvgmenegasso/split").inheritIO().start() ;
	  
	  try {
		p.waitFor() ;
	} catch (InterruptedException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}*/
	
	  
	  ArrayList<Connection> threads = new ArrayList<Connection>() ;
	  
	  
	  long starttime = System.currentTimeMillis() ;
	  
	  
	  startconns(threads) ;
	  
	  for(Connection current: threads)
	  {
		 current.checkconn() ;
	  }
	  
	  for(Connection current : threads)
	  {
		  current.join();
	  }
	  
	  threads.clear();

	  
	  long currtime = System.currentTimeMillis() ;
	  System.out.println("Deploy time:  " + (currtime - starttime));
	  
	  startconns(threads) ;
			  
			  for(Connection t: threads)
			  {
				  t.map();
			  }
			  
			  
			  for(Thread current : threads)
			  {
				  current.join();
			  }
			  
			  threads.clear();
		
		
		
		System.out.println("map time:  " + (System.currentTimeMillis() - starttime));
		currtime = System.currentTimeMillis() ;
		
		startconns(threads) ;
		  
		  for(Connection t: threads)
		  {
			  t.shuffle();
		  }
		  
		  
		  for(Thread current : threads)
		  {
			  current.join();
		  }
		  
		  threads.clear();
		  
		  
		  System.out.println("shuffle time:  " + (System.currentTimeMillis() - starttime));
			currtime = System.currentTimeMillis() ;
		
		

			startconns(threads) ;
			  
			  for(Connection t: threads)
			  {
				  t.reduce();
			  }
			  
			  
			  for(Thread current : threads)
			  {
				  current.join();
			  }
			  
			  threads.clear();
		
		
		System.out.println("reduce time:  " + (System.currentTimeMillis() - starttime));
		currtime = System.currentTimeMillis() ;
		
		
		
	 
		
	  
			  
			  
			  
	
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