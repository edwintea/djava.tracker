package com.trackr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.trackr.TestUtil;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;


public class DepartmentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Department.class);
        Department department1 = new Department();
        department1.id = new ObjectId("id1");
        Department department2 = new Department();
        department2.id = department1.id;
        assertThat(department1).isEqualTo(department2);
        department2.id = new ObjectId("id2");
        assertThat(department1).isNotEqualTo(department2);
        department1.id = null;
        assertThat(department1).isNotEqualTo(department2);
    }
}
