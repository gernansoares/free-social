package com.freesocial.users.dto;

import com.freesocial.users.entity.FreeSocialUser;
import lombok.Data;
import lombok.Getter;

@Data
public class UserDTO {

    private String name;

    private String username;

    public static UserDTO of(FreeSocialUser user) {
        UserDTO dto = new UserDTO();
        dto.username = user.getAuthentication().getUsername();
        dto.name = user.getProfile().getName();
        return dto;
    }

}
