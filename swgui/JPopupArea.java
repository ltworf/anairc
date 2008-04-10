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

import java.awt.Color;
import java.util.LinkedList;

import javax.swing.ImageIcon;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import tree.Element;
import tree.Node;

/**
 * This class is an extended JText Pane.
 * It provides a popup menu on right click and it can insert text useing some xml tags
 * to select colors, styles and to insert images.
 * 
 * All insert methods are thread safe, so inserting text into this object can be
 * done by several threads at the same time.
 * 
 * @author salvo "LtWorf" Tomaselli
 *
 */
public class JPopupArea extends javax.swing.JTextPane {
    
    private static final long serialVersionUID = 5766733399980909134L;

    /**
     * Standard constructor
     * Creates the TextArea adding a popup menu in it.
     *
     */
    public JPopupArea(){
        super();        
        this.addMouseListener(PopupListener.POPUP); 
    }
    
    /**
     * Standard constructor
     * Creates the TextArea adding a popup menu in it.
     * 
     * The string specify the text inside the area.
     *
     */
    public JPopupArea(String e0){
        super();
        this.setText(e0);
        this.addMouseListener(PopupListener.POPUP);
    }
    
    /**
     * Returns a color from a string of its name.
     * @param color: name of the color
     * @param c: color to be returned if the string isn't recognized
     * @return
     */
    private static Color GetColor(String color,Color c) {
    	if (color==null) return c;
    	color=color.toLowerCase();
    	if (color.equals("white")) {
    		return Color.WHITE;
    	} else if (color.equals("blue")) {
    		return Color.BLUE;
    	} else if (color.equals("yellow")) {
    		return Color.YELLOW;
    	} else if (color.equals("green")) {
    		return Color.GREEN;
    	} else if (color.equals("gray")) {
    		return Color.GRAY;
    	} else if (color.equals("red")) {
    		return Color.RED;
    	} else if (color.equals("pink")) {
    		return Color.PINK;
    	} else if (color.equals("orange")) {
    		return Color.ORANGE;
    	} else if (color.equals("magenta")) {
    		return Color.MAGENTA;
    	} else if (color.equals("cyan")) {
    		return Color.CYAN;
    	} else if (color.equals("black")) {
    		return Color.BLACK;
    	}
    	
    	return c;
    	
    	
    }
    
    /**
     * Appends a string specifying font style and colors
     * @param append
     * @param fg
     * @param bg
     * @param bold
     * @param italic
     * @param underline
     */    
    public synchronized void append(String append,Color fg,Color bg, boolean bold,boolean italic, boolean underline) {
    	try {
            // Get the text pane's document                        
            StyledDocument doc = (StyledDocument)this.getDocument();
        
            // The color must first be wrapped in a style
            Style style = doc.addStyle("StyleName", null);
            StyleConstants.setForeground(style, fg);
            StyleConstants.setBackground(style,bg);
            StyleConstants.setBold(style,bold);
            StyleConstants.setItalic(style,italic);
            StyleConstants.setUnderline(style,underline);
            
        
            // Insert the text at the end of the text
            doc.insertString(doc.getLength(), append, style);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.setCaretPosition (this.getDocument().getLength()-1);
        
    }
    
    /**
     * Appends a string specifing its color.
     * @param append
     * @param c
     */
    public synchronized void append(String append,Color c) {
    	append(append,c,new Color(0,0,0,0),false,false,false);    	
    	
    }
    
    /**
     * returns true if the string is equals to "true"
     * returns false otherwise.
     * @param val
     * @return
     */
    private static boolean parseBoolean(String val) {
    	if (val==null) return false;
    	if (val.equals("true")) return true;
    	return false;
    } 
    
    /**
     * Adds 2 xml tags so the text becomes xml.
     * Then calls insert(Node) to process the xml
     * @param append
     */
    public synchronized void insert (String append) {
    	insert( new Node("<testo>"+append+"</testo>"));
    }
        
    /**
     * Insetrs a text to the end.
     * Parses the node to retrive attributes and images.
     * @param n
     */
    public synchronized void insert (Node n) {
    	try {
    	LinkedList<Element> elements= n.getElements();
    	for (int i=0;i<elements.size();i++) {
    		Element elem=elements.get(i);
    		if (elem.getKind()==Element.NODE) {
    			Node e=elem.getNode();
    			
    			if (e.getName().equals("smile")) {//Inserting image    				
    				this.append(new ImageIcon( Img.getUrl("smiles/"+e.getAttribute("src"),Img.class)),e.getAttribute("src"));
    			} else if (e.getName().equals("font")) {//Non default font
    				
    				LinkedList<Element> elems =e.getElements();
    				for (int j=0;j<elems.size();j++) {
    					Element elemento= elems.get(j);
    					if (elemento.getKind()==Element.STRING) {//The element is a string, i add it
    						String text=elemento.getString();
    						this.append(text,
    	    						GetColor(e.getAttribute("color"),Color.BLACK),
    	    						GetColor(e.getAttribute("bgcolor"),new Color(0,0,0,0)),
    	    						parseBoolean(e.getAttribute("bold")),
    	    						parseBoolean(e.getAttribute("italic")),
    	    						parseBoolean(e.getAttribute("underline")));
    					} else if (elemento.getKind()==Element.NODE) {//The element is a node, i do a recoursive call
    						
    						insert (elemento.getNode().toString());
    						
    					}
    				}
    				
    				
    			}
    			
    			
    		} else {
    			this.append(elem.getString(),
						GetColor(n.getAttribute("color"),Color.BLACK),
						GetColor(n.getAttribute("bgcolor"),new Color(0,0,0,0)),
						parseBoolean(n.getAttribute("bold")),
						parseBoolean(n.getAttribute("italic")),
						parseBoolean(n.getAttribute("underline")));
    		}
    	}
        
        } catch (Exception e) {
            e.printStackTrace();
        }
    	
    		
    	
    }
    /**
     * Appends a simple text to the box, and moves caret to the end.
     * @param append
     */
    public synchronized void append (String append) {
    	
    	try {
            // Get the text pane's document                        
            StyledDocument doc = (StyledDocument)this.getDocument();       
            
        
            // Insert the text at the end of the text
            doc.insertString(doc.getLength(), append, null);
        } catch (Exception e) {}
        
        this.setCaretPosition (this.getDocument().getLength()-1);
        //this.setCaretPosition (this.getText ().length ()-1);
    	
    }
    
    /**
     * Appends an image, its description text, and sets the position to the end.
     * @param append
     * @param text
     */
    public synchronized void append (ImageIcon append,String text) {
    	
    	try {
            // Get the text pane's document                        
            StyledDocument doc = (StyledDocument)this.getDocument();
        
            // The image must first be wrapped in a style
            Style style = doc.addStyle("StyleName", null);
            StyleConstants.setIcon(style, append);
        
            // Insert the image at the end of the text
            doc.insertString(doc.getLength(), text, style);
        } catch (Exception e) {}
        
        this.setCaretPosition (this.getDocument().getLength()-1);
        //this.setCaretPosition (this.getText ().length ()-1);
    	
    }

}




