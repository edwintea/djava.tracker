package com.trackr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.trackr.TestUtil;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;


public class NotificationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Notification.class);
        Notification notification1 = new Notification();
        notification1.id = new ObjectId("id1");
        Notification notification2 = new Notification();
        notification2.id = notification1.id;
        assertThat(notification1).isEqualTo(notification2);
        notification2.id = new ObjectId("id2");
        assertThat(notification1).isNotEqualTo(notification2);
        notification1.id = null;
        assertThat(notification1).isNotEqualTo(notification2);
    }
}
