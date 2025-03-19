package com.trackr.web.util;

import javax.security.auth.login.AccountNotFoundException;
import javax.ws.rs.core.HttpHeaders;

public final class RequestHeaderUtil {

    public static String getOrganisationId(HttpHeaders httpHeaders) throws AccountNotFoundException {

        String organisationId = httpHeaders.getHeaderString("Organisation");

        if(organisationId == null) {
            throw new AccountNotFoundException("Organisation not found!");
        }
        return organisationId;
    }

}
