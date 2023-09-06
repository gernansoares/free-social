package com.freesocial.users.dto;

import com.freesocial.users.validation.BioValidation;
import com.freesocial.users.validation.NameValidation;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UserProfileDTO {

    @NameValidation
    private String name;

    @BioValidation
    private String bio;

}
