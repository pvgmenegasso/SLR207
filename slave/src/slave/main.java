package slave;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;



public class main {

	public static void main (String[] args) {
		
		
		

		int mode = Integer.parseInt(args[0]) ;
		int splitnr = Integer.parseInt(args[1]) ;	
		
		
		LinkedHashMap<String, Integer> words = new LinkedHashMap<String, Integer>();
		FileReader fr = null ;
		
		
		switch(mode)
		{
		
			case 0:
					try
					{
						
						// Declaration des variables relatives au scanner
						
						// Choisir l'archive a lire
						
						fr = new FileReader("/tmp/pgallo/splits/split"+splitnr) ;
						
						
						BufferedReader br = new BufferedReader(fr) ;
						Scanner        sc = new Scanner(br) ;
						
						
						System.out.println("begin counting... /n") ;
			
						
						// Declaration de variable pour compter le temps
						long startTime = System.currentTimeMillis();
						
						// Compter les mots
						wordCounter.counter(sc, words);
						
						// Encore pour le temps
						long endTime  = System.currentTimeMillis();
						long time = endTime - startTime ;
						System.out.println("Temps de contage: "+ time + "ms");
						
						
			
						
						
					    // Fermeture des readers et scanners
						sc.close();
						br.close();
						
						
						System.out.println(" Commençant le triage ");
						
						startTime = System.currentTimeMillis() ;
						
						// Faire le sorting du HashMap
						words = Sort.sort(words) ;
			
						endTime = System.currentTimeMillis() ;
						time = endTime - startTime ;
						
						
					    System.out.println("Temps de triage: " + time);
					    LinkedHashMap<String, Integer> myNewMap = words.entrySet().stream()
					    	    .limit(50)
					    	    .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
					    
					    //System.out.println(myNewMap.entrySet().toString());
					    try 
					    {
					    	Process p = new ProcessBuilder("mkdir", "-p", "/tmp/pgallo/maps").inheritIO().start() ;
					    	
					    		
					    	PrintWriter out = new PrintWriter("/tmp/pgallo/maps/UM"+splitnr+".txt") ;
					    	out.write(myNewMap.toString());
					    	out.flush();
					    	out.close();
					    	
					    	System.out.println("MAP READY");
					    }catch(Exception e)
					    {
					    	e.printStackTrace();
					    }
					}catch(Exception e){e.printStackTrace();}
					finally
					{
						try
						{
							fr.close() ;
								
						}catch(Exception e){}
						
					}
					
					break ;
					
					
					
			case 1: 
					
					try
					{
						
						//THIRD ARGUMENT IS MAX NUMBER OF MACHINES FOR THE HASH
						long modulo = Integer.parseInt(args[2]) ;
						
						fr = new FileReader("/tmp/pgallo/maps/UM"+splitnr+".txt") ;
						BufferedReader br = new BufferedReader(fr) ;
						Scanner        sc = new Scanner(br) ;
						
						
						
						String next ;
						
						
						System.out.println("creating shuffle dir....");
						
						Process p = new ProcessBuilder("mkdir", "-p", "/tmp/pgallo/shuffles").inheritIO().start() ;
						
						
						p.waitFor() ;
						
						System.out.println("\n  Done");
						
						System.out.println("Hashing the map.. /n") ;
						
						
						System.out.println("Creating shufflesreceived dirs");
						
						for(int i = 1 ; i <= modulo ; i++)
						{
							if(i < 10)
							{
								p = new ProcessBuilder("ssh","-o StrictHostKeyChecking = no", "pgallo@tp-1a222-0" + i + ".enst.fr","mkdir","-p", "/tmp/pgallo/shufflesreceived").inheritIO().start() ;
								p.waitFor() ;
							}
							else
							{
								p = new ProcessBuilder("ssh","-o StrictHostKeyChecking = no", "pgallo@tp-1a222-" + i + ".enst.fr","mkdir","-p", "/tmp/pgallo/shufflesreceived").inheritIO().start() ;
								p.waitFor() ;
							}
						}
						
						System.out.println("creating local shuffles...") ;
						
						
						
						
						while (sc.hasNext())
						{
							next = sc.next() ;
							File file = new File("/tmp/pgallo/shuffles/"+next.hashCode()+"-"+splitnr+".txt");
							file.createNewFile() ;
							FileWriter writer = new FileWriter(file); 
							
							writer.write(sc.next());
							writer.flush();
							writer.close();
							
							System.out.println("Copying shuffle to distant machine");
							if(next.hashCode()%modulo < 10)
							{
								System.out.println("modulo do hash ="+next.hashCode()%modulo);
						

								p = new ProcessBuilder("rsync","-azv","/tmp/pgallo/shuffles/"+next.hashCode()+"-"+splitnr+".txt", "pgallo@tp-1a222-0" + Math.abs(next.hashCode())%modulo + ".enst.fr:/tmp/pgallo/shufflesreceived").inheritIO().start() ;
					
								p.waitFor();
							
							}
							else
							{
								System.out.println("modulo do hash ="+next.hashCode()%modulo);
								

								p = new ProcessBuilder("rsync","-azv","/tmp/pgallo/shuffles/"+next.hashCode()+"-"+splitnr+".txt", "pgallo@tp-1a222-" + Math.abs(next.hashCode())%modulo + ".enst.fr:/tmp/pgallo/shufflesreceived").inheritIO().start() ;
					
								p.waitFor();
							}
						}
						
						
						
						
						
					}catch(Exception e)
					{
						e.printStackTrace();
					}
				
				
				
					break;
			
		}
			// TODO Auto-generated method stub

	}

}
