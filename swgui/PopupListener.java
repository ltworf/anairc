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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.text.JTextComponent;

/**
 * 
 * @author salvo "LtWorf" Tomaselli
 * This class creates the popup menu for text areas and fields.
 * Then listen to its events and performs requested actions.
 *
 */
public class PopupListener extends MouseAdapter implements ActionListener {
    
    public static PopupListener POPUP = new PopupListener();
    
    private  JPopupMenu popup = new JPopupMenu();
    
    private  JMenuItem cut = new JMenuItem("Cut",new ImageIcon( Img.getUrl("images/Cut.png",Img.class)) );
    private  JMenuItem copy = new JMenuItem("Copy" ,new ImageIcon( Img.getUrl("images/Copy.png",Img.class)));
    private  JMenuItem paste = new JMenuItem("Paste",new ImageIcon( Img.getUrl("images/Paste.png",Img.class)));
    
    private  JSeparator s = new JSeparator();
    private  JMenuItem select = new JMenuItem("Select all");
    
    
    private JTextComponent source;
    
    public PopupListener(){      
        cut.addActionListener(this);
        copy.addActionListener(this);
        paste.addActionListener(this);
        select.addActionListener(this);
        
        popup.add(cut);
        popup.add(copy);
        popup.add(paste);   
        popup.add(s);
        popup.add(select);   
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            this.source = (JTextComponent)e.getSource();
            
            paste.setEnabled(this.source.isEditable());
            cut.setEnabled(this.source.isEditable());
            
            popup.show(e.getComponent(),
                       e.getX(), e.getY());
            
        }
    }

    
    public void actionPerformed(ActionEvent arg0) {
        JMenuItem src = (JMenuItem)arg0.getSource();
        if (src==copy) this.source.copy();
        else if(src==paste) this.source.paste();
        else if (src==cut) this.source.cut();
        else if(src==select) this.source.selectAll();        
        
    }
}


