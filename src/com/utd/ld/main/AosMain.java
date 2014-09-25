package com.utd.ld.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sun.nio.sctp.SctpChannel;
import com.utd.ld.constants.AllConstants;
import com.utd.ld.networkprotocol.ConnectionManager;
import com.utd.ld.sendreceive.Receiver;
import com.utd.ld.sendreceive.SendReceive;
import com.utd.ld.utils.Message;
import com.utd.ld.utils.NodeDetails;
import com.utd.ld.utils.RandomNumberGenerator;
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
	public static int myRandomLabel;
	public static int myNodeId;
	public static int meAsDestination;
	public static boolean sendExitFlag=false;
	public static boolean receiveExitFlag=false;
	@SuppressWarnings("rawtypes")
	public static Queue<List> sendQueue = new LinkedList<>();
	public static BlockingQueue<Message> receiveQueue= new LinkedBlockingQueue<>();
	public static void setMyNodeId(int myNodeId) {
		AosMain.myNodeId = myNodeId;
	}

	public static void main(String args[]) {
		myNodeId= Integer.parseInt(args[0]);
		ReadConfiguration.readBasicConfiguration(AllConstants.basicConfigFile);
		ConnectionManager.createConnections();
		ReadConfiguration.readInputConfiguration(AllConstants.configFile);
		System.out.println("PRINTING QUEUE "+sendQueue.toString());
		myRandomLabel=RandomNumberGenerator.generateRandomNumber();
		SendReceive.initiateSendReceive();
	}


}
