package com.trackr.service.dto;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.trackr.TestUtil;

public class NewsletterDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(NewsletterDTO.class);
        NewsletterDTO newsletterDTO1 = new NewsletterDTO();
        newsletterDTO1.id = new ObjectId("id1");
        NewsletterDTO newsletterDTO2 = new NewsletterDTO();
        assertThat(newsletterDTO1).isNotEqualTo(newsletterDTO2);
        newsletterDTO2.id = newsletterDTO1.id;
        assertThat(newsletterDTO1).isEqualTo(newsletterDTO2);
        newsletterDTO2.id = new ObjectId("id2");
        assertThat(newsletterDTO1).isNotEqualTo(newsletterDTO2);
        newsletterDTO1.id = null;
        assertThat(newsletterDTO1).isNotEqualTo(newsletterDTO2);
    }
}
