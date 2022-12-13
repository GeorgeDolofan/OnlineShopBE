package OnlineShopProject.models;

import OnlineShopProject.repos.UserRepository;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;

@Service
public class UserServices {

    @Autowired
    private UserRepository repo;


    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(16);


    @Autowired
    private JavaMailSender mailSender;


    public void register(User user, String siteURL)
            throws UnsupportedEncodingException, MessagingException {

        List<User> userResponse = new ArrayList<>();
        List<User> emailResponse = new ArrayList<>();

        userResponse = repo.findByusername(user.username);
        emailResponse = repo.findByemailAddress((user.emailAddress));

        if(userResponse.isEmpty()){
            if(emailResponse.isEmpty()){
                String encodedPassword = passwordEncoder.encode(user.getPassword());
                user.setPassword(encodedPassword);
                RandomStringGenerator generator = new RandomStringGenerator
                        .Builder()
                        .withinRange('0', 'z')
                        .build();

                String randomCode = generator.generate(64);
                user.setVerificationCode(randomCode);
                user.setEnabled(false);

                repo.save(user);

                sendVerificationEmail(user, siteURL);
            }else throw new IllegalStateException("Email already used");
        }else throw new IllegalStateException("Username already used");
    }

    private void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {

        String toAddress = user.getEmailAddress();
        String fromAddress = "dolofanxxgeoo@gmail.com";
        String senderName = "OnlineShop";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Your company name.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getUsername());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);

    }

}