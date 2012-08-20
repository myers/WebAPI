/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package com.wolftein.webapi.rocky;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.http.websocketx.WebSocketFrame;

import com.wolftein.webapi.WebPlugin;

/**
 * Encapsulate the client that act as proxy between both protocol
 * 
 * @author Agustin Alvarez <agustin.l.alvarez@hotmail.com>
 */
public class WebRockyClient implements ChannelFutureListener {
	/**
	 * The websocket channel proxy
	 */
	private Channel proxy;
	/**
	 * The TCP worker channel
	 */
	private Channel worker;
	/**
	 * The TCP bootstrap
	 */
	private ClientBootstrap bootstrap;

	/**
	 * Default constructor of the client
	 * 
	 * @param proxy
	 *            the websocket channel
	 */
	public WebRockyClient(Channel proxy) {
		this.proxy = proxy;

		// Create the client bootstrap
		bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(
				Executors.newCachedThreadPool(),
				Executors.newCachedThreadPool()));
		bootstrap.setPipelineFactory(new WebRockyClientPipelineFactory(this));

		// Start the connection attempt
		bootstrap.connect(
				new InetSocketAddress("localhost", WebPlugin.getInstance()
						.getServerPort())).addListener(this);
	}

	/**
	 * {@inhericDoc}
	 */
	@Override
	public void operationComplete(ChannelFuture arg0) throws Exception {
		worker = arg0.getChannel();
	}

	/**
	 * Close both channels and release all memory
	 */
	public void close() {
		if (proxy.isConnected()) {
			proxy.close();
		}
		if (worker.isConnected()) {
			worker.close();
		}
		bootstrap.releaseExternalResources();
	}

	/**
	 * Convert the frame to raw binary and send it to the client channel
	 * 
	 * @param frame
	 *            the frame to convert and write into the channel
	 */
	public void sendToWorker(WebSocketFrame frame) {
		worker.write(frame.getBinaryData());
	}

	/**
	 * Convert the bytes to raw string and send it to the proxy channel
	 * 
	 * @param data
	 *            the bytes to convert and write into the channel
	 */
	public void sendToProxy(byte[] data) {
		proxy.write(new String(data));
	}

}
