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
@SmartBusCommand(CommandCode = 0x0031)
public class SingleChannelControl extends OperationBase {
	
	short lightChannelNo;
	short brightnessLevel;
	int runningTime;
	
    public SingleChannelControl(SmartbusPacket packet) {
		super(packet);
	}

    public SingleChannelControl(short lightChannelNo, short brightnessLevel, int runningTime) {
		super();
		this.lightChannelNo = lightChannelNo;
		this.brightnessLevel = brightnessLevel;
		this.runningTime = runningTime;
	}

    public void parsePacket(SmartbusPacket packet) {
    	byte[] additionalContent = packet.getAdditionalContent();

    	lightChannelNo = Helpers.byteArray2Short(additionalContent, 0);
    	brightnessLevel = Helpers.byteArray2Short(additionalContent, 1);
    	runningTime = Helpers.byteArray2Int(additionalContent, 2);    	
    }

	@Override
	public byte[] getData() {
		byte[] data = new byte[4];
		Helpers.updateArray(data, 0, lightChannelNo);
		Helpers.updateArray(data, 1, brightnessLevel);
		Helpers.updateArray(data, 2, runningTime);
		return data;
	}


}
