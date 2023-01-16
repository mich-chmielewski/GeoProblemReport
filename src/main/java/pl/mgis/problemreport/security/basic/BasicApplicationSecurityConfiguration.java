package pl.mgis.problemreport.security.basic;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

import static pl.mgis.problemreport.model.UserRole.ROLE_ADMIN;
import static pl.mgis.problemreport.model.UserRole.ROLE_USER;

@Configuration
@EnableWebSecurity
public class BasicApplicationSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final DataSource dataSource;

    public BasicApplicationSecurityConfiguration(PasswordEncoder passwordEncoder, DataSource dataSource) {
        this.passwordEncoder = passwordEncoder;
        this.dataSource = dataSource;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username,password,enabled from users where username = ?")
                .authoritiesByUsernameQuery("select username,user_role as authority from users where username = ?")
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                //  .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                //  .and()
                .authorizeRequests()
                .antMatchers("/*", "/unregister/*", "/css/**", "/js/*", "/map/**", "/data/**", "/files/**").permitAll()
                .antMatchers("/manager/", "/manager/dashboard", "/manager/report", "/manager/js/**", "/manager/css/**").hasAnyRole(ROLE_USER.asRole(), ROLE_ADMIN.asRole())
                .antMatchers(HttpMethod.GET, "/manager/api/status/list").hasAnyRole(ROLE_USER.asRole(), ROLE_ADMIN.asRole())
                .antMatchers("/manager/api/report/**","/manager/api/comment/**").hasAnyRole(ROLE_USER.asRole(), ROLE_ADMIN.asRole())
                .antMatchers("/manager/**").hasAnyRole(ROLE_ADMIN.asRole())
                .antMatchers("/swagger-ui/**").hasAnyRole(ROLE_ADMIN.asRole())
                .antMatchers("/authorize").permitAll()
                .antMatchers("/").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/manager/", false)
                .and()
                .rememberMe().tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(1))
                .and()
                .logout().logoutUrl("/logout")
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me", "XSRF-TOKEN")
                .logoutSuccessUrl("/login")
                .and()
                .headers().frameOptions().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/manager/api/report/geojson/**","/swagger-resources/**", "/configuration/**", "/swagger-ui.html");
    }
}
