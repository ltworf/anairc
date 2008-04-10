/* *************************************************************************
 *   Copyright (C) 19/dec/07 by Salvo "LtWorf" Tomaselli                   *
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
package gui;

/**
 * Defines an interface for the main GUI who contains all the chats and user list
 * @author Salvo "LtWorf" Tomaselli
 *
 */
public interface MainGui {

	/**
	 * Called to add a new chat.
	 * It is very generic.
	 * @param gui
	 */
	public void addChat(ChatGUI gui);
	
	/**
	 * Called to notify that there is a new message in the specified chat
	 * @param gui
	 */
	public void notifyChat(ChatGUI gui);
	
	
	
}


