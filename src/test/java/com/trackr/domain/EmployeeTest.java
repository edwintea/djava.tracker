package com.trackr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.trackr.TestUtil;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;


public class EmployeeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Employee.class);
        Employee employee1 = new Employee();
        employee1.id = new ObjectId("id1");
        Employee employee2 = new Employee();
        employee2.id = employee1.id;
        assertThat(employee1).isEqualTo(employee2);
        employee2.id = new ObjectId("id2");
        assertThat(employee1).isNotEqualTo(employee2);
        employee1.id = null;
        assertThat(employee1).isNotEqualTo(employee2);
    }
}
