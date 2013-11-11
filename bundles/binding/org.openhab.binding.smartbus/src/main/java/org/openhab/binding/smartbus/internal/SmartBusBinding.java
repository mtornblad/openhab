/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smartbus.internal;

import java.net.SocketException;
import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.smartbus.SmartBusBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.brittatorp.common.Helpers;
import se.brittatorp.homeauto.smartbus.SmartbusCallbacks;
import se.brittatorp.homeauto.smartbus.operations.ForwardlyReportStatus;
import se.brittatorp.homeauto.smartbus.operations.OperationBase;
import se.brittatorp.homeauto.smartbus.operations.SingleChannelControl;
import se.brittatorp.homeauto.smartbus.transports.*;;
	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author Mattias
 * @since 1.4.0
 */
public class SmartBusBinding extends AbstractActiveBinding<SmartBusBindingProvider> implements ManagedService, SmartbusCallbacks {

	private static final Logger logger = 
		LoggerFactory.getLogger(SmartBusBinding.class);

	
	/** 
	 * the refresh interval which is used to poll values from the SmartBus
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
    private TransportIP transportUdp;

	
	public SmartBusBinding() {
	}
		
	
	public void activate() {
        try {
			transportUdp = new TransportIP(this);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        transportUdp.start();
        logger.debug("transportUDP thread started!");
	}
	
	public void deactivate() {
        transportUdp.interrupt();
        try {
			transportUdp.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// deallocate resources here that are no longer needed and 
		// should be reset when activating this binding again
	}

	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "SmartBus Refresh Service";
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		logger.debug("execute() method is called!");
		logger.debug(transportUdp.getState().name());
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// the code being executed when a command was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
		SmartBusBindingProvider provider = findFirstMatchingBindingProvider(itemName);
		int channelNo = provider.getChannelNo(itemName);
		int devId = provider.getDeviceId(itemName);
		int snetId = provider.getSubnetId(itemName);
		logger.debug("DevId: " + devId);
		SingleChannelControl operation = new SingleChannelControl((short)channelNo, (short)1, 1);
		SmartbusPacket packet = new SmartbusPacket((short)1,(short)1,118,(short)snetId,(short)devId,(OperationBase)operation);
		transportUdp.send(packet);
	}
	
	private SmartBusBindingProvider findFirstMatchingBindingProvider(String itemName) {

		SmartBusBindingProvider firstMatchingProvider = null;

		for (SmartBusBindingProvider provider : this.providers) {

			int channelNo = provider.getChannelNo(itemName);

			if (channelNo > 0) {
				firstMatchingProvider = provider;
				break;
			}
		}

		return firstMatchingProvider;
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}
		
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {
			
			// to override the default refresh interval one has to add a 
			// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			
			// read further config parameters here ...

			setProperlyConfigured(true);
		}
	}


	@Override
    public void PackageReceivedEvent(SmartbusPacket smartbusPacket) {
		switch(smartbusPacket.getOperationCode()){
			case 0xEFFF	:
				ForwardlyReportStatus operation = new ForwardlyReportStatus(smartbusPacket);
				for (SmartBusBindingProvider provider : providers) {
					for (String itemName : provider.getItemNames()) {
						if ((provider.getSubnetId(itemName)==smartbusPacket.getOriginalSubnetId()) & (provider.getDeviceId(itemName)==smartbusPacket.getOriginalDeviceId())) {
							eventPublisher.postUpdate(itemName, operation.statusOfChannel[provider.getChannelNo(itemName)-1] ? OnOffType.ON : OnOffType.OFF);
						}
					}
				}
				break;
			default:
				OperationBase operationBase = OperationBase.getOperation(smartbusPacket);
				if (operationBase != null) {
					logger.debug("OperationFields: " + Helpers.getDeclaredFieldsAsString(operationBase));
				}
		}
			
	}

}
