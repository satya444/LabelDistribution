package com.utd.ld.sendreceive;

import java.util.Iterator;
import java.util.List;

import com.utd.ld.main.AosMain;
import com.utd.ld.networkprotocol.ConnectionManager;
import com.utd.ld.utils.Message;

public class Sender implements Runnable {

	private int myId = AosMain.myNodeId;

	public static void invokeAllSends() {
		Sender s = new Sender();
		Thread t = new Thread(s);
		t.start();
		s.processSendQueue();

	}

	/**
	 * dequeues the sendQueue and sends its label to the second element in the
	 * list
	 */
	public void processSendQueue() {
		while (!AosMain.sendQueue.isEmpty()) {
			int randNumber = AosMain.myRandomLabel;
			System.out.println("MY LABEL IS "+randNumber);
			@SuppressWarnings("unchecked")
			List<Integer> destIds = AosMain.sendQueue.poll();
			Message m = new Message(randNumber, destIds);
			Iterator<Integer> itr = destIds.iterator();
			itr.next();
			int destId = itr.next();
			/*String myPrint="****************************** ";
			System.out.println("SENDING TO THE FOLLOWING");
			System.out.println(myPrint+destId+" "+myPrint);*/
			send(m, destId);
		}
		System.out.println("****************FINISHED PROCESSING SEND QUEUE*************");
		AosMain.sendExitFlag=true;
		return;
	
	}

	/**
	 * invokes sctp send written in ConnectionManager class
	 * 
	 * @param randNumber
	 * @param destId
	 */
	public void send(Message m, int destId) {

		ConnectionManager.sendMessage(m, destId);
	}

	@Override
	public void run() {
		processReceiveQueue();
	}

	/**
	 * Processes the receiveQueue and terminates when the node receives all the
	 * messages intended to receive
	 */

	public void processReceiveQueue() {
		try {
		while (AosMain.meAsDestination > 0) {
			Message m;
				m = AosMain.receiveQueue.take();

				int label = m.getLabel();
				List<Integer> destIds = m.getDestId();
				AosMain.meAsDestination--;
				if (destIds.get(0) == myId) {
					System.out.println("GOT BACK MY MESSAGE");
					System.out.println("**********LABEL SUM IS " + label
							+ " ***********");
					System.out.println("************PATH IS "+destIds.toString()+ " ***************");
				} else {
					label += AosMain.myRandomLabel;
					m.setLabel(label);
					Iterator<Integer> itr = destIds.iterator();
					int destId = 0;
					while (itr.hasNext()) {
						if (myId == itr.next()) {
							if (itr.hasNext()) {
								destId = itr.next();
								break;
							} else {
								destId = destIds.get(0);
								break;
							}
						}
					}
					
					send(m, destId);
				}
				System.out.println("************FINISHED PROCESSING RECEIVE QUEUE***************");
			
		}
		AosMain.receiveExitFlag=true;
		return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
