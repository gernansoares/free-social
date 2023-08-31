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
     * @param profile      new profile information
     * @param userUuid identifies the user
     * @throws UserNotFoundException if user is not found
     */
    public void update(UserProfileDTO profile, String userUuid) {
        Optional<UserProfile> optUser = userProfileRepository.findByUser_Uuid(userUuid);
        optUser.orElseThrow(() -> new UserNotFoundException(ErroUtil.getMessage(Constants.USER_NOT_FOUND)));

        userProfileRepository.updateNameAndBioByUserUuid(profile.getName(), profile.getBio(), userUuid);
    }

}
