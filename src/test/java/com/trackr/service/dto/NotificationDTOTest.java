package com.trackr.service.dto;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.trackr.TestUtil;

public class NotificationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NotificationDTO.class);
        NotificationDTO notificationDTO1 = new NotificationDTO();
        notificationDTO1.id = new ObjectId("id1");
        NotificationDTO notificationDTO2 = new NotificationDTO();
        assertThat(notificationDTO1).isNotEqualTo(notificationDTO2);
        notificationDTO2.id = notificationDTO1.id;
        assertThat(notificationDTO1).isEqualTo(notificationDTO2);
        notificationDTO2.id = new ObjectId("id2");
        assertThat(notificationDTO1).isNotEqualTo(notificationDTO2);
        notificationDTO1.id = null;
        assertThat(notificationDTO1).isNotEqualTo(notificationDTO2);
    }
}
