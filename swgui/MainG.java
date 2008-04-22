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

import gui.ChatGUI;
import gui.MainGui;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.Image;
import java.awt.Toolkit;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import util.Constants;


/**
 *
 * @author salvo
 * 
 * Main GUI window. Never create it with new MainG();
 * Use Constants instead.
 */
public class MainG extends JFrame implements MainGui, ActionListener, WindowListener {
    
    private JTabbedPane tabs=new JTabbedPane();
    private JPopupField txtNick=new JPopupField(Constants.nickname);
    public TrayIcon trayIcon=null;

    
    /**
     * Creates the GUI and shows it
     */
    public MainG() {
        this.setTitle("Anairc");
        
        
        this.getContentPane().setLayout(new BorderLayout());
        
        this.addWindowListener(this);
	
        {//Toolbar
            JToolBar bar=new JToolBar();
            JLabel lblnick=new JLabel("Nickname:");
            bar.add(lblnick);
            bar.add(txtNick);
            this.add(bar,BorderLayout.NORTH);
	    
            txtNick.addActionListener(this);
        }
	
	if(SystemTray.isSupported()) {//TrayIcon
		SystemTray tray = SystemTray.getSystemTray();
		
		Image image = Toolkit.getDefaultToolkit().getImage(Img.getUrl("smiles/world",Img.class));
		
		trayIcon= new TrayIcon(image, "Anairc", null);
		trayIcon.setImageAutoSize(true);
		
		try {
			tray.add(trayIcon);
		} catch (Exception e) {
			System.err.println("TrayIcon could not be added.");
		}
		
		//trayIcon.displayMessage("Finished downloading", 
		//			"Your Java application has finished downloading",TrayIcon.MessageType.INFO);
		
		
	}


        
        {
            UList ulist=new UList();
            
            JSplitPane split=new JSplitPane();

            tabs.setMinimumSize(new Dimension(400,400));
            tabs.addChangeListener(new ChangeListener () {

                public void stateChanged(ChangeEvent arg0) {//When selecting a tab
                    int sel=tabs.getSelectedIndex();
                    String t=tabs.getTitleAt(sel);
                    if (t.endsWith(" *")) {//Removes the mark if there is one
                        
                        tabs.setTitleAt(sel, t.substring(0, t.length()-2));
                    }
                }
                
            });
            split.setLeftComponent(tabs);
            split.setRightComponent(ulist);
            this.add(split,BorderLayout.CENTER);
            
        }
        

        
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        this.setMinimumSize(new Dimension(550,400));
        this.pack();
        this.setVisible(true);
    }
    

    /**
     * Adds a new chat
     * @param gui
     */
    public void addChat(ChatGUI gui) {
        
        ImageIcon icon;
        
        if (gui.getRemoteUser().isBroadcast())
            icon=new ImageIcon( Img.getUrl("images/Home.png",Img.class));
        else
            icon=new ImageIcon( Img.getUrl("images/Text.png",Img.class));
        
        
        tabs.addTab( gui.getRemoteUser().getNickname(), icon, (ChatG)gui);
        
        
    }

    /**
     * A chat received an event
     * This event will mark the tab as unread unless it has the focus
     * @param gui
     */
    public void notifyChat(ChatGUI gui) {
	if (!this.isFocused()) //Changes the trayIcon to show that there is a new message 
		trayIcon.setImage(Toolkit.getDefaultToolkit().getImage(Img.getUrl("smiles/oworld",Img.class)));
	
        ChatG g=(ChatG)gui;
        int i=0;
        for ( i=0;i<tabs.getComponentCount();i++) {//Searches the tab for the chat
            if (tabs.getComponent(i)==g) break;
        }

        if (i<tabs.getComponentCount()) {
            if (tabs.getSelectedIndex()!=i) {
                if (!tabs.getTitleAt(i).endsWith(" *")) {//If the tab isn't already marked
                    tabs.setTitleAt(i, tabs.getTitleAt(i)+ " *");//marks it
		    
                }
            }
        }
    }

    /**
     * Changes global nickname
     * @param arg0
     */
    public void actionPerformed(ActionEvent arg0) {
        JTextField source=(JTextField)arg0.getSource();
        
        if (source==this.txtNick) {
            Constants.nickname=txtNick.getText();
        }
        
    }

    

    /**
     * Closes the current tab if it is a normal conversation.
     * Exits if it is the public chat.
     * @param arg0
     */
    public void windowClosing(WindowEvent arg0) {
        ChatGUI chat=(ChatGUI)tabs.getSelectedComponent();
        
        
        
        if (chat.getRemoteUser()!=null &&  chat.getRemoteUser().isBroadcast()) { 
            Constants.exit(0);
            
        }
        
        
        tabs.remove(tabs.getSelectedIndex());
        if (chat.getRemoteUser()!=null) chat.getRemoteUser().hid();
        
        
    }
    public void windowClosed(WindowEvent arg0) {}
    public void windowIconified(WindowEvent arg0) {}
    public void windowDeiconified(WindowEvent arg0) {}
    public void windowActivated(WindowEvent arg0) {
	    trayIcon.setImage(Toolkit.getDefaultToolkit().getImage(Img.getUrl("smiles/world",Img.class)));
    }
    public void windowOpened(WindowEvent arg0) {}
    public void windowDeactivated(WindowEvent arg0) {}

}
