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
import java.net.URL;


/**
 * @author Salvo Tomaselli
 * 
 * This class is used to obtain URL to load images located within the jar file
 */
public class Img {
    
    
    /**
     * This method gives out an url
     * @param resource
     * @param object
     * @return URL of the wanted resource
     */
    public static URL getUrl(String resource,Object object){       
        return object.getClass().getResource(resource);
    }
    
    public static URL getUrl(String resource, Class object){
        return object.getResource(resource);
    }      

}

