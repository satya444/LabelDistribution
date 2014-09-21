package com.utd.ld.sendreceive;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.ByteBuffer;

import com.sun.nio.sctp.SctpChannel;
import com.utd.ld.main.AosMain;
import com.utd.ld.utils.Message;

public class Receiver {

	public void receive() {
		ByteBuffer byteBuffer = ByteBuffer.allocate(10000);
		try {
			while (true) {
				System.out.println("SEND FLAG "+ AosMain.sendExitFlag);
				System.out.println("RECEIVE FLAG "+AosMain.receiveExitFlag);
				if(AosMain.sendExitFlag&&AosMain.receiveExitFlag){
					System.out.println("**************EXITING RECEIVER***********");
					return;
				}

				for (int id : AosMain.connectionSocket.keySet()) {
					SctpChannel schnl = AosMain.connectionSocket.get(id);

					byteBuffer.clear();
					schnl.configureBlocking(false);
					schnl.receive(byteBuffer, null, null);
					byteBuffer.flip();

					if (byteBuffer.remaining() > 0) {
						Message receivedMsg = (Message) deserialize(byteBuffer
								.array());
						AosMain.receiveQueue.add(receivedMsg);

						byteBuffer.clear();
					}
				}
				//System.out.println("OUT OF FOR LOOP*****************");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Object deserialize(byte[] array) {
		ObjectInputStream ois;
		ByteArrayInputStream bis = new ByteArrayInputStream(array);
		try {
			ois = new ObjectInputStream(bis);
			return ois.readObject();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
}
