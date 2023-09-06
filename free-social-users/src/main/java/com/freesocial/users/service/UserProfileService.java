package com.freesocial.users.service;

import com.freesocial.lib.config.exceptions.UserNotFoundException;
import com.freesocial.lib.properties.ErrorUtil;
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
     * @param newProfile new profile information
     * @param userUuid   identifies the user
     * @throws UserNotFoundException if user is not found
     */
    public void update(UserProfileDTO newProfile, String userUuid) {
        Optional<UserProfile> optUser = userProfileRepository.findByUser_Uuid(userUuid);
        UserProfile profile = optUser.orElseThrow(() -> new UserNotFoundException(ErrorUtil.getMessage(Constants.USER_NOT_FOUND)));

        profile.setName(newProfile.getName());
        profile.setBio(newProfile.getBio());

        userProfileRepository.save(profile);
    }

}
