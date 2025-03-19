package com.trackr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.trackr.TestUtil;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;


public class AssignmentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Assignment.class);
        Assignment assignment1 = new Assignment();
        assignment1.id = new ObjectId("id1");
        Assignment assignment2 = new Assignment();
        assignment2.id = assignment1.id;
        assertThat(assignment1).isEqualTo(assignment2);
        assignment2.id = new ObjectId("id2");
        assertThat(assignment1).isNotEqualTo(assignment2);
        assignment1.id = null;
        assertThat(assignment1).isNotEqualTo(assignment2);
    }
}
