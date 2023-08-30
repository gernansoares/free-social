package com.freesocial.users.service;

import com.freesocial.lib.config.exceptions.UserNotFoundException;
import com.freesocial.lib.properties.ErroUtil;
import com.freesocial.users.common.util.Constants;
import com.freesocial.users.dto.UserProfileDTO;
import com.freesocial.users.entity.UserProfile;
import com.freesocial.users.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;

    /**
     * Update user's profile
     *
     * @param dto new profile information
     * @param userUuid identifies the user
     */
    public void update(UserProfileDTO dto, String userUuid) {
        Optional<UserProfile> optUser = userProfileRepository.findByUser_Uuid(userUuid);

        UserProfile user = optUser.orElseThrow(() -> new UserNotFoundException(ErroUtil.getMessage(Constants.USER_NOT_FOUND)));
        user.setName(dto.getName());
        user.setBio(dto.getBio());

        userProfileRepository.save(user);
    }

}
