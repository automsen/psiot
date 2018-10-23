package cn.com.tw.gw.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import cn.com.tw.gw.entity.MailParam;

@Service("emailService")
public class EmailService {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username}")  
	private String from;
	
	public void sendEmail(final MailParam mail){
		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
		simpleMailMessage.setFrom(from); // 发送人,从配置文件中取得
		simpleMailMessage.setTo(mail.getTo()); // 接收人
		simpleMailMessage.setSubject(mail.getSubject());
		simpleMailMessage.setText(mail.getContent());
		mailSender.send(simpleMailMessage);
	}

}
