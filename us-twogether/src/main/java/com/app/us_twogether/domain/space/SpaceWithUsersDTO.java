package com.app.us_twogether.domain.space;

import java.util.List;

public record SpaceWithUsersDTO(String spaceName, String sharedToken, List<UserAccessDTO> users) { }
