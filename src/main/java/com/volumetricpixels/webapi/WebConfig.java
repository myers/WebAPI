/*
 * This file is part of WebAPI.
 *
 * Copyright (c) 2012-2012, VolumetricPixels <http://volumetricpixels.com/>
 * WebAPI is licensed under the VolumetricPixels License Version 1.
 *
 * The VolumetricPixels License is a triple license combined out of the AGPL v3
 * License, the MIT License and the Classpath Exception License.
 *
 * You should have received a copy of the GNU Affero General Public License,
 * the MIT license and the VolumetricPixels License v1 along with this program.
 * If not, see <http://github.com/VolumetricPixels/Vitals/blob/master/License.txt>
 * for the full license.
 */
package com.volumetricpixels.webapi;

import java.io.File;

import org.spout.api.exception.ConfigurationException;
import org.spout.api.util.config.ConfigurationHolder;
import org.spout.api.util.config.ConfigurationHolderConfiguration;
import org.spout.api.util.config.yaml.YamlConfiguration;

/**
 * Contain all the information regarding the plugin
 * 
 * @author Agustin Alvarez <agustin.l.alvarez@hotmail.com>
 */
public class WebConfig extends ConfigurationHolderConfiguration {

	/**
	 * The port where the server will listen
	 */
	public static final ConfigurationHolder LISTENING_PORT = new ConfigurationHolder(
			"81", "listening-port");
	/**
	 * Is the websocket enabled
	 */
	public static final ConfigurationHolder WEBSOCKET_ENABLED = new ConfigurationHolder(
			"true", "websocket-enabled");
	/**
	 * The location of the websocket
	 */
	public static final ConfigurationHolder WEBSOCKET_LOCATION = new ConfigurationHolder(
			"/websocket", "websocket-uri");
	/**
	 * Is rocky client enabled?
	 */
	public static final ConfigurationHolder ROCKY_ENABLED = new ConfigurationHolder(
			"true", "rocky-enabled");
	/**
	 * The server port
	 */
	public static final ConfigurationHolder ROCKY_PORT = new ConfigurationHolder(
			"25565", "rocky-server-port");

	/**
	 * Default constructor of the configuration class
	 */
	public WebConfig() {
		super(new YamlConfiguration(new File("plugins/WebAPI/setting.yml")));
	}

	/**
	 * Load the configuration of the plugin
	 */
	public void load() {
		File dir = new File("plugins/WebAPI");
		if (!dir.exists())
			dir.mkdir();
		try {
			super.load();
			super.save();
		} catch (ConfigurationException ex) {
			WebPlugin.getInstance().printMessage(
					"Can not create configuration file setting.yml");
		}
	}

}
