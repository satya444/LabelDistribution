package com.utd.ld.sendreceive;

public class SendReceive implements Runnable{

	public static void initiateSendReceive(){
		Thread t = new Thread(new SendReceive());
		t.start();
		Sender.invokeAllSends();
	}
	@Override
	public void run() {
		Receiver r= new Receiver();
		r.receive();
	}

}
