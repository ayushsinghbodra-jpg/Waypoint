package com.ayush.waypoint.controller.api;

import com.ayush.waypoint.config.ConfigProperty;
import com.ayush.waypoint.data.ShortUrlDto;
import com.ayush.waypoint.domain.ShortUrl;
import com.ayush.waypoint.service.ShortUrlService;
import com.ayush.waypoint.utils.ApiUtils;
import com.ayush.waypoint.utils.UrlShortUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/shorturl")
public class ShortUrlController {
    private static final Logger log = LoggerFactory.getLogger(ShortUrlController.class);

    private final ConfigProperty configProperty;
    private final ShortUrlService shortUrlService;

    public ShortUrlController(ConfigProperty configProperty, ShortUrlService shortUrlService) {
        this.configProperty = configProperty;
        this.shortUrlService = shortUrlService;
    }

    @PostMapping("")
    public ApiUtils.ApiResult<ShortUrlDto.Res> generateUrl(@RequestBody @Valid ShortUrlDto.Req req) {
        log.debug("req : {}", req);

        ShortUrl shortUrl = shortUrlService.findByOriginalUrl(req.getUrl());
        String encodedUrl = configProperty.getBaseUrl() + UrlShortUtil.encode(shortUrl.getId());
        return ApiUtils.success(new ShortUrlDto.Res(shortUrl.getCount(), encodedUrl));
    }
}