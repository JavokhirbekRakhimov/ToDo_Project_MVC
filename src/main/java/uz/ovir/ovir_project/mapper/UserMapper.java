package uz.ovir.ovir_project.mapper;

import org.mapstruct.Mapper;
import uz.ovir.ovir_project.dto.userDto.UserCreate;
import uz.ovir.ovir_project.dto.userDto.UserDto;
import uz.ovir.ovir_project.dto.userDto.UserDtoDocument;
import uz.ovir.ovir_project.dto.userDto.UserUpdate;
import uz.ovir.ovir_project.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto fromUser(User user);
    User fromCreateDto(UserCreate userCreate);
    UserUpdate toUpdateUser(User user);

    List<UserDto> fromUserList(List<User> users);

    User fromUpdateDto(UserUpdate update);

    List<UserDtoDocument> toUserDtoDocument(List<User> userList);
}
