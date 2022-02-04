package com.example.shop;

import com.example.shop.DTO.Message;
import com.example.shop.DTO.ProductRequest;
import com.example.shop.DTO.ProductsRequest;
import com.example.shop.service.ShopService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.util.Arrays;
import java.util.List;

@Configuration
@IntegrationComponentScan
@Log4j2

public class MqttConnector {
    private final ShopService shopService;
    @Value("${spring.mqtt.username}")
    private String username;
    @Value("${spring.mqtt.password}")
    private String password;
    @Value("${spring.mqtt.url}")
    private String hostUrl;
    @Value("${spring.mqtt.client.id}")
    private String clientId;
    @Value("${spring.mqtt.default.topic}")
    private String defaultTopic;
    @Value("#{'${spring.mqtt.topics}'.split(',')}")
    private List<String> topics;
    @Value("#{'${spring.mqtt.qosValues}'.split(',')}")
    private List<Integer> qosValues;

    public MqttConnector(ShopService shopService) {
        this.shopService = shopService;
    }

    @Bean
    public MqttConnectOptions getMqttConnectOptions() {
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setUserName(username);
        mqttConnectOptions.setPassword(password.toCharArray());
        mqttConnectOptions.setServerURIs(new String[]{hostUrl});
        mqttConnectOptions.setKeepAliveInterval(2);
        mqttConnectOptions.setConnectionTimeout(60);
        mqttConnectOptions.setMaxInflight(100000000);
        return mqttConnectOptions;
    }

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
        factory.setConnectionOptions(getMqttConnectOptions());
        return factory;
    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        String[] strings = new String[topics.size()];
        Integer[] ints = new Integer[qosValues.size()];
        topics.toArray(strings);
        qosValues.toArray(ints);
        System.out.println("strings==" + Arrays.toString(strings));
        int[] its = Arrays.stream(ints).mapToInt(Integer::valueOf).toArray();
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(clientId + "_inbound", mqttClientFactory(), strings);

        adapter.setCompletionTimeout(3000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setOutputChannel(mqttInputChannel());

        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return message -> {

            ObjectMapper objectMapper = new ObjectMapper();

            try {
                Message p = objectMapper.readValue(message.getPayload().toString(), Message.class);

                System.out.println(message.getPayload());
                System.out.print(p.getPayload());

                if (p.getType().equals("add")) {

                    ProductRequest prod = objectMapper.convertValue(p.getPayload(), ProductRequest.class);
                    shopService.addProduct(prod);
                    System.out.println(message.getPayload());

                }
                if (p.getType().equals("buy")) {

                    ProductsRequest productsRequest = objectMapper.convertValue(p.getPayload(), ProductsRequest.class);

                    //ProductsRequest product = (ProductsRequest) p.getPayload();
                    shopService.buyProducts(productsRequest.getName(), productsRequest.getColor(), productsRequest.getSize(), productsRequest.getPieces());
                    System.out.print(message.getPayload());

                }
            } catch (JsonProcessingException | ClassCastException e) {
                e.printStackTrace();
            }

        };
    }
}
