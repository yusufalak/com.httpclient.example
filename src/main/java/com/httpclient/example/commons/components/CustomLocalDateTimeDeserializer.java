package com.httpclient.example.commons.components;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomLocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd[[ ]['T']HH:mm[:ss][XXX]]");

	@Override
	public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		String value = p.getText();
		TemporalAccessor parseBest = formatter.parseBest(value, ZonedDateTime::from, LocalDateTime::from, LocalDate::from);

		if (parseBest instanceof ZonedDateTime) {
			ZonedDateTime zonedDateTime = ZonedDateTime.from(parseBest);
			return zonedDateTime.toLocalDateTime();
		} else if (parseBest instanceof LocalDateTime) {
			return LocalDateTime.from(parseBest);
		} else if (parseBest instanceof LocalDate) {
			return LocalDate.from(parseBest).atStartOfDay();
		}

		throw new IllegalArgumentException("Wrong date format:" + value);
	}

}
