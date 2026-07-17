package com.ayush.waypoint.controller;

import com.ayush.waypoint.service.ShortUrlService;
import com.ayush.waypoint.utils.UrlShortUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {
    private final ShortUrlService shortUrlService;

    public HomeController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @GetMapping("/{path}")
    public String redirect(@PathVariable String path) {
        Long decode = UrlShortUtil.decode(path);
        String targetUrl = shortUrlService.findById(decode);
        return "redirect:" + targetUrl;
    }

    @GetMapping("/not-found")
    public String notFound() {
        return "not-found";
    }
}