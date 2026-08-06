package com.ayush.waypoint.service;

import com.ayush.waypoint.domain.ShortUrl;
import com.ayush.waypoint.exception.NotFoundException;
import com.ayush.waypoint.repository.ShortUrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ShortUrlServiceTest {

    @Mock
    private ShortUrlRepository shortUrlRepository;

    @InjectMocks
    private ShortUrlService shortUrlService;

    private final String originalUrl = "https://example.com/test";

    @BeforeEach
    void setUp() {
        reset(shortUrlRepository);
    }

    @Test
    void findByOriginalUrlShouldSaveOnlyOnceWhenCalledTwiceWithSameUrl() {
        ShortUrl existing = new ShortUrl(originalUrl);
        when(shortUrlRepository.findByUrl(originalUrl)).thenReturn(Optional.empty()).thenReturn(Optional.of(existing));
        when(shortUrlRepository.save(any(ShortUrl.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ShortUrl firstCall = shortUrlService.findByOriginalUrl(originalUrl);
        ShortUrl secondCall = shortUrlService.findByOriginalUrl(originalUrl);

        assertNotNull(firstCall);
        assertNotNull(secondCall);
        assertEquals(firstCall.getUrl(), secondCall.getUrl(), "Both results should refer to the same original URL");
        verify(shortUrlRepository, times(2)).findByUrl(originalUrl);
        verify(shortUrlRepository, times(1)).save(any(ShortUrl.class));
        verifyNoMoreInteractions(shortUrlRepository);
    }

    @Test
    void findByIdThrowsNotFoundExceptionWhenMissing() {
        when(shortUrlRepository.findById(eq(42L))).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> shortUrlService.findById(42L));

        assertTrue(exception.getMessage().contains("Invalid short URL id"));
        verify(shortUrlRepository, times(1)).findById(42L);
    }

    @Test
    void increaseCountShouldCallRepositoryIncrementCountOnce() {
        doNothing().when(shortUrlRepository).incrementCount(eq(123L));

        shortUrlService.increaseCount(123L);

        verify(shortUrlRepository, times(1)).incrementCount(123L);
        verifyNoMoreInteractions(shortUrlRepository);
    }
}
