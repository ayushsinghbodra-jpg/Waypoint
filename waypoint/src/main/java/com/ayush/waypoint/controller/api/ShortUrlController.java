package com.ayush.waypoint.controller.api;

import com.ayush.waypoint.config.ConfigProperty;
import com.ayush.waypoint.data.ShortUrlDto;
import com.ayush.waypoint.domain.ShortUrl;
import com.ayush.waypoint.exception.NotFoundException;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/shorturl")
public class ShortUrlController {
    private static final Logger log = LoggerFactory.getLogger(ShortUrlController.class);
    private final UrlShortUtil urlShortUtil;
    private final ConfigProperty configProperty;
    private final ShortUrlService shortUrlService;

    public ShortUrlController(ConfigProperty configProperty, ShortUrlService shortUrlService, UrlShortUtil urlShortUtil) {
        this.configProperty = configProperty;
        this.shortUrlService = shortUrlService;
        this.urlShortUtil = new UrlShortUtil(configProperty.getHashIdSalt());
    }

    @PostMapping("")
    public ApiUtils.ApiResult<ShortUrlDto.Res> generateUrl(@RequestBody @Valid ShortUrlDto.Req req) {
        log.debug("req : {}", req);

        ShortUrl shortUrl = shortUrlService.findByOriginalUrl(req.getUrl());
        String encodedUrl = configProperty.getBaseUrl() + "r/"+urlShortUtil.encode(shortUrl.getId());
        return ApiUtils.success(new ShortUrlDto.Res(encodedUrl));
    }

    @GetMapping("/{code}/stats")
    public ApiUtils.ApiResult<ShortUrlDto.StatsRes> getStats(@PathVariable String code) {
        log.debug("code : {}", code);

        try {
            Long id = UrlShortUtil.decode(code);
            ShortUrl shortUrl = shortUrlService.findShortUrlById(id);
            return ApiUtils.success(new ShortUrlDto.StatsRes(shortUrl.getCount()));
        } catch (IllegalArgumentException e) {
            throw new NotFoundException("Invalid short URL code: " + code);
        }
    }
}