package slave;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;

public abstract class wordCounter {
	
	
	public static void counter(Scanner sc, HashMap<String, Integer> words)
	{
		while (sc.hasNext())
		{

				String mot = sc.next() ;
				if(words.containsKey(mot))
				{
					int nr = words.get(mot) ;
					nr ++ ;
					words.replace(mot, nr) ;
				}
				else
				{
					words.put(mot, 1) ;
				}
			}
		}	

}
