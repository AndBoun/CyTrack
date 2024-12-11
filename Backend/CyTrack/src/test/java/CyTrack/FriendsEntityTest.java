package CyTrack;

import CyTrack.Entities.FriendRequest;
import CyTrack.Entities.Friends;
import CyTrack.Entities.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FriendsEntityTest {

    @Test
    public void testDefaultConstructor() {
        Friends friends = new Friends();
        assertNull(friends.getFriendID());
        assertNull(friends.getUser1());
        assertNull(friends.getUser2());
        assertNull(friends.getFriendRequest());
    }


    @Test
    public void testGettersAndSetters() {
        Friends friends = new Friends();

        User user1 = new User();
        user1.setUserID(1L);
        user1.setUsername("User1");

        User user2 = new User();
        user2.setUserID(2L);
        user2.setUsername("User2");

        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setFriendRequestID(100L);
        friendRequest.setSender(user1);
        friendRequest.setReceiver(user2);

        friends.setFriendID(10L);
        friends.setUser1(user1);
        friends.setUser2(user2);
        friends.setFriendRequest(friendRequest);

        assertEquals(10L, friends.getFriendID());
        assertEquals(user1, friends.getUser1());
        assertEquals(user2, friends.getUser2());
        assertEquals(friendRequest, friends.getFriendRequest());
        assertEquals(100L, friends.getFriendRequestID());
        assertEquals(user1, friends.getSender());
        assertEquals(user2, friends.getReceiver());
    }

    @Test
    public void testSetSenderAndReceiver() {
        Friends friends = new Friends();

        User sender = new User();
        sender.setUserID(1L);
        sender.setUsername("Sender");

        User receiver = new User();
        receiver.setUserID(2L);
        receiver.setUsername("Receiver");

        FriendRequest friendRequest = new FriendRequest();
        friends.setFriendRequest(friendRequest);

        friends.setSender(sender);
        friends.setReceiver(receiver);

        assertEquals(sender, friends.getSender());
        assertEquals(receiver, friends.getReceiver());
    }
}
