package eu.derbed.openmu.gs.client;

import java.io.IOException;

import com.notbed.muonline.util.DataDecrypter;

import eu.derbed.openmu.gs.ClientThread;

/**
 * @author Alexandru Bledea
 * @since Jul 27, 2014
 */
public abstract class SimpleClientPackage implements ClientPackage {

	/* (non-Javadoc)
	 * @see eu.derbed.openmu.gs.clientPackage.ClientPackage#process(byte[], eu.derbed.openmu.gs.ClientThread)
	 */
	@Override
	public final void process(final byte[] data, final ClientThread client) throws IOException {
		process(new DataDecrypter(data), client);
	}

	/**
	 * @param dataDecrypter
	 * @param client
	 */
	protected abstract void process(DataDecrypter dataDecrypter, ClientThread client) throws IOException;

}
