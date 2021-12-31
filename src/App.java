import java.io.*;
import com.umarabdul.jmailer.*;

public class App{
  
  public static void main(String[] args) throws JMailerError{
    

    // Create a JMailer instance to the given SMTP server.
    JMailer mailer = new JMailer("smtp.gmail.com", 465);
    // Define sender's email and password.
    mailer.setSender("someuser@somesite.testing", "somestrongpassword");
    if (!(mailer.authenticate())) // Invalid email or password?
      throw new JMailerError("Authentication failed!");
    // Add recipients (more than one is supported).
    mailer.addRecipient("rcpt1@somesite.testing");
    mailer.addRecipient("rcpt2@somesite.testing");
    // Set the subject of the email.
    mailer.setSubject("This is a test email");
    // Set the email body.
    mailer.setBody("Hello fellas, this is just a test email, so please ignore :)");
    // Attach a file.
    mailer.setAttachFile(new File("/some/file.pdf"), "Test_file.pdf");
    // Send the email.
    mailer.send();
  }
}