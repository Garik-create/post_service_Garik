package faang.school.postservice.validator;

import faang.school.postservice.client.UserServiceClient;
import faang.school.postservice.dto.AlbumDto;
import faang.school.postservice.dto.PostDto;
import faang.school.postservice.exceptions.EntityNotFoundException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserValidator {
    private UserServiceClient userServiceClient;

    public void checkUserExistence(AlbumDto albumDto) {
        try {
            userServiceClient.getUser(albumDto.getAuthorId());
        } catch (FeignException.NotFound e) {
            log.error("Feign exception occurred: ", e);
            throw new EntityNotFoundException("User not found with id: " + albumDto.getAuthorId());
        }
    }
}
