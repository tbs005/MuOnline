package eu.derbed.openmu.gs;

import static com.notbed.muonline.util.UPacket.logTransfer;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.derbed.openmu.gs.clientPackage.CA0Request;
import eu.derbed.openmu.gs.clientPackage.CAddFrendRequest;
import eu.derbed.openmu.gs.clientPackage.CAddLvlPointsRequest;
import eu.derbed.openmu.gs.clientPackage.CAttackOnId;
import eu.derbed.openmu.gs.clientPackage.CBuyItemRequest;
import eu.derbed.openmu.gs.clientPackage.CChangeDirectoryOrStatus;
import eu.derbed.openmu.gs.clientPackage.CCharacterListRequest;
import eu.derbed.openmu.gs.clientPackage.CClientSettingsSaveRequest;
import eu.derbed.openmu.gs.clientPackage.CDeleteChar;
import eu.derbed.openmu.gs.clientPackage.CEnterInGateRequest;
import eu.derbed.openmu.gs.clientPackage.CItemDropFromInwentoryRequest;
import eu.derbed.openmu.gs.clientPackage.CItemPickUpRequest;
import eu.derbed.openmu.gs.clientPackage.CItemUseRequest;
import eu.derbed.openmu.gs.clientPackage.CMoveCharacter;
import eu.derbed.openmu.gs.clientPackage.CMoveItemRequest;
import eu.derbed.openmu.gs.clientPackage.CNewCharacterRequest;
import eu.derbed.openmu.gs.clientPackage.CNpcRunRequest;
import eu.derbed.openmu.gs.clientPackage.CPasVeryfcation;
import eu.derbed.openmu.gs.clientPackage.CPublicMsg;
import eu.derbed.openmu.gs.clientPackage.CSelectCharacterOrExitRequest;
import eu.derbed.openmu.gs.clientPackage.CSelectedCharacterEnterRequest;
import eu.derbed.openmu.gs.clientPackage.ClientPackage;


/**
 * This class ...
 *
 * @version $Revision: 1.18 $ $Date: 2004/10/26 20:43:03 $
 */
public class PacketHandler {

	private static final Logger log = LoggerFactory.getLogger(PacketHandler.class);

	// .getLogger(PacketHandler.class.getName());
	private final ClientThread _client;

	public PacketHandler(ClientThread client) {
		_client = client;
	}

	public void handlePacket(byte[] data) throws IOException, Throwable {
		// int pos = 0;
		// System.out.println("lenght="+data.length);
		final int id = data[0] & 0xff;
		int id2 = 0;

		if (data.length > 1) {
			id2 = data[1] & 0xff;
		}
		logTransfer(log, data, "[C->S]");
		switch (id) {
		case 0xa0:
			new CA0Request().process(data, _client);
			break;
		case 0x00:
			new CPublicMsg().process(data, _client);
			break;
		case 0x18:
			new CChangeDirectoryOrStatus().process(data, _client);
			break;
		case 0x1C:
			new CEnterInGateRequest().process(data, _client);
			break;
		case 0x22:
			new CItemPickUpRequest().process(data, _client);
			break;
		case 0x23:
			new CItemDropFromInwentoryRequest().process(data, _client);
			break;
		case 0x24:
			new CMoveItemRequest().process(data, _client);
			break;
		case 0x26:
			new CItemUseRequest().process(data, _client);
			break;
		case 0x30:
			new CNpcRunRequest(data, _client);
			break;
		case 0x32:
			new CBuyItemRequest().process(data, _client);
			break;
		case 0xd7:
			new CMoveCharacter().process(data, _client);
			break;
		case 0xd9:
			new CAttackOnId().process(data, _client);
			break;
		case 0xc1:
			new CAddFrendRequest().process(data, _client);
			break;
		case 0xf1: {
			ClientPackage cp = null;
				switch (id2) {
					case 0x01:
						cp = new CPasVeryfcation();
						break;
					case 0x02:
						cp = new CSelectCharacterOrExitRequest();
						break;
					default:
						break;
				}
			if (null == cp) {
				log.debug("Unknown implementation " + Integer.toHexString(id));
			} else {
				log.debug("Received {}", cp.getClass().getSimpleName());
				cp.process(data, _client);
			}
		}
			break;
		case 0xf3: {
			switch (id2) {
			case 0x00: {
				new CCharacterListRequest().process(data, _client);
			}
				break;
			case 0x01: {
				new CNewCharacterRequest().process(data, _client);
			}
				break;
			case 0x02: {
				new CDeleteChar().process(data, _client);
			}
				break;
			case 0x03: {
				new CSelectedCharacterEnterRequest().process(data, _client);
			}
				break;
			case 0x06: {
				new CAddLvlPointsRequest().process(data, _client);
			}
				break;
			case 0x30: {
				new CClientSettingsSaveRequest().process(data, _client);
			}
				break;
			default: {
				log.debug("Unknown Packet or no implament: "
						+ Integer.toHexString(id));

			}
				break;
			}
		}
			break;
		// 24 00 0c e3 00 00 80 00 00 14
		default:
			log.debug("Unknown Packet or no implament: "
					+ Integer.toHexString(id));

		}

	}

}
