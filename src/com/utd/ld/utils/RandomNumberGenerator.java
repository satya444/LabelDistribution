package com.utd.ld.utils;

import java.util.Random;

import com.utd.ld.main.AosMain;

public class RandomNumberGenerator {

	private static int myId= AosMain.myNodeId;
	private static int size= AosMain.mapNodes.size();
	public static int generateRandomNumber(){
		Random r= new Random();
		int randomNum= r.nextInt(2*size-myId)+5*myId;
		return randomNum;
	}
}
