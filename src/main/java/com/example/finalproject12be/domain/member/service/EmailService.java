package com.example.finalproject12be.domain.member.service;

import java.io.UnsupportedEncodingException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
	// private final JavaMailSender mailSender;
	// private static final String ADMIN_ADDRESS = "ektour0914@naver.com";
	//
	// @Async
	// public void sendMail(EstimateRequest form) throws UnsupportedEncodingException, MessagingException {
	// 	MimeMessage message = mailSender.createMimeMessage();
	// 	message.addRecipients(Message.RecipientType.TO, ADMIN_ADDRESS);
	// 	message.setSubject("[이케이하나관광 견적요청]");
	// 	String text = "";
	// 	text += form.getName() + " " + form.getPhone() + "\n";
	// 	text += form.getTravelType() + " " + form.getVehicleType() + " " + form.getVehicleNumber() + "\n";
	// 	text += form.getDepartPlace() + " ~ " + form.getArrivalPlace() + "\n";
	// 	text += "경유지(" + form.getStopPlace() + ")\n";
	// 	text += form.getDepartDate() + " ~ " + form.getArrivalDate() + "\n";
	// 	message.setText(text, "utf-8");
	// 	message.setFrom(new InternetAddress(ADMIN_ADDRESS, form.getName()));
	// 	mailSender.send(message);
	// }


	private final MailSender mailSender;

	public void sendMail(String newPassword, String email){
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email); //수신자 설정
		message.setSubject("오디약! 비밀번호 변경"); //메일 제목
		message.setText("임시 비밀번호: " + newPassword); //메일 내용 설정
		message.setFrom("kmskes0917@naver.com"); //발신자 설정
		// message.setReplyTo("보낸이@naver.com");
		// System.out.println("message"+message);
		mailSender.send(message);
	}

}
