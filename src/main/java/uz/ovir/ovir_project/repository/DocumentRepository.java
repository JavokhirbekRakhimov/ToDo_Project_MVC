package uz.ovir.ovir_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import uz.ovir.ovir_project.entity.Document;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
    @Query(nativeQuery = true,value = "select max(order_number) from document ")
    Long getMaxOrder();
@Query(nativeQuery = true,value = "select * from document where status not in(?1) order by order_number  limit ?2 offset (?2*?3) ")
    List<Document> getAllDocuments(Set<String> statusList, Integer size, Integer ofset);
    @Query(nativeQuery = true,value = "select count(id) from document where status not in(?1)  ")
    Integer getAllDocumentsCount(Set<String> statusList);
    @Query(nativeQuery = true,value = "select * from document d join document_files df on d.id = df.document_id where df.files_id=?1")
    Document getIdDocumentByFileID(UUID id);
  @Modifying
  @Query(nativeQuery = true,value = "delete from document_files where files_id=?1")
  @Transactional
    void deleteDocumentFiles(UUID id);


    @Modifying
    @Query(nativeQuery = true,value = "delete from file where id=?1")
    @Transactional
    void deleteFiles(UUID id);
    @Modifying
    @Query(nativeQuery = true,value = "update document set status=?1,done_date=?3 where id=?2")
    @Transactional
    void setStatus(String bajarilgan, UUID id, LocalDate now);
    @Modifying
    @Query(nativeQuery = true,value = "update document set status=?1 where id=?2")
    @Transactional
    void setStatusDelete(String name, UUID id);

    @Modifying
    @Query(nativeQuery = true,value = "update document set status=?1 where status not in(?2) and cast((document.end_date- cast(now() as date)) as int)<=0")
    @Transactional
    void cronJobNotDone(String status, List<String> stringList);
    @Query(nativeQuery = true,value = "select count(d.id) from document d join document_users du on d.id = du.document_id where d.status not in(?1) and du.users_id=?2")
    Integer getAllDocumentsCountPersonal(Set<String> name, UUID id);
    @Query(nativeQuery = true,value = "select * from document d join document_users du on d.id = du.document_id where status not in(?1) and du.users_id=?4 order by order_number  limit ?2 offset (?2*?3) ")
    List<Document> getAllDocumentsPersonal(Set<String> name, int parseInt, Integer page, UUID id);
    @Query(nativeQuery = true,value = "select count(d.id) from document d join document_users du on d.id = du.document_id where d.status=?1 and du.users_id=?2")
    Integer getCountDocumetsBYStatusAndUserId(String status, UUID id);
    @Query(nativeQuery = true,value = "select count(d.id) from document d where d.status=?1")
    Integer getCountDocumetsBYStatus(String status);
}
