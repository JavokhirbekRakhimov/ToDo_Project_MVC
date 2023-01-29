package uz.ovir.ovir_project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.ovir.ovir_project.config.MyPasswordEncoder;
import uz.ovir.ovir_project.dto.document.ChartData;
import uz.ovir.ovir_project.dto.userDto.UserCreate;
import uz.ovir.ovir_project.dto.userDto.UserDto;
import uz.ovir.ovir_project.dto.userDto.UserDtoDocument;
import uz.ovir.ovir_project.dto.userDto.UserUpdate;
import uz.ovir.ovir_project.entity.MyFile;
import uz.ovir.ovir_project.entity.Role;
import uz.ovir.ovir_project.entity.User;
import uz.ovir.ovir_project.entity.enums.DocumentStatus;
import uz.ovir.ovir_project.entity.enums.Gender;
import uz.ovir.ovir_project.exceptions.UniversalException;
import uz.ovir.ovir_project.mapper.UserMapper;
import uz.ovir.ovir_project.repository.DocumentRepository;
import uz.ovir.ovir_project.repository.FileRepository;
import uz.ovir.ovir_project.repository.RoleRepository;
import uz.ovir.ovir_project.repository.UserRepository;
import uz.ovir.ovir_project.response.ContentList;
import uz.ovir.ovir_project.response.ResponseDto;
import uz.ovir.ovir_project.util.SecretKeys;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MyPasswordEncoder myPasswordEncoder;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final DocumentRepository documentRepository;

    public ResponseDto<UserDto> getUser(User user) {
        ResponseDto<UserDto> responseDto = new ResponseDto<>();
        Optional<User> byEmailOrUserName = userRepository.findByEmailOrUserName(user.getUsername());
        if (byEmailOrUserName.isPresent()) {
            User userCurrent = byEmailOrUserName.get();
            if (myPasswordEncoder.passwordEncoder().matches(user.getPassword(), userCurrent.getPassword())) {
                responseDto.setSuccess(true);
                UserDto userDto = userMapper.fromUser(userCurrent);
                responseDto.setObj(userDto);
                return responseDto;
            } else {

                responseDto.setMessage("User not found");
                return responseDto;
            }
        } else {

            responseDto.setMessage("User not found");

            return responseDto;
        }

    }

    public ResponseDto<UserDto> hasUser(String token) {
        boolean validationToken = jwtService.validationToken(token);
        ResponseDto<UserDto> usernameByResponse = new ResponseDto<>();
        if (validationToken) {
            return jwtService.getUsernameByResponse(token);
        } else {
            usernameByResponse.setSuccess(false);
            usernameByResponse.setMessage("index");
            return usernameByResponse;
        }
    }

    public ResponseDto<User> addUser(UserCreate create) {
        ResponseDto<User> responseDto = new ResponseDto<>();
        User user = userMapper.fromCreateDto(create);
        user.setId(UUID.randomUUID());
        user.setCreatedAt(LocalDateTime.now());
        user.setActive(true);
        Optional<Role> byId = roleRepository.findById(create.getRoleId());
        byId.ifPresent(user::setRole);
        /// hasemail
        if (create.getEmail() != null && create.getEmail().length() != 0) {
            Boolean hasEmail = hasUserByEmail(create.getEmail());
            if (hasEmail) {
                responseDto.setMessage("This email exist");
                return responseDto;
            }
        } else user.setEmail(null);
        if (create.getPhone() != null && create.getPhone().length() != 0) {
            Boolean hasPhone = hasUserByPhone(create.getPhone());
            if (hasPhone) {
                responseDto.setMessage("This phone exist");
                return responseDto;
            }
        } else
            user.setPhone(null);

        if (create.getFemale()) {
            user.setGender(Gender.FEMALE);
        } else {
            user.setGender(Gender.MALE);
        }
        String[] split = create.getBirthDayStr().split("/");
        if (split.length == 3)
            user.setBirthDay(LocalDate.of(Integer.parseInt(split[2]), Integer.parseInt(split[1]), Integer.parseInt(split[0])));
        else user.setBirthDay(LocalDate.now());
        user.setPassword(myPasswordEncoder.passwordEncoder().encode(create.getPassword()));
        Integer integer = userRepository.maxOrder();
        user.setOrderNumber(integer + 1);
        userRepository.save(user);

        responseDto.setSuccess(true);
        return responseDto;
    }

    private Boolean hasUserByPhone(String phone) {
        return userRepository.findByPhone(phone).isPresent();
    }

    private Boolean hasUserByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public ContentList<UserDto> getUserTable(String sizee, Integer page) {
        if (page > 0) page = page - 1;
        ContentList<UserDto> contentList = new ContentList<>();
        Integer size = Integer.parseInt(sizee);
        List<User> users = userRepository.listUserByLimit(size, page);
        List<UserDto> userDtos = userMapper.fromUserList(users);
        Integer totalUser = userRepository.totalUser();
        Integer div = totalUser / size;
        Integer mult = div * size;
        if (mult.equals(totalUser)) {
            contentList.setCount(div);
        } else {
            contentList.setCount(div + 1);
        }
        contentList.setList(userDtos);
        return contentList;
    }

    public ResponseDto<UserUpdate> getUserById(UUID id) {
        Optional<User> byId = userRepository.findById(id);
        ResponseDto<UserUpdate> responseDto = new ResponseDto<>();
        if (byId.isPresent()) {
            responseDto.setSuccess(true);
            User user = byId.get();
            UserUpdate userUpdate = userMapper.toUpdateUser(user);
            userUpdate.setBirthDayStr(user.getBirthDay().getDayOfMonth() + "/" + user.getBirthDay().getMonthValue() + "/" + user.getBirthDay().getYear());
            if (user.getGender().name().equals(Gender.MALE.name())) {
                userUpdate.setMale(true);
                userUpdate.setFemale(false);
            } else {
                userUpdate.setMale(false);
                userUpdate.setFemale(true);
            }
            userUpdate.setRoleId(user.getRole().getId());
            responseDto.setObj(userUpdate);
            return responseDto;
        } else {
            throw new UniversalException("User not found", HttpStatus.NOT_FOUND);
        }

    }

    public ResponseDto<UserUpdate> updateUser(UserUpdate update, UUID id, MultipartFile file) {
        ResponseDto<UserUpdate> userUpdateResponseDto = new ResponseDto<>(true);
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User lastUser = byId.get();
            User user = userMapper.fromUpdateDto(update);
            Optional<Role> byIdRole = roleRepository.findById(update.getRoleId());
            byIdRole.ifPresent(user::setRole);

            String[] split = update.getBirthDayStr().split("/");
            if (split.length == 3) {
                user.setBirthDay(LocalDate.of(Integer.parseInt(split[2]), Integer.parseInt(split[1]), Integer.parseInt(split[0])));
            } else user.setBirthDay(LocalDate.now());
            if (update.getPassword() != null) {
                user.setPassword(myPasswordEncoder.passwordEncoder().encode(update.getPassword()));
            }
            user.setOrderNumber(update.getOrderNumber());
            if (update.getMale()) {
                user.setGender(Gender.MALE);
            } else {
                user.setGender(Gender.FEMALE);
            }
            MyFile avatar = fileService.upload(SecretKeys.PHOTO_PATH, file);
            if (avatar != null) {
                user.setAvatar(avatar);
            } else {
                MyFile lastUserAvatar = lastUser.getAvatar();
                if (lastUserAvatar != null) {
                    user.setAvatar(lastUserAvatar);
                }
            }

            if (update.getEmail() != null && update.getEmail().length() != 0) {

            } else user.setEmail(null);
            if (update.getPhone() != null && update.getPhone().length() != 0) {

            } else
                user.setPhone(null);

            userRepository.save(user);
            userUpdateResponseDto.setObj(update);
            return userUpdateResponseDto;

        } else {
            throw new UniversalException("User not fund", HttpStatus.NOT_FOUND);
        }
    }

    public List<UserDtoDocument> getActiveUsersList() {
        List<User> userList = userRepository.getActiveUsers();
        return userMapper.toUserDtoDocument(userList);
    }

    public List<ChartData> chartInfoPersonal(UUID id) {
        int totalCount=0;
        String total="Total";
        List<ChartData>chartData=new ArrayList<>();
        List<String> collect = Arrays.stream(DocumentStatus.values()).map(Enum::name).collect(Collectors.toList());
        for (String status : collect) {
            Integer count=documentRepository.getCountDocumetsBYStatusAndUserId(status,id);
            ChartData chart=new ChartData(status,count);
            chartData.add(chart);
            totalCount+=count;
        }
        ChartData chartDataLast=new ChartData(total,totalCount);
        chartData.add(chartDataLast);
        return chartData;
    }

    public List<ChartData> chartInfoPersonalAll() {
        int totalCount=0;
        String total="Total";
        List<ChartData>chartData=new ArrayList<>();
        List<String> collect = Arrays.stream(DocumentStatus.values()).map(Enum::name).collect(Collectors.toList());
        for (String status : collect) {
            Integer count=documentRepository.getCountDocumetsBYStatus(status);
            ChartData chart=new ChartData(status,count);
            chartData.add(chart);
            totalCount+=count;
        }
        ChartData chartDataLast=new ChartData(total,totalCount);
        chartData.add(chartDataLast);
        return chartData;
    }
}
