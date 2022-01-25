# JMailer

JMailer is a simple library for sending emails over SMTP. It uses the Java mail library, and features support for (multiple) email attachments.

## Build

* `$ git clone https://github.com/4g3nt47/JMailer.git`
* `$ cd JMailer`
* `$ ant fat-jar`
* `$ cp jmailer.jar /your/class/path`

## Todo

* [x] Add support for multiple file attachments.

## Simple demo

```java

// Import
import com.umarabdul.jmailer.*;

// Create a JMailer instance to the given SMTP server.
JMailer mailer = new JMailer("smtp.gmail.com", 465);
// Set timeout to 2 seconds.
mailer.setTimeout(2000);
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
// Attach some files.
mailer.setAttachFile(new File("/some/file.pdf"), "Test_file.pdf");
mailer.setAttachFile(new File("/some/file2.pdf"), "Another_test_file.pdf");
// Send the email.
mailer.send();

```
