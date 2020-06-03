package slave;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;



public class main {

	public static void main (String[] args) {
		
		
		

		int mode = Integer.parseInt(args[0]) ;
		
		
		
		//LinkedHashMap<String, Integer> words = new LinkedHashMap<String, Integer>();
		
		FileReader fr = null ;
		
		
		switch(mode)
		{
		
			case 0:
					try
					{
						
						// Declaration des variables relatives au scanner
						
						// Choisir l'archive a lire
						int splitnr = Integer.parseInt(args[1]) ;	
						fr = new FileReader("/tmp/pgallo/splits/split"+splitnr) ;
						
						
						BufferedReader br = new BufferedReader(fr) ;
						Scanner        sc = new Scanner(br) ;
						String line ;
						String[] values ;
						ArrayList<String> words = new ArrayList<String>();
						
						System.out.println("mapping...") ;
			
					
						
						// Declaration de variable pour compter le temps
						long startTime = System.currentTimeMillis();
						
						
						// Compter les mots
						while((line = br.readLine()) != null)
						{
							values = line.split(" ");
							
							for( int i = 0 ; i< values.length ; i++ )
							{
								words.add(values[i]) ;
							}
						}
						
						// Encore pour le temps
						long endTime  = System.currentTimeMillis();
						long time = endTime - startTime ;
						System.out.println("Temps de contage: "+ time + "ms");
						
						
			
						
						
					    // Fermeture des readers et scanners
						sc.close();
						br.close();
						
						
						System.out.println(" Commençant le triage ");
						
						//startTime = System.currentTimeMillis() ;
						
						// Faire le sorting du HashMap
						//words = Sort.sort(words) ;
			
						//endTime = System.currentTimeMillis() ;
						//time = endTime - startTime ;
						
						
					    //System.out.println("Temps de triage: " + time);
					    /*LinkedHashMap<String, Integer> myNewMap = words.entrySet().stream()
					    	    .limit(50)
					    	    .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), Map::putAll);
					    */
					    //System.out.println(myNewMap.entrySet().toString());
					    try 
					    {
					    	Process p = new ProcessBuilder("mkdir", "-p", "/tmp/pgallo/maps").inheritIO().start() ;
					    	
					    		
					    	PrintWriter out = new PrintWriter("/tmp/pgallo/maps/UM"+splitnr+".txt") ;
					    	
					    	words.forEach( word ->
								    	{
								    		out.write(word + "  1");
								    		out.write("\n");
								    		
								    	}
					    			
					    			
					    			
					    			
					    			
					    			);
					    	
					    	
					    	
					    	
					    	
					    	
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
						int splitnr = Integer.parseInt(args[1]) ;	
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
							if(i < 11)
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
							
							if(sc.hasNext())
							{
								writer.write(next);
								writer.write(" ");
								writer.write(sc.next());
								writer.flush();
								writer.close();
							}
							
							System.out.println("Copying shuffle to distant machine");
							if(next.hashCode()%modulo < 10)
							{
								System.out.println("modulo do hash ="+next.hashCode()%modulo);
						

								p = new ProcessBuilder("rsync","-azv","/tmp/pgallo/shuffles/"+next.hashCode()+"-"+splitnr+".txt", "pgallo@tp-1a222-0" + ((Math.abs(next.hashCode())%modulo+1)) + ".enst.fr:/tmp/pgallo/shufflesreceived").inheritIO().start() ;
					
								p.waitFor();
							
							}
							else
							{
								System.out.println("modulo do hash ="+next.hashCode()%modulo);
								

								p = new ProcessBuilder("rsync","-azv","/tmp/pgallo/shuffles/"+next.hashCode()+"-"+splitnr+".txt", "pgallo@tp-1a222-" + ((Math.abs(next.hashCode())%modulo))+".enst.fr:/tmp/pgallo/shufflesreceived").inheritIO().start() ;
					
								p.waitFor();
							}
						}
						
						
						
						
						
					}catch(Exception e)
					{
						e.printStackTrace();
					}
				
				
				
					break;
					
					
			case 2:
				
					File directoryPath = new File("/tmp/pgallo/shufflesreceived/");
					String contents[] = directoryPath.list();
					LinkedHashMap<Integer, Integer> hashes = new LinkedHashMap<Integer, Integer>() ;
					String[] words ;
					
					
					System.out.println("Beggining reduction...");
					System.out.println("\n");
					
					LinkedHashMap<Integer, String> unhashed = new LinkedHashMap<Integer, String>() ;
					
					for(int i = 0 ; i < contents.length ; i++)
					{
						System.out.println("...") ;
						System.out.println("\n") ;
						System.out.println("full string: "+contents[i]) ;
						words = contents[i].split("-") ;
						
						System.out.println("divided: "+words[0] +"  " + words[1]) ;
						if(hashes.containsKey(Integer.parseInt(words[0])))
						{
							int count = hashes.get(Integer.parseInt(words[0])) ;
							System.out.println("valor atual da chave"+Integer.parseInt(words[0])+":  "+count);
							count ++ ;
							hashes.replace(Integer.parseInt(words[0]), count) ;
						}
						else
						{
							System.out.println("não contem a key");
							hashes.put(Integer.parseInt(words[0]), 1) ;
							try {
									fr = new FileReader("/tmp/pgallo/shufflesreceived/"+words[0]+"-"+words[1]) ;
									BufferedReader br = new BufferedReader(fr) ;
									Scanner sc = new Scanner(br) ;
									unhashed.put(Integer.parseInt(words[0]), sc.next()) ;
									fr.close();
									br.close();
									sc.close();
									
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
						}
					}
					
					System.out.println("creating reduce dir....");
					
					try {
						Process p = new ProcessBuilder("mkdir", "-p", "/tmp/pgallo/reduces").inheritIO().start() ;
						p.waitFor() ;
						System.out.println("\n reduce dir created");
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					System.out.println("Starting to write reduction.. \n");
					
					
					for (Map.Entry<Integer, Integer> entry : hashes.entrySet()) {
					    Integer key = entry.getKey();
					    System.out.println("key: "+key);
					    Integer value = entry.getValue();
					    System.out.println("\n value: "+ value) ;
					    
					    File file = new File("/tmp/pgallo/reduces/"+key+".txt");
					    System.out.println("Writing reduction for key:" +key  +"\n");
					    System.out.println("Which stands for the word:" + unhashed.get(key)) ;
						try {
							file.createNewFile() ;
							FileWriter writer = new FileWriter(file); 
							writer.write(unhashed.get(key));
							writer.write(" ");
							writer.write(value.toString());
							writer.flush();
							writer.close();
							
							//writer.write(value.);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					    
					    
					    // ...
					}
					
					
					
				
				
					
				
					break;
			
		}
			// TODO Auto-generated method stub

	}

}
