package master;


import java.io.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;



public class main {

	public static void main(String[] args) {
		

		Process pb ;

		try 
		{

			pb = new ProcessBuilder("java", "-jar", "/tmp/pgallo/slave.jar").start() ;
			stdErrReader se = new stdErrReader(pb) ;
			stdOutReader so = new stdOutReader(pb) ;
			
			new Thread(so).start();
			new Thread(se).start();
			

			
			try {
					Thread.sleep(15000);

				if(so.hasout() || se.hasout() )
				{

					if(se.hasout() == true)
					{
						pb.getErrorStream().transferTo(System.out) ;
					}
					else
					{
						pb.getInputStream().transferTo(System.out) ;
					}
					so.kill();
					se.kill();
					so.join();
					se.join();

					pb.destroyForcibly() ;
				}
				else
				{

					so.kill();
					se.kill();
					so.join();
					se.join();
					pb.destroyForcibly() ;
					System.out.println("TIMEOUT");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	
			
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}





	}

}
