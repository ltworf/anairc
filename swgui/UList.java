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

package swgui;

import gui.UserListListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import users.RemoteUser;
import users.UserList;

/**
 *
 * @author salvo
 * 
 * This class graphically shows the list of connected users
 */
public class UList extends JPanel implements UserListListener, MouseListener {
    
    
    DefaultListModel usrs = new DefaultListModel();
    JList lista =new JList(usrs);
    
    /**
     * Creates the graphic
     */
    public UList() {
        UserList usrl=UserList.getUserList();
        
        usrl.addListener(this);
        
        this.setLayout(new BorderLayout());
        
        JScrollPane scroll = new JScrollPane();
        
        scroll.setViewportView(lista);
        this.add(scroll,BorderLayout.CENTER);
        
        lista.addMouseListener(this);
        this.setMaximumSize(new Dimension(250,800));
        
        
        
    }

    /**
     * Event called when a new user joins the chat
     * @param u
     */
    public void userAdded(RemoteUser u) {
        usrs.addElement(u);

        
    }
    

    /**
     * Event called when an user leaves
     * @param u
     */
    public void userRemoved(RemoteUser u) {
        usrs.removeElement(u);
        
    }

    /**
     * Event called when user changes nickname
     * @param u
     */
    public void userChanged(RemoteUser u) {
        usrs.removeElement(u);
        usrs.addElement(u);
        
    }

    /**
     * 
     * @param arg0
     */
    public void mouseClicked(MouseEvent arg0) {
        if (arg0.getClickCount()>=2 && arg0.getButton()==MouseEvent.BUTTON1) {
            //Gets the RemoteUser
            RemoteUser u = (RemoteUser)usrs.get(lista.getSelectedIndex());
            if (u!=null) {
                u.showChat();//Says to it to show the GUI
            }
        } else if (arg0.getClickCount()>=2 && arg0.getButton()==MouseEvent.BUTTON3) {
            RemoteUser u = (RemoteUser)usrs.get(lista.getSelectedIndex());
            if (u!=null) {
                u.setIgnored(!u.getIgnored());//Ignores an user
                UserList.getUserList().userChanged(u);//Generates the change event
            }
        }
            

    }

    public void mousePressed(MouseEvent arg0) {

    }

    public void mouseReleased(MouseEvent arg0) {

    }

    public void mouseEntered(MouseEvent arg0) {

    }

    public void mouseExited(MouseEvent arg0) {

    }

}
