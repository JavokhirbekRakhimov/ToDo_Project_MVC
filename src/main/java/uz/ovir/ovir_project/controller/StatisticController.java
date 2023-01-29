package uz.ovir.ovir_project.controller;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import uz.ovir.ovir_project.config.annotation.CheckRole;
import uz.ovir.ovir_project.dto.document.ChartData;
import uz.ovir.ovir_project.dto.userDto.UserDto;
import uz.ovir.ovir_project.entity.enums.RoleEnum;
import uz.ovir.ovir_project.repository.UserRepository;
import uz.ovir.ovir_project.service.UserService;
import uz.ovir.ovir_project.util.MyBaseUtil;
import java.util.List;
@Controller
@RequestMapping(path = "/statistic")
@RequiredArgsConstructor
public class StatisticController {
    private final UserRepository userRepository;
    private final MyBaseUtil baseUtil;
    private final UserService userService;
    @GetMapping
    @CheckRole({RoleEnum.USER,RoleEnum.BOSS,RoleEnum.ADMIN})
    public String statistic(Model model){
        UserDto userDto = baseUtil.userDto();
       List<ChartData>chartData= userService.chartInfoPersonal(userDto.getId());
        model.addAttribute("user",userDto);
        String toJson = new Gson().toJson(chartData);
        model.addAttribute("chartData",toJson);
        System.out.println(chartData);
        return "hStatistic";
    }
    @GetMapping(path = "/all")
    @CheckRole({RoleEnum.USER,RoleEnum.BOSS,RoleEnum.ADMIN})
    public String statisticAll(Model model){
        UserDto userDto = baseUtil.userDto();
        List<ChartData>chartData= userService.chartInfoPersonalAll();
        model.addAttribute("user",userDto);
        String toJson = new Gson().toJson(chartData);
        model.addAttribute("chartData",toJson);
        System.out.println(chartData);
        return "hStatisticAll";
    }

}
