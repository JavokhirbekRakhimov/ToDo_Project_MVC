package uz.ovir.ovir_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.ovir.ovir_project.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query(nativeQuery = true,value = "select * from users where active=true and username=?1 or email=?1")
    Optional<User> findByEmailOrUserName(String username);

    Optional<User>findByEmail(String email);
    Optional<User>findByPhone(String phone);
    @Query(value = "select max(order_number) from users",nativeQuery = true)
    Integer maxOrder();
    @Query(value = "select count(id) from users",nativeQuery = true)
    Integer totalUser();
    @Query(value = "select * from users order by users.order_number limit ?1 offset (?1*?2) ",nativeQuery = true)
    List<User>listUserByLimit(Integer size,Integer page);
    @Query(value = "select * from users where active=true order by users.order_number  ",nativeQuery = true)
    List<User> getActiveUsers();
    @Query(value = "select * from users where id in(?1) order by users.order_number  ",nativeQuery = true)
    List<User> getUsersIds(List<UUID> userIds);
}
