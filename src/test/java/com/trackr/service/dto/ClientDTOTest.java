package com.trackr.service.dto;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.trackr.TestUtil;

public class ClientDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClientDTO.class);
        ClientDTO clientDTO1 = new ClientDTO();
        clientDTO1.id = new ObjectId("id1");
        ClientDTO clientDTO2 = new ClientDTO();
        assertThat(clientDTO1).isNotEqualTo(clientDTO2);
        clientDTO2.id = clientDTO1.id;
        assertThat(clientDTO1).isEqualTo(clientDTO2);
        clientDTO2.id = new ObjectId("id2");
        assertThat(clientDTO1).isNotEqualTo(clientDTO2);
        clientDTO1.id = null;
        assertThat(clientDTO1).isNotEqualTo(clientDTO2);
    }
}
