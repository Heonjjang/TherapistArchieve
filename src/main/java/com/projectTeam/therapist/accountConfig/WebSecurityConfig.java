package com.projectTeam.therapist.accountConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private DataSource dataSource; // 스프링 컨테이너에 의해 자동 주입되며 application.properties에 있는 객체들을 사용할 수 있도록 해주는 dataSource

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers("/", "/auth/**", "/asset/**", "/account/register", "/css/**").permitAll() 	// "/"이나, "/home"같은 URI는 누구나 접근할 수 있다, 또한 css 접근 권한을 저렇게 명시해서 css디렉토리 하위에 대한 권한을 줄 수 있따.
                    .anyRequest().authenticated()			// 그 밖의 어느 요청이 무엇이든, 인증 절차(authenticated)를 걸쳐야 한다.
                    .and()							// and()를 만나면, authorizeRequests가 끝난 것임.
                .formLogin()
                    .loginPage("/account/login")		// 로그인 페이지를 설정해줌. (anyRequest에 해당하는 페이지로 이동하게 되면, 로그인 페이지로 자동으로 리디렉션됨.)
                    .permitAll()			// 로그인하지 않은 사용자에 대해 로그인 페이지에 접근할 수 있도록 permitAll()을 해준다.
                .and()
                    .logout()
                    .permitAll();			// 로그아웃 페이지에 대해서도 로그인을 하지 않아도 접근할 수 있도록 해놓는다.
    }

    // 인증처리를 해주는 메서드
    /* ///// 용어 정리 /////
    * Authentication(인증) : 로그인에 대한 처리를 하는 개념
    * Authorization(권한) : 로그인 한 이후에 권한에 대한 처리를 하는 개념 */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(passwordEncoder()) //아래 Bean객체로 지정되어 있는 PasswordEncoder 인스턴스를 호출하여 인증 처리시 알아서 암호화를 진행함.
                .usersByUsernameQuery("select user_name, user_password, user_enabled "
                        + "from user_dto "
                        + "where user_name = ?")
                .authoritiesByUsernameQuery("select u.user_name, r.role_name "
                        + "from user_role_dto ur "
                        + "inner join user_dto u on ur.user_id = u.user_id "
                        + "inner join role_dto r on ur.role_id = r.role_id "
                        + "where u.user_name = ?");
    }

    // 사용자 암호를 안전하게 암호화 할 수 있는 기능을 제공
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
