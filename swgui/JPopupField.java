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

import javax.swing.JTextField;

/**
 * @author S. Tomaselli
 *
 * This class creates a JTextField with a contextual menu.
 */
public class JPopupField extends JTextField{
    
    /**
     * 
     */
    private static final long serialVersionUID = 2739109735646806834L;

    
    public JPopupField(){
        super();
        this.addMouseListener(PopupListener.POPUP);      
    }
    
    public JPopupField(String e0){
        super(e0);
        this.addMouseListener(PopupListener.POPUP);
    }

}


