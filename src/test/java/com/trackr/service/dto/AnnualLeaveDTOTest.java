package com.trackr.service.dto;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.trackr.TestUtil;

public class AnnualLeaveDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnnualLeaveDTO.class);
        AnnualLeaveDTO annualLeaveDTO1 = new AnnualLeaveDTO();
        annualLeaveDTO1.id = new ObjectId("id1");
        AnnualLeaveDTO annualLeaveDTO2 = new AnnualLeaveDTO();
        assertThat(annualLeaveDTO1).isNotEqualTo(annualLeaveDTO2);
        annualLeaveDTO2.id = annualLeaveDTO1.id;
        assertThat(annualLeaveDTO1).isEqualTo(annualLeaveDTO2);
        annualLeaveDTO2.id = new ObjectId("id2");
        assertThat(annualLeaveDTO1).isNotEqualTo(annualLeaveDTO2);
        annualLeaveDTO1.id = null;
        assertThat(annualLeaveDTO1).isNotEqualTo(annualLeaveDTO2);
    }
}
