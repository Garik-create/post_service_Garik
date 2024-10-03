package faang.school.postservice.service.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageValidatorTest {

    @InjectMocks
    private ImageValidator imageValidator;

    @Mock
    private MultipartFile image;

    private static final long MAX_FILE_SIZE = 5242816;
    private static final long LARGE_FILE_SIZE = 5300000;
    private static final long DEFAULT_FILE_SIZE = 3000000;

    @Nested
    class ValidatorTests {
        @Test
        @DisplayName("Success if the size is equal to the allowed size")
        void whenImageSizeExceededAllowedSizeThenSuccess() {
            when(image.getSize()).thenReturn(MAX_FILE_SIZE);

            assertDoesNotThrow(() -> imageValidator.checkImageSizeExceeded(image));
        }

        @Test
        @DisplayName("Success if the size is smaller than allowed")
        void whenImageSizeNotExceededThenSuccess() {
            when(image.getSize()).thenReturn(DEFAULT_FILE_SIZE);

            assertDoesNotThrow(() -> imageValidator.checkImageSizeExceeded(image));
        }

        @Test
        @DisplayName("Error when exceeding the allowable size")
        void whenImageSizeExceededThenTrowException() {
            when(image.getSize()).thenReturn(LARGE_FILE_SIZE);

            assertThrows(MaxUploadSizeExceededException.class,
                    () -> imageValidator.checkImageSizeExceeded(image));
        }
    }
}