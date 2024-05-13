package me.staek.itemservice.config;

import me.staek.itemservice.domain.login.LoginService;
import me.staek.itemservice.domain.member.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MemberRepository
 * LoginService
 */
@Configuration
public class MemberConfig {

    @Bean
    public MemberRepository memberRepository() {
        return new MemberRepository();
    }

    @Bean
    public LoginService loginService() {
        return new LoginService(memberRepository());
    }
}
