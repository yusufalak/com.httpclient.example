package com.httpclient.example.commons.httpclient.models;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;

import org.apache.http.entity.AbstractHttpEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GzipEntity extends AbstractHttpEntity {

	private final Logger LOG = LoggerFactory.getLogger(this.getClass());
	private byte[] bytes;

	public GzipEntity(final String content, String contentType) {
		super();
		if (content == null) {
			throw new IllegalArgumentException("Source input stream may not be null");
		}

		try (ByteArrayOutputStream bout = new ByteArrayOutputStream(1024)) {
			GZIPOutputStream out = new GZIPOutputStream(bout);
			bytes = content.getBytes(StandardCharsets.UTF_8);
			out.write(bytes);
			out.flush();
			out.close();
			bytes = bout.toByteArray();
		} catch (Exception e) {
			LOG.error("::gzipEntity", e);
		}

		setContentEncoding("gzip");
		setContentType(contentType);

	}

	public boolean isRepeatable() {
		return true;
	}

	public long getContentLength() {
		return bytes.length;
	}

	public InputStream getContent() throws IOException {

		return null;
	}

	public void writeTo(final OutputStream outstream) throws IOException {
		if (outstream == null) {
			throw new IllegalArgumentException("Output stream may not be null");
		}
		outstream.write(bytes);
		outstream.flush();
	}

	public boolean isStreaming() {
		return false;
	}
}
