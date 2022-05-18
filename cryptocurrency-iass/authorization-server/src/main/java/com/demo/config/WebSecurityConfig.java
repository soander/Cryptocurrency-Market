package com.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @ClassName WebSecurityConfig
 * @Description Configuration about web security
 * @Author Yaozheng Wang
 * @Date 2022/5/18 11:39
 **/
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
    * @Author Yaozheng Wang
    * @Description The authentication manager
    * @Date 2022/5/18 11:43
    * @Param null
    * @Return * @return null
    **/
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
    * @Author Yaozheng Wang
    * @Description The configuration
    * @Date 2022/5/18 11:44
    * @Param Http
    * @Return * @return null
    **/
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(); // close csrf
        http.authorizeRequests().anyRequest().authenticated();
    }

//     /**
//     * @Author Yaozheng Wang
//     * @Description Test user detail server
//     * @Date 2022/5/18 11:42
//     * @Param null
//     * @Return * @return null
//     **/
//    @Bean
//    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
//        User user = new User("admin", "123456", Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"))) ;
//        inMemoryUserDetailsManager.createUser(user);
//        return inMemoryUserDetailsManager;
//    }

    /**
    * @Author Yaozheng Wang
    * @Description PASSWORD encoder
    * @Date 2022/5/18 11:42
    * @Param null
    * @Return * @return encode password
    **/
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
