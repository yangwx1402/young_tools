package com.young.tools.email;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang3.StringUtils;

public class MailSender {

	private String mailHost = "";
	private String sender_username = "";
	private String sender_password = "";

	private Properties config = new Properties();

	private boolean debug;

	public MailSender(Properties config, boolean debug) throws Exception {
		if (config == null) {
			throw new Exception("Properties is null!");
		}
		if (StringUtils.isBlank(config.getProperty("mail.smtp.host"))
				|| StringUtils.isBlank(config
						.getProperty("mail.sender.username"))
				|| StringUtils.isBlank(config
						.getProperty("mail.sender.password"))) {
			throw new Exception("host or username or password is null");
		}

		this.mailHost = config.getProperty("mail.smtp.host");
		this.sender_username = config.getProperty("mail.sender.username");
		this.sender_password = config.getProperty("mail.sender.password");
		this.config = config;
		this.debug = debug;
	}

	/**
	 * Session类代表JavaMail中的一个邮件会话。
	 * 每一个基于JavaMail的应用程序至少有一个Session（可以有任意多的Session）。
	 * JavaMail需要Properties来创建一个session对象。 寻找"mail.smtp.host" 属性值就是发送邮件的主机
	 * 寻找"mail.smtp.auth" 身份验证，目前免费邮件服务器都需要这一项
	 * 
	 * @return
	 */

	private Session openSession() {
		Session session = null;
		session = Session.getInstance(config);
		session.setDebug(debug);
		return session;
	}

	/**
	 * Message对象将存储我们实际发送的电子邮件信息，
	 * Message对象被作为一个MimeMessage对象来创建并且需要知道应当选择哪一个JavaMail session。
	 * 
	 * @return
	 * @throws Exception
	 */
	private MimeMessage createMessage(Session session, String subject,
			String sendHtml, String[] toUsers, String[] ccUsers)
			throws Exception {
		MimeMessage message = new MimeMessage(session);
		// 设置发送人
		InternetAddress from = new InternetAddress(sender_username);
		message.setFrom(from);
		// 收件人
		InternetAddress[] to = new InternetAddress[toUsers.length];
		for (int i = 0; i < toUsers.length; i++) {
			to[i] = new InternetAddress(toUsers[i]);
		}
		message.setRecipients(Message.RecipientType.TO, to);
		if (ccUsers != null) {
			InternetAddress[] cc = new InternetAddress[ccUsers.length];
			for (int i = 0; i < ccUsers.length; i++) {
				cc[i] = new InternetAddress(ccUsers[i]);
			}
			message.setRecipients(Message.RecipientType.CC, cc);
		}
		// 邮件主题
		message.setSubject(subject);
		return message;
	}

	/**
	 * 邮件是既可以被发送也可以被受到。JavaMail使用了两个不同的类来完成这两个功能：Transport 和 Store。 Transport
	 * 是用来发送信息的，而Store用来收信。对于这的教程我们只需要用到Transport对象。
	 * 
	 * @return
	 * @throws MessagingException
	 */
	private Transport getTransport(Session session, Message message)
			throws MessagingException {
		Transport transport = null;
		transport = session.getTransport("smtp");
		// smtp验证，就是你用来发邮件的邮箱用户名密码
		transport.connect(mailHost, sender_username, sender_password);
		return transport;
	}
	
	private void closeTransport(Transport transport) throws MessagingException{
		if (transport != null) {
			transport.close();
		}
	}

	/**
	 * 
	 * @param subject
	 *            主题
	 * @param sendHtml
	 *            html内容
	 * @param toUsers
	 *            toUsers
	 * @param ccUsers
	 *            ccUsers
	 * @throws Exception
	 */
	public void sendHtmlEmail(String subject, String sendHtml,
			String[] toUsers, String[] ccUsers) throws Exception {
		if (toUsers == null || toUsers.length == 0) {
			throw new Exception("toUsers is bank");
		}
		Session session = openSession();
		Message message = createMessage(session, subject, sendHtml, toUsers,
				ccUsers);
		Transport transport = getTransport(session, message);
		String content = sendHtml.toString();
		// 邮件内容,也可以使纯文本"text/plain"
		message.setContent(content, "text/html;charset=UTF-8");
		// 保存邮件
		message.saveChanges();
		// send
		transport.sendMessage(message, message.getAllRecipients());
		closeTransport(transport);
	}

	/**
	 * @param subject
	 * @param sendHtml
	 * @param toUsers
	 * @param ccUsers
	 * @param attachment
	 *            附件
	 * @throws Exception
	 */
	public void sendHtmlEmailWithAttach(String subject, String sendHtml,
			String[] toUsers, String[] ccUsers, File[] attachments)
			throws Exception {
		if (toUsers == null || toUsers.length == 0) {
			throw new Exception("toUsers is bank");
		}
		Session session = openSession();
		MimeMessage message = createMessage(session, subject, sendHtml,
				toUsers, ccUsers);
		// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
		MimeMultipart multipart = new MimeMultipart();
		multipart.setSubType("mixed");
		// 添加邮件正文
		BodyPart contentPart = new MimeBodyPart();
		contentPart.setContent(sendHtml, "text/html;charset=UTF-8");
		multipart.addBodyPart(contentPart);
		// 添加附件的内容
		if (attachments != null) {
			for (File attachment : attachments) {
				BodyPart attachmentBodyPart = new MimeBodyPart();
				DataSource source = new FileDataSource(attachment);
				attachmentBodyPart.setDataHandler(new DataHandler(source));
				attachmentBodyPart.setFileName(MimeUtility
						.encodeWord(attachment.getName()));
				multipart.addBodyPart(attachmentBodyPart);
			}
		}
		message.setContent(multipart);
		// 保存邮件
		message.saveChanges();
		Transport transport = getTransport(session, message);
		// 发送
		transport.sendMessage(message, message.getAllRecipients());
		closeTransport(transport);
	}
}
