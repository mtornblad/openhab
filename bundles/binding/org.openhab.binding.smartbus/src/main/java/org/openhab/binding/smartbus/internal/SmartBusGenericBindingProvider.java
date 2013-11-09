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
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
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
	private boolean autoUpdate = false;	//nyull

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
		logger.debug("processBindingConfiguration() method is called for item: " + item.getName());
		super.processBindingConfiguration(context, item, bindingConfig);
		SmartBusBindingConfig config = new SmartBusBindingConfig();
		
		//parse bindingconfig here ...
		
		addBindingConfig(item, config);		
	}
	
	
	class SmartBusBindingConfig implements BindingConfig {
		public int targetSubnetId = 0;
		public int targetDeviceId = 0;
		public int channelNo = 0;
		// put member fields here which holds the parsed values
	}


	@Override
	public Boolean autoUpdate(String itemName) {
		logger.debug("Autoupdate is off for: " + itemName);
		return this.autoUpdate;
	}
	
	
}
