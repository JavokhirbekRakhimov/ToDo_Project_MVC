package uz.ovir.ovir_project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.ovir.ovir_project.config.annotation.CheckRole;
import uz.ovir.ovir_project.dto.document.DocumentDto;
import uz.ovir.ovir_project.dto.document.DocumentDtoCreat;
import uz.ovir.ovir_project.dto.document.DocumentEdie;
import uz.ovir.ovir_project.dto.document.DocumentEdieIn;
import uz.ovir.ovir_project.dto.userDto.UserDto;
import uz.ovir.ovir_project.dto.userDto.UserDtoDocument;
import uz.ovir.ovir_project.entity.Document;
import uz.ovir.ovir_project.entity.enums.RoleEnum;
import uz.ovir.ovir_project.response.ContentList;
import uz.ovir.ovir_project.response.ResponseDto;
import uz.ovir.ovir_project.service.DocumentService;
import uz.ovir.ovir_project.service.FileService;
import uz.ovir.ovir_project.service.UserService;
import uz.ovir.ovir_project.util.MyBaseUtil;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "/document")
@RequiredArgsConstructor
public class DocumentController {
    private final UserService userService;
    private final FileService fileService;
    private final DocumentService documentService;
    private final MyBaseUtil baseUtil;

    @GetMapping
    @CheckRole({RoleEnum.USER,RoleEnum.BOSS,RoleEnum.ADMIN})
    public String documents(@RequestParam(name = "page" ,defaultValue = "0") Integer page,Model model){
        int firstPage;
        if(page==0) firstPage=1;
                else firstPage = page;
        if (page>0)
            page=page-1;
      ContentList<DocumentDto> contentList= documentService.getDocumentsAll(page);
      model.addAttribute("documents",contentList.getList());
      model.addAttribute("count",contentList.getCount());
      model.addAttribute("page",firstPage);
      model.addAttribute("user",baseUtil.userDto());

        return "hDocumentTable";
    }
    @GetMapping(path = "/personal")
    @CheckRole({RoleEnum.USER,RoleEnum.BOSS,RoleEnum.ADMIN})
    public String documentsPersonal(@RequestParam(name = "page" ,defaultValue = "0") Integer page,Model model){
        int firstPage;
        if(page==0) firstPage=1;
        else firstPage = page;
        if (page>0)
            page=page-1;
        UUID id = baseUtil.userDto().getId();
        ContentList<DocumentDto> contentList= documentService.getDocumentsPersonal(page,id);
        model.addAttribute("documents",contentList.getList());
        model.addAttribute("count",contentList.getCount());
        model.addAttribute("page",firstPage);
        model.addAttribute("user",baseUtil.userDto());

        return "hDocumentTablePersonal";
    }
    @GetMapping(path = "/create")
    @CheckRole({RoleEnum.BOSS,RoleEnum.ADMIN})
    public String documentPage(Model model){
       List<UserDtoDocument>list=userService.getActiveUsersList();
        model.addAttribute("message","");
        model.addAttribute("document",new DocumentDtoCreat());
        model.addAttribute("users",list);
        model.addAttribute("user",baseUtil.userDto());
        return "hAddDocument";
    }

    @GetMapping(path = "/edite/{id}")
    @CheckRole({RoleEnum.USER,RoleEnum.BOSS,RoleEnum.ADMIN})
    public String documentPage(@PathVariable(name = "id") UUID id, Model model){
        List<UserDtoDocument>list=userService.getActiveUsersList();
        model.addAttribute("message","");
       DocumentEdie documentEdie= documentService.getDocumentEditeDto(id);
        model.addAttribute("document",documentEdie);
        model.addAttribute("users",list);
        model.addAttribute("user",baseUtil.userDto());
        model.addAttribute("userIds",documentEdie.getUsers().stream().map(UserDto::getId).collect(Collectors.toList()));

        return "hEditeDocument";
    }

    @PostMapping(path = "/edite/{id}")
    @CheckRole({RoleEnum.USER,RoleEnum.BOSS,RoleEnum.ADMIN})
    public String documentEdite(@PathVariable(name = "id") UUID id, Model model, @ModelAttribute(name ="document" ) DocumentEdieIn documentEdie, MultipartFile[] files) {
        ResponseDto<DocumentEdie> documentEdieResponseDto = documentService.documentEdite(id, documentEdie, files);
        if (documentEdieResponseDto.getSuccess()) {
            return "redirect:/document";
        } else {
            DocumentEdie documentEditeDto = documentService.getDocumentEditeDto(id);
            List<UserDtoDocument> list = userService.getActiveUsersList();
            model.addAttribute("message", documentEdieResponseDto.getMessage());
            model.addAttribute("document", documentEditeDto);
            model.addAttribute("users", list);
            model.addAttribute("user",baseUtil.userDto());
            model.addAttribute("userIds", documentEdieResponseDto.getObj().getUsers().stream().map(UserDto::getId).collect(Collectors.toList()));
            return "hEditeDocument";
        }
    }
    @PostMapping(path = "/create")
    @CheckRole(  {RoleEnum.USER,RoleEnum.BOSS,RoleEnum.ADMIN})
    public String documentCreate(Model model, @ModelAttribute(name = "document") DocumentDtoCreat documentDtoCreat, MultipartFile[] files){
        ResponseDto<Document>responseDto=documentService.createDocument(documentDtoCreat,files);
        List<UserDtoDocument>list=userService.getActiveUsersList();
        model.addAttribute("message",responseDto.getMessage());
        model.addAttribute("document",new DocumentDtoCreat());
        model.addAttribute("user",baseUtil.userDto());
        model.addAttribute("users",list);
        return "hAddDocument";
    }
    @GetMapping(path = "/view/{id}")
    @CheckRole({RoleEnum.USER,RoleEnum.BOSS,RoleEnum.ADMIN})
    public  ResponseEntity<ByteArrayResource> viewFile(@PathVariable(value = "id") UUID id, HttpServletResponse response){
        return   fileService.viewFile2(id);
    }
    @GetMapping(path = "/file/delete/{id}")
    @CheckRole({RoleEnum.USER,RoleEnum.BOSS,RoleEnum.ADMIN})
    public String deleteFile(@PathVariable(name = "id") UUID id, Model model){
       UUID docId= documentService.deleteDocumentFile(id);
        List<UserDtoDocument>list=userService.getActiveUsersList();
        model.addAttribute("message","");
        DocumentEdie documentEdie= documentService.getDocumentEditeDto(docId);
        model.addAttribute("document",documentEdie);
        model.addAttribute("users",list);
        model.addAttribute("userIds",documentEdie.getUsers().stream().map(UserDto::getId).collect(Collectors.toList()));

        return "hEditeDocument";
    }
    @GetMapping(path = "/done/{id}")
    @CheckRole({RoleEnum.USER,RoleEnum.BOSS,RoleEnum.ADMIN})
    public String done(@PathVariable(name = "id") UUID id, Model model){
        documentService.setDone(id);
        return "redirect:/document";
    }
    @GetMapping(path = "/delete/{id}")
    @CheckRole({RoleEnum.USER,RoleEnum.BOSS,RoleEnum.ADMIN})
    public String delete(@PathVariable(name = "id") UUID id){
        documentService.setDelete(id);
        return "redirect:/document";
    }
}
