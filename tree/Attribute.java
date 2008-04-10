/* *************************************************************************
 *   Copyright (C) 07/ago/06 by Salvo Tomaselli                              *
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
 * This class describes an attribute of a node.
 * @author Salvo Tomaselli
 * @version 0.0.0.1
 */
public class Attribute {
    private String name;
    private String value;
    
    /**
     * Inits the attribute.using the name and the value
     * @param name
     * @param value
     */
    public Attribute(String name, String value) {
        this.name=name;
        this.value=value;
    }
    
    /**
     * Inits the attribute using a string wich will be parsed
     * @param s
     */
    public Attribute(String s) {
        String[] t = s.split("=");
        name=t[0];
        t[1]=t[1].replaceAll("\"", "");
        value=t[1];
    }
    
    /**
     * Returns the name of this attribute.
     * @return
     */
    public String getName(){
        return name;
    }
    
    /**
     * Returns the value of this attribute.
     * @return
     */
    public String getValue(){
        return value;
    }
    
    public String toString(){
        return name+"=\"" + value + "\"";
    }
}
