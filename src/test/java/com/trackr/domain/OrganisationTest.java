package com.trackr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.trackr.TestUtil;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;


public class OrganisationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Organisation.class);
        Organisation organisation1 = new Organisation();
        organisation1.id = new ObjectId("id1");
        Organisation organisation2 = new Organisation();
        organisation2.id = organisation1.id;
        assertThat(organisation1).isEqualTo(organisation2);
        organisation2.id = new ObjectId("id2");
        assertThat(organisation1).isNotEqualTo(organisation2);
        organisation1.id = null;
        assertThat(organisation1).isNotEqualTo(organisation2);
    }
}
