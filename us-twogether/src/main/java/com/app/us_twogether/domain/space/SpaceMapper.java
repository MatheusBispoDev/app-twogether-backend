package com.app.us_twogether.domain.space;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpaceMapper {

    public SpaceResponseDTO toResponseDTO(Space space){
        return new SpaceResponseDTO(space.getSpaceId(), space.getName(), space.getSharedToken());
    }

    public SpaceWithUsersDTO toSpaceWithUsersDTO(Space space, List<UserAccessDTO> users){
        return new SpaceWithUsersDTO(space.getName(), space.getSharedToken(), users);
    }

}
