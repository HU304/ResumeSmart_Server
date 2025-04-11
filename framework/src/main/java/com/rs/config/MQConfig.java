package com.rs.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class MQConfig {

    // 定义队列的名称
    public static final String RESUME_QUEUE = "resume.queue";
    // 定义交换机的名称
    public static final String RESUME_EXCHANGE = "resume.exchange";
    // 定义路由键
    public static final String ROUTING_KEY = "resume.process"; //

    // 创建交换机的bean
    @Bean
    public TopicExchange resumeExchange() {
        return new TopicExchange(RESUME_EXCHANGE);
    }

    // 创建队列的bean
    @Bean
    public Queue resumeQueue() {
        return new Queue(RESUME_QUEUE, true); // true 表示队列持久化
    }

    // 创建绑定关系的bean
    @Bean
    public Binding binding() {
        return BindingBuilder.bind(resumeQueue())  // 将队列绑定到交换机
                .to(resumeExchange())  // 指定交换机
                .with(ROUTING_KEY); // 使用指定的路由键
    }



}
