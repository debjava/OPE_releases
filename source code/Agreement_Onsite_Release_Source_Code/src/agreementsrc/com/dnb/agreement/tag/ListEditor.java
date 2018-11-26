package com.dnb.agreement.tag;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.beans.PropertyChangeListener;
import java.beans.PropertyEditor;
import java.util.ArrayList;
import java.util.List;

public class ListEditor implements PropertyEditor {

	List theList = new ArrayList();
	
	private void log(Object obj) {
		System.out.println("[" + getClass() + "] " + obj);
	}

	public Object getValue() {
		return theList;
	}

	public void setValue(Object value) {
		log("public void setValue(Object " + value + ")");
		theList = new ArrayList((List) value);
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		log("public void addPropertyChangeListener(PropertyChangeListener" + listener + ")");
	}

	public String getAsText() {
		log("public String getAsText()");
		return theList.toString();
	}

	public Component getCustomEditor() {
		log("public Component getCustomEditor()");
		return null;
	}

	public String getJavaInitializationString() {
		log("public String getJavaInitializationString()");
		return null;
	}

	public String[] getTags() {
		log("public String[] getTags()");
		return null;
	}

	public boolean isPaintable() {
		log("public boolean isPaintable()");
		return false;
	}

	public void paintValue(Graphics gfx, Rectangle box) {
		log("public void paintValue(Graphics " + gfx + ", Rectangle " + box + ")");
		
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		log("public void removePropertyChangeListener(PropertyChangeListener " + listener + ")");
		
	}

	public void setAsText(String text) throws IllegalArgumentException {
		log("public void setAsText(String " + text + ")");
		
	}

	public boolean supportsCustomEditor() {
		log("public boolean supportsCustomEditor()");
		return false;
	}


}
