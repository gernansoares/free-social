package com.freesocial.posts.common.exceptions;

import com.freesocial.lib.config.exceptions.FreeSocialException;
import com.freesocial.lib.properties.ErroUtil;
import com.freesocial.posts.common.util.Constants;

public class PostNotFoundException extends FreeSocialException {

    public PostNotFoundException() {
        super(ErroUtil.getMessage(Constants.POST_NOT_FOUND));
    }

}
