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

import gui.ChatGUI;
import gui.MainGui;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import swgui.ChatG;
import swgui.MainG;
import users.RemoteUser;
import users.UserList;



/**
 *
 * @author salvo
 * 
 * This class provides static funcionalities.
 * Works as a factory for GUI and returns global vars and paramethers
 */
public class Constants {
    /**
     * This var contains the nickname
     */
    public static String nickname= (System.getenv("USER")!=null)?System.getenv("USER"):getRandomNick();
    
    /**
     * This contains the Maximum allowed size for messages
     */
    public static final int MLEN=2048;
    
    /**
     * Port used
     */
    public static final int PORT=5000;
    
    /**
     * Timeout
     */
    public static final long TIMEOUT=800000;

    
    /**
     * Used to exit, it will send a message to tell that it is leaving the chat
     * @param i
     */
    public static void exit(int i) {
        
        UserList l = UserList.getUserList();
        RemoteUser bcast=  l.getUser(Constants.getBroadcastAddr());
        bcast.sendMessage("<event type=\"leave\" />");
    
        
        System.exit(i);
    }
    
    
    
    private static InetAddress broadcast=null;
    /**
     * Returns broadcast address
     * @return
     */
    public static InetAddress getBroadcastAddr() {
        
        if (broadcast!=null) return broadcast;
        
        String addr =System.getenv("BROADCAST");
        if (addr==null) {
            addr="255.255.255.255";
        }
        InetAddress BROADCAST=null;
        try {
            

            BROADCAST = InetAddress.getByName(addr);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Constants.class.getName()).log(Level.SEVERE, null, ex);
        }
        broadcast=BROADCAST;
        return BROADCAST;
    }
    
    
    private static MainGui maingui =null;
    /**
     * Returns the only instance of mainGui
     * @return
     */
    public static MainGui getMainGui() {
        
        
        if (maingui!=null)
            return maingui;
        else {
            
            return (maingui=new MainG());
        } 
            
        
    }
    
    /**
     * Creates a new ChatGUI
     * @return
     */
    public static ChatGUI getNewChatGui() {
        return new ChatG();
    }

    
    private static String getRandomNick() {
        String[] nicks={"Picard","Worf","Data","Riker","Troy","Kirk","Spock","Trillian","Zaphod","Arthur Dent","Marvin","Aragorn","Gimli","Pipino","Baggins","Frodo","Skywalker","Leila","Baccador","Gandalf","Legolas","Boromir","Faramir","Dax","Sisko","Quark","Sylar"};
        
        
        return nicks[(int)(Math.random()*nicks.length)];
    }
    
    private static String getLocalHostname() {
        String hostname=getRandomNick();
        try {
        
        hostname=InetAddress.getLocalHost().getHostName();
    
        
    } catch (UnknownHostException e) {
    }
        return hostname;
        
    }
    
    

}
