/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.brittatorp.homeauto.smartbus.transports;

/**
 *
 * @author tornbmat
 */
public abstract class TransportBase extends Thread {

    abstract boolean send(SmartbusPacket smartbusPackage);

    abstract public void run();
}
