package com.app.us_twogether.domain.space;

import java.util.List;

public class SpaceMapper {

    public SpaceResponseDTO toResponseDTO(Space space){
        return new SpaceResponseDTO(space.getName(), space.getSharedToken());
    }

    public SpaceWithUsersDTO toSpaceWithUsersDTO(Space space, List<UserAccessDTO> users){
        return new SpaceWithUsersDTO(space.getName(), space.getSharedToken(), users);
    }

}
