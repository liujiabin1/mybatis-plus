package com.riverstar.config;

import com.riverstar.component.srv.Receiver;
import com.riverstar.constant.common.RabbitConst;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.DirectMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  Hardy
 * Date:    2018/7/18 20:05
 * Description:
 **/
@Configuration
public class RabbitConfig {

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        for (RabbitConst rabbitConst : RabbitConst.values()) {
            if (!rabbitConst.init) continue;

            Queue queue = new Queue(rabbitConst.queue, true, false, false);
            DirectExchange exchange = new DirectExchange(rabbitConst.ex, true, false);

            admin.declareQueue(queue);
            admin.declareExchange(exchange);
            admin.declareBinding(BindingBuilder.bind(queue).to(exchange).with(rabbitConst.key));
        }
        return admin;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(MessageConverter messageConverter,
                                                  Receiver receiver) {
        MessageListenerAdapter adapter = new MessageListenerAdapter(receiver, "message");
        adapter.setMessageConverter(messageConverter);
        return adapter;
    }

    @Bean
    public MessageListenerContainer container(ConnectionFactory connectionFactory,
                                              MessageListenerAdapter listenerAdapter) {
        List<String> queues = new ArrayList<String>();
        for (RabbitConst rabbitConst : RabbitConst.values()) {
            // 是否需要接受队列消息
            if (!rabbitConst.receive) continue;
            queues.add(rabbitConst.queue);
        }

        DirectMessageListenerContainer container = new DirectMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setMessageListener(listenerAdapter);
        container.setQueueNames(queues.toArray(new String[]{}));
        container.setConsumersPerQueue(1);
        container.setPrefetchCount(1);
        return container;
    }
}