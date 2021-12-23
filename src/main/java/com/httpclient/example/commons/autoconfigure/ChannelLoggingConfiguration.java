package com.httpclient.example.commons.autoconfigure;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

@Configuration
@ConditionalOnProperty(name = "commons.logger.enabled", havingValue = "true")
public class ChannelLoggingConfiguration {

	private DateTimeFormatter yyyy_MM_dd_HH_mm_ss = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	private DateTimeFormatter yyyy_MM_dd = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	@Bean(name = "commons.gson")
	public Gson gson() {
		return new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonSerializer<LocalDateTime>) (src, typeOfSrc, context) -> {
			if (src == null) {
				return JsonNull.INSTANCE;
			}
			return new JsonPrimitive(src.format(yyyy_MM_dd_HH_mm_ss));
		}).registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) -> {
			if (src == null) {
				return JsonNull.INSTANCE;
			}
			return new JsonPrimitive(src.format(yyyy_MM_dd));
		}).create();
	}

	@Bean
	public SimpleApplicationEventMulticaster applicationEventMulticaster() {
		SimpleApplicationEventMulticaster applicationEventMulticaster = new SimpleApplicationEventMulticaster();
		SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
		taskExecutor.setDaemon(true);
		taskExecutor.setThreadNamePrefix("commons-event-");
		applicationEventMulticaster.setTaskExecutor(taskExecutor);
		return applicationEventMulticaster;
	}
}
