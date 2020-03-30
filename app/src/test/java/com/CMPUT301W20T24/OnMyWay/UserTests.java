package com.CMPUT301W20T24.OnMyWay;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * Tests for the User class. The method names here correspond to the ones they are testing
 * @author John
 */
public class UserTests {
    private User mockUser1() {
        return new User("0000", "John", "Doe", true, "johndoe@example.com", "(123) 456-7890", 10, 10);
    }

    private User mockUser2() {
        return new User("0001", "Jane", "Doe", false, "janedoe@example.com", "+12 (123) 456-7890", 0, 0);
    }

    private User mockUser3() {
        return new User("0002", "Peter", "Rabbit", false, "mrpeterrabbit@420.com", "+123 (123) 456-7890", 1, 2);
    }

    @Test
    void testSetGetFirstName() {
        User user1 = mockUser1();
        User user2 = mockUser2();
        User user3 = mockUser3();

        assertEquals("John", user1.getFirstName());
        user1.setFirstName("Terry");
        assertEquals("Terry", user1.getFirstName());

        assertEquals("Jane", user2.getFirstName());
        user2.setFirstName("Mary");
        assertEquals("Mary", user2.getFirstName());

        assertEquals("Peter", user3.getFirstName());
        user3.setFirstName("Bad");
        assertEquals("Bad", user3.getFirstName());

        assertThrows(IllegalArgumentException.class, () -> {
            user1.setFirstName("");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            user2.setFirstName(null);
        });
    }

    @Test
    void testSetGetLastName() {
        User user1 = mockUser1();
        User user2 = mockUser2();
        User user3 = mockUser3();

        assertEquals("Doe", user1.getLastName());
        user1.setLastName("Paul");
        assertEquals("Paul", user1.getLastName());

        assertEquals("Doe", user2.getLastName());
        user2.setLastName("Fonda");
        assertEquals("Fonda", user2.getLastName());

        assertEquals("Rabbit", user3.getLastName());
        user3.setLastName("Porcupine");
        assertEquals("Porcupine", user3.getLastName());

        assertThrows(IllegalArgumentException.class, () -> {
            user1.setLastName("");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            user2.setLastName(null);
        });
    }

    @Test
    void testGetFullName() {
        User user1 = mockUser1();
        User user2 = mockUser2();
        User user3 = mockUser3();

        assertEquals("John Doe", user1.getFullName());
        assertEquals("Jane Doe", user2.getFullName());
        assertEquals("Peter Rabbit", user3.getFullName());
    }

    @Test
    void testSetGetEmailGetProfilePhotoUrl() {
        User user1 = mockUser1();
        User user2 = mockUser2();
        User user3 = mockUser3();

        assertEquals("johndoe@example.com", user1.getEmail());
        assertEquals("https://www.gravatar.com/avatar/fd876f8cd6a58277fc664d47ea10ad19?d=identicon&s=512", user1.getProfilePhotoUrl());
        user1.setEmail("john@example.com");
        assertEquals("john@example.com", user1.getEmail());
        assertEquals("https://www.gravatar.com/avatar/d4c74594d841139328695756648b6bd6?d=identicon&s=512", user1.getProfilePhotoUrl());

        assertEquals("janedoe@example.com", user2.getEmail());
        assertEquals("https://www.gravatar.com/avatar/e1f3994f2632af3d1c8c2dcc168a10e6?d=identicon&s=512", user2.getProfilePhotoUrl());
        user2.setEmail("jane@example.com");
        assertEquals("jane@example.com", user2.getEmail());
        assertEquals("https://www.gravatar.com/avatar/9e26471d35a78862c17e467d87cddedf?d=identicon&s=512", user2.getProfilePhotoUrl());

        assertEquals("mrpeterrabbit@420.com", user3.getEmail());
        assertEquals("https://www.gravatar.com/avatar/dfe1653ac2d11d7fe0f149c94c9c9344?d=identicon&s=512", user3.getProfilePhotoUrl());
        user3.setEmail("mrpeterrabbit@outlook.com");
        assertEquals("mrpeterrabbit@outlook.com", user3.getEmail());
        assertEquals("https://www.gravatar.com/avatar/3e73fbe5cd01973d5c8fc0295b13d7a5?d=identicon&s=512", user3.getProfilePhotoUrl());

        assertThrows(IllegalArgumentException.class, () -> {
            user1.setEmail("");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            user2.setEmail(null);
        });
    }

    @Test
    void testSetGetPhone() {
        User user1 = mockUser1();
        User user2 = mockUser2();
        User user3 = mockUser3();

        assertEquals("(123) 456-7890", user1.getPhoneNumber());
        user1.setPhone("(000) 000-0000");
        assertEquals("(000) 000-0000", user1.getPhoneNumber());

        assertEquals("+12 (123) 456-7890", user2.getPhoneNumber());
        user2.setPhone("(000) 000-0000");
        assertEquals("(000) 000-0000", user2.getPhoneNumber());

        assertEquals("+123 (123) 456-7890", user3.getPhoneNumber());
        user3.setPhone("(000) 000-0000");
        assertEquals("(000) 000-0000", user3.getPhoneNumber());

        assertThrows(IllegalArgumentException.class, () -> {
            user1.setPhone("");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            user2.setPhone(null);
        });
    }

    @Test
    void testSetGetAddRatings() {
        User user1 = mockUser1();
        User user2 = mockUser2();
        User user3 = mockUser3();

        assertEquals(10, user1.getUpRatings());
        assertEquals(10, user1.getTotalRatings());
        user1.addRating(true);
        assertEquals(11, user1.getUpRatings());
        assertEquals(11, user1.getTotalRatings());
        assertEquals("5.0", user1.getRating());
        user1.addRating(false);
        assertEquals(11, user1.getUpRatings());
        assertEquals(12, user1.getTotalRatings());
        assertEquals("4.6", user1.getRating());

        assertEquals(0, user2.getUpRatings());
        assertEquals(0, user2.getTotalRatings());
        user2.addRating(true);
        assertEquals(1, user2.getUpRatings());
        assertEquals(1, user2.getTotalRatings());
        user2.addRating(false);
        assertEquals(1, user2.getUpRatings());
        assertEquals(2, user2.getTotalRatings());
        assertEquals("2.5", user2.getRating());

        assertEquals(1, user3.getUpRatings());
        assertEquals(2, user3.getTotalRatings());
        user3.addRating(true);
        assertEquals(2, user3.getUpRatings());
        assertEquals(3, user3.getTotalRatings());
        user3.addRating(false);
        assertEquals(2, user3.getUpRatings());
        assertEquals(4, user3.getTotalRatings());
        assertEquals("2.5", user3.getRating());
    }

    @Test
    void testGetUserId() {
        User user1 = mockUser1();
        User user2 = mockUser2();
        User user3 = mockUser3();

        assertEquals("0000", user1.getUserId());
        assertEquals("0001", user2.getUserId());
        assertEquals("0002", user3.getUserId());
    }
}
