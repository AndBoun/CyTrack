package CyTrack;

import CyTrack.Entities.Message;
import CyTrack.Entities.User;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class MessageSystemTest {


    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    public void testDefaultConstructor() {
        Message message = new Message();
        assertNull(message.getId());
        assertNull(message.getSender());
        assertNull(message.getReceiver());
        assertNull(message.getContent());
        assertNull(message.getDate());
    }


    @Test
    public void testCustomConstructor() {
        Long senderID = 1L;
        Long receiverID = 2L;
        String senderName = "Sender";
        String receiverName = "Receiver";
        String content = "Hello, this is a custom constructor test.";

        Message message = new Message(senderID, receiverID, senderName, receiverName, content);

        assertEquals(senderID, message.getSender().getUserID());
        assertEquals(senderName, message.getSender().getUsername());
        assertEquals(receiverID, message.getReceiver().getUserID());
        assertEquals(receiverName, message.getReceiver().getUsername());
        assertEquals(content, message.getContent());
        assertNotNull(message.getDate());
    }

    @Test
    public void testGettersAndSetters() {
        Message message = new Message();

        User sender = new User();
        sender.setUserID(1L);
        sender.setUsername("Sender");

        User receiver = new User();
        receiver.setUserID(2L);
        receiver.setUsername("Receiver");

        String content = "Testing setters and getters.";
        Date date = new Date();

        message.setId(100L);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setDate(date);

        assertEquals(100L, message.getId());
        assertEquals(sender, message.getSender());
        assertEquals(receiver, message.getReceiver());
        assertEquals(content, message.getContent());
        assertEquals(date, message.getDate());
    }

    @Test
    public void testGetSenderID() {
        User sender = new User();
        sender.setUserID(1L);

        Message message = new Message();
        message.setSender(sender);

        assertEquals(1L, message.getSenderID());
    }

}
