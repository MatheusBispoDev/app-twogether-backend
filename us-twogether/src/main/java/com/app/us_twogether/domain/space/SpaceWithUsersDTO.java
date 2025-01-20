package com.app.us_twogether.domain.space;

import lombok.Data;

import java.util.List;

@Data
public class SpaceWithUsersDTO {
    private String spaceName;
    private String sharedToken;
    private List<UserAccessDTO> users;

    public SpaceWithUsersDTO(String spaceName, String sharedToken, List<UserAccessDTO> users) {
        this.spaceName = spaceName;
        this.sharedToken = sharedToken;
        this.users = users;
    }
}
