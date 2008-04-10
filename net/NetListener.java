/* *************************************************************************
 *   Copyright (C) 13/feb/08 by Salvo "LtWorf" Tomaselli                   *
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

package net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import util.Constants;
import util.log.Logger;

/**
 *
 * @author salvo
 * 
 * This singleton is a separate thread which listens to incoming messages
 */
public class NetListener implements Runnable {
    
    private Thread thread;
    private DatagramSocket receiver;
    private Router router=Router.getRouter();
    
    
    private static NetListener listener;
    
    /**
     * Returns the instance of the listener
     * @return
     */
    public static NetListener getListener() {
        if (listener!=null)
            return listener;
        else
            return (listener=new NetListener());
    }
    
    /**
     * Starts the thread
     */
    private NetListener() {
        thread=new Thread(this);
        thread.start();
    }
    
    
    /**
     * Closes the listener
     */
    @Override
    public void finalize() {
        receiver.close();
    }

    /**
     * Infinite loop who gets incoming messages and sends them to the Router
     */
    public void run() {


        try {
            receiver = new DatagramSocket(Constants.PORT);
            byte[] buffer = new byte[Constants.MLEN];
           while ( true ) {               
               DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
               receiver.receive(packet);
               String message = new String(packet.getData(), packet.getOffset(), packet.getLength());

                router.newMessage(message,packet.getAddress());
           }
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    

}
