/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smartbus;

import org.openhab.core.binding.BindingProvider;

/**
 * @author Mattias
 * @since 1.4.0
 */
public interface SmartBusBindingProvider extends BindingProvider {
	public int getSubnetId(String itemName);
	public int getDeviceId(String itemName);
	public int getChannelNo(String itemName);
}
