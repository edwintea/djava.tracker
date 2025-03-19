package com.trackr.service.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ClientOverviewDTO {

    public Long outsourcedBased;

    public Long projectBased;

    public ClientOverviewDTO(Long outsourcedBased, Long projectBased) {
        this.outsourcedBased = outsourcedBased;
        this.projectBased = projectBased;
    }

    @Override
    public int hashCode() {
        int result = outsourcedBased != null ? outsourcedBased.hashCode() : 0;
        result = 31 * result + (projectBased != null ? projectBased.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ClientOverviewDTO{" +
            "outsourcedBased=" + outsourcedBased +
            ", projectBased=" + projectBased +
            '}';
    }
}
