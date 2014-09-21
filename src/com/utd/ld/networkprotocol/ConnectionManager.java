/**
 * 
 */
package com.utd.ld.networkprotocol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import com.sun.nio.sctp.MessageInfo;
import com.sun.nio.sctp.SctpChannel;
import com.sun.nio.sctp.SctpServerChannel;
import com.utd.ld.main.AosMain;
import com.utd.ld.utils.Message;
import com.utd.ld.utils.NodeDetails;

/**
 * @author Dilip
 * 
 */
public class ConnectionManager {

	
	/**
	 * Function connectHigherNodes This function connects the following process
	 * to all the processes with a lower PID
	 */
	private static int myId = AosMain.myNodeId;

	public static boolean connectHigherNodes(SctpServerChannel serverChannel) {
		int totalNodes = AosMain.mapNodes.size();
		int i = myId + 1;
		while (i <= totalNodes) {
			SctpChannel sctpChannel;
			try {
				sctpChannel = serverChannel.accept();
				AosMain.connectionSocket.put(i, sctpChannel);
				System.out.println(i + "Connected");
			} catch (IOException e) {
				System.out.println("Error in Waiting for Higher Nodes to join");
				e.printStackTrace();
				return false;
			}
			i++;
		}
		return true;

	}

	/**
	 * Function connectLowerNodes This function connects the following process
	 * to all the processes with a lower PID
	 */
	public static boolean connectLowerNodes() {

		for (int i = 1; i < myId; i++) {

			try {
				SctpChannel sctpChannel;
				InetSocketAddress Sa = new InetSocketAddress(AosMain.mapNodes
						.get(i).getAddress(), AosMain.mapNodes.get(i)
						.getPortNumber());
				sctpChannel = SctpChannel.open();
				sctpChannel.connect(Sa);
				AosMain.connectionSocket.put(i, sctpChannel);

				System.out.println(i + "is up and connected");

			} catch (IOException e) {
				System.out
						.println("This Node is not able to connect to lower Nodes");
				System.out.println("Please bring them into existence first");
				System.out.println("Exiting");

				e.printStackTrace();
				return false;
			}

		}
		return true;
	}

	/**
	 * This is the main function that needs to be called for creating the
	 * connections.
	 * 
	 * @param currentNode
	 * @param connectionSocket
	 * @param mapNodes
	 * @return
	 */
	public static boolean createConnections() {

		NodeDetails currentNode = AosMain.mapNodes.get(myId);
		SctpServerChannel serverChannel = null;

		try {
			serverChannel = com.sun.nio.sctp.SctpServerChannel.open();
			InetSocketAddress sA = new InetSocketAddress(
					currentNode.getAddress(), currentNode.getPortNumber());
			// Binding the server Address
			serverChannel.bind(sA);
		} catch (IOException e) {
			System.out.println("Process Identification PID-" + myId
					+ " ip:port" + currentNode.getAddress() + ":"
					+ currentNode.getPortNumber());
			e.printStackTrace();
			return false;
		}
		if (!connectLowerNodes())
			return false;
		System.out.println(myId + "IS UP AND CONNECTED");
		System.out.println(myId + "is Waiting for higher nodes to join");
		if (!connectHigherNodes(serverChannel))
			return false;
		System.out.println(myId + "is connected to all");
		return true;
	}

	/**
	 * 
	 * @param msgToSend
	 */
	public static void sendMessage(Message m, int destId) {
		// get the connection object from already stored connections in the map
		SctpChannel clientSocket = AosMain.connectionSocket.get(destId);
		if (clientSocket == null) {
			System.out
					.println("Connection Manager can't get the SCTP Channel object for Recipeint Node Id "
							+ destId + " in theconnection Map.");
			return;
		}

		try {

			sendMessageSCTP(clientSocket, m);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Function sendMessageSCTP Actual SCTP send
	 */

	private static void sendMessageSCTP(SctpChannel clientSock, Message m)
			throws IOException {

		ByteBuffer Buffer = ByteBuffer.allocate(10000);
		Buffer.clear();
		byte[] serialized = null;
		serialized = serialize(m);

		// Reset a pointer to point to the start of buffer
		Buffer.put(serialized);
		Buffer.flip();

		try {
			// Send a message in the channel
			MessageInfo messageInfo = MessageInfo.createOutgoing(null, 0);
			clientSock.send(Buffer, messageInfo);
			System.out.println("SENDING THIS MESSAGE");
			String msgPrint = "*********************************************";
			msgPrint += m.getLabel() + msgPrint;
			System.out.println(msgPrint);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public static byte[] serialize(Object obj) throws IOException {
		ObjectOutputStream out;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		out = new ObjectOutputStream(bos);
		out.writeObject(obj);
		return bos.toByteArray();
	}

}
