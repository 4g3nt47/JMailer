package com.umarabdul.jmailer;

import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * JMailer is a simple library for sending out emails over SMTP.
 * @author Umar Abdul
 * @version 1.0
 * Date: 27/Dec/2021
 */

public class JMailer {

  private String smtpHost;
  private int smtpPort;
  private boolean useSSL;
  private int timeout;
  private String senderAddr;
  private String senderPassword;
  private ArrayList<String> recipientAddrs;
  private String subject;
  private String body;
  private File attachFile;
  private String attachFileAs;

  /**
   * JMailer's constructor.
   * @param smtpHost The SMTP server's IP/hostname.
   * @param smtpPort The SMTP server's port.
   */
  public JMailer(String smtpHost, int smtpPort){

    this.smtpHost = smtpHost;
    this.smtpPort = smtpPort;
    useSSL = true; // Use SSL by default.
    timeout = 5000; // Set default timeout to 5 seconds.
    senderAddr = null;
    senderPassword = null;
    recipientAddrs = new ArrayList<String>();
    subject = null;
    body = null;
    attachFile = null;
    attachFileAs = null;
  }


  /**
   * Set the timeout of the network connection.
   * @param timeout Network timeout, in milliseconds.
   */
  public void setTimeout(int timeout){
    this.timeout = timeout;
  }

  /**
   * Set the email address and password of the email sender.
   * @param senderAddr The email address of the sender.
   * @param senderPassword The password of the sender.
   */
  public void setSender(String senderAddr, String senderPassword){

    this.senderAddr = senderAddr;
    this.senderPassword = senderPassword;
  }

  /**
   * Add a recipient address.
   * @param recipientAddr The address of the recipient.
   */
  public void addRecipient(String recipientAddr){

    if (!(recipientAddrs.contains(recipientAddr)))
      recipientAddrs.add(recipientAddr);
  }

  /**
   * Set the subject of the email.
   * @param subject The email subject.
   */
  public void setSubject(String subject){
    this.subject = subject;
  }

  /**
   * Set the body of the email.
   * @param body The email body.
   */
  public void setBody(String body){
    this.body = body;
  }

  /**
   * Define a file to attach to the email.
   * @param attachFile The file to attach.
   * @param attachFileAs The name to attach the file with.
   */
  public void setAttachFile(File attachFile, String attachFileAs){

    this.attachFile = attachFile;
    this.attachFileAs = attachFileAs;
  }

  /**
   * Define the use of SSL. Enabled by default.
   * @param useSSL {@code false} to disable use of SSL.
   */
  public void setSSL(boolean useSSL){
    this.useSSL = useSSL;
  }

  /**
   * Create a {@link Session} object containing the SMTP and sender's config.
   * @return An instance of {@link Session}
   */
  private Session createSession(){

    Properties props = System.getProperties();
    props.put("mail.smtp.host", smtpHost);
    props.put("mail.smtp.port", String.valueOf(smtpPort));
    props.put("mail.smtp.ssl.enable", String.valueOf(useSSL));
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.timeout", String.valueOf(timeout));
    props.put("mail.smtp.connectiontimeout", String.valueOf(timeout));
    Session session = Session.getInstance(props, new javax.mail.Authenticator(){
      protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(senderAddr, senderPassword);
      }
    });
    return session;
  }

  /**
   * Authenticate to the SMTP service using given credentials.
   * This method is not called by the library, and should be used by other program's to check if
   * access to the SMTP service can be obtained.
   * @return {@code true} on successful authentication.
   */
  public boolean authenticate(){

    Session sess = createSession();
    try{
      sess.getTransport("smtp").connect();
      return true;
    }catch(Exception e){
      return false;
    }
  }

  /**
   * Send the email.
   * @return {@code true} if email was sent successfully.
   * @throws JMailerError on any error.
   */
  public boolean send() throws JMailerError{

    // Create the email.
    MimeMessage message = new MimeMessage(createSession());
    // Load addresses.
    try{
      message.setFrom(new InternetAddress(senderAddr));
      for (String addr : recipientAddrs)
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(addr));
    }catch(Exception e){
      throw new JMailerError("Error loading recipients: " + e.getMessage());
    }
    // Add email subject.
    if (subject != null){
      try{
        message.setSubject(subject);
      }catch(MessagingException e){
        throw new JMailerError("Error setting email subject: " + e.getMessage());
      }
    }
    // Create a multipart mail.
    Multipart multipart = new MimeMultipart();
    // Add the email body.
    MimeBodyPart emailBody = null;
    if (body != null){
      try{
        emailBody = new MimeBodyPart();
        emailBody.setText(body);   
        multipart.addBodyPart(emailBody);
      }catch(Exception e){
        throw new JMailerError("Error setting email body: " + e.getMessage());
      }
    }
    // Add email attachment.
    MimeBodyPart attachment = null;
    if (attachFile != null){
      try{
        attachment = new MimeBodyPart();
        attachment.setFileName(attachFileAs);
        attachment.attachFile(attachFile);
        multipart.addBodyPart(attachment);
      }catch(Exception e){
        throw new JMailerError("Error attaching file: " + e.getMessage());
      }
    }
    // Send it.
    try{
      message.setContent(multipart);
      Transport.send(message);      
    }catch(Exception e){
      throw new JMailerError("Error sending email: " + e.getMessage());
    }
    return true;
  }
  
}
