package com.utd.ld.main;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.utd.ld.utils.NodeDetails;


public class AosMain {
	
	/**
	 * @author Dilip
	 *
	 * github Profile: https://github.com/satya444
	 *
	 */
	

	public static HashMap<Integer,NodeDetails> mapNodes= new HashMap<>();
	public static int myNodeId;
	@SuppressWarnings("rawtypes")
	public static Queue<List> sendQueue= new LinkedList<>();
	
	public static void setMyNodeId(int myNodeId) {
		AosMain.myNodeId = myNodeId;
	}

	public static void main(String args[]){
		
	}
	

}
