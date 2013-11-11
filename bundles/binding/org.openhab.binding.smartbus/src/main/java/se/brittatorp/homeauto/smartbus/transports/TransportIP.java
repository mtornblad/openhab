/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.brittatorp.homeauto.smartbus.transports;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import se.brittatorp.common.Helpers;
import se.brittatorp.homeauto.smartbus.SmartbusCallbacks;

/**
 *
 * @author tornbmat
 */
public class TransportIP extends TransportBase {

    private int portNumber = 6000;
    private int maxPackageLength = 80 + 14;
    private DatagramSocket socket = null;
    private SmartbusCallbacks packageReceivedEvent;

    public TransportIP(SmartbusCallbacks packageReceivedEvent) throws SocketException {   //Todo: Replace with error handling
        this.packageReceivedEvent = packageReceivedEvent;
        this.socket = new DatagramSocket(portNumber);
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            try {
                byte[] buf = new byte[maxPackageLength];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);
                socket.receive(packet);
                SmartbusIPPacket udpPacket = new SmartbusIPPacket(packet);;
                packageReceivedEvent.PackageReceivedEvent(udpPacket);
            } catch (IOException ex) {
                Logger.getLogger(TransportIP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        socket.close();
    }

    @Override
	public boolean send(SmartbusPacket smartbusPackage) {
        System.out.printf("Sending!!!!\n");
    	byte[]buffer = smartbusPackage.getPacket();
    	byte[]ip = new byte[4];
        try {
        	Helpers.updateArray(ip, 0, (short)192);
        	Helpers.updateArray(ip, 1, (short)168);
        	Helpers.updateArray(ip, 2, (short)1);
        	Helpers.updateArray(ip, 3, (short)254);
			InetAddress address = InetAddress.getByAddress(ip);

        	Helpers.updateArray(ip, 3, (short)101);
			InetAddress laddr = InetAddress.getByAddress(ip);

			
			System.out.printf("Sending! IP: %s, byte[]: %s, buffer: %s\n", address.toString(), Arrays.toString(ip), Arrays.toString(Helpers.byteArray2ShortArray(buffer)));
	        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, 6000);
	        DatagramSocket datagramSocket = new DatagramSocket();
//	        DatagramSocket datagramSocket = new DatagramSocket(6000, laddr);
	        System.out.printf("Sending! IP: %s, len; %d\n", packet.getSocketAddress().toString(), buffer.length);
	        datagramSocket.send(packet);
//	        datagramSocket.close();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return true;
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
