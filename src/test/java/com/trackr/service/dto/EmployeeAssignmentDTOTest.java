package com.trackr.service.dto;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.trackr.TestUtil;

public class EmployeeAssignmentDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeAssignmentDTO.class);
        EmployeeAssignmentDTO employeeAssignmentDTO1 = new EmployeeAssignmentDTO();
        employeeAssignmentDTO1.id = new ObjectId( "id1");
        EmployeeAssignmentDTO employeeAssignmentDTO2 = new EmployeeAssignmentDTO();
        assertThat(employeeAssignmentDTO1).isNotEqualTo(employeeAssignmentDTO2);
        employeeAssignmentDTO2.id = employeeAssignmentDTO1.id;
        assertThat(employeeAssignmentDTO1).isEqualTo(employeeAssignmentDTO2);
        employeeAssignmentDTO2.id = new ObjectId("id2");
        assertThat(employeeAssignmentDTO1).isNotEqualTo(employeeAssignmentDTO2);
        employeeAssignmentDTO1.id = null;
        assertThat(employeeAssignmentDTO1).isNotEqualTo(employeeAssignmentDTO2);
    }
}
