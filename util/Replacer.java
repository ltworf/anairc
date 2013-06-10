/* *************************************************************************
 *   Copyright (C) 10/jan/08 by Salvo "LtWorf" Tomaselli                   *
 *   salvatore.tomaselli@galileo.dmi.unict.it                              *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 3 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/
package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * @author Salvo "LtWorf" Tomaselli
 * 
 * Class for replacing. 
 * Can be used to replace short forms with correct ones.
 * It is used to replace smilies shortcuts with correct xml tag.
 *
 */
public class Replacer {
    private static String[][] replace=null;
    
    /**
     * This will replace according to instructions loaded from the file.
     * @param string
     * @return
     */
    public static String replace(String string) {
    	if (replace==null) 
            if (!loadStrings())
                return string;
    	
    	for (int i=0;i<replace.length;i++) {

    		string=string.replaceAll(" "+replace[i][0]+" "," "+replace[i][1] +" ");    		
    	}
    	
    	return string.trim();
    }
    
    /**
     * Loads the list of strings from a file. 
     * It is static so the list will have to be loaded once.
     *
     */
    private static boolean loadStrings() {
    	File f =new File("replace");
        if (!f.exists()) return false;

    	StringBuffer buffer=new StringBuffer();
        try {


            BufferedReader br = new BufferedReader(new InputStreamReader( new
	        FileInputStream( f ), "UTF-8" ));
            
            while (br.ready()) {
            	buffer.append(br.readLine()+"\n");
            }
            br.close();
            
        } catch (FileNotFoundException e1) {
            return false;
        } catch (IOException e1) {
            return false;
        }
        
        String s=buffer.toString();
        
        String[] temp=s.split("\n");
        replace=new String[temp.length][2];
        
        for (int i=0;i<temp.length;i++) {        	
        	String[] tmp = temp[i].split(",");
        	
        	replace[i][0] = tmp[0];
        	replace[i][1]=tmp[1];
        }
        return true;
        
    }
    
    
}
