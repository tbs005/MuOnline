/*
 * Copyright [mikiones] [Michal Kinasiewicz]
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.derbed.openmu.cs;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

import eu.derbed.openmu.netty.abstracts.AbstractMuPackageBuilder;
import eu.derbed.openmu.netty.abstracts.AbstractMuPackageData;
import eu.derbed.openmu.netty.abstracts.MuBaseMessage;
import eu.derbed.openmu.netty.filters.MuFrameDecoder;
import eu.derbed.openmu.netty.filters.MuMessageDecrytor;
/**
 *
 * @author mikiones
 *	<br>The {@link ChannelPipelineFactory} for CS<br>
 *
 *	the income messages are: <br>
 *	1: decided into single frames( {@link MuBaseMessage} ) by {@link MuFrameDecoder}<br>
 *	2: The income frames are encrypted by {@link MuMessageDecrytor}<br>
 *	3: and finally go to {@link CSSesionHandler}<br><br>
 *
 *	The Outcome messages are represent by {@link AbstractMuPackageData}<br>
 *	1: the {@link AbstractMuPackageData} are coded by specified PackageBuilder {@link AbstractMuPackageBuilder}<br>
 *	2: the package builder create binary frame what is sanded to client<br>
 *
 */
public class CSChanellPipelineFactory implements ChannelPipelineFactory {

	/**
	 * @param serverList
	 */
	public CSChanellPipelineFactory(final ServerList serverList) {
		super();
		pipe.addLast("1 frameDecoder", new MuFrameDecoder());
		pipe.addLast("2 frame decrypter", new MuMessageDecrytor());
		pipe.addLast("3 protocol Builder", new CSProtocolEncoder(serverList));
		pipe.addLast("4 sesionHandler", new CSSesionHandler(serverList));
	}

	ChannelPipeline pipe = Channels.pipeline();

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		return pipe;
	}

}
