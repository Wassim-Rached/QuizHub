package org.wa55death405.quizhub.interfaces.dto;

/*
    interface with one purpose
    help to convert DTO to Entity
    T - Entity
    K - additional data
 */

public interface EntityDTO<T,K> {
    T toEntity(K additionalData);
}
