package com.young.tools.email;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class SendMailTest {

	public static void main(String[] args) throws Exception {
		Properties p = new Properties();
		System.out.println(p);
		System.out.println(SendMailTest.class.getResource("/").getPath()
				+ File.separator + "mail.properties");
		p.load(new FileInputStream(SendMailTest.class.getResource("/")
				.getPath() + File.separator + "mail.properties"));
		MailSender sender = new MailSender(p, true);
		// sender.sendHtmlEmail("测试", "测试", new String[]{"yangy@net-east.com"},
		// new String[]{"yangy@net-east.com"});
		sender.sendHtmlEmailWithAttach("测试", "测试",
				new String[] { "yangyong@jetyun.com" },
				new String[] { "yangyong@jetyun.com" }, new File[]{new File(
						"F:\\外媒数据.xlsx"),new File("F:\\11.txt")});
	}
}
