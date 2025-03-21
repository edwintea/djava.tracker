package com.trackr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.trackr.TestUtil;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;

public class UserTest {

    @Test
    public void testUserEquals() throws Exception {
        TestUtil.equalsVerifier(User.class);
        var user1 = new User();
        user1.id = new ObjectId( "1");
        var user2 = new User();
        user2.id = user1.id;
        assertThat(user1).isEqualTo(user2);
        user2.id = new ObjectId("2");
        assertThat(user1).isNotEqualTo(user2);
        user1.id = null;
        assertThat(user1).isNotEqualTo(user2);
    }
}
