package com.utd.ld.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.utd.ld.main.AosMain;

/**
 * @author Dilip
 * 
 *         github Profile: https://github.com/satya444
 * 
 */
public class ReadConfiguration {
	/**
	 * Parse config.txt file- This file contains NodeIds, their Ip addresses and
	 * portNumbers To populate mapNodes HashMap
	 * 
	 * @param inputFile
	 *            : path to input configuration file
	 */
	public static void readBasicConfiguration(String inputFile) {
		File f = new File(inputFile);
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.length() == 0) {
					continue;
				}
				String[] lineSplit = line.split(" ");
				Integer nodeId = Integer.parseInt(lineSplit[0]);
				String address = lineSplit[1];
				Integer portNumber = Integer.parseInt(lineSplit[2]);
				NodeDetails nd = new NodeDetails();
				nd.setAddress(address);
				nd.setNodeID(nodeId);
				nd.setPortNumber(portNumber);
				AosMain.mapNodes.put(nodeId, nd);
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This reads input configuration file and populates the senders queue
	 * 
	 * @param inpuFile : pass entire filepath as argument
	 * @param myNodeId : pass current Nodeid as input argument
	 */
	public static void readInputConfiguration(String inputFile){
		int myNodeId= AosMain.myNodeId;
	//	AosMain.setMyNodeId(myNodeId);
		File f = new File(inputFile);
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			String line = null;
			while ((line = br.readLine()) != null) {
				if (line.length() == 0) {
					continue;
				}
				String[] lineSplit = line.split(" ");
				Integer nId= Integer.parseInt(lineSplit[0]);
				if(nId==myNodeId){
					List<Integer> destinationList= new LinkedList<>();
					for(int i=1; i<lineSplit.length; i++){
						destinationList.add(Integer.parseInt(lineSplit[i]));
					}
					AosMain.sendQueue.add(destinationList);
				}
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
