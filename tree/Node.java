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

import java.util.LinkedList;

/**
 * This is a node of the XML structure. It is created using a simple string with the XML.
 * It will parse it and create subnodes, attributes and string elements.
 * @author Salvo Tomaselli
 * @version 0.0.0.9
 */
public class Node {

    
    private String name; //Name of this node
    private LinkedList<Attribute> attrs = new LinkedList<Attribute>();
    private LinkedList<Element> info = new LinkedList<Element>();
    
    private static final boolean DEBUG=false;
    private boolean selfClosing=false;//If true, this node will have only attributes and no info.
    /**
     * This constructor will parse the XML code to create a tree structure.
     * It can be modified too.
     * To obtain the XML again, just call the toString() method.
     * @param XML is the XML code to parse. It must be valid.
     */
    public Node(String XML) {
        /*XML.replaceAll(" =", "=");
        XML.replaceAll("= ", "=");
        XML.replaceAll(" = ", "=");*/
        XML=XML.trim();//Removes eventual spaces
        
       
        
        
        
        {
            int close=XML.indexOf('>');
            //Sets the name of the node
            {
                
                String temp = XML.substring(1, close);
                String t[] = temp.split(" ");
                name = t[0].trim();
            }
            
            //Checking if this is a self closing node
            if (XML.charAt(close-1)=='/') selfClosing=true;            
            
            
            
            if (DEBUG) System.out.println("Node created: "+name);
            
            //Sets its attributes
            if (XML.indexOf(' ')<close && XML.indexOf(' ')!=-1){
                String temp=XML.substring(XML.indexOf(' '), XML.indexOf('>')).trim();
                String a[] = temp.split(" ");
                for (int c=0;c<a.length;c++) {
                    if (!a[c].equals("/")) {
                        
                        if (//This if will join an attribute with a space in its value
                                a[c].indexOf('"')==a[c].lastIndexOf('"') || 
                                a[c].indexOf('"')==-1) {
                            a[c+1]=a[c]+' '+a[c+1];
                        } else {
                            if (DEBUG) System.out.println("Adding attribute: "+a[c]);
                            attrs.add(new Attribute(a[c]));
                        }
                        
                        
                    }
                    
                }
                
            }
        }
        
        if (selfClosing) return;
        
        //Removing the tags of this node
        XML = XML.substring(XML.indexOf('>')+1).trim();
        XML=XML.substring(0, XML.lastIndexOf('<'));
        
        
        //Locating subNodes.
        {
         
          
            while (XML.indexOf('<')!=-1) {
                int open=XML.indexOf('<');
                
                if (open!=0) {//Adding a string element
                    String temp=XML.substring(0, open);
                    if (DEBUG) System.out.println("Adding String element: "+temp);
                    info.add(new Element(temp));
                    XML=XML.substring(open);
                    open=XML.indexOf('<');
                }
                
                int close=XML.indexOf('>',open+1);
                if (XML.charAt(close-1)=='/') {//Self closing node
                    String temp = XML.substring(open, close+1);
                    if (DEBUG) System.out.println("Self closing node found: "+temp);
                    XML=XML.substring(0,open)+XML.substring(close+1);
                    Node n =new Node(temp);
                    info.add(new Element(n));
                } else {//Node with two tags (open and close)
                    String temp = XML.substring(open+1, close);
                    String t[] = temp.split(" ");
                    String nodename = t[0].trim();
                    if (DEBUG) System.out.println(name + " is adding new subnode: "+nodename);
                    
                    int end= XML.indexOf("</"+nodename+">", open);
       
                    String content=XML.substring(open, end).concat("</"+nodename+">");
                  
                    Node n =new Node(content);                   
                    info.add(new Element(n));
                    XML=XML.substring(0,open)+XML.substring(XML.indexOf(">",end)+1);
                }
                
                
            }
            //Adding as string whats left in the node
            info.add(new Element(XML));
            
        }
        
        //Setting the info
        //info = XML;
        if (DEBUG) System.out.println("Node "+name+ " has info: "+info);
        
        
    }
    
    @Override
    /**
     * Returns the XML representation of the Node and its children.
     */
    public String toString(){
        String nodo="<"+name;
        for (int c=0;c<attrs.size();c++){
            nodo=nodo+" "+attrs.get(c).toString();
        }
        if (selfClosing) {
            nodo=nodo+" />";
        } else {
            nodo=nodo+">";        
            for (int c=0;c<info.size();c++){
                nodo=nodo+ info.get(c).toString();
            }
            
            nodo=nodo+"</"+name+'>';
        }
        
        
        return nodo;
    }
    
    /**
     * Returns the name of this node
     * @return
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets a new name for this node. Overriding the old one.
     * @param name
     */
    public void setName(String name) {
        this.name=name;
    }
    
    /**
     * Returns the list of the attributes of this node
     * @return
     */
    public LinkedList<Attribute> getAttributes(){
        return this.attrs;
    }
    
    /**
     * Returns the value of the given attribute
     * @param name of the attribute. It is case-sensitive
     * @return value of the given attribute. Null if it doesn't exist.
     */
    public String getAttribute(String name) {
        for (int c=0;c<attrs.size();c++) {//Searching the attribute with the given name
            if (attrs.get(c).getName().equals(name))
                return attrs.get(c).getValue();
        }
        
        //Returns null because the attribute werent found.
        return null;
    }
    
    /**
     * Returns the first node found named with name.
     * This search isn't recoursive, it will search only in the 1st level of subnodes.
     * @param name of the node searched. It is case sensitive.
     * @return the 1st node found or null.
     */
    public Node getNode(String name) {
        return getNode(name,0);
    }
    
    /**
     * Returns the first node found named with name.
     * This search isn't recoursive, it will search only in the 1st level of subnodes.
     * @param name of the node searched. It is case sensitive.
     * @param from: does the search starting from this index
     * @return the 1st node found or null.
     */
    public Node getNode(String name, int from) {
        for (int c=from;c<info.size();c++) {//Searching the attribute with the given name
            if ( (info.get(c).getKind()==Element.NODE) && ( info.get(c).getNode().getName().equals(name))  )
                return info.get(c).getNode();
        }
        
        //Returns null because the attribute werent found.
        return null;
    }
    
   
    /**
     * Returns the list of all the data contained between the <tag> and the </tag>
     * @return
     */
    public LinkedList<Element> getElements() {
        return this.info;
    }
    
    /**
     * Returns the number of subnodes
     * @return
     */
    public int getSubNodesCount() {
        int counter=0;
        
        for  (int c=0;c<info.size();c++) {
           if( info.get(c).getKind()==Element.NODE) counter++;
        }
        
        return counter;
    }
    

    
    
}
