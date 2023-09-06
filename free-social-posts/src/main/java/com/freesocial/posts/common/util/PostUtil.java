package com.freesocial.posts.common.util;

import com.freesocial.lib.properties.ErroUtil;
import com.freesocial.posts.entity.Post;
import org.springframework.stereotype.Component;

@Component
public class PostUtil {

    /**
     * Validate if a solicited object UUID owner is equals to the active user UUID
     *
     * @param post     post being validated
     * @param userUuid active user UUID
     */
    public void validatePostBelongsToUser(Post post, String userUuid) {
        if (!post.getUserUuid().equals(userUuid)) {
            throw new IllegalArgumentException(ErroUtil.getMessage(Constants.INVALID_POST));
        }
    }

}
