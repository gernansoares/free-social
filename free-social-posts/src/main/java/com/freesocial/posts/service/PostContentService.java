package com.freesocial.posts.service;

import com.freesocial.lib.config.exceptions.FileUploadException;
import com.freesocial.lib.properties.ErroUtil;
import com.freesocial.posts.common.enums.ValidExtensions;
import com.freesocial.posts.common.exceptions.PostNotFoundException;
import com.freesocial.posts.common.util.Constants;
import com.freesocial.posts.common.util.PostUtil;
import com.freesocial.posts.dto.PostDTO;
import com.freesocial.posts.entity.Post;
import com.freesocial.posts.entity.PostContent;
import com.freesocial.posts.repository.PostContentRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class PostContentService {

    @Autowired
    private PostContentRepository postContentRepository;

    @Autowired
    private PostUtil postUtil;

    @Value("${freesocial.posts.filesdir}")
    private String filesDir;

    /**
     * Validate if post has text or provided a file
     *
     * @param post
     */
    public void validatePostContent(Post post) {
        if (Strings.isNullOrEmpty(post.getContent().getText()) && Strings.isNullOrEmpty(post.getContent().getFileDir())) {
            throw new IllegalArgumentException(ErroUtil.getMessage(Constants.TEXT_OR_FILE_REQUIRED));
        }
    }

    /**
     * Validate if upload extension is valid by comparing it with the ones in ValidExtensions enum
     *
     * @param file its name will be the source of the extension
     */
    private void validateExtensions(FilePart file) {
        if (!ValidExtensions.isExtensionValid(FilenameUtils.getExtension(file.filename()))) {
            throw new FileUploadException(ErroUtil.getMessage(Constants.INVALID_FILE_EXTENSION,
                    Arrays.stream(ValidExtensions.values()).map(ValidExtensions::name).collect(Collectors.joining(", "))));
        }
    }

    /**
     * Saves the upload to disk since its exists (its not required)
     * will validate the extension of the file first
     *
     * @param content of the post in which the file belongs
     * @param file    file being saved to disk
     */
    public void saveFile(PostContent content, FilePart file) {
        if (Objects.nonNull(file)) {
            validateExtensions(file);

            File diskFile = new File(String.format(filesDir, content.getPost().getPostUuid(),
                    FilenameUtils.getExtension(file.filename())));
            file.transferTo(diskFile).subscribe();

            content.setFileDir(diskFile.getAbsolutePath());
        }
    }

    /**
     * Deletes the file of a post if its exists
     *
     * @param content post in which the file belong
     */
    public void deleteFile(PostContent content) {
        Optional.ofNullable(content.getFileDir()).ifPresent(dir -> {
            new File(content.getFileDir()).delete();
        });
    }

    /**
     * Update post's text
     *
     * @param contentDto new post information
     * @param postUuid   identifies the post
     * @param userUuid   identifies the user
     * @throws PostNotFoundException if user is not found
     */
    public void update(PostDTO contentDto, String postUuid, String userUuid) {
        Optional<PostContent> postOpt = postContentRepository.findByPost_PostUuid(postUuid);
        PostContent postContent = postOpt.orElseThrow(() -> new PostNotFoundException());

        postUtil.validatePostBelongsToUser(postContent.getPost(), userUuid);
        validatePostContent(postContent.getPost());

        postContent.setText(contentDto.getText());
        postContentRepository.save(postContent);
    }

}
