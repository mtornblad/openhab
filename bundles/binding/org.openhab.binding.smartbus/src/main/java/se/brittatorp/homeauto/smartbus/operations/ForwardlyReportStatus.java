/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package se.brittatorp.homeauto.smartbus.operations;


import java.util.Arrays;

import se.brittatorp.common.Helpers;
import se.brittatorp.homeauto.smartbus.operations.OperationBase.SmartBusCommand;
import se.brittatorp.homeauto.smartbus.transports.SmartbusPacket;

/**
 *
 * @author tornbmat
 */
@SmartBusCommand(CommandCode = 0xEFFF, ResponseCode = 0)
public class ForwardlyReportStatus extends OperationBase {
	
	// String fieldNames[] = {"QtyOfZones", "ByteArray"};
	short qtyOfZones;
	short[] statusOfZone;
	short qtyOfChannels;
	boolean[] statusOfChannel;	//True = on
	
    @Override
    public void parsePacket(SmartbusPacket packet) {
    	byte[] additionalContent = packet.getAdditionalContent();
    	qtyOfZones = Helpers.byteArray2Short(additionalContent, 0);
    	statusOfZone = new short[qtyOfZones];
    	for(int i=0;i<qtyOfZones;i++){
    		statusOfZone[i]=additionalContent[i+1];
    	}
    	qtyOfChannels=Helpers.byteArray2Short(additionalContent, qtyOfZones+1);
    	statusOfChannel = new boolean[qtyOfChannels];
    	for(int i=0, j=0, k=0; i<qtyOfChannels;i++){
    		k=i / 8;
    		j=additionalContent[k + qtyOfZones + 2] >> (i % 8);
    		statusOfChannel[i]=((j&1)==1);
    	}
    }
}
