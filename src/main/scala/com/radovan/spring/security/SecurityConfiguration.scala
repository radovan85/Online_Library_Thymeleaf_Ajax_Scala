package com.radovan.spring.security

import com.radovan.spring.security.handler.LoginSuccessHandler
import com.radovan.spring.service.impl.UserDetailsImpl
import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.{HttpSecurity, WebSecurity}
import org.springframework.security.config.annotation.web.configuration.{EnableWebSecurity, WebSecurityConfigurerAdapter}
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @throws[Exception]
  override def configure(auth: AuthenticationManagerBuilder): Unit = {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder)
  }

  @throws[Exception]
  override protected def configure(http: HttpSecurity): Unit = {
    http.formLogin.loginPage("/login").successHandler(new LoginSuccessHandler).loginProcessingUrl("/login").usernameParameter("email").passwordParameter("password").permitAll
    http.logout.permitAll.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout").and.csrf.disable
    http.authorizeRequests.antMatchers("/login").anonymous.antMatchers("/loginErrorPage", "/books/**", "/aboutUs", "/suspensionPage").permitAll.antMatchers("/", "/home", "/registerComplete", "/registerFail", "/loginPassConfirm").permitAll.antMatchers("/get*").hasAnyAuthority("ADMIN", "ROLE_USER").antMatchers("/admin/**").hasAuthority("ADMIN").antMatchers("/cart/**", "/order/**", "/reviews/**").hasAuthority("ROLE_USER").antMatchers("/userRegistration").anonymous.antMatchers("/register").anonymous.anyRequest.authenticated
  }

  @Bean
  override def userDetailsService = new UserDetailsImpl

  @Bean
  def passwordEncoder = new BCryptPasswordEncoder

  @throws[Exception]
  override def configure(web: WebSecurity): Unit = {
    web.ignoring.antMatchers("/resources/**", "/static/**", "/images/**", "/bootstrap/**", "/css/**", "/js/**", "/countdown/**", "/fonts/**")
  }
}

