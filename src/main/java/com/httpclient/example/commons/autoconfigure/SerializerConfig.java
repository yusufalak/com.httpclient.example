package com.httpclient.example.commons.autoconfigure;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.httpclient.example.commons.components.CustomLocalDateDeserializer;
import com.httpclient.example.commons.components.CustomLocalDateTimeDeserializer;

@Configuration
public class SerializerConfig {

	private DateTimeFormatter yyyy_MM_dd_HH_mm_ss = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	private DateTimeFormatter yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Bean
	public ObjectMapper objectMapper() {

		return Jackson2ObjectMapperBuilder.json()
				.propertyNamingStrategy(PropertyNamingStrategies.LOWER_CAMEL_CASE)
				.featuresToEnable(MapperFeature.USE_ANNOTATIONS, DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, SerializationFeature.INDENT_OUTPUT,
						MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES)
				.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(yyyy_MM_dd_HH_mm_ss))
				.deserializerByType(LocalDateTime.class, new CustomLocalDateTimeDeserializer())
				.serializerByType(LocalDate.class, new LocalDateSerializer(yyyy_MM_dd))
				.deserializerByType(LocalDate.class, new CustomLocalDateDeserializer())
				.serializerByType(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE))
				.build();
	}

}
