package eu.derbed.openmu.gs;

import static com.notbed.muonline.util.UPacket.logTransfer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.notbed.muonline.util.PacketResolver;

import eu.derbed.openmu.gs.client.ClientPackage;
import eu.derbed.openmu.gs.clientPackage.CCharacterManipulator;
import eu.derbed.openmu.gs.clientPackage.CItemUseRequest;


/**
 * This class ...
 *
 * @version $Revision: 1.18 $ $Date: 2004/10/26 20:43:03 $
 */
public class PacketHandler {

	private static final Logger log = LoggerFactory.getLogger(PacketHandler.class);

	private final PacketResolver<ClientPackage> resolver;

	private final ClientThread _client;

	/**
	 * @param client
	 * @param resolver
	 */
	public PacketHandler(final ClientThread client, final PacketResolver<ClientPackage> resolver) {
		_client = client;
		this.resolver = resolver;
	}

	/**
	 * Synchronized because when entering gates the character sometimes doesn't
	 * appear on the other side. Synchronizing this doesn't eliminate the problem
	 * but it does seem to happen less often, investigate this when you have time
	 *
	 * @param data
	 * @throws IOException
	 */
	public synchronized void handlePacket(final byte[] data) throws IOException {
		// int pos = 0;
		// System.out.println("lenght="+data.length);
		final int id = data[0] & 0xff;
		logTransfer(log, data, "[C->S]");
		ClientPackage cp = resolver.resolvePacket(data);

		if (cp == null) { // try old method
			switch (id) {
				case 0x26:
					cp = new CItemUseRequest();
					break;
				case 0xf3:
					cp = new CCharacterManipulator();
					break;
					// 24 00 0c e3 00 00 80 00 00 14
			}
		}

		if (null == cp) {
			log.debug("Unknown implementation " + Integer.toHexString(id));
		} else {
			log.debug("Received {}", cp.getClass().getSimpleName());
			cp.process(data, _client);
		}
	}

}
