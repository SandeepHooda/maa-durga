package email;




// [START simple_includes]

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
// [END simple_includes]

// [START multipart_includes]
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
// [END multipart_includes]



public class MailService {
	
	public void sendSimpleMail(String toAddress,  String from, String subject, String body) {
	    
	    Properties props = new Properties();
	    Session session = Session.getDefaultInstance(props, null);

	    try {
	      Message msg = new MimeMessage(session);
	      msg.setFrom(new InternetAddress("kusum.hooda@gmail.com", from));
	      msg.addRecipient(Message.RecipientType.TO,
	                       new InternetAddress(toAddress, ""));
	      msg.setSubject(subject);
	      msg.setText(body);
	      Transport.send(msg);
	    } catch (AddressException e) {
	      // ...
	    } catch (MessagingException e) {
	      // ...
	    } catch (UnsupportedEncodingException e) {
	      // ...
	    }
	    // [END simple_example]
	  }

	  public void sendMultipartMail(String toAddress, String ccAddress,  String from, byte[] attachmentData, String subject, String body) {
	    Properties props = new Properties();
	    Session session = Session.getDefaultInstance(props, null);

	    String msgBody = "...";

	    try {
	      Message msg = new MimeMessage(session);
	      msg.setFrom(new InternetAddress("kusum.hooda@gmail.com", from));
	      msg.addRecipient(Message.RecipientType.TO,
	                       new InternetAddress(toAddress, ""));
	      if (null != ccAddress){
	    	  msg.addRecipient(Message.RecipientType.CC,
	                  new InternetAddress(ccAddress, ""));
	      }
	     
	      msg.setSubject(subject);
	      msg.setText(msgBody);

	      // [START multipart_example]
	      String htmlBody = body;          // ...
	     
	      Multipart mp = new MimeMultipart();

	      MimeBodyPart htmlPart = new MimeBodyPart();
	      htmlPart.setContent(htmlBody, "text/html");
	      mp.addBodyPart(htmlPart);

	      if (null != attachmentData){
	    	  MimeBodyPart attachment = new MimeBodyPart();
		      InputStream attachmentDataStream = new ByteArrayInputStream(attachmentData);
		      attachment.setFileName("sales.pdf");
		      attachment.setContent(attachmentDataStream, "application/pdf");
		      mp.addBodyPart(attachment);
	      }
	     

	      msg.setContent(mp);
	      // [END multipart_example]

	      Transport.send(msg);

	    } catch (AddressException e) {
	      // ...
	    } catch (MessagingException e) {
	      // ...
	    } catch (UnsupportedEncodingException e) {
	      // ...
	    }
	  }
	

}
