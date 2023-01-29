package uz.ovir.ovir_project.service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uz.ovir.ovir_project.config.MyPasswordEncoder;
import uz.ovir.ovir_project.dto.userDto.UserDto;
import uz.ovir.ovir_project.mapper.UserMapper;
import uz.ovir.ovir_project.response.ResponseDto;
import uz.ovir.ovir_project.util.SecretKeys;
import uz.ovir.ovir_project.entity.User;
import uz.ovir.ovir_project.exceptions.UniversalException;
import uz.ovir.ovir_project.repository.UserRepository;

import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final MyPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public boolean validationToken(String token) {
        return true;
    }

    public String getUsername(String token) {
        try {

            Claims body = Jwts.parser().setSigningKey(SecretKeys.secretWord).parseClaimsJws(token).getBody();
            return body.getSubject();
        } catch (Exception e) {
            throw new UniversalException("Access_token invalid", HttpStatus.FORBIDDEN);
        }
    }
    public ResponseDto<UserDto> getUsernameByResponse(String token) {
        ResponseDto<UserDto>userResponseDto=new ResponseDto<>();
        try {

            Claims body = Jwts.parser().setSigningKey(SecretKeys.secretWord).parseClaimsJws(token).getBody();
            String subject = body.getSubject();
            Optional<User> byEmailOrUserName = userRepository.findByEmailOrUserName(subject);
            if (byEmailOrUserName.isPresent()) {
                userResponseDto.setMessage("hStatistic");
                userResponseDto.setSuccess(true);
                UserDto userDto = userMapper.fromUser(byEmailOrUserName.get());
                userResponseDto.setObj(userDto);
                return userResponseDto;
            }
        } catch (Exception ignored) {

        }
        userResponseDto.setMessage("index");
        userResponseDto.setSuccess(false);
        return userResponseDto;
    }

//    public String createJWTToken(String userName, String password) {
//        Optional<User> byUserName = userRepository.findByUserName(userName);
//        if (byUserName.isPresent()) {
//            User user = byUserName.get();
//            if (!passwordEncoder.passwordEncoder().matches(password, user.getPassword()))
//                throw new UniversalException("Username or password wrong", HttpStatus.NOT_FOUND);
//            return createToken(user);
//        } else throw new UniversalException("Username or password wrong", HttpStatus.NOT_FOUND);
//    }

    public String createToken(User user) {
        String accessToken = "";
        Claims claims = Jwts.claims().setSubject(user.getUsername());

        accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + SecretKeys.accessTokenDate))
                .signWith(SignatureAlgorithm.HS512, SecretKeys.secretWord).compact();

        claims.put("user", user.getRole());
        claims.put("access_token", accessToken);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + SecretKeys.accessTokenDate))
                .signWith(SignatureAlgorithm.HS512, SecretKeys.secretWord).compact();

    }

//    public String createJWTTokenWithUserName(String userName) {
//        Optional<User> byUserName = userRepository.findByUserName(userName);
//        if (byUserName.isPresent()) {
//            User user = byUserName.get();
//            return createToken(user);
//        } else throw new UniversalException("Username or password wrong", HttpStatus.NOT_FOUND);
//    }
}