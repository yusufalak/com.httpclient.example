package com.httpclient.example.commons.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.httpclient.example.commons.channel.RestChannel;


@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RestChannel.class)
@Import({ SerializerConfig.class, WebConfig.class })
public class RestChannelAutoConfiguration {

}