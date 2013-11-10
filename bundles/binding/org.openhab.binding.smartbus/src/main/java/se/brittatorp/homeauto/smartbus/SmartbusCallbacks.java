/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.brittatorp.homeauto.smartbus;

import se.brittatorp.homeauto.smartbus.transports.SmartbusPacket;

/**
 *
 * @author tornbmat
 */
public interface SmartbusCallbacks {

    public void PackageReceivedEvent(SmartbusPacket smartbusPacket);
}
