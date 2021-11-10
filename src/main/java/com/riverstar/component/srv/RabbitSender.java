package com.riverstar.component.srv;

import com.riverstar.constant.common.RabbitConst;
import com.riverstar.model.message.BaseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.transaction.RabbitTransactionManager;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * Author:  Hardy
 * Date:    2018/7/18 20:05
 * Description: RabbitMQ 发送组件 同步发送且同步确认
 **/
@Component
public class RabbitSender {

    private static final Logger log = LoggerFactory.getLogger(RabbitSender.class);

    private final int retryCount;
    private final long retryInterval;
    private final RabbitTemplate rabbitTemplate;
    private final RabbitTransactionManager rabbitTransactionManager;

    @Autowired
    public RabbitSender(@Value("${rabbit.retry-count:3}") int count,
                        @Value("${rabbit.retry-interval:200}") long interval,
                        ConnectionFactory connectionFactory,
                        MessageConverter messageConverter) {
        this.retryCount = count;
        this.retryInterval = interval;
        this.rabbitTransactionManager = new RabbitTransactionManager(connectionFactory);
        this.rabbitTemplate = new RabbitTemplate(connectionFactory);
        this.rabbitTemplate.setMessageConverter(messageConverter);
        this.rabbitTemplate.setChannelTransacted(true);
    }

    @Transactional
    public void sendAndRetry(RabbitConst rabbit, BaseMessage message) {
        sendAndRetry(rabbit.ex, rabbit.key, message);
    }

    @Transactional
    public void sendAndRetry(String exchange, String routingKey, BaseMessage message) {
        // 不存在msgId 则任务时认为新消息
        sendAndRetryPtv(exchange, routingKey, message);
    }

    private void sendAndRetryPtv(String exchange, String routingKey, Object object) {
        int retry = 0;
        while (true) {
            try {
                send(exchange, routingKey, object);
                return;
            } catch (Exception e) {
                if (++retry > retryCount)
                    throw e;
                sleep();
                log.warn("Retry {} time publish message {}", retry, object.toString());
            }
        }
    }

    private void send(String exchange, String routingKey, Object object) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        definition.setName("RabbitTool.send");

        TransactionStatus status = rabbitTransactionManager.getTransaction(definition);
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, object);
            rabbitTransactionManager.commit(status);
        } catch (Exception e) {
            rabbitTransactionManager.rollback(status);
            throw e;
        }
    }

    private void sleep() {
        try {
            Thread.sleep(retryInterval);
        } catch (InterruptedException e) {
            log.warn("Retry interval Interrupted");
        }
    }
}