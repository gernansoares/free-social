package com.freesocial.users.dto;

import com.freesocial.users.common.validation.BioValidation;
import com.freesocial.users.common.validation.NameValidation;
import lombok.Data;

@Data
public class UserProfileDTO {

    @NameValidation
    private String name;

    @BioValidation
    private String bio;

}
