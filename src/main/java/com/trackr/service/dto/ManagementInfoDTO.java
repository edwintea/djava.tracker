package com.trackr.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.json.bind.annotation.JsonbProperty;
import java.util.ArrayList;
import java.util.List;

/**
*  DTO to emulate /management/info response
*/
@RegisterForReflection
public class ManagementInfoDTO {

    public List<String> activeProfiles = new ArrayList<>();

    @JsonbProperty("display-ribbon-on-profiles")
    public String displayRibbonOnProfiles;

}
