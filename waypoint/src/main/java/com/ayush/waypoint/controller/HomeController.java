package com.ayush.waypoint.controller;

import com.ayush.waypoint.exception.NotFoundException;
import com.ayush.waypoint.service.ShortUrlService;
import com.ayush.waypoint.utils.UrlShortUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HomeController {
    private final ShortUrlService shortUrlService;
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    public HomeController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @GetMapping("/r/{path}")
    public RedirectView redirect(@PathVariable String path, HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
        
        try {
            Long id = UrlShortUtil.decode(path);
            shortUrlService.increaseCount(id);
            String targetUrl = shortUrlService.findById(id);
            RedirectView redirectView = new RedirectView(targetUrl);
            redirectView.setStatusCode(HttpStatus.TEMPORARY_REDIRECT);
            return redirectView;
        } catch (NotFoundException e) {
            log.warn("Short URL not found for path: {}", path);
            return new RedirectView("/not-found");
        } catch (IllegalArgumentException e) {
            log.warn("Invalid short URL code: {}", path);
            return new RedirectView("/not-found");
        } catch (Exception e) {
            log.error("Unexpected error occurred while redirecting: {}", e.getMessage(), e);
            return new RedirectView("/not-found");
        }
    }

    @GetMapping("/not-found")   
    public String notFound() {
        return "not-found";
    }
}