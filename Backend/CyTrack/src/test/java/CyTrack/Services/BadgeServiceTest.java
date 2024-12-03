package CyTrack.Services;

import CyTrack.Entities.Badge;
import CyTrack.Entities.User;
import CyTrack.Repositories.BadgeRepository;
import CyTrack.Repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class BadgeServiceTest {

    @Mock
    private BadgeRepository badgeRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BadgeService badgeService;

    private User testUser;
    private Badge testBadge;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // Mock user and badge data
        testUser = new User();
        testUser.setUserID(1L);
        testUser.setTotalTime(800);
        testUser.setHighestStreak(35);

        testBadge = new Badge();
        testBadge.setBadgeID(1L);
        testBadge.setBadgeName("Test Badge");
        testBadge.setDescription("Test Description");
    }

    @Test
    void getAllBadges() {
        // Arrange
        List<Badge> badges = Arrays.asList(testBadge);
        when(badgeRepository.findAll()).thenReturn(badges);

        // Act
        List<Badge> result = badgeService.getAllBadges();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Badge", result.get(0).getBadgeName());
        verify(badgeRepository, times(1)).findAll();
    }

    @Test
    void findByBadgeId() {
        // Arrange
        when(badgeRepository.findByBadgeID(1L)).thenReturn(Optional.of(testBadge));

        // Act
        Optional<Badge> result = badgeService.findByBadgeId(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Badge", result.get().getBadgeName());
        verify(badgeRepository, times(1)).findByBadgeID(1L);
    }

    @Test
    void getUserBadges() {
        // Arrange
        testUser.setBadges(Arrays.asList(testBadge));

        // Act
        List<Badge> result = badgeService.getUserBadges(testUser);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Badge", result.get(0).getBadgeName());
    }

    /*
    @Test
    void awardBadgeToUser() {
        // Arrange
        testUser.setBadges(Arrays.asList()); // User has no badges

        // Act
        boolean awarded = badgeService.awardBadgeToUser(testUser, testBadge);

        // Assert
        assertTrue(awarded);
        assertEquals(1, testUser.getBadges().size());
        assertEquals("Test Badge", testUser.getBadges().get(0).getBadgeName());
        verify(userRepository, times(1)).save(testUser);
    }
     */

    @Test
    void awardBadgeToUser_AlreadyHasBadge() {
        // Arrange
        testUser.setBadges(Arrays.asList(testBadge)); // User already has this badge

        // Act
        boolean awarded = badgeService.awardBadgeToUser(testUser, testBadge);

        // Assert
        assertFalse(awarded);
        assertEquals(1, testUser.getBadges().size());
        verify(userRepository, never()).save(testUser);
    }

    @Test
    void awardEligibleBadges() {
        // Arrange
        when(badgeRepository.findByUserAndBadgeName(testUser, "Initiate Gymrat"))
                .thenReturn(Optional.empty());
        when(badgeRepository.findByUserAndBadgeName(testUser, "Intermediate Gymrat"))
                .thenReturn(Optional.empty());

        // Act
        badgeService.awardEligibleBadges(testUser);

        // Assert
        verify(badgeRepository, times(2)).save(any(Badge.class)); // Two badges should be awarded
        verify(userRepository, times(1)).save(testUser); // User should be saved once
    }

    @Test
    void findByBadgeName() {
        // Arrange
        when(badgeRepository.findByBadgeName("Test Badge")).thenReturn(Optional.of(testBadge));

        // Act
        Optional<Badge> result = badgeService.findByBadgeName("Test Badge");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Test Badge", result.get().getBadgeName());
        verify(badgeRepository, times(1)).findByBadgeName("Test Badge");
    }
}
