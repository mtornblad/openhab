/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.brittatorp.homeauto.smartbus.transports;

import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

import se.brittatorp.common.Helpers;
import se.brittatorp.homeauto.smartbus.operations.OperationBase;

public class SmartbusIPPacket extends SmartbusPacket {

    private String asciiHead = "SMARTCLOUD";
    private InetAddress sourceIp;
    
    	public SmartbusIPPacket(DatagramPacket datagramPacket) {
    	loadPacket(datagramPacket);
	}

	public boolean loadPacket(DatagramPacket datagramPacket) {
        loadPacket(Arrays.copyOfRange(datagramPacket.getData(), 14, datagramPacket.getLength()));
        try {
			sourceIp = InetAddress.getByAddress(Arrays.copyOfRange(datagramPacket.getData(), 0, 4));
			asciiHead = new String(datagramPacket.getData(), 4, 10, "US-ASCII");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return true;
    }

    private byte[] getUdpPacket() {
    	
    	byte[] packet = new byte[this.getLength()+14];
    	
    	//SmartbusPacket
    	byte[]ip = sourceIp.getAddress();
    	for (int i=0;i<ip.length;i++){
    		packet[i]=ip[i];
    	}
    	
    	//SmartbusPacket
    	byte[]head = asciiHead.getBytes();
    	for (int i=0;i<head.length;i++){
    		packet[i+4]=head[i];
    	}    	
    	
    	//SmartbusPacket
    	byte[]content = this.getPacket();
    	for (int i=0;i<content.length;i++){
    		packet[i+14]=content[i];
    	}
    	
    	return packet;
    }


    public String getAsciiHead() {
        return asciiHead;
    }

    public InetAddress getSourceIp() {
        return sourceIp;
    }
}