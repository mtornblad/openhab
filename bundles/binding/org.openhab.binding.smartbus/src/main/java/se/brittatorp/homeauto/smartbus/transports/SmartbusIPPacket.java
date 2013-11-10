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

public class SmartbusIPPacket extends SmartbusPacket {

    private String asciiHead;
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

    public DatagramPacket getDatagramPacket() {
        return null;
    }

    public String getAsciiHead() {
        return asciiHead;
    }

    public InetAddress getSourceIp() {
        return sourceIp;
    }
}