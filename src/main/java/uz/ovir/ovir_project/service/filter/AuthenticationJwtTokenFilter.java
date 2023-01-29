package uz.ovir.ovir_project.service.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import uz.ovir.ovir_project.service.AuthService;
import uz.ovir.ovir_project.service.JwtService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class AuthenticationJwtTokenFilter extends OncePerRequestFilter {
    private final AuthService authService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable FilterChain filterChain) throws ServletException, IOException {
        assert request != null;
        Cookie[] cookies = request.getCookies();
        if(cookies!=null) {
            List<Cookie> user = Arrays.stream(cookies).filter(cookie -> cookie.getName().equals("user")).collect(Collectors.toList());
            if (!user.isEmpty() && user.get(0).getName().equals("user")) {
                String token = user.get(0).getValue();
                if (jwtService.validationToken(token)) {
                    String username = jwtService.getUsername(token);
                    UserDetails userDetails = authService.loadUserByUsername(username);
                    if (!Objects.isNull(userDetails.getUsername())) {
                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            }
        }
        assert filterChain != null;
        filterChain.doFilter(request, response);
    }
}
