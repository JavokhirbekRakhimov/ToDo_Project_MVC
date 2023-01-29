package uz.ovir.ovir_project.controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.ovir.ovir_project.config.annotation.CheckRole;
import uz.ovir.ovir_project.dto.document.ChartData;
import uz.ovir.ovir_project.dto.userDto.UserDto;
import uz.ovir.ovir_project.entity.User;
import uz.ovir.ovir_project.entity.enums.RoleEnum;
import uz.ovir.ovir_project.response.ResponseDto;
import uz.ovir.ovir_project.service.JwtService;
import uz.ovir.ovir_project.service.UserService;
import uz.ovir.ovir_project.util.MyBaseUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MyBaseUtil baseUtil;
    private final UserService userService;
    private final JwtService jwtService;
    @GetMapping(path = "/")
    public String homePage(Model model,@CookieValue(value = "user",defaultValue = "") String token){
        ResponseDto<UserDto> responseDto = userService.hasUser(token);
        if (responseDto.getSuccess()) {
            UserDto userDto = baseUtil.userDto();
            List<ChartData> chartData= userService.chartInfoPersonal(userDto.getId());
            model.addAttribute("user",userDto);
            String toJson = new Gson().toJson(chartData);
            model.addAttribute("chartData",toJson);
            System.out.println(chartData);
            return responseDto.getMessage();

        }else {
            User user=new User();
            model.addAttribute("user",user);
            model.addAttribute("message","");
            System.out.println(user);
            return "index";
        }

    }

    @PostMapping(path = "/login")
    public String login(@ModelAttribute(name = "user") User user, HttpServletResponse response,Model model){
      ResponseDto<UserDto>responseDto= userService.getUser(user);
       if(responseDto.getSuccess()){
           String token = jwtService.createToken(user);
           Cookie cookie = new Cookie("user", token);
           cookie.setMaxAge(3600);
           response.addCookie(cookie);

           model.addAttribute("user",responseDto.getObj());
           model.addAttribute("message","");

           return "hStatistic";

       } else {
           return "redirect:/";
       }
    }
    @GetMapping(path = "/login")
    public String home(Model model,@CookieValue(value = "user",defaultValue = "") String token){
        ResponseDto<UserDto> responseDto = userService.hasUser(token);
        if (responseDto.getSuccess()) {
            model.addAttribute("user",responseDto.getObj());
            return responseDto.getMessage();

        }else {
            return "redirect:/statistic";
        }

    }
    @GetMapping("/logOut")
    @CheckRole({RoleEnum.USER,RoleEnum.BOSS,RoleEnum.ADMIN})    
    public String getCookie(HttpServletRequest request, HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("user")) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
                break;
            }
        }

        return "redirect:/";
    }
}
