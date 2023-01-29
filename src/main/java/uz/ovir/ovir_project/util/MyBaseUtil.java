package uz.ovir.ovir_project.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.ovir.ovir_project.dto.userDto.UserDto;
import uz.ovir.ovir_project.entity.Document;
import uz.ovir.ovir_project.entity.User;
import uz.ovir.ovir_project.mapper.UserMapper;

import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class MyBaseUtil {
    private final UserMapper userMapper;
    public LocalDate getDate(String date){
        try {

            String[] split = date.split("/");
            return LocalDate.of(Integer.parseInt(split[2]),Integer.parseInt(split[1]),Integer.parseInt(split[0]));
        } catch (Exception e){
            return LocalDate.now();
        }
    }
    public UserDto userDto(){
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       return userMapper.fromUser(principal);
    }
    public String dateToString(LocalDate localDate){
        return localDate.getDayOfMonth()+"/"+localDate.getMonthValue()+"/"+localDate.getYear();
    }
    public Boolean doneDocumentPosition(Document document){
        int days = Period.between(LocalDate.now(), document.getEndDate()).getDays();
        return days > 0;
    }

}
