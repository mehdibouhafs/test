package batch.util;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/**
Created by MBS on 20/07/2016.
 **/
public class ReadXMLFile2 {

	private List<String> s = new ArrayList<>();

	private List<String> att = new ArrayList<>();

	public void printNote(NodeList nodeList) {
    for (int count = 0; count < nodeList.getLength(); count++) {
	Node tempNode = nodeList.item(count);
	// make sure it's element node.
	if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
		// get node name and value
		if(!s.contains(tempNode.getNodeName())){
		s.add(tempNode.getNodeName());
		}
		if (tempNode.hasAttributes()) {
			// get attributes names and values
			NamedNodeMap nodeMap = tempNode.getAttributes();
			for (int i = 0; i < nodeMap.getLength(); i++) {
				Node node = nodeMap.item(i);
				if(!att.contains(node.getNodeName())){
					att.add(node.getNodeName());
				}
			}
		}
		if (tempNode.hasChildNodes()) {
			// loop again if has child nodes
			printNote(tempNode.getChildNodes());
		}
	}

    }


  }
	public List<String> getS() {
		return s;
	}

	public void setS(List<String> s) {
		this.s = s;
	}

	public List<String> getAtt() {
		return att;
	}

	public void setAtt(List<String> att) {
		this.att = att;
	}

}
