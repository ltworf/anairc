/* *************************************************************************
 *   Copyright (C) 08/ago/06 by Salvo Tomaselli                            *
 *   s.tomaselli@libero.it                                                 *
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
package tree;

/**
 * This class describes an element of a node, wich can be another node or a simple string.
 * @author Salvo Tomaselli
 * @version 0.0.0.1
 */
public class Element {
    private Object element;
    private byte kind;
    
    public static final byte NODE=0;
    public static final byte STRING=1;
    
    /**
     * This element will be a node
     * @param node
     */
    public Element(Node node) {
        element=node;
        kind=NODE;
    }
    
    /**
     * This element will be a string
     * @param string
     */
    public Element(String string){
        element=string;
        kind=STRING;
    }
    
    /**
     * Returns the kind of the data contained in this element
     * @return
     */
    public byte getKind(){
        return kind;
    }
    
    /**
     * Returns the element as an object.
     * @return
     */
    public Object getElement() {
        return element;
    }
    
    /**
     * Returns the string contained by this element (If it contains a string)
     * @return
     */
    public String getString() {
        if (kind==STRING) {
            return element.toString();
        } else {
            return null;
        }
    }
    
    /**
     * Returns the node or null if this element isn't a node
     * @return
     */
    public Node getNode() {
        if (kind==NODE) {
            return (Node)element;
        } else {
            return null;
        }
    }
    
    public String toString(){
        return element.toString();
    }
}
