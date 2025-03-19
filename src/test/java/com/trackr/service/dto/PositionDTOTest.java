package com.trackr.service.dto;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.trackr.TestUtil;

public class PositionDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PositionDTO.class);
        PositionDTO positionDTO1 = new PositionDTO();
        positionDTO1.id = new ObjectId("id1");
        PositionDTO positionDTO2 = new PositionDTO();
        assertThat(positionDTO1).isNotEqualTo(positionDTO2);
        positionDTO2.id = positionDTO1.id;
        assertThat(positionDTO1).isEqualTo(positionDTO2);
        positionDTO2.id = new ObjectId("id2");
        assertThat(positionDTO1).isNotEqualTo(positionDTO2);
        positionDTO1.id = null;
        assertThat(positionDTO1).isNotEqualTo(positionDTO2);
    }
}
