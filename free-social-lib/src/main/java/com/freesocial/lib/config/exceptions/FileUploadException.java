package com.freesocial.lib.config.exceptions;

import com.freesocial.lib.common.util.Constants;
import com.freesocial.lib.properties.ErrorUtil;

public class FileUploadException extends FreeSocialException {

    public FileUploadException() {
        super(ErrorUtil.getMessage(Constants.FILE_UPLOAD_ERROR));
    }

    public FileUploadException(String msg) {
        super(msg);
    }

}
