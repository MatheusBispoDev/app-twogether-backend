package com.app.us_twogether.controller;

import com.app.us_twogether.service.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/spaces")
public class SpaceController {
    @Autowired
    private SpaceService spaceService;

    @GetMapping("/shared")
    public ResponseEntity<String> getSharedLink(){
        UsernamePasswordAuthenticationToken authentication =
                (org.springframework.security.authentication.UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        String spaceTokenShared = "";
        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(spaceTokenShared);
    }
}
