package uz.ovir.ovir_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.ovir.ovir_project.config.annotation.CheckRole;
import uz.ovir.ovir_project.dto.userDto.UserCreate;
import uz.ovir.ovir_project.dto.userDto.UserDto;
import uz.ovir.ovir_project.dto.userDto.UserUpdate;
import uz.ovir.ovir_project.entity.Role;
import uz.ovir.ovir_project.entity.User;
import uz.ovir.ovir_project.entity.enums.RoleEnum;
import uz.ovir.ovir_project.repository.RoleRepository;
import uz.ovir.ovir_project.response.ContentList;
import uz.ovir.ovir_project.response.ResponseDto;
import uz.ovir.ovir_project.service.FileService;
import uz.ovir.ovir_project.service.UserService;
import uz.ovir.ovir_project.util.MyBaseUtil;
import uz.ovir.ovir_project.util.SecretKeys;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final FileService fileService;
    private final MyBaseUtil baseUtil;
    @GetMapping(path = "/add")
    @CheckRole({RoleEnum.ADMIN})
    public String addUserPage(Model model){
      User user= (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("message","");
        model.addAttribute("user",baseUtil.userDto());
        model.addAttribute("userDto",new UserCreate());
        List<Role> all = roleRepository.findAll();
        model.addAttribute("roles",all);
        return "hRegister";
    }
    @PostMapping(path = "/add")
    @CheckRole({RoleEnum.ADMIN})
    public String addUser(Model model, @ModelAttribute(name = "user") UserCreate create){
       ResponseDto<User> responseDto= userService.addUser(create);
       if(responseDto.getSuccess()){
           return "redirect:/";
       }else {
           model.addAttribute("userDto",create);
           model.addAttribute("user",baseUtil.userDto());
           List<Role> all = roleRepository.findAll();
           model.addAttribute("roles",all);
           model.addAttribute("message",responseDto.getMessage());
           return "hRegister";
       }
    }

    @GetMapping(path = "/table")
    @CheckRole({RoleEnum.ADMIN,RoleEnum.BOSS})
    public String users(@RequestParam(value = "page",defaultValue = "0") Integer page,Model model){
        int firstPage;
        if(page==0) firstPage=1;
        else firstPage = page;
        ContentList<UserDto>contentList=userService.getUserTable(SecretKeys.SIZE,page);
        System.out.println(contentList);
        model.addAttribute("users",contentList.getList());
        model.addAttribute("count",contentList.getCount());
        model.addAttribute("user",baseUtil.userDto());
        model.addAttribute("page",firstPage);
        return "hUserTable";
    }
    @GetMapping(path = "/edite/{id}")
    @CheckRole({RoleEnum.ADMIN,RoleEnum.BOSS})
    public String editeUser(@PathVariable(value = "id") UUID id,Model model){
        ResponseDto<UserUpdate> userById = userService.getUserById(id);
        model.addAttribute("userDto",userById.getObj());
        model.addAttribute("user",baseUtil.userDto());
        List<Role> all = roleRepository.findAll();
        model.addAttribute("roles",all);
        model.addAttribute("message","");
        return "hEditeUser";
    }
    @PostMapping(path = "/update/{id}")
    @CheckRole({RoleEnum.USER,RoleEnum.BOSS,RoleEnum.ADMIN})
    public String updateUser(@PathVariable(value = "id") UUID id, Model model, @ModelAttribute(name = "user") UserUpdate update, MultipartFile file){
        ResponseDto<UserUpdate> userUpdateResponseDto = userService.updateUser(update, id,file);
        model.addAttribute("user",update);
        List<Role> all = roleRepository.findAll();
        model.addAttribute("roles",all);
        if (userUpdateResponseDto.getSuccess()) {
            model.addAttribute("message","Update user");
        }
        return "redirect:/";
        }

        @GetMapping(path ="/avatar/{id}")
        @CheckRole({RoleEnum.USER,RoleEnum.BOSS,RoleEnum.ADMIN})
    public void downloadAvatar(@PathVariable(value = "id") UUID id, HttpServletResponse response){
        fileService.downloadAvatar(id,response);
        }
        @GetMapping(path = "/setting")
        @CheckRole({RoleEnum.USER,RoleEnum.BOSS,RoleEnum.ADMIN})
         public String cabinet(Model model){
            User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            ResponseDto<UserUpdate> userById = userService.getUserById(principal.getId());
            model.addAttribute("userDto",userById.getObj());
            model.addAttribute("user",baseUtil.userDto());
            List<Role> all = roleRepository.findAll();
            model.addAttribute("roles",all);
            model.addAttribute("message","");
         return "hUserSetting";
        }

}
