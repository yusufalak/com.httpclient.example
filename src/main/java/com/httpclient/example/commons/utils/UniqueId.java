package com.httpclient.example.commons.utils;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import org.springframework.util.Base64Utils;

public class UniqueId implements Serializable {

	private static final int BUFFER_SIZE = 28;
	private static final long serialVersionUID = -1455000396084013556L;

	private LocalDateTime dateTime;
	private int providerId;
	private long leastSignificantBits;
	private long mostSignificantBits;
	private UniqueId other;

	private UniqueId() {
		UUID uuid = UUID.randomUUID();

		this.dateTime = LocalDateTime.now();
		this.providerId = 0;
		this.leastSignificantBits = uuid.getLeastSignificantBits();
		this.mostSignificantBits = uuid.getMostSignificantBits();
	}

	private UniqueId(int providerId, LocalDateTime dateTime) {
		UUID uuid = UUID.randomUUID();

		this.dateTime = dateTime;
		this.providerId = providerId;
		this.leastSignificantBits = uuid.getLeastSignificantBits();
		this.mostSignificantBits = uuid.getMostSignificantBits();
	}

	public UniqueId(LocalDateTime dateTime, int providerId, long leastSignificantBits, long mostSignificantBits) {
		this.dateTime = dateTime;
		this.providerId = providerId;
		this.leastSignificantBits = leastSignificantBits;
		this.mostSignificantBits = mostSignificantBits;
	}

	public static UniqueId create() {
		return new UniqueId();
	}

	public static UniqueId create(int providerId, LocalDateTime dateTime) {
		return new UniqueId(providerId, dateTime);
	}

	public static UniqueId fromString(String str) {
		byte[] decodeFromUrlSafeString = Base64Utils.decodeFromUrlSafeString(str);

		ByteBuffer wrap = ByteBuffer.wrap(decodeFromUrlSafeString);
		UniqueId id = fromBuffer(wrap);
		if (wrap.capacity() == BUFFER_SIZE * 2) {
			id.setOther(fromBuffer(wrap));
		}

		return id;
	}

	private static UniqueId fromBuffer(ByteBuffer wrap) {

		long millis = wrap.getLong();
		int id = wrap.getInt();
		long leastSignificantBits = wrap.getLong();
		long mostSignificantBits = wrap.getLong();

		Instant instance = java.time.Instant.ofEpochMilli(millis);
		LocalDateTime localDateTime = java.time.LocalDateTime.ofInstant(instance, java.time.ZoneId.of("Europe/Istanbul"));

		return new UniqueId(localDateTime, id, leastSignificantBits, mostSignificantBits);
	}

	@Override
	public String toString() {

		ByteBuffer buf = this.toByteBuffer();
		if (this.other == null) {
			return Base64Utils.encodeToUrlSafeString(buf.array());
		}

		ByteBuffer result = ByteBuffer.allocate(BUFFER_SIZE * 2);
		buf.flip();
		result.put(buf);
		ByteBuffer otherBuf = this.other.toByteBuffer();
		otherBuf.flip();
		result.put(otherBuf);
		return Base64Utils.encodeToUrlSafeString(result.array());
	}

	private ByteBuffer toByteBuffer() {

		ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, ZoneId.of("Europe/Istanbul"));
		ByteBuffer buf = ByteBuffer.allocate(BUFFER_SIZE);

		buf.putLong(zonedDateTime.toInstant().toEpochMilli());
		buf.putInt(providerId);
		buf.putLong(this.leastSignificantBits);
		buf.putLong(this.mostSignificantBits);

		return buf;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public int getProviderId() {
		return providerId;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public long getLeastSignificantBits() {
		return leastSignificantBits;
	}

	public void setLeastSignificantBits(long leastSignificantBits) {
		this.leastSignificantBits = leastSignificantBits;
	}

	public long getMostSignificantBits() {
		return mostSignificantBits;
	}

	public void setMostSignificantBits(long mostSignificantBits) {
		this.mostSignificantBits = mostSignificantBits;
	}

	public UniqueId getOther() {
		return other;
	}

	public void setOther(UniqueId other) {
		this.other = other;
	}

}
