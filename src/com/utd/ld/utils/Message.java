package com.utd.ld.utils;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Message(int label, List<Integer> destId) {
		super();
		this.label = label;
		this.destId = destId;
	}
	int label;
	public void setLabel(int label) {
		this.label = label;
	}
	public int getLabel() {
		return label;
	}
	public List<Integer> getDestId() {
		return destId;
	}
	List<Integer> destId;
	
}
