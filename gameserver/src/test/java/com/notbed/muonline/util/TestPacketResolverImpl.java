package com.notbed.muonline.util;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alexandru Bledea
 * @since Jul 31, 2014
 */
@SuppressWarnings ("static-method")
public class TestPacketResolverImpl {

	/**
	 * @throws RegistrationConflictException
	 */
	@Test
	public void testFailRegistrationConflict3() throws RegistrationException {
		final PacketResolverImpl<Packet> resolver = new PacketResolverImpl<>(Packet.class);

		final Packet problem1 = new Packet_0x5B_0x59_0x0();
		resolver.register(problem1);

		final Packet failure = new Packet_0x5B();
		try {
			resolver.register(failure);
			fail("Should have thrown an error!");
		} catch (final RegistrationConflictException ex) {
			assertSame(failure, ex.getFailedObject());
			assertSameElements(Collections.singleton(problem1), ex.getReasons());
		}
	}

	/**
	 * @throws RegistrationConflictException
	 */
	@Test
	public void testFailRegistrationConflict2() throws RegistrationException {
		final PacketResolverImpl<Packet> resolver = new PacketResolverImpl<>(Packet.class);

		final Packet problem1 = new Packet_0x5B_0x59_0x0();
		final Packet problem2 = new Packet_0x5B_0x60();
		final Collection<Packet> reason = Arrays.asList(problem1, problem2);

		final Packet failure = new Packet_0x5B();

		resolver.register(problem1);
		resolver.register(problem2);
		try {
			resolver.register(failure);
			fail("Should have thrown an error!");
		} catch (final RegistrationConflictException ex) {
			assertSame(failure, ex.getFailedObject());
			assertSameElements(reason, ex.getReasons());
		}
	}

	/**
	 * @throws RegistrationConflictException
	 */
	@Test
	public void testFailRegistrationConflict() throws RegistrationException {
		final PacketResolverImpl<Packet> resolver = new PacketResolverImpl<>(Packet.class);

		final Packet reason = new Packet_0x5B();
		final Packet failure = new Packet_0x5B_0x59_0x0();

		resolver.register(reason);
		try {
			resolver.register(failure);
			fail("Should have thrown an error!");
		} catch (final RegistrationConflictException ex) {
			assertSame(failure, ex.getFailedObject());
			assertSameElements(Collections.<Object> singleton(reason), ex.getReasons());
		}
	}

	/**
	 * @throws RegistrationException
	 */
	@Test (expected = RegistrationException.class)
	public void testFailNoHeader() throws RegistrationException {
		final PacketResolverImpl<Packet> resolver = new PacketResolverImpl<>(Packet.class);

		final Packet reason = new Packet();
		resolver.register(reason);
		fail("Should have thrown an error!");
	}

	/**
	 * @throws RegistrationException
	 */
	@Test (expected = RegistrationException.class)
	public void testFailEmptyHeader() throws RegistrationException {
		final PacketResolverImpl<Packet> resolver = new PacketResolverImpl<>(Packet.class);

		final Packet reason = new PacketEmptyHeader();
		resolver.register(reason);
		fail("Should have thrown an error!");
	}

	/**
	 * @throws RegistrationException
	 */
	@Test
	public void testResolveHeader() throws RegistrationException {
		final PacketResolverImpl<Packet> resolver = new PacketResolverImpl<>(Packet.class);

		final Packet p5b60 = new Packet_0x5B_0x60();
		final Packet p5b590 = new Packet_0x5B_0x59_0x0();
		final Packet_0xC pc = new Packet_0xC();

		resolver.register(p5b60);
		resolver.register(p5b590);
		resolver.register(pc);

		final Packet nothing = resolver.resolvePacket(data(0x0, 0x5));
		assertNull(nothing);

		final Packet packet = resolver.resolvePacket(data(0x5B, 0x60, 0x55));
		assertSame(p5b60, packet);
	}

	@Test
	public void testName() throws Exception {
//		failed here
//		[Thread-40] DEBUG PacketHandler - [C->S] 0000: A9 01 00 08                                        ....
		final PacketResolverImpl<Packet> resolver = new PacketResolverImpl<>(Packet.class);
		resolver.register(new Packet_0x00());
		final Packet nothing = resolver.resolvePacket(data(0x18, 0x01, 0x00, 0x08));
		assertNull(nothing);
	}

	/**
	 * @param expected
	 * @param actual
	 */
	private static void assertSameElements(final Collection<?> expected, final Collection<?> actual) {
		final Set<Object> c1a = new HashSet<>(expected);
		final Set<Object> c2a = new HashSet<>(actual);
		Assert.assertEquals(c1a.size(), c2a.size());
		Assert.assertEquals(c1a, c2a);
	}

	/**
	 * @param bytes
	 * @return
	 */
	private static Data data(final int... unsigned) {
		final byte[] bytes = new byte[unsigned.length];
		for (int i = 0; i < unsigned.length; i++) {
			bytes[i] = (byte) unsigned[i];
		}
		return new Data(bytes);
	}
}
