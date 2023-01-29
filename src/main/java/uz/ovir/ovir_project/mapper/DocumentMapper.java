package uz.ovir.ovir_project.mapper;

import org.mapstruct.Mapper;
import uz.ovir.ovir_project.dto.document.DocumentDto;
import uz.ovir.ovir_project.dto.document.DocumentEdie;
import uz.ovir.ovir_project.entity.Document;

import java.util.List;
@Mapper(componentModel = "spring")
public interface DocumentMapper {
    List<DocumentDto>fromDocumentList(List<Document> documentList);
    DocumentEdie toDocumentEdite(Document document);
}
