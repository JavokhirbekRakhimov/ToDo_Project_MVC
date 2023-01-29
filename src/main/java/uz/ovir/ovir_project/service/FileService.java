package uz.ovir.ovir_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import uz.ovir.ovir_project.entity.MyFile;
import uz.ovir.ovir_project.exceptions.UniversalException;
import uz.ovir.ovir_project.repository.FileRepository;
import uz.ovir.ovir_project.util.SecretKeys;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileService {
    private final FileRepository fileRepository;

    public MyFile upload(String pathLogo, MultipartFile multipartFile) {
        if (!multipartFile.isEmpty() && multipartFile.getSize() > 0) {
            try {
                String originalFilename = multipartFile.getOriginalFilename();
                long size = multipartFile.getSize();
                File filecheck = new File(pathLogo);
                if (!filecheck.isDirectory()) {
                    filecheck.mkdirs();
                }

                String generatedName = UUID.randomUUID() + originalFilename;
                String url = pathLogo + "\\" + generatedName;

                File fileSAve = new File(url);
                FileOutputStream outputStream = new FileOutputStream(fileSAve);
                outputStream.write(multipartFile.getBytes());
                outputStream.close();
                outputStream.flush();
                MyFile myFile = new MyFile();
                myFile.setFilePath(url);
                String contentType = multipartFile.getContentType();
                myFile.setContentType(contentType);
                System.out.println(contentType);
                myFile.setSize(size);
                myFile.setOriginalName(originalFilename);
                myFile.setGeneretedName(generatedName);
                return fileRepository.save(myFile);
            } catch (IOException e) {
                return null;
            }
        } else return null;
    }

//    public HttpEntity<?> saveDuplicateFiles(Long orgId,MultipartHttpServletRequest request) throws IOException {
//        String folder = "";
//        if (orgId != null) {
//            folder = DIRECTORY_TO_STORE_FILE_ON_SERVER + "/" + orgId;
//        } else {
//            folder = PATH_AVATAR;
//        }
//        File file2=new File(folder);
//        if (!file2.isDirectory()){
//            file2.mkdirs();
//        }
//        String fileN = "file";
//        String fishka = "fishka";
//        String avatar = "avatar";
//        String logo = "logo";
//        MultipartFile mF = request.getFile(fileN);
//        if (mF == null) {
//            mF = request.getFile(fishka);
//        }
//        if (mF == null) {
//            mF = request.getFile(avatar);
//        }
//        if (mF == null) {
//            mF = request.getFile(logo);
//        }
//        if (mF != null) {
//            try {
//                if (mF.getSize() > 100 * 1024 * 1024) {
//                    throw new UniversalException("File hajmi 100 mb dan kichik bo'lishi kerak", HttpStatus.BAD_REQUEST);
//                }
//                String contentType = mF.getContentType();
//                String originalFilename = mF.getOriginalFilename();
//                long size = mF.getSize();
//                String extention = "";
//                if (contentType.contains("pdf")) {
//                    extention = ".pdf";
//                } else if (contentType.contains("png")) {
//                    extention = ".png";
//                } else if (contentType.contains("jpg")) {
//                    extention = ".jpg";
//                } else if (contentType.contains("jpeg")) {
//                    extention = ".jpeg";
//                } else if (contentType.contains("word")) {
//                    extention = ".docx";
//                }
//                String generatedName = UUID.randomUUID().toString() + extention;
//                String url = folder + "/" + generatedName;
//
//                File fileSAve = new File(url);
//                FileOutputStream outputStream = new FileOutputStream(fileSAve);
//                outputStream.write(mF.getBytes());
//                outputStream.close();
//                outputStream.flush();
//                File file = new File();
//                file.setFilePath(url);
//                file.setExtention(contentType);
//                file.setSize(size);
//                file.setOriginalName(originalFilename);
//                file.setGeneratedName(generatedName);
//                File save = fileRepository.save(file);
//                return ResponseEntity.ok(save.getId());
//
//            } catch (IOException e) {
//                return ResponseEntity.status(HttpStatus.CONFLICT).body("I can't save this photo");
//            }
//        } else return ResponseEntity.status(409).body("Error");
//    }
//

    public ResponseEntity<ByteArrayResource> viewFile2(UUID id)  {
        MyFile file = null;
        try {

            Optional<MyFile> byId = fileRepository.findById(id);
            if (byId.isPresent()) {
                file = byId.get();
            }

        } catch (Exception exception) {

        }

        if (file == null) {
            throw new RuntimeException("Find not found");
        }
        File send = new File(file.getFilePath());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(file.getContentType()));
        headers.add("content-disposition", "inline;filename=" + file.getOriginalName());
        // Way 1
        //  InputStreamResource resource = new InputStreamResource(new FileInputStream(send));
        // Way 2
        Path path = Paths.get(file.getFilePath());
        byte[] data = new byte[0];
        try {
            data = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok().headers(headers).contentLength(send.length()).contentType(MediaType.parseMediaType(file.getContentType())).body(resource);
    }

//    public ResponseEntity<InputStreamResource> viewFile3(String id) throws IOException {
//        File cfile = null;
//        Optional<File> byGeneratedName = fileRepository.findByGeneratedName(id);
//        if (byGeneratedName.isPresent()) {
//            cfile = byGeneratedName.get();
//        } else {
//            Long fileId = null;
//            try {
//                fileId = Long.valueOf(id);
//            } catch (NumberFormatException e) {
//                throw new NotFoundException("File Not Found");
//            }
//            Optional<File> fileOptional = fileRepository.findByIdAndIsActive(fileId, true);
//            cfile = fileOptional.orElseThrow(() -> {
//                throw new NotFoundException("File Not Found");
//            });
//        }
//
//        boolean exists = Files.exists(Path.of(cfile.getFilePath()));
//        if (!exists) {
//            throw new NotFoundException("File topilmadi");
//        }
//
//        File send = new File(cfile.getFilePath());
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("content-disposition", "inline;filename=" + cfile.getOriginalName());
//
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(send));
//
//
//        return ResponseEntity.ok().headers(headers).contentLength(send.length()).contentType(MediaType.parseMediaType(cfile.getExtention())).body(resource);
//    }

//    public void download3(HttpServletResponse resonse, String id) throws IOException {
//
//        File cfile = null;
//        try {
//
//            Long fileId = Long.parseLong(id);
//            Optional<File> byId = fileRepository.findById(fileId);
//            if (byId.isPresent()) {
//                cfile = byId.get();
//            }
//
//        } catch (Exception e) {
//            Optional<File> byGeneratedName = fileRepository.findByGeneratedName(id);
//            if (byGeneratedName.isPresent()) {
//                cfile = byGeneratedName.get();
//            }
//
//        }
//
//        if (cfile == null) {
//            throw new RuntimeException("Find not found");
//        }
//
//        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext, cfile.getOriginalName());
//        File file = new File(cfile.getFilePath());
//
//        // Content-Type
//        // application/pdf
//        resonse.setContentType(mediaType.getType());
//
//        // Content-Disposition
//        resonse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + cfile.getOriginalName());
//
//        // Content-Length
//        resonse.setContentLength((int) file.length());
//
//        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
//        BufferedOutputStream outStream = new BufferedOutputStream(resonse.getOutputStream());
//
//        byte[] buffer = new byte[1024];
//        int bytesRead = 0;
//        while ((bytesRead = inStream.read(buffer)) != -1) {
//            outStream.write(buffer, 0, bytesRead);
//        }
//        outStream.flush();
//        inStream.close();
//    }

//    public ResponseEntity<byte[]> getPDF1(String id) throws IOException {
//
//        File cfile = null;
//        try {
//
//            Long fileId = Long.parseLong(id);
//            Optional<File> byId = fileRepository.findById(fileId);
//            if (byId.isPresent()) {
//                cfile = byId.get();
//            }
//
//        } catch (Exception e) {
//            Optional<File> byGeneratedName = fileRepository.findByGeneratedName(id);
//            if (byGeneratedName.isPresent()) {
//                cfile = byGeneratedName.get();
//            }
//
//        }
//
//        if (cfile == null) {
//            throw new RuntimeException("Find not found");
//        }
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.parseMediaType(cfile.getExtention()));
//        String filename = "pdf1.pdf";
//        headers.add("content-disposition", "inline;filename=" + cfile.getOriginalName());
//        headers.setContentDispositionFormData(filename, cfile.getOriginalName());
//        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
//        byte[] content = Files.readAllBytes(Paths.get(cfile.getFilePath()));
//        return new ResponseEntity<byte[]>(content, headers, HttpStatus.OK);
//    }


    public void downloadAvatar(UUID id, HttpServletResponse response) {
        Optional<MyFile> byId = fileRepository.findById(id);
        if (byId.isPresent()) {
            MyFile file = byId.get();
            try {
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getOriginalName());
                FileInputStream inputStream = new FileInputStream(file.getFilePath());
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            } catch (IOException ignored) {

            }
        } else {
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "user photo");
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(SecretKeys.PHOTO_ANONIM);
                FileCopyUtils.copy(inputStream, response.getOutputStream());
            } catch (IOException e) {
                System.out.println("Error avatar");
            }
        }
    }

    public List<MyFile> saveDocumentFiles(MultipartFile[] multipartFiles) {
        int counter = 1;
        List<MyFile> myFiles = new ArrayList<>();
        String path = SecretKeys.DOCUMENT_PATH;
        for (MultipartFile multipartFile : multipartFiles) {

            if (!multipartFile.isEmpty() && multipartFile.getSize() > 0) {
                try {
                    String originalFilename = multipartFile.getOriginalFilename();
                    long size = multipartFile.getSize();
                    File filecheck = new File(path);
                    if (!filecheck.isDirectory()) {
                        filecheck.mkdirs();
                    }

                    String generatedName = UUID.randomUUID() + originalFilename;
                    String url = path + "\\" + generatedName;

                    File fileSAve = new File(url);
                    FileOutputStream outputStream = new FileOutputStream(fileSAve);
                    outputStream.write(multipartFile.getBytes());
                    outputStream.close();
                    outputStream.flush();
                    MyFile myFile = new MyFile();
                    myFile.setFilePath(url);
                    String contentType = multipartFile.getContentType();
                    myFile.setContentType(contentType);
                    myFile.setSize(size);
                    myFile.setOrderNumber(counter);
                    myFile.setOriginalName(originalFilename);
                    myFile.setGeneretedName(generatedName);
                    MyFile save = fileRepository.save(myFile);
                    myFiles.add(save);
                    counter++;
                } catch (IOException e) {

                }
            }
        }
        return myFiles;
    }


    public void viewFile(UUID id, HttpServletResponse response)  {

        Optional<MyFile> byId = fileRepository.findById(id);
        if (byId.isPresent()) {


            MyFile file = byId.get();
            response.setContentType(file.getContentType());
            response.addHeader("Content-Disposition", "attachment; filename=" + file.getOriginalName());
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file.getFilePath());
                int nRead;
                while ((nRead = inputStream.read()) != -1) {
                    response.getWriter().write(nRead);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

    }
}