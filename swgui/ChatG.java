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
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.filechooser.FileFilter;
import tree.Node;
import users.RemoteUser;
import util.Constants;
import util.Replacer;

/**
 *
 * @author salvo
 * 
 * Swing implemetation of ChatGUI interface.
 * It extends a JPanel so it can be inserted into several containers.
 * 
 * DON'T create this class. Use Constants instead.
 */
public class ChatG extends JPanel implements ChatGUI, ActionListener{

    private RemoteUser remote;
    JPopupField txt_in = new JPopupField();
    JPopupArea txt_area=new JPopupArea();
    
    private JToggleButton cmdBold = new JToggleButton("B");
    private JToggleButton cmdItalic = new JToggleButton("i");
    private JToggleButton cmdUnderline = new JToggleButton("U");
    private JComboBox<String> cmbFColor = new JComboBox<String>();
    private JComboBox<String> cmbBColor = new JComboBox<String>();
    
    /**
     * Inits the GUI
     */
    public ChatG() {
        this.setLayout(new BorderLayout());
        
        {
            JToolBar tool=new JToolBar();
            JButton save=new JButton("Save");
            JButton about=new JButton("About");
            tool.setFloatable(false);
            tool.add(save);
            tool.add(about);
            
            this.add(tool,BorderLayout.NORTH);
            
            save.addActionListener(new ActionListener() {
            
                public void actionPerformed(ActionEvent src) {
                    
		    String toSave =txt_area.getText();
		    
		    File f=null;
		    JFileChooser chooser = new JFileChooser();
		    chooser.setMultiSelectionEnabled(false);
                    chooser.setFileFilter(new FileFilter() {

                        @Override
                        public boolean accept(File arg0) {
                            return (arg0.isDirectory() || arg0.getName().toLowerCase().endsWith(".txt") );
                            
                        }

                        @Override
                        public String getDescription() {
                            return "Text file (*.txt)";
                            
                        }
                        
                    });
                            
		    
		    int state = chooser.showSaveDialog(null);
		    if (state == JFileChooser.APPROVE_OPTION) {
			    f = chooser.getSelectedFile();
		    }
		    if (f==null) return;
		    try {			    
			    BufferedWriter bw = new BufferedWriter(
			    new OutputStreamWriter( new
			    FileOutputStream( f ), "UTF-8" ));
			    bw.write(toSave);
			    bw.close();
		    } catch (FileNotFoundException e1) {
			    e1.printStackTrace();
		    } catch (IOException e1) {
			    e1.printStackTrace();
		    }		    
                }
                
            });
            
            about.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent arg0) {
                    About a= About.getAboutFrame();
                    a.setVisible(true);
                }
                
            });
        }
        
        {
            JPanel southPanel=new JPanel();
            southPanel.setLayout(new BorderLayout());
            
            
            
            txt_in.addActionListener(this);
            
            JToolBar tools= new JToolBar();
            
            tools.setFloatable(false);
            
            tools.add(cmdBold);
            tools.add(cmdItalic);
            tools.add(cmdUnderline);
            
            {
            	String[] colors = {"Default","black","white","blue","yellow","green","gray","red","pink"
                		,"orange","magenta","cyan"};
            	for (int i=0;i<colors.length;i++){
            		cmbFColor.addItem(colors[i]);
            		cmbBColor.addItem(colors[i]);
            	}
            	
            	cmbFColor.setToolTipText("Foreground color");
            	cmbBColor.setToolTipText("Background color");
            	
            	JPanel panel=new JPanel();
            	panel.setLayout(new java.awt.GridLayout(2,1));
            	
            	panel.add(cmbFColor);                
            	panel.add(cmbBColor);
            	
            	tools.add(panel);
            		
            }
            
            southPanel.add(tools,BorderLayout.NORTH);
            southPanel.add(txt_in,BorderLayout.SOUTH);
            
            this.add(southPanel,BorderLayout.SOUTH);
        }
        
        
        JScrollPane scroll = new JScrollPane(txt_area, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        JButton panic=new JButton();
        scroll.setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, panic);
        panic.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                txt_area.setText("");
            }
            
        });

        txt_area.setEditable(false);
        
        this.add(scroll,BorderLayout.CENTER);
        this.setPreferredSize(new Dimension(400,350));
    }
    
    
    /**
     * Called when there is a new message.
     * @param msg
     */
    public void newMessage(String msg) {
        newMessage(new Node(msg));
    }

    /**
     * Shows the message
     * @param node
     */
    public void newMessage(Node node) {
        txt_area.insert(node);
        Constants.getMainGui().notifyChat(this);
        
    }

    /**
     * RemoteUser leaved.
     * Sets the var as null
     * and prevents user from sending messages
     */
    public void userLeaved() {
        txt_in.setEditable(false);
        txt_in.removeActionListener(this);
        this.remote=null;
    }

    /**
     * Associates a remote user to this chat
     * @param user
     */
    public void setRemoteUser(RemoteUser user) {
        this.remote=user;
    }

    /**
     * Returns the remote user associated to this chat
     * @return
     */
    public RemoteUser getRemoteUser() {
        return this.remote;
    }

    /**
     * Event generated by the JTextField when user wants to send the message.
     * Packs the message as XML and sends it trught RemoteUser
     * @param arg0
     */
    public void actionPerformed(ActionEvent arg0) {
        if (txt_in.getText().length()==0) return;
        String text=Replacer.replace(" " + txt_in.getText()+ " ");
        
        if (cmdBold.isSelected()|| cmdItalic.isSelected()|| cmdUnderline.isSelected() || (cmbFColor.getSelectedIndex()!=0) || (cmbBColor.getSelectedIndex()!=0)){
                	String font="<font ";
                	if (cmdBold.isSelected()) font=font+"bold=\"true\" "; 
                	if (cmdItalic.isSelected()) font=font+"italic=\"true\" "; 
                	if (cmdUnderline.isSelected()) font=font+"underline=\"true\" "; 
                	if (cmbFColor.getSelectedIndex()!=0) font=font+"color=\""+ cmbFColor.getSelectedItem().toString() +"\" "; 
                	if (cmbBColor.getSelectedIndex()!=0) font=font+"bgcolor=\""+ cmbBColor.getSelectedItem().toString() +"\" ";
                	
                	font=font+">";
                	text=font+text + "</font>";
                }
        
        
        String message="<testo>"+ Constants.nickname+ ": "+ text  +"\n</testo>";
        if (remote.sendMessage(message)) txt_in.setText("");
        if (!remote.isBroadcast()) //Not broadcast, wont come back
            this.newMessage(message);
    }

}
