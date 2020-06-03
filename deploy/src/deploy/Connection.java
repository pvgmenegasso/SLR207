package deploy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.sql.Time;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Connection extends Thread {
	
	
	private Process p ;

    private int machinenr ;
    
    public static ArrayList<Integer> connectedmachines = new ArrayList<Integer>() ;
    
    private int splitnr = -99;
    
    private boolean connected = false ;
    
    static int split = 0 ;
    

    
    public Connection(int machinenr)
    {
    	
    	this.machinenr = machinenr ;
    
    }
    
    public boolean isconnected()
    {
    	
    	return this.connected ;
    }
    
    public int getsplit()
    {

    		int oldsplit = split ;
    		split ++ ;
    		return oldsplit ;

    }
    
    public boolean newconnection()
    {
      try {
    	  
        //Process process = pb.start();
        //BufferedWriter br = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
        if (machinenr < 10) {
        	System.out.println("Starting connection on machine"+machinenr+"...");
        	ProcessBuilder pb = new ProcessBuilder("ssh", "-tt", "pgallo@tp-1a222-0" + machinenr + ".enst.fr", "-o StrictHostKeyChecking = no", "-i id_rsa") ;
        	 p = pb.start() ;
        	 
        	 p.waitFor(4, TimeUnit.SECONDS) ;
        	 Thread.yield();
        	 return p.isAlive() ;

          //br.write("ssh pgallo@tp-1a222-0" + i + ".enst.fr -o StrictHostKeyChecking = no &");
          //pb.redirectErrorStream(true);

        } else {
        	System.out.println("Starting connection on machine"+machinenr+"...");
           	ProcessBuilder pb = new ProcessBuilder("ssh", "-tt", "pgallo@tp-1a222-" + machinenr + ".enst.fr", "-o StrictHostKeyChecking = no", "-i id_rsa") ;
           	 p = pb.start() ;
           	p.waitFor(4, TimeUnit.SECONDS) ;
           	Thread.yield();
           	return p.isAlive() ;

          //br.write("ssh pgallo@tp-1a222-" + i + ".enst.fr -o StrictHostKeyChecking = no &");
          //pb.redirectErrorStream(true);
          
        }
        }catch (IOException iOException) {
        iOException.printStackTrace();
      } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
      return false ;
      
    }
    
    public boolean isRunning()
    {
    	return p.isAlive() ;
    }
    

    
    public Process getProcess()
    {
    	return this.p ;
    }
    
    public boolean checkconn()
    {
    	System.out.println("checking connection on machine "+machinenr+"...") ;
    	try
        	{
    		
    		
    		BufferedOutputStream out = new BufferedOutputStream(p.getOutputStream()) ;
    	
    	
    	
    		BufferedReader br =new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8")) ;
	    	out.write("echo $hostname".getBytes());
	    	out.write("\n".getBytes());
	    	
	    	String s ;
	    	if((s = br.readLine()) != null)
	    	{
	    		System.out.println(s);

		    	this.deploy(out, br) ;
		    	connectedmachines.add(this.machinenr) ;
		    	return true ;
	    	}
	    	else
	    	{
	    		out.close();
	    		br.close();
	    		return false ;
	    	}
    	}catch(Exception e)
    	{
    		e.printStackTrace();
    	}
    	return false ;
    	}

    public void shuffle()
    {
    	if( this.machinenr < 10)
    	{
			ProcessBuilder pb = new ProcessBuilder("ssh", "-tt","-o StrictHostKeyChecking = no", "pgallo@tp-1a222-0" + machinenr + ".enst.fr","java -jar /tmp/pgallo/slave.jar", "1", ""+this.splitnr, ""+connectedmachines.size()).inheritIO() ;
			try {
					Process p  = pb.start() ;
					p.waitFor() ;
			}catch (Exception e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
			}
    	}
    	else
    	{
    		ProcessBuilder pb = new ProcessBuilder("ssh", "-tt","-o StrictHostKeyChecking = no", "pgallo@tp-1a222-" + machinenr + ".enst.fr","java -jar /tmp/pgallo/slave.jar", "1", ""+this.splitnr, ""+connectedmachines.size()).inheritIO() ;
        	try {
	    			Process p  = pb.start() ;
	    			p.waitFor() ;
    		}catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    }
    
    public boolean deploy(BufferedOutputStream out, BufferedReader br)
    {
    	if (p.isAlive())
    	{
    	
    	try
    	{
    		System.out.println("cleaning previous deploy...");
    		System.out.println("\n");
	    	out.write("rm -rf /tmp/pgallo/shuffles".getBytes());
	    	out.write("\n".getBytes());
	    	out.flush();
	    	
	    	System.out.println("...");
    		System.out.println("\n");
	    	out.write("rm -rf /tmp/pgallo/shufflesreceived".getBytes());
	    	out.write("\n".getBytes());
	    	out.flush();
	    	
	    	System.out.println("...");
    		System.out.println("\n");
	    	out.write("rm -rf /tmp/pgallo/slave.jar".getBytes());
	    	out.write("\n".getBytes());
	    	out.flush();
    		
    		System.out.println("Trying to create folder...");
    		System.out.println("\n");
	    	out.write("mkdir -p /tmp/pgallo".getBytes());
	    	out.write("\n".getBytes());
	    	out.flush();
	    	
	    	System.out.println("Creating subfolders...");
    		System.out.println("\n");
	    	out.write("mkdir -p /tmp/pgallo/maps".getBytes());
	    	out.write("\n".getBytes());
	    	out.flush();
	    	
	    	System.out.println("...");
    		System.out.println("\n");
	    	out.write("mkdir -p /tmp/pgallo/splits".getBytes());
	    	out.write("\n".getBytes());
	    	out.flush();
	   

	    		
	    		System.out.println("Directory structure created !!");
	    		System.out.println("Begginign rsync...");
	    		if(this.machinenr<10)
	    		{
	    			
			    		/*
			    		ProcessBuilder pb = new ProcessBuilder("rsync", "-az", "/Users/pvgmenegasso/slave.jar", "pgallo@tp-1a222-0" + machinenr + ".enst.fr:/tmp/pgallo/").inheritIO() ;
			   
			        	Process p = pb.start() ;
			        
			        	Thread.sleep(6000);
			        	
			        	ProcessBuilder pb2 = new ProcessBuilder("ssh", "-tt", "pgallo@tp-1a222-0" + machinenr + ".enst.fr","java -jar /tmp/pgallo/slave.jar", "-o StrictHostKeyChecking = no", "-i id_rsa").inheritIO() ;
			     	    pb2.start() ;
			        	
			        	out.close();
			    		br.close();
			    		*/
	    			
	    				int number = this.getsplit() ;
			    			
		    			ProcessBuilder pb = new ProcessBuilder("rsync", "-azv", "/Users/pvgmenegasso/split"+number, "pgallo@tp-1a222-0" + machinenr + ".enst.fr:/tmp/pgallo/splits").inheritIO() ;
		
		    			Process p = pb.start() ;
		
		    			this.splitnr = number ;
		    			
		    			p.waitFor() ;
		    			
		
		    			
		    			pb = new ProcessBuilder("rsync", "-azv", "/Users/pvgmenegasso/slave.jar", "pgallo@tp-1a222-0" + machinenr + ".enst.fr:/tmp/pgallo/").inheritIO() ;
		    			
		    			p = pb.start() ;
		    			
		    			p.waitFor() ;
		    			
		    			this.connected = true ;
		    			
		    			
			    				
			        	return true ;
	    		}
	    		else
	    		{
	    			
	    			/*
	    			ProcessBuilder pb = new ProcessBuilder("rsync", "-az", "/Users/pvgmenegasso/slave.jar", "pgallo@tp-1a222-" + machinenr + ".enst.fr:/tmp/pgallo/").inheritIO() ;
	    			   
		        	Process p = pb.start() ;
		        
		        	Thread.sleep(6000);
		        	
		        	ProcessBuilder pb2 = new ProcessBuilder("ssh", "-tt", "pgallo@tp-1a222-" + machinenr + ".enst.fr","java -jar /tmp/pgallo/slave.jar", "-o StrictHostKeyChecking = no", "-i id_rsa").inheritIO() ;
		     	    pb2.start() ;
		        	
		        	out.close();
		    		br.close();
		    		*/
	    			
	    			int number = this.getsplit() ;
	    			
	    			ProcessBuilder pb = new ProcessBuilder("rsync", "-azv", "/Users/pvgmenegasso/split"+number, "pgallo@tp-1a222-" + machinenr + ".enst.fr:/tmp/pgallo/splits").inheritIO() ;

	    			Process p = pb.start() ;
	    			
	    			this.splitnr = number ;
	    			
	    			p.waitFor() ;
	    			
	    			pb = new ProcessBuilder("rsync", "-azv", "/Users/pvgmenegasso/slave.jar", "pgallo@tp-1a222-" + machinenr + ".enst.fr:/tmp/pgallo/").inheritIO() ;
	    			
	    			p = pb.start() ;
	    			
	    			p.waitFor() ;
	    			
	    			this.connected = true ;
	    			
	    			

		        	return true ;
	    		}
	    		
	    	
    	}catch(Exception e)
    	{
    		
    		e.printStackTrace();
    		return false ;
    	}
    	}
    	return false; 
    }
    
    public static void resetsplit()
    {
    	split = 0 ;
    }
    
    public void map()
    {
    	if( this.machinenr < 10)
    	{
			ProcessBuilder pb = new ProcessBuilder("ssh", "-tt","-o StrictHostKeyChecking = no", "pgallo@tp-1a222-0" + machinenr + ".enst.fr","java -jar /tmp/pgallo/slave.jar", "0", ""+this.splitnr).inheritIO() ;
			try {
					Process p  = pb.start() ;
					p.waitFor() ;
			}catch (Exception e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
			}
    	}
    	else
    	{
    		ProcessBuilder pb = new ProcessBuilder("ssh", "-tt","-o StrictHostKeyChecking = no", "pgallo@tp-1a222-" + machinenr + ".enst.fr","java -jar /tmp/pgallo/slave.jar", "", ""+this.splitnr).inheritIO() ;
        	try {
	    			Process p  = pb.start() ;
	    			p.waitFor() ;
    		}catch (Exception e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}
    }
     
	public void run()
	{
		try {
			
			newconnection() ;

			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
    

 }

