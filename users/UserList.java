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

package users;

import gui.UserListListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.util.LinkedList;
import javax.swing.Timer;
import util.Constants;

/**
 * 
 *
 * @author salvo
 * 
 * This class is a singleton
 * It is ued to store and retrieve all known RemoteUser.
 * It also generates timeout for inactive RemoteUsers.
 */
public class UserList implements ActionListener {

    
    
    private LinkedList<RemoteUser> list=new LinkedList<RemoteUser>();
    private static UserList userlist;
    private LinkedList<UserListListener> listeners =new LinkedList<UserListListener>();
    
    /**
     * 
     * @return the instance of the list
     */
    public static UserList getUserList() {
        if (userlist!=null)
            return userlist;
        else
                return (userlist=new UserList());
    }
    
    private UserList() {
        
        //Starts the timer
        int delay = 5000; //milliseconds
        new Timer(delay, this).start();
        
    }
    
    /**
     * Remves the user and sends the event to the listeners
     * @param u
     */
    public synchronized void removeUser(RemoteUser u) {
        
        //Detaches GUI from user
        u.getChatGUI().userLeaved();
        
        //Removes user
        
        list.remove(u);
        
        //Sends the event
        int count=listeners.size();
        for (int i=0;i<count;i++) {
            listeners.get(i).userRemoved(u);
        }
    }
    
    /**
     * Adds a new user and sends the event.
     * It doesn't check if the user is a duplicate, that has to be done
     * before inserting the user.
     * @param u
     */
    public synchronized void addUser(RemoteUser u) {

        list.add(u);
        
        //Sends the event
        int count=listeners.size();
        for (int i=0;i<count;i++) {
            listeners.get(i).userAdded(u);
        }
        
    }
    
    /**
     * Returns the user corresponding to an address.
     * Returns null if the user isn't in the list.
     * @param addr
     * @return
     */
    public RemoteUser getUser(InetAddress addr) {
        int count=list.size();
        for (int i=0;i<count;i++) {
            RemoteUser u=list.get(i);
            if (u.getAddress().equals(addr)) return u;
        }
        return null;
    }
    
    /**
     * Adds a listener for events on the list
     * @param l
     */
    public synchronized void addListener(UserListListener l) {
        listeners.add(l);
    }
    
    /**
     * Removes a listener for events on the list
     * @param l
     */
    public synchronized void removeListeer(UserListListener l) {
        listeners.remove(l);
    }
    
    /**
     * Called by a RemoteUser when it changes, this will generate
     * and event.
     * @param u
     */
    public void userChanged(RemoteUser u) {
        int count=listeners.size();
        for (int i=0;i<count;i++) {
            listeners.get(i).userChanged(u);
        }
        
    }

    
    /**
     * Timer event
     * @param arg0
     */
    public void actionPerformed(ActionEvent arg0) {

        
        for (int i=0;i<list.size();i++) {//Checks all RemoteUser
            
            if (list.get(i).isBroadcast()) {//Sends an empty message to notify the presence
                list.get(i).sendMessage("<message></message>");
            }
            
            //If inactivity time is too big
            if ((System.currentTimeMillis()-  list.get(i).getLastActivityTime()) > Constants.TIMEOUT && !list.get(i).isBroadcast()) {
               this.removeUser(list.get(i));//Removes the user 
            }
        }
    }

}
