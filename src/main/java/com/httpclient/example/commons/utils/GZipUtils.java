package com.httpclient.example.commons.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GZipUtils {

	private static final Logger LOG = LoggerFactory.getLogger(GZipUtils.class);

	private GZipUtils() {
		throw new AssertionError();
	}

	public static byte[] compressGZIP(byte[] dataToCompress) {

		try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream(dataToCompress.length)) {
			try (GZIPOutputStream zipStream = new GZIPOutputStream(byteStream)) {
				zipStream.write(dataToCompress);
			}
			return byteStream.toByteArray();
		} catch (IOException e) {
			LOG.warn("::compressGZIP", e);
			return dataToCompress;
		}
	}

	public static byte[] decompresseGZIP(byte[] compressedBytes) {

		try {
			ByteArrayInputStream bytein = new ByteArrayInputStream(compressedBytes);
			GZIPInputStream gzin = new GZIPInputStream(bytein);
			ByteArrayOutputStream byteout = new ByteArrayOutputStream();

			int res = 0;
			byte[] buf = new byte[1024];
			while (res >= 0) {
				res = gzin.read(buf, 0, buf.length);
				if (res > 0) {
					byteout.write(buf, 0, res);
				}
			}

			return byteout.toByteArray();

		} catch (IOException e) {
			LOG.warn("::decompresseGZIP", e);
			return compressedBytes;
		}
	}

}
