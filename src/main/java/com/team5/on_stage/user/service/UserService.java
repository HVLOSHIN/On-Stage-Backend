package com.team5.on_stage.user.service;

import com.team5.on_stage.global.config.s3.S3Uploader;
import com.team5.on_stage.global.constants.ErrorCode;
import com.team5.on_stage.global.exception.GlobalException;
import com.team5.on_stage.summary.repository.SummaryRespository;
import com.team5.on_stage.summary.service.SummaryService;
import com.team5.on_stage.user.dto.UpdateUserDto;
import com.team5.on_stage.user.dto.UserProfileDto;
import com.team5.on_stage.user.entity.*;
import com.team5.on_stage.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;
    private final SummaryService summaryService;


    public void checkNicknameDuplicated(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new GlobalException(ErrorCode.NICKNAME_DUPLICATED);
        }
    }

    public void updateUserNickname(String username,
                                   String nickname) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new GlobalException(ErrorCode.USER_NOT_FOUND);
        }

        if (nickname.equals(user.getNickname())) {
            throw new GlobalException(ErrorCode.NOT_MODIFIED);
        }

        checkNicknameDuplicated(nickname);
        user.setNickname(nickname);
        userRepository.save(user);
    }

    public void updateUserDescription(String username,
                                      String description) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new GlobalException(ErrorCode.USER_NOT_FOUND);
        }

        if (description.equals(user.getDescription())) {
            throw new GlobalException(ErrorCode.NOT_MODIFIED);
        }

        user.setDescription(description);

        userRepository.save(user);
    }


    public void deleteUser(String username) {

        if (userRepository.findByUsername(username) == null) {

            throw new GlobalException(ErrorCode.USER_NOT_FOUND);
        }

        userRepository.softDeleteUserByUsername(username);
    }


    public UserProfileDto getUserProfile(String username) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new GlobalException(ErrorCode.USER_NOT_FOUND);
        }

        UserProfileDto userProfileDto = UserProfileDto.builder()
                .username(user.getUsername())
                .nickname(user.getNickname())
                .description(user.getDescription())
                .profileImage(user.getProfileImage())
                .build();

        return userProfileDto;
    }


    public UserProfileDto updateUserProfileImage(String username, MultipartFile profileImage) throws IOException {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new GlobalException(ErrorCode.USER_NOT_FOUND);
        }

        String imageUrl = s3Uploader.upload(profileImage, "profileImages/" + username);
        user.setProfileImage(imageUrl);
        userRepository.save(user);

        return getUserProfile(username);
    }


    public void setUserProfileDefault(String username) {

        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new GlobalException(ErrorCode.USER_NOT_FOUND);
        }

        user.setProfileImage("https://s3-on-stage.s3.ap-northeast-2.amazonaws.com/profileImages/defaultProfile.jpg");

        userRepository.save(user);
    }


    public String convertNicknameToUsername(String nickname) {

        User user = userRepository.findByNickname(nickname);

        return user.getUsername();
    }


}
