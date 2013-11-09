/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.brittatorp.homeauto.smartbus.transports;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.brittatorp.homeauto.smartbus.events.PackageReceivedEvent;

/**
 *
 * @author tornbmat
 */
public class TransportIP extends TransportBase {

    private int portNumber = 6000;
    private int maxPackageLength = 80 + 14;
    private DatagramSocket socket = null;
    private PackageReceivedEvent packageReceivedEvent;

    public TransportIP(PackageReceivedEvent packageReceivedEvent) throws SocketException {   //Todo: Replace with error handling
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
                SmartbusIPPacket udpPacket = new SmartbusIPPacket(packet);
            } catch (IOException ex) {
                Logger.getLogger(TransportIP.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        socket.close();
    }

    @Override
    boolean Send(SmartbusPacket smartbusPackage) {
        return true;
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
