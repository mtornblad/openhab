/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smartbus.internal;

import org.openhab.binding.smartbus.SmartBusBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.autoupdate.AutoUpdateBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Mattias
 * @since 1.4.0
 */
public class SmartBusGenericBindingProvider extends AbstractGenericBindingProvider implements SmartBusBindingProvider, AutoUpdateBindingProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(SmartBusBinding.class);	

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "smartbus";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		//if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		//	throw new BindingConfigParseException("item '" + item.getName()
		//			+ "' is of type '" + item.getClass().getSimpleName()
		//			+ "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		//}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		String[]params=bindingConfig.split(",");
	
		SmartBusBindingConfig config = new SmartBusBindingConfig();
		config.subnetId = Integer.parseInt(params[0]);
		config.deviceId = Integer.parseInt(params[1]);
		config.channelNo = Integer.parseInt(params[2]);
		logger.debug(String.format("processBindingConfiguration() method is called for context: %s, item: %s, bindingConfig: %s, subnet: %d, device: %d, channel: %d",context, item.getName(), bindingConfig, config.subnetId, config.deviceId, config.channelNo));
		
		
		//parse bindingconfig here ...
		
		addBindingConfig(item, config);		
	}
	
	
	class SmartBusBindingConfig implements BindingConfig {
		public int subnetId = 0;
		public int deviceId = 0;
		public int channelNo = 0;
		// put member fields here which holds the parsed values
	}


	@Override
	public Boolean autoUpdate(String itemName) {
		logger.debug("Autoupdate is off for: " + itemName);
		//return this.autoUpdate;
		return Boolean.FALSE;
	}

	@Override
	public int getSubnetId(String itemName) {
		SmartBusBindingConfig config = (SmartBusBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.subnetId: 0;
	}

	@Override
	public int getDeviceId(String itemName) {
		SmartBusBindingConfig config = (SmartBusBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.deviceId: 0;
	}

	@Override
	public int getChannelNo(String itemName) {
		SmartBusBindingConfig config = (SmartBusBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.channelNo: 0;
	}
	

	
}
