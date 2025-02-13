package com.app.us_twogether.domain.space;

import com.app.us_twogether.domain.user.User;
import com.app.us_twogether.domain.user.UserService;
import com.app.us_twogether.exception.DataAlreadyExistsException;
import com.app.us_twogether.exception.DataNotFoundException;
import jakarta.transaction.Transactional;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class SpaceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private SpaceService spaceService;

    private User user;

    @BeforeEach
    public void setup(){
        user = createUser(new User("john_doe", "1234", "John Doe", "john@example.com", "11932178425", "US"));
    }

    @Test
    public void shouldCreateSpace_whenCredentialsAreValid(){
        Long spaceId = spaceService.createSpace(user).spaceId();

        SpaceWithUsersDTO spaceResponse = spaceService.getSpaceWithUsers(spaceId);

        assertNotNull(spaceResponse);
        assertNotNull(spaceResponse.users());
        assertNotNull(spaceResponse.sharedToken());
    }

    @Test
    public void shouldCreateSpace_whenIsAlreadyExisting(){
        spaceService.createSpace(user);

        assertThrows(DataAlreadyExistsException.class, () -> spaceService.createSpace(user));
    }

    @Test
    public void shouldGetSharedLink_whenCredentialsAreValid(){
        SpaceResponseDTO space = spaceService.createSpace(user);

        SpaceResponseDTO spaceResponse = spaceService.getSharedLink(space.spaceId());

        assertNotNull(space);
        assertNotNull(spaceResponse);
        assertEquals(space.sharedToken(), spaceResponse.sharedToken());
    }

    @Test
    public void shouldUpdateSpace_whenCredentialsAreValid(){
        Long spaceId = spaceService.createSpace(user).spaceId();

        String spaceName = "Space Test";

        spaceService.updatedSpace(spaceId, spaceName);

        SpaceResponseDTO spaceResponse = spaceService.getSharedLink(spaceId);

        assertNotNull(spaceResponse);
        assertEquals(spaceName, spaceResponse.name());
    }

    @Test
    public void shouldDeleteSpace_whenCredentialsAreValid(){
        Long spaceId = spaceService.createSpace(user).spaceId();

        spaceService.deleteSpace(spaceId);

        assertThrows(DataNotFoundException.class, () -> spaceService.getSpaceWithUsers(spaceId));
    }

    @Test
    public void shouldAddUserToSpaceByToken_whenCredentialsAreValid(){
        SpaceResponseDTO space = spaceService.createSpace(user);

        User userInvited = createUser(new User("jane_doe", "1234", "Jane Doe",
                "katharina@example.com", "11932178425", "US"));

        spaceService.addUserToSpaceByToken(space.sharedToken(), userInvited, AccessLevel.US);

        SpaceWithUsersDTO response = spaceService.getSpaceWithUsers(space.spaceId());

        assertNotNull(response);
        assertNotNull(response.users());
        assertEquals(2, response.users().size());
        assertEquals("john_doe", response.users().get(0).username());
        assertEquals(AccessLevel.US, response.users().get(0).accessLevel());
        assertEquals("jane_doe", response.users().get(1).username());
        assertEquals(AccessLevel.US, response.users().get(1).accessLevel());
    }

    @Test
    public void shouldAddUserToSpaceByToken_whenTokenIsInvalid(){
        spaceService.createSpace(user);

        User userInvited = createUser(new User("jane_doe", "1234", "Jane Doe",
                "katharina@example.com", "11932178425", "US"));

        assertThrows(ResourceNotFoundException.class, () -> spaceService.addUserToSpaceByToken("123132151456415", userInvited, AccessLevel.US));
    }

    @Test
    public void shouldAddUserToSpaceByToken_whenUsersIsAlreadyAdded(){
        String sharedToken = spaceService.createSpace(user).sharedToken();

        assertThrows(DataAlreadyExistsException.class, () -> spaceService.addUserToSpaceByToken(sharedToken, user, AccessLevel.US));
    }

    @Test
    public void shouldAddUserToSpaceByToken_whenUsersUSAlreadyReachedTheLimit(){
        String sharedToken = spaceService.createSpace(user).sharedToken();

        User userInvited1 = createUser(new User("jane_doe", "1234", "Jane Doe",
                "katharina@example.com", "11932178425", "US"));

        spaceService.addUserToSpaceByToken(sharedToken, userInvited1, AccessLevel.US);

        User userInvited2 = createUser(new User("jorge_doe", "1234", "Jorge Doe",
                "jorge@example.com", "11932178425", "US"));

        assertThrows(DataAlreadyExistsException.class, () -> spaceService.addUserToSpaceByToken(sharedToken, userInvited2, AccessLevel.US));
    }

    @Test
    public void shouldChangeSpaceUserAccessLevel_whenChangeToFamily(){
        SpaceResponseDTO space = spaceService.createSpace(user);

        User userInvited = createUser(new User("jane_doe", "1234", "Jane Doe",
                "katharina@example.com", "11932178425", "US"));

        spaceService.addUserToSpaceByToken(space.sharedToken(), userInvited, AccessLevel.US);

        spaceService.changeSpaceUserAccessLevel(space.spaceId(), userInvited.getUsername(), AccessLevel.FAMILY);

        SpaceWithUsersDTO response = spaceService.getSpaceWithUsers(space.spaceId());

        assertNotNull(response);
        assertNotNull(response.users());
        assertEquals(2, response.users().size());
        assertEquals("jane_doe", response.users().get(1).username());
        assertEquals(AccessLevel.FAMILY, response.users().get(1).accessLevel());
    }

    @Test
    public void shouldChangeSpaceUserAccessLevel_whenChangeToFamilyAndAddUserToSpaceByToken(){
        SpaceResponseDTO space = spaceService.createSpace(user);

        User userInvited = createUser(new User("jane_doe", "1234", "Jane Doe",
                "katharina@example.com", "11932178425", "US"));

        spaceService.addUserToSpaceByToken(space.sharedToken(), userInvited, AccessLevel.US);

        spaceService.changeSpaceUserAccessLevel(space.spaceId(), userInvited.getUsername(), AccessLevel.FAMILY);

        User userInvited2 = createUser(new User("jorge_doe", "1234", "Jorge Doe",
                "jorge@example.com", "11932178425", "US"));

        spaceService.addUserToSpaceByToken(space.sharedToken(), userInvited2, AccessLevel.US);

        SpaceWithUsersDTO response = spaceService.getSpaceWithUsers(space.spaceId());

        assertNotNull(response);
        assertNotNull(response.users());
        assertEquals(3, response.users().size());
        assertEquals("jane_doe", response.users().get(1).username());
        assertEquals(AccessLevel.FAMILY, response.users().get(1).accessLevel());
        assertEquals("jorge_doe", response.users().get(2).username());
        assertEquals(AccessLevel.US, response.users().get(2).accessLevel());
    }

    @Test
    public void shouldRemoveUserFromSpace_whenUserIsPresentInSpace(){
        SpaceResponseDTO space = spaceService.createSpace(user);

        User userInvited = createUser(new User("jane_doe", "1234", "Jane Doe",
                "katharina@example.com", "11932178425", "US"));

        spaceService.addUserToSpaceByToken(space.sharedToken(), userInvited, AccessLevel.US);

        spaceService.removeUserFromSpace(space.spaceId(), userInvited.getUsername());

        SpaceWithUsersDTO response = spaceService.getSpaceWithUsers(space.spaceId());

        assertNotNull(response);
        assertNotNull(response.users());
        assertEquals(1, response.users().size());
        assertEquals("john_doe", response.users().get(0).username());
        assertEquals(AccessLevel.US, response.users().get(0).accessLevel());
    }

    @Test
    public void shouldRemoveUserFromSpace_whenUserIsNotPresentInSpace(){
        SpaceResponseDTO space = spaceService.createSpace(user);

        User userInvited = createUser(new User("jane_doe", "1234", "Jane Doe",
                "katharina@example.com", "11932178425", "US"));

        assertThrows(DataNotFoundException.class, () -> spaceService.removeUserFromSpace(space.spaceId(), userInvited.getUsername()));
    }

    private User createUser(User user){
        userService.createUser(new User(user.getUsername(), user.getPassword(), user.getName(),
                user.getName(), user.getPhoneNumber(), user.getType()));

        return userService.getUserByUsername(user.getUsername());
    }
}
