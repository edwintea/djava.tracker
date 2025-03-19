package com.trackr.service.dto;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.trackr.TestUtil;

public class EmployeeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeDTO.class);
        EmployeeDTO employeeDTO1 = new EmployeeDTO();
        employeeDTO1.id = new ObjectId("id1");
        EmployeeDTO employeeDTO2 = new EmployeeDTO();
        assertThat(employeeDTO1).isNotEqualTo(employeeDTO2);
        employeeDTO2.id = employeeDTO1.id;
        assertThat(employeeDTO1).isEqualTo(employeeDTO2);
        employeeDTO2.id = new ObjectId("id2");
        assertThat(employeeDTO1).isNotEqualTo(employeeDTO2);
        employeeDTO1.id = null;
        assertThat(employeeDTO1).isNotEqualTo(employeeDTO2);
    }
}
