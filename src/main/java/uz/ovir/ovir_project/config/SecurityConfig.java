package uz.ovir.ovir_project.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import uz.ovir.ovir_project.service.filter.AuthenticationJwtTokenFilter;

import javax.servlet.Filter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationJwtTokenFilter authenticationJwtTokenFilter;

//    @Autowired
//    private AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        String[] staticResources = {
                "/style/**",
                "/js/**",
                "/vendor/**",};

        http
                .headers().defaultsDisabled().cacheControl();

        http.cors().and().csrf().disable()
//        http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
                .antMatchers("/favicon.ico",
                        "//*.png",
                        "//*.gif",
                        "//*.svg",
                        "//*.jpg",
                        "//*.html",
                        "//*.css",
                        "//*.js",
                        "/swagger-ui/index.html",
                        "/swagger-resources/",
                        "/v2/",
                        "/csrf",
                        "/webjars/",
                       "/resources/**"
                )
                .permitAll()
                .antMatchers("/*").permitAll()
                .antMatchers(staticResources).permitAll()
 //               .antMatchers("/auth/*", "/today/*", "/today").permitAll()
                .anyRequest().authenticated();
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private Filter authenticationJwtTokenFilter() {
        return authenticationJwtTokenFilter;
    }
}
