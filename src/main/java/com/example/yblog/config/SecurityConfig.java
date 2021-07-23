package com.example.yblog.config;

import com.example.yblog.config.auth.PrincipalDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// 빈등록 : 스프링컨테이너가 객체를 관리할수있게 올려놓는다
@Configuration
@EnableWebSecurity //  필터를 거는것 (스프링 구조 보면 필터의 그부분을 더 추가하는 개념인거같다)
//스프링 시큐리티가 활성화 되어있지만 설정을 지금 클래스에서 하겠다 라는 의미
@EnableGlobalMethodSecurity(prePostEnabled = true) //특정 주소로 접근을 시도하면 권한 및 인증을 미리체크
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalDetailService principalDetailService;

    @Bean
    public BCryptPasswordEncoder encodePWD(){
        return new BCryptPasswordEncoder();
    }

    // 시큐리티 대신 로그인주는데 password를 가로채기를 하는데
    // 해당패스워드가 뭘로 해쉬가 되어 회원가입이 됐는지 알아야
    // 같은 해쉬로 암호화해서 DB에있는 해쉬랑 비교할수 있다


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                    .antMatchers("/","/auth/**" , "/js/**" , "/index","/css/**","/img/**") // 다음 요청에 관한 값들은
                    //.mvcMatchers(HttpMethod.POST, "/auth/**", "/" , "/index")
                    .permitAll() // permitAll 어떤 권한자가 들어와도 접속하게 해주며
                    .anyRequest() // 그왜 모든 리퀘스트들 은
                    .authenticated() // 인증을 받아내야한다
                .and()
                    .formLogin()
                    .loginPage("/auth/loginForm")
                    .loginProcessingUrl("/auth/login/try") //스프링 시큐리티가 해주는 해당주소로 요청오는 로그인을 가로챔
                    .defaultSuccessUrl("/");
    }
}
