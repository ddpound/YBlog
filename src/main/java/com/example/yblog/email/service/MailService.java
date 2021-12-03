package com.example.yblog.email.service;


import com.example.yblog.allstatic.AllStaticElement;
import com.example.yblog.authkey.StaticAuthKey;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import javax.mail.internet.MimeMessage;
import java.util.Random;


@AllArgsConstructor
@RequiredArgsConstructor // 이게 멤버변수와 관련있는거같은데 뭐지
@Service
public class MailService {


    @Autowired
    private JavaMailSender mailSender;

    public static  String authKey;
    private int size;

    public String getKey(int size) {
        this.size = size;
        return getAuthCode() ;
    }

    public String getAuthCode() {
        Random random = new Random();
        StringBuffer buffer = new StringBuffer();
        int num = 0;

        while (buffer.length() < size) {
            num = random.nextInt(10);
            buffer.append(num);
        }

        return buffer.toString();
    }

    public void mailSend(String mail){
        // Token 인증 허가 방법 변경 요망
        String authkey = getKey(6);
        StaticAuthKey.authKey = authkey; // 스태틱에 담아놓은 어스키 초기화와 동시에 같이 입력 인증수단임


        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                final MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true,"UTF-8");
                message.setTo(mail);
                message.setSubject("YBlogLab 인증메일입니다");
                String mailContent = new StringBuffer().append("<h1>[이메일 인증]</h1>").append("<p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p>")
                        .append("<a href= '"+ AllStaticElement.StaticURL +"auth/signUpConfirm?email=").append(mail)
                        .append("&authKey=").append(authkey).append("'target='_blenk'>이메일 인증 확인</a>").toString();

                message.setText(mailContent,true );
            }
        };

        try{
            mailSender.send(preparator);
        }catch (MailException e){
            System.out.println("이메일 에러발생");
        }
    }

}
