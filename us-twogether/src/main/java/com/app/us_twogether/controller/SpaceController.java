package com.app.us_twogether.controller;

import com.app.us_twogether.domain.space.AccessLevel;
import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.service.SpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.api.base-url}/spaces")
public class SpaceController {

    @Autowired
    private SpaceService spaceService;

    @GetMapping("/shared")
    public ResponseEntity<String> getSharedLink(){

        UsernamePasswordAuthenticationToken authentication =
                (org.springframework.security.authentication.UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        String spaceTokenShared = spaceService.getSharedLink(authentication.getName());

        return ResponseEntity.ok(spaceTokenShared);
    }

    @PostMapping("/join/{token}")
    public ResponseEntity<String> joinSpace(@PathVariable String token) {
        UsernamePasswordAuthenticationToken authentication =
                (org.springframework.security.authentication.UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        spaceService.addUserToSpaceByToken(token, authentication.getName(), AccessLevel.FAMILY);
        return ResponseEntity.ok("Você foi adicionado ao espaço!");
    }

    @PostMapping("/join/us/{token}")
    public ResponseEntity<String> joinUsSpace(@PathVariable String token) {
        UsernamePasswordAuthenticationToken authentication =
                (org.springframework.security.authentication.UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        spaceService.addUserToSpaceByToken(token, authentication.getName(), AccessLevel.US);
        return ResponseEntity.ok("Você foi adicionado ao espaço!");
    }
}
