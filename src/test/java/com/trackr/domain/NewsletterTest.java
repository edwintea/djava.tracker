package com.trackr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.trackr.TestUtil;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;


public class NewsletterTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Newsletter.class);
        Newsletter newsletter1 = new Newsletter();
        newsletter1.id = new ObjectId("id1");
        Newsletter newsletter2 = new Newsletter();
        newsletter2.id = newsletter1.id;
        assertThat(newsletter1).isEqualTo(newsletter2);
        newsletter2.id = new ObjectId("id2");
        assertThat(newsletter1).isNotEqualTo(newsletter2);
        newsletter1.id = null;
        assertThat(newsletter1).isNotEqualTo(newsletter2);
    }
}
