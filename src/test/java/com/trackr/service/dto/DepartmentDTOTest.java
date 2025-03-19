package com.trackr.service.dto;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.trackr.TestUtil;

public class DepartmentDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepartmentDTO.class);
        DepartmentDTO departmentDTO1 = new DepartmentDTO();
        departmentDTO1.id = new ObjectId("id1");
        DepartmentDTO departmentDTO2 = new DepartmentDTO();
        assertThat(departmentDTO1).isNotEqualTo(departmentDTO2);
        departmentDTO2.id = departmentDTO1.id;
        assertThat(departmentDTO1).isEqualTo(departmentDTO2);
        departmentDTO2.id = new ObjectId("id2");
        assertThat(departmentDTO1).isNotEqualTo(departmentDTO2);
        departmentDTO1.id = null;
        assertThat(departmentDTO1).isNotEqualTo(departmentDTO2);
    }
}
