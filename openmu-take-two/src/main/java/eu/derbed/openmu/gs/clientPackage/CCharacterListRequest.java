package eu.derbed.openmu.gs.clientPackage;

import java.io.IOException;

import com.notbed.muonline.util.Header;

import eu.derbed.openmu.gs.ClientThread;
import eu.derbed.openmu.gs.client.ClientPackage;
import eu.derbed.openmu.gs.muObjects.MuCharacterList;
import eu.derbed.openmu.gs.serverPackets.SCharacterListAnsfer;

@Header ({0xf3, 0x00})
public class CCharacterListRequest implements ClientPackage {

	/* (non-Javadoc)
	 * @see eu.derbed.openmu.gs.clientPackage.ClientPackage#process(byte[], eu.derbed.openmu.gs.ClientThread)
	 */
	@Override
	public void process(byte[] data, ClientThread client) throws IOException {
		MuCharacterList result = new MuCharacterList();
		try {
			MuCharacterList chList = client.getChList();
			if (chList.needRead()) {
				client.readCharacterList();
			}
			result = chList;
		} finally {
			client.getConnection().sendPacket(
				new SCharacterListAnsfer(result));
		}
	}

}
