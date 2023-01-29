package uz.ovir.ovir_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.ovir.ovir_project.dto.document.DocumentDto;
import uz.ovir.ovir_project.dto.document.DocumentDtoCreat;
import uz.ovir.ovir_project.dto.document.DocumentEdie;
import uz.ovir.ovir_project.dto.document.DocumentEdieIn;
import uz.ovir.ovir_project.entity.Document;
import uz.ovir.ovir_project.entity.MyFile;
import uz.ovir.ovir_project.entity.User;
import uz.ovir.ovir_project.entity.enums.DocumentStatus;
import uz.ovir.ovir_project.exceptions.UniversalException;
import uz.ovir.ovir_project.mapper.DocumentMapper;
import uz.ovir.ovir_project.repository.DocumentRepository;
import uz.ovir.ovir_project.repository.UserRepository;
import uz.ovir.ovir_project.response.ContentList;
import uz.ovir.ovir_project.response.ResponseDto;
import uz.ovir.ovir_project.util.MyBaseUtil;
import uz.ovir.ovir_project.util.SecretKeys;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DocumentService {
    private final FileService fileService;
    private final UserRepository userRepository;
    private final MyBaseUtil baseUtil;
    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;

    public ResponseDto<Document> createDocument(DocumentDtoCreat documentDtoCreat, MultipartFile[] files) {
        ResponseDto<Document>responseDto=new ResponseDto<>();
        Document document = new Document();
        document.setDescription(documentDtoCreat.getDescription());
        document.setStartDate(baseUtil.getDate(documentDtoCreat.getStartDateStr()));
        document.setEndDate(baseUtil.getDate(documentDtoCreat.getEndDateStr()));
        if(files.length>0) {
            List<MyFile> fileSet = fileService.saveDocumentFiles(files);
            document.setFiles(fileSet);
        }
        if(documentDtoCreat.getUserIds().size()>0) {
         List<User>userList= userRepository.getUsersIds(documentDtoCreat.getUserIds());
         document.setUsers(userList);
        }
        if(!documentDtoCreat.getLocal()) {
            document.setLocal(false);
        }
        Long order=documentRepository.getMaxOrder();
        if (order==null){
            document.setOrderNumber(1L);
        }else
            document.setOrderNumber(order+1);
        Document save = documentRepository.save(document);
       responseDto.setSuccess(true);
       responseDto.setObj(save);
       responseDto.setMessage("Add document");
       return responseDto;
    }

    public ContentList<DocumentDto> getDocumentsAll(Integer page) {
        ContentList<DocumentDto>contentList=new ContentList<>();
        Integer allDocumentsCount = documentRepository.getAllDocumentsCount(Set.of(DocumentStatus.DELETE.name()));
         int div=allDocumentsCount/Integer.parseInt(SecretKeys.SIZE);
         int mult=div*Integer.parseInt(SecretKeys.SIZE);
         if(div>0 && div==mult){
             contentList.setCount(div);
         }else contentList.setCount(div+1);
        List<Document>documentList= documentRepository.getAllDocuments(Set.of(DocumentStatus.DELETE.name()), Integer.parseInt(SecretKeys.SIZE),page);
        List<DocumentDto> documentDtos = documentMapper.fromDocumentList(documentList);
        contentList.setList(documentDtos);
        return contentList;
    }

    public DocumentEdie getDocumentEditeDto(UUID id) {
        Optional<Document> byId = documentRepository.findById(id);
        if (byId.isPresent()) {
            Document document = byId.get();
            DocumentEdie documentEdie = documentMapper.toDocumentEdite(document);
            documentEdie.setStartDateStr(baseUtil.dateToString(document.getStartDate()));
            documentEdie.setEndDateStr(baseUtil.dateToString(document.getEndDate()));
            if(document.getLocal()){
                documentEdie.setLocal(true);
                documentEdie.setNotLocal(false);

            }
            else{
                documentEdie.setNotLocal(true);
                documentEdie.setLocal(false);
            }
            List<UUID> collect = document.getUsers().stream().map(User::getId).collect(Collectors.toList());
            documentEdie.setUserIds(collect);
            return documentEdie;
        } else throw new UniversalException("Document not found", HttpStatus.NOT_FOUND);
    }

    public UUID deleteDocumentFile(UUID id) {
        UUID idDoc=documentRepository.getIdDocumentByFileID(id).getId();
        documentRepository.deleteDocumentFiles(id);
        documentRepository.deleteFiles(id);
        return idDoc;
    }

    public ResponseDto<DocumentEdie> documentEdite(UUID id, DocumentEdieIn documentEdie, MultipartFile[] files) {
        ResponseDto<DocumentEdie>responseDto=new ResponseDto<>();
        Optional<Document> byId = documentRepository.findById(id);
        if (byId.isPresent()) {
            Document document = byId.get();
            document.setDescription(documentEdie.getDescription());
            document.setStartDate(baseUtil.getDate(documentEdie.getStartDateStr()));
            document.setEndDate(baseUtil.getDate(documentEdie.getEndDateStr()));
            if (documentEdie.getLocal()){
                document.setLocal(true);
            }else {
                document.setLocal(false);
            }
            List<User> usersIds = userRepository.getUsersIds(documentEdie.getUserIds());
            List<MyFile> myFiles = fileService.saveDocumentFiles(files);
            List<MyFile> myFileList = document.getFiles();
            myFileList.addAll(myFiles);
            document.setFiles(myFileList);
            document.setUsers(usersIds);
           documentRepository.save(document);
            DocumentEdie documentEditeDto = getDocumentEditeDto(id);
            responseDto.setSuccess(true);
            responseDto.setObj(documentEditeDto);
            return responseDto;
        } else {
            responseDto.setMessage("Document not found");
            return responseDto;
        }
    }

    public void setDone(UUID id) {
        Optional<Document> byId = documentRepository.findById(id);
        if (byId.isPresent()) {
            Document document = byId.get();
            Boolean aBoolean = baseUtil.doneDocumentPosition(document);
            if (aBoolean)
            documentRepository.setStatus(DocumentStatus.BAJARILGAN.name(),id, LocalDate.now());
            else
                documentRepository.setStatus(DocumentStatus.KECHIKTRIB_BAJARILGAN.name(),id, LocalDate.now());

        }
    }

    public void setDelete(UUID id) {
        Optional<Document> byId = documentRepository.findById(id);
        if (byId.isPresent()) {
                documentRepository.setStatusDelete(DocumentStatus.DELETE.name(),id);
        }
    }

    public ContentList<DocumentDto> getDocumentsPersonal(Integer page, UUID id) {
        ContentList<DocumentDto>contentList=new ContentList<>();
        Integer allDocumentsCount = documentRepository.getAllDocumentsCountPersonal(Set.of(DocumentStatus.DELETE.name()),id);
        int div=allDocumentsCount/Integer.parseInt(SecretKeys.SIZE);
        int mult=div*Integer.parseInt(SecretKeys.SIZE);
        if(div>0 && div==mult){
            contentList.setCount(div);
        }else contentList.setCount(div+1);
        List<Document>documentList= documentRepository.getAllDocumentsPersonal(Set.of(DocumentStatus.DELETE.name()), Integer.parseInt(SecretKeys.SIZE),page,id);
        List<DocumentDto> documentDtos = documentMapper.fromDocumentList(documentList);
        contentList.setList(documentDtos);
        return contentList;
    }
}
