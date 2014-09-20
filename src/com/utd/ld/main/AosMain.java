package com.utd.ld.main;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.sun.nio.sctp.SctpChannel;
import com.utd.ld.constants.AllConstants;
import com.utd.ld.networkprotocol.ConnectionManager;
import com.utd.ld.utils.NodeDetails;
import com.utd.ld.utils.ReadConfiguration;

public class AosMain {

	/**
	 * @author Dilip
	 * 
	 *         github Profile: https://github.com/satya444
	 * 
	 */

	public static HashMap<Integer, NodeDetails> mapNodes = new HashMap<>();
	public static HashMap<Integer, SctpChannel> connectionSocket=new HashMap<>();
	public static int myNodeId;
	@SuppressWarnings("rawtypes")
	public static Queue<List> sendQueue = new LinkedList<>();

	public static void setMyNodeId(int myNodeId) {
		AosMain.myNodeId = myNodeId;
	}

	public static void main(String args[]) {
		myNodeId= Integer.parseInt(args[0]);
		ReadConfiguration.readBasicConfiguration(AllConstants.basicConfigFile);
		ConnectionManager.createConnections();
		ReadConfiguration.readInputConfiguration(AllConstants.configFile);
		System.out.println("PRINTING QUEUE "+sendQueue.toString());
	}

}
