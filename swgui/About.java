/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package swgui;


import javax.swing.JFrame;
import javax.swing.JScrollPane;


/**
 *
 * @author salvo
 * This class is a singleton.
 * Use the method show() to show the about window.
 */
public class About extends JFrame{
    
    private static About about=null;
    public static About getAboutFrame() {
        if (about!=null) return about;
        return (about=new About());
    }
    
    
    private About() {
        JPopupArea area=new JPopupArea("Anairc\nAnairc: Not an internet relay chat\n\nReleased under GNU/GPLv3\n\n\nSalvo Tomaselli\nEnzo Rivello");
        area.setEditable(false);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        
        JScrollPane pane=new JScrollPane();
        pane.setViewportView(area);
        
        this.setTitle("Anairc");
        this.add(pane);
        
        this.pack();
        
    }
    
    

}
