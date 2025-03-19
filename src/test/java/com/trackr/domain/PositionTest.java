package com.trackr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.trackr.TestUtil;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;


public class PositionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Position.class);
        Position position1 = new Position();
        position1.id = new ObjectId("id1");
        Position position2 = new Position();
        position2.id = position1.id;
        assertThat(position1).isEqualTo(position2);
        position2.id = new ObjectId("id2");
        assertThat(position1).isNotEqualTo(position2);
        position1.id = null;
        assertThat(position1).isNotEqualTo(position2);
    }
}
