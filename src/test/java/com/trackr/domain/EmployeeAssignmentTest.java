package com.trackr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.trackr.TestUtil;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;


public class EmployeeAssignmentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EmployeeAssignment.class);
        EmployeeAssignment employeeAssignment1 = new EmployeeAssignment();
        employeeAssignment1.id = new ObjectId("id1");
        EmployeeAssignment employeeAssignment2 = new EmployeeAssignment();
        employeeAssignment2.id = employeeAssignment1.id;
        assertThat(employeeAssignment1).isEqualTo(employeeAssignment2);
        employeeAssignment2.id = new ObjectId("id2");
        assertThat(employeeAssignment1).isNotEqualTo(employeeAssignment2);
        employeeAssignment1.id = null;
        assertThat(employeeAssignment1).isNotEqualTo(employeeAssignment2);
    }
}
