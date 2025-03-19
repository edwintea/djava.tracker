package com.trackr.service.dto;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.trackr.TestUtil;

public class OrganisationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrganisationDTO.class);
        OrganisationDTO organisationDTO1 = new OrganisationDTO();
        organisationDTO1.id = new ObjectId("id1");
        OrganisationDTO organisationDTO2 = new OrganisationDTO();
        assertThat(organisationDTO1).isNotEqualTo(organisationDTO2);
        organisationDTO2.id = organisationDTO1.id;
        assertThat(organisationDTO1).isEqualTo(organisationDTO2);
        organisationDTO2.id = new ObjectId("id2");
        assertThat(organisationDTO1).isNotEqualTo(organisationDTO2);
        organisationDTO1.id = null;
        assertThat(organisationDTO1).isNotEqualTo(organisationDTO2);
    }
}
