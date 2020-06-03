package master;
import java.io.*;
import java.util.concurrent.* ;
import java.util.concurrent.atomic.AtomicBoolean;

public class stdOutReader extends Thread 
{
		private InputStream is = null ;
		private boolean hasout ;
		private final AtomicBoolean running = new AtomicBoolean(false);
		
		stdOutReader(Process pb)
		{
			this.hasout = false ;
			is = pb.getInputStream() ;

		}
	
		boolean hasout()
		{
			
			return this.hasout ;
		}
		
		public void kill()
		{
			running.set(false);
		}
		
		void put()
		{
			running.set(true);
			while(running.get())
			{
				try {
						if(is.available() > 0)
							{
								this.hasout = true ;
							}

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
				
		}
		
		public void run()
		{

				put() ;
			
		}
		


}
