package com.trackr.service.dto;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.trackr.TestUtil;

public class AssignmentDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AssignmentDTO.class);
        AssignmentDTO assignmentDTO1 = new AssignmentDTO();
        assignmentDTO1.id = new ObjectId("id1");
        AssignmentDTO assignmentDTO2 = new AssignmentDTO();
        assertThat(assignmentDTO1).isNotEqualTo(assignmentDTO2);
        assignmentDTO2.id = assignmentDTO1.id;
        assertThat(assignmentDTO1).isEqualTo(assignmentDTO2);
        assignmentDTO2.id = new ObjectId("id2");
        assertThat(assignmentDTO1).isNotEqualTo(assignmentDTO2);
        assignmentDTO1.id = null;
        assertThat(assignmentDTO1).isNotEqualTo(assignmentDTO2);
    }
}
