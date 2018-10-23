package cn.com.tw.common.core.jms.activemq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.apache.activemq.ScheduledMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import cn.com.tw.common.core.jms.MqHandler;

@Component
public class ActiveMqService implements MqHandler{
	
	//public static final Logger logger = LoggerFactory.getLogger(ActiveMqService.class);
	
	@Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

	@Override
	public void send(Object destinationName, String msg) {
		jmsMessagingTemplate.convertAndSend((String)destinationName, msg);
	}

	@Override
	public void sendDelay(Object destinationName, final String msg, final long delayTimeSeconds) {
		JmsTemplate jmsTemplate = jmsMessagingTemplate.getJmsTemplate();
		jmsTemplate.send((String) destinationName, new MessageCreator() {
			public Message createMessage(Session session) throws JMSException {
				Message message =  session.createTextMessage(msg);
				//延迟20秒发送消息
				long time = delayTimeSeconds * 1000;
				message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, time);
				return message;
			}
		});
	}

}
