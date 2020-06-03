package master;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;



public class stdErrReader extends Thread{

	private boolean hasout ;
	
	private final AtomicBoolean running = new AtomicBoolean(false);

	private InputStream is = null;
	
	stdErrReader(Process pb)
	{
		is = pb.getErrorStream() ;
		this.hasout = false ;

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
