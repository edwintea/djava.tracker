package com.trackr.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.trackr.TestUtil;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;


public class ClientTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Client.class);
        Client client1 = new Client();
        client1.id = new ObjectId("id1");
        Client client2 = new Client();
        client2.id = client1.id;
        assertThat(client1).isEqualTo(client2);
        client2.id = new ObjectId("id2");
        assertThat(client1).isNotEqualTo(client2);
        client1.id = null;
        assertThat(client1).isNotEqualTo(client2);
    }
}
