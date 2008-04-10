/* *************************************************************************
 *   Copyright (C) 20/jan/08 by Salvo "LtWorf" Tomaselli                   *
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

package net;

import java.net.InetAddress;
import tree.Node;
import users.RemoteUser;
import users.UserList;
import util.Constants;

/**
 *
 * @author salvo
 * 
 * This class is a singleton
 * It receives incoming messages and routes them to their destination
 */
public class Router {
    private static Router router;
    private UserList list= UserList.getUserList();
    
    /**
     * Returns the instance of the Router class
     * @return
     */
    public static Router getRouter() {
        if (router!=null)
            return router;
        else
            return (router=new Router());
    }
    
    private Router() {
        RemoteUser bcast_user=new RemoteUser(Constants.getBroadcastAddr(),"Public");
        list.addUser(bcast_user);//Adds the public chat
        bcast_user.showChat();//Forces it to show the chat even if there aren't incoming messages
    }
    
    /**
     * Called when there is a new incoming message
     * @param message: message
     * @param addr: sender's address
     */
    public void newMessage(String message,InetAddress addr) {
     Node m;
     try {
        m=new Node(message);//Parses the message as XML
     } catch (Exception e) {
         return;
     }
        
     //Searches the user in the list
     RemoteUser user=list.getUser(addr);
     if (user==null) {//User doesn't exist
         //Creates the user
         String nick=m.getNode("nick").getAttribute("value");
         user=new RemoteUser(addr,nick);
         list.addUser(user);
     }
     
     //If the sender is leaving
     if (m.getNode("event")!=null && "leave".equals(m.getNode("event").getAttribute("type"))) {
         list.removeUser(user);//Removes it from the list
     }
     
     
     
     //Determines if the message was sent to broadcast
     String bcast=m.getAttribute("broadcast");
     if (bcast!=null && bcast.equals("true") ) {//Broadcast message
         
         {//Checks if the user changed its nickname
             String n=m.getNode("nick").getAttribute("value");
            if (!user.getNickname().equals(n)) {
                UserList.getUserList().userChanged(user);
                user.setNickname(n);
            }
                
         }
         
         //Set broadcast RemoteUser
        addr=Constants.getBroadcastAddr();
        user=list.getUser(addr);
     }
     
     
     //Routes the message to the user
     if (m.getNode("testo")!=null) user.newMessage(m);
    }

}
