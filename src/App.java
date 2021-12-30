import com.umarabdul.jmailer.*;

public class App{

    public static void main(String[] args) throws JMailerError{

        JMailer mailer = new JMailer("mail.google.com", 465);
        mailer.send();
    }
}