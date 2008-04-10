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

import gui.ChatGUI;
import gui.MainGui;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.logging.Level;
import tree.Node;
import util.Constants;



/**
 *
 * @author salvo
 * 
 * This class is the representation of a remote user.
 * It is created by the Router when the user sends a message, and will receive
 * messages sent by the user.
 * It is also used to send messages back to the user
 */
public class RemoteUser {
    
    private InetAddress address;
    private String nickname;
    private DatagramSocket sender;
    private ChatGUI chat;
    private boolean shown=false;
    private long lastAct=System.currentTimeMillis();
    private boolean ignored=false;
    
    /**
     * Sets the flag ignored.
     * If true, private messages from this user will be ignored
     * @param i
     */
    public void setIgnored(boolean i) {
        ignored=i;
    }
    
    /**
     * Says if the user is ignored or not
     * @return
     */
    public boolean getIgnored() {
        return ignored;
    }

    
    /**
     * Returns the timestamp of the last action
     * @return
     */
    public long getLastActivityTime() {
        return lastAct;
    }
    
    /**
     * Returns the ChatGUI associated to this user
     * @return
     */
    public ChatGUI getChatGUI() {
        return chat;
    }
    
    /**
     * Creates the user. 
     * @param addr: addr to identify the user. It can't be changed once it is set
     * @param nick: nickname used by the user. Can change over time..
     */
    public RemoteUser(InetAddress addr, String nick) {
        try {
            //Init the socket to send
            sender = new DatagramSocket();
        } catch (SocketException ex) {
            java.util.logging.Logger.getLogger(RemoteUser.class.getName()).log(Level.SEVERE, null, ex);
        }
        

            //Sets nickname and address
            this.address = addr;
            this.nickname = nick;


            //Inits the chat GUI
            chat = Constants.getNewChatGui();
            chat.setRemoteUser(this);
            
            //Set the moment of the last activity
        lastAct=System.currentTimeMillis();
    }
    
    /**
     * Returns the address which identifies this user
     * @return
     */
    public InetAddress getAddress() {
        return address;
    }
    
    /**
     * Return the nickname used by the user
     * @return
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Event called when the chat with the user his hidden.
     */
    public void hid() {
        shown=false;
    }
    
    /**
     * Sets nickname
     * @param newNick
     */
    public void setNickname(String newNick) {
        this.nickname=newNick;
    }
    
    /**
     * Called when there is an incoming message
     * @param message
     */
    public void newMessage(String message) {
        newMessage(new Node(message));
    }
    
    /**
     * This will add the chat GUI to the main window
     */
    public void showChat() {
        shown=true;
        MainGui main = Constants.getMainGui();
        main.addChat(chat);
    }
    
    /**
     * New message incoming
     * Usually called by the Router
     * @param message
     */
    public void newMessage(Node message) {
        if (ignored) return;//Returns if the user is ignored
        
        if (!shown) {//Shows the chat if it is hidden
            showChat();
        }
        
        if (!isBroadcast()) {//Eventual nickname change
            String n=message.getNode("nick").getAttribute("value");
            if (!nickname.equals(n)) {
                UserList.getUserList().userChanged(this);//Generates the change event
                nickname=n;
            }
        }
        
        //Sends the message to the chat
        chat.newMessage( message.getNode("testo"));
        
        //Sets the last activity time
        lastAct=System.currentTimeMillis();
    }
    
    
    
    @Override
    /**
     * Show a string representation of the user
     */
    public String toString() {
        if (ignored)
            return "[ignoring] " + this.nickname+ "@" + this.address.getHostAddress();
        return this.nickname+ "@" + this.address.getHostAddress();
    }
    
    /**
     * 
     * @return true if this RemoteUser will send messages to the whole network and
     * will receive them if they are sent to broadcast
     */
    public boolean isBroadcast() {
        return address.equals(Constants.getBroadcastAddr());
    }
    
    /**
     * Sends a message
     * @param message
     * @return
     */
    public boolean sendMessage(String message) {
        
        if (isBroadcast()) {//Broadcast user
            message="<message broadcast=\"true\" ><nick value=\""+ Constants.nickname+ "\" />" + message + "</message>";
        } else
            message="<message broadcast=\"false\" ><nick value=\""+ Constants.nickname+ "\" />" + message + "</message>";
        
        byte[] buffer = message.getBytes();
        try {    
                //Checks if the message isn't bigger than the allowed size
                if (buffer.length>Constants.MLEN) return false;
                 DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, Constants.PORT);
                 sender.send(packet);
           } catch (IOException e) {
           }

        lastAct=System.currentTimeMillis();
        return true;
    }
    
    @Override
    /**
     * If 2 RemoteUser have the same InedAddress, then they are equal
     */
    public boolean equals(Object o) {
        if (o instanceof RemoteUser) {
            return ((RemoteUser)o).getAddress().equals(this.address);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + (this.address != null ? this.address.hashCode() : 0);
        return hash;
    }

}
