package countwords;
import java.io.* ;
import java.util.Scanner;

import javax.swing.JFileChooser;

import java.net.*;


import java.util.HashMap;
public class main {

	public static void main(String[] args) {
		
			HashMap<String, Integer> words = new HashMap<String, Integer>();
			FileReader fr = null ;

			try
			{
				JFileChooser chooser = new JFileChooser() ;
				int returnVal = chooser.showOpenDialog(chooser);
			    if(returnVal == JFileChooser.APPROVE_OPTION) {
			       System.out.println("You chose to open this file: " +
			            chooser.getSelectedFile().getName());
			    }
				String returnval = chooser.getSelectedFile().getAbsolutePath() ;
				fr = new FileReader(returnval.toString()) ;
				BufferedReader br = new BufferedReader(fr) ;
				Scanner        sc = new Scanner(br) ;
				System.out.println("Chegou 2");
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
				
				System.out.println(words.toString());
				
				sc.close();
				br.close();
				
			}catch(Exception e){System.out.println("nao rolou");}
			finally
			{
				try
				{
					fr.close() ;
					
				}catch(Exception e){}
				
			}


		// TODO Auto-generated method stub

	}

}
