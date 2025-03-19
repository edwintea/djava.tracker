package com.trackr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.trackr.TestUtil;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;


public class AnnualLeaveTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnnualLeave.class);
        AnnualLeave annualLeave1 = new AnnualLeave();
        annualLeave1.id = new ObjectId("id1");
        AnnualLeave annualLeave2 = new AnnualLeave();
        annualLeave2.id = annualLeave1.id;
        assertThat(annualLeave1).isEqualTo(annualLeave2);
        annualLeave2.id = new ObjectId("id2");
        assertThat(annualLeave1).isNotEqualTo(annualLeave2);
        annualLeave1.id = null;
        assertThat(annualLeave1).isNotEqualTo(annualLeave2);
    }
}
