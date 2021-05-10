package aimproject.aim.config;


import aimproject.aim.service.MemberService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private MemberService memberService;

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/templates/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                    .authorizeRequests() // 6
                    .antMatchers("/login", "/signup", "/user","/").permitAll() // 누구나 접근 허용
                    .antMatchers("/").hasRole("USER") // USER, ADMIN만 접근 가능
                    .antMatchers("/admin").hasRole("ADMIN") // ADMIN만 접근 가능
                    .anyRequest().authenticated() // 나머지 요청들은 권한의 종류에 상관 없이 권한이 있어야 접근 가능
                .and()
                    .formLogin()                                // formLogin 인증을 적용
                    .loginPage("/login")                    // 사용자 정의 로그인 페이지
                    .defaultSuccessUrl("/")                     // 로그인 성공 후 이동 페이지
                    //.failureUrl("/login")                       // 로그인 실패 후 이동 페이지
                    .usernameParameter("memberId")              // 아이디 파라미터명 설정
                    .passwordParameter("memberPw")              // 패스워드 파라미터명 설정
                    .loginProcessingUrl("/loginMember")           // 로그인 Form Action Url 설정
                    .successHandler(new AuthenticationSuccessHandler() {
                        @Override
                        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                            System.out.println("authentication : " + authentication.getName());
                            response.sendRedirect("/");//로그인 성공시 메인 페이지로 이동
                        }
                    })
                    .failureHandler(new AuthenticationFailureHandler() {
                        @Override
                        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                            System.out.println("exception : " + exception.getMessage());
                            response.sendRedirect("/login");//실패시 로그인으로 이동
                        }
                    })
                .and()
                    .logout() // 8
                    .logoutSuccessUrl("/") // 로그아웃 성공시 리다이렉트 주소
                    .invalidateHttpSession(true) // 세션 날리기
        ;


    }
}
