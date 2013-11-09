/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.brittatorp.homeauto.smartbus.operations;


import se.brittatorp.common.Helpers;
import se.brittatorp.homeauto.smartbus.operations.OperationBase.SmartBusCommand;
import se.brittatorp.homeauto.smartbus.transports.SmartbusPacket;

/**
 *
 * @author tornbmat
 */
@SmartBusCommand(CommandName = "0031")
public class SingleChannelControl extends OperationBase {
	
	short lightChannelNo;
	short brightnessLevel;
	int runningTime;

    @Override
    public void parsePacket(SmartbusPacket packet) {
    	byte[] additionalContent = packet.getAdditionalContent();

    	lightChannelNo = Helpers.byteArray2Short(additionalContent, 0);
    	brightnessLevel = Helpers.byteArray2Short(additionalContent, 1);
    	runningTime = Helpers.byteArray2Int(additionalContent, 2);    	
    }


}
