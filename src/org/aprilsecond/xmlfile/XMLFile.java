package org.aprilsecond.xmlfile;

import java.io.*;
import java.util.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * <p>
 * This is a class that manipulates the content of 
 * an XML file. It reads and writes to the XML file and
 * obtains nodes from the XML file while appending 
 * additional nodes. This class acts as a wrapper for jdom
 * </p>
 * 
 * @author Reagan Mbitiru <reaganmbitiru@gmail.com>
 */
public class XMLFile {

   /**
    * stores the path XML file to be manipulated
    */
    private String xmlFilePath ;
    
    /**
     * stores the XML file object
     */
    private File xmlFile ;
        
    /**
     * stores the SAX Builder
     */
    private SAXBuilder builder ;
    
    /**
     * stores document object
     */
    private Document document ;
    
    /**
     * constructor
     */
    public XMLFile(String fPath, String xmlFileContent) {
        
        // initialize path to file
        xmlFilePath = fPath ;
        
        // initialize the file 
        xmlFile = new File(xmlFilePath) ;
                
        // initialize the builder
        builder = new SAXBuilder() ;
        
        
        // initialize the document object 
        try {          
        
            // create a new input stream
            InputStream xmlFileIS 
                    = new ByteArrayInputStream(xmlFileContent.getBytes());
            
            // create a document object
            document = (Document) builder.build(xmlFileIS) ;
        } catch (JDOMException ex) {
            System.out.println("Error building XML doc "
                    + fPath + " "
                    + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("Error loading xml file " 
                    + fPath + " "
                    + ex.getMessage());
        }
    }       
    
    /**
     * gets the value of a specific attribute for
     * tags with a specific node name
     * e.g. gets the 'title' or 'id' attribute in 
     * <pre>
     * <calendars title="Google Doodles" id="https://www.google.com/..." />
     * </pre>
     */
    public ArrayList<String> 
            getTagNameAttributes(String elementName, String[] attrsName) {
        ArrayList<String> tagEntries = new ArrayList<String>() ;
        
        // loop through obtaining tags with the 
        // specified element name
        Element rootNode = document.getRootElement(); 
        List list = rootNode.getChildren(elementName) ;
        
        for (int childrenCount = 0 ; childrenCount < list.size() ;
                childrenCount ++) {
            
            // extract the attribute values
            Element currNode = (Element) list.get(childrenCount) ;
            
            for (int attsCounter = 0 ; attsCounter < attrsName.length ;
                    attsCounter ++) {
                
                // add to tag entries
                tagEntries.add(currNode
                        .getAttributeValue(attrsName[attsCounter]));
            }
        }
        
        // return all values for the tag entries
        return tagEntries ;
    }
    
    /**
     * gets the set of tag elements' attributes for a specific parent 
     * tag e.g. gets <entries id='...'> in the Elem tag
     * <pre>
     * <calendar title="..." id="..." >
     *  <entries date="...">
     *  <entries date="...">
     * </calendar>
     */
    public String[] getChildElemAttsWithinElem (String parentElemName, 
            HashMap<String, String> parentElemProperties, String childTag,
            String [] childNameParamsRequired) {
        String [] childParamsRequired = {} ;
        
        // get the specific parent tag name required
        Element rootNode = document.getRootElement(); 
        List list = rootNode.getChildren(parentElemName) ;
        
        // filter out those elems without the 
        // desired atts
        for(int elemCounter = 0 ; elemCounter<list.size() ;
                elemCounter ++) {
        
            // get the current node
            Element currNode = (Element) list.get(elemCounter) ;
            
            // find out if the node has the desired 
            // properties
            
            Iterator <Map.Entry<String, String>> iter 
                    = parentElemProperties.entrySet().iterator() ;
            
            for ( int parentAtts = 0 ; parentAtts < parentElemProperties.size() ;
                    parentAtts ++ ) {
                
                boolean elemHasRequiredAtts = true ;
                
                while (iter.hasNext()) {
                    
                    Map.Entry<String, String> properties = iter.next() ;
                    
                    if (currNode.getAttributeValue(properties.getKey()) == null) {
                        elemHasRequiredAtts = false ;
                    }
                }
                
                // if this is the parent tag desired, 
                // then get the child tag atts required
                if (elemHasRequiredAtts) {
                    String [] properties  = {} ;
                    List <Element> childNodes 
                            = currNode.getChildren(childTag) ;
                    
                    for (int childNodesCounter = 0 ;  childNodesCounter < childNodes.size() ;
                            childNodesCounter ++) {
                        
                        // get the current child node
                        Element currChildNode = childNodes.get(childNodesCounter) ;
                        
                        // get the required properties from the child
                        for (int childPropertiesCounter = 0 ; childPropertiesCounter <
                                childNameParamsRequired.length ; childPropertiesCounter++) {
                            properties[childPropertiesCounter] 
                                    = currChildNode
                                    .getAttributeValue(childNameParamsRequired
                                        [childPropertiesCounter]) ;
                        }
                    }
                    
                    // no need to continue looping
                    break ;
                }
            }                                
        }
        
        // return the required properties for the node
        return childParamsRequired ;
    }
    
     /**
     * gets the set of an elements' tags for a specific parent 
     * tag e.g. gets <date> tag within <entries> tag
     * <pre>
     * <calendar title="..." id="..." >
     *  <entries><date>2009-06-05T09:00:000Z</date>
     * </calendar>
     */
    public String[] getChildElemtagsWithinElem (String parentElemName, 
            HashMap<String, String> parentElemProperties, String childTag,
            String [] childNameParamsRequired) {
        String [] childParamsRequired = {} ;
        
        // get the specific parent tag name required
        Element rootNode = document.getRootElement(); 
        List list = rootNode.getChildren(parentElemName) ;
        
        // filter out those elems without the 
        // desired atts
        for(int elemCounter = 0 ; elemCounter<list.size() ;
                elemCounter ++) {
        
            // get the current node
            Element currNode = (Element) list.get(elemCounter) ;
            
            // find out if the node has the desired 
            // properties
            
            Iterator <Map.Entry<String, String>> iter 
                    = parentElemProperties.entrySet().iterator() ;
            
            for ( int parentAtts = 0 ; parentAtts < parentElemProperties.size() ;
                    parentAtts ++ ) {
                
                boolean elemHasRequiredAtts = true ;
                
                while (iter.hasNext()) {
                    
                    Map.Entry<String, String> properties = iter.next() ;
                    
                    if (currNode.getAttributeValue(properties.getKey()) == null) {
                        elemHasRequiredAtts = false ;
                    }
                }
                
                // if this is the parent tag desired, 
                // then get the child tag atts required
                if (elemHasRequiredAtts) {
                    String [] properties  = {} ;
                    List <Element> childNodes 
                            = currNode.getChildren(childTag) ;
                    
                    for (int childNodesCounter = 0 ;  childNodesCounter < childNodes.size() ;
                            childNodesCounter ++) {
                        
                        // get the current child node
                        Element currChildNode = childNodes.get(childNodesCounter) ;
                        
                        // get the required properties from the child
                        for (int childPropertiesCounter = 0 ; childPropertiesCounter <
                                childNameParamsRequired.length ; childPropertiesCounter++) {
                            properties[childPropertiesCounter] 
                                    = currChildNode
                                    .getChildText(childNameParamsRequired
                                        [childPropertiesCounter]) ;
                        }
                    }
                    
                    // no need to continue looping
                    break ;
                }
            }                                
        }
        
        // return the required properties for the node
        return childParamsRequired ;
    }
    
    /**
     * gets the value of tag(s) within an element e.g.
     * gets the text value for the <time> & <summary>
     * tags in 
     * 
     * <pre>
     *  <message>
     *		<time>2006-04-03T15:00:000Z</time>
     *		<summary>Visit to Auntie Macharia's House</summary>
     *	</message>
     * </pre>
     */
    public String [][] getTagValuesWithinElems(String tagName,
            String [] desiredTagValue) {
        
        String [][] tagValues = {{}} ;
        
        // get the specific parent tag name required
        Element rootNode = document.getRootElement(); 
        List list = rootNode.getChildren(tagName) ;
        
        // loop thtrough the children obtaining the
        // child tag elements
        for (int childrenCounter = 0 ;  childrenCounter < list.size() ;
                childrenCounter++) {
            
            // get the current elem
            Element currNode = (Element) list.get(childrenCounter) ;
            
            // stores the properties
            String [] propertiesStore = {} ;
            
            // get the values for the children
            for (int tagCounter = 0 ; tagCounter < desiredTagValue.length;
                    tagCounter ++ ) {
                
                // store the desired property
                propertiesStore[tagCounter] 
                        = currNode.getChildText(desiredTagValue[tagCounter]) ;
            }
            
            // add to the resultant array
            tagValues[childrenCounter] = propertiesStore ;
        }
        
        return tagValues ;
    }
    
    /**
     * returns the XML document
     */
    public Document getDocument() {
        return document ;
    }
}
