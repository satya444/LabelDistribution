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
			System.out.println("MY LABEL IS " + randNumber);
			@SuppressWarnings("unchecked")
			List<Integer> destIds = AosMain.sendQueue.poll();
			int totLen = destIds.size(); // for stopping after sending to entire
											// list
			Message m = new Message(randNumber, destIds);
			m.setTotLen(totLen);
			m.setPrevPos(0); //previous index initially points to the first element
			int destId = destIds.get(1);
			/*
			 * String myPrint="****************************** ";
			 * System.out.println("SENDING TO THE FOLLOWING");
			 * System.out.println(myPrint+destId+" "+myPrint);
			 */
			send(m, destId);
		}
		System.out
				.println("****************FINISHED PROCESSING SEND QUEUE*************");
		AosMain.sendExitFlag = true;
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
			System.out.println("TRYING TO PROCESS RECEIVE QUEUE");
			System.out.println("MY LABEL IS " + AosMain.myRandomLabel);
			while (AosMain.meAsDestination > 0) {
				Message m;
				m = AosMain.receiveQueue.take();

				List<Integer> destIds = m.getDestId();
				AosMain.meAsDestination--;
				System.out.println("PREV POS IS " + m.getPrevPos());
				System.out.println("TOTLEN is " + m.getTotLen());
				System.out.println("ME AS DEST " + AosMain.meAsDestination);
				if (m.getPrevPos() == m.getTotLen() - 1) {
					System.out.println("GOT BACK MY MESSAGE");
					System.out.println("**********LABEL SUM IS " + m.getLabel()
							+ " ***********");
					System.out.println("************PATH IS "
							+ destIds.toString() + " ***************");
				} else {
					System.out.println("IN ELSE**");
					m.setLabel(m.getLabel()+AosMain.myRandomLabel);
					m.setPrevPos(m.getPrevPos()+1);
					int destId = 0;
					if (m.getPrevPos()== m.getTotLen() - 1) {
						destId = destIds.get(0);
					} else {
						destId = destIds.get(m.getPrevPos() + 1);
						/*
						 * while (itr.hasNext()) { if (myId == itr.next()) { if
						 * (itr.hasNext()) { destId = itr.next(); break; } else
						 * { destId = destIds.get(0); break; } } }
						 */
					}

					if (AosMain.myNodeId != destId)
						send(m, destId);

					else {
						sendToLocal(m);
					}
					System.out.println("RETURNED BACK ");
					System.out.println("ME AS DEST LEFT"
							+ AosMain.meAsDestination);
				}

			}
			System.out
					.println("************FINISHED PROCESSING RECEIVE QUEUE***************");
			AosMain.receiveExitFlag = true;
			return;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Local Send Updates Label
	 * 
	 * @param m
	 */
	public void sendToLocal(Message m) {
		List<Integer> destIds = m.getDestId();
		int count = 0;
		int temp= m.getPrevPos();
		for (int i = (temp + 1); i < m.getTotLen(); i++) {
			if (destIds.get(i) == AosMain.myNodeId) {
				AosMain.meAsDestination--;
				m.setPrevPos(m.getPrevPos()+1);// taking prevposition to last same value
				count++;
			}
			else{
				break;
			}
		}
		m.setLabel(m.getLabel()+(count*AosMain.myRandomLabel));

		int destId = 0;
		
		if (m.getPrevPos()==m.getTotLen()-1 ) {
			destId = destIds.get(0);
			if (destId == AosMain.myNodeId) {
				AosMain.meAsDestination--;
				m.setPrevPos(m.getPrevPos()+ 1);

				System.out.println("GOT BACK MY MESSAGE");
				System.out.println("**********LABEL SUM IS " + m.getLabel()
						+ " ***********");
				System.out.println("************PATH IS " + destIds.toString()
						+ " ***************");
				return;
			}
		} else {
			destId = destIds.get(m.getPrevPos() + 1);
		}
		send(m, destId);
		return;
	}
}
