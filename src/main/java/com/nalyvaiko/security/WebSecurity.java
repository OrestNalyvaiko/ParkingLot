package com.nalyvaiko.security;

import java.time.Clock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  @Autowired
  private UserDetailsService userDetailsService;
  @Autowired
  private JwtRequestFilter jwtRequestFilter;

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth)
      throws Exception {
    auth.userDetailsService(userDetailsService)
        .passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public Clock clock() {
    return Clock.systemDefaultZone();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.csrf().disable().cors().configurationSource(
        request -> new CorsConfiguration().applyPermitDefaultValues()).and()
        .authorizeRequests().antMatchers("/authenticate", "/register")
        .permitAll()
        //for h2 db testing
        .antMatchers("/h2-console/**").permitAll()
        .antMatchers("/admin").hasRole("ADMIN").anyRequest()
        .authenticated().and().
        exceptionHandling()
        .authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    httpSecurity.addFilterBefore(jwtRequestFilter,
        UsernamePasswordAuthenticationFilter.class);
    //for testing h2 db
    httpSecurity.headers().frameOptions().disable();
  }
}
