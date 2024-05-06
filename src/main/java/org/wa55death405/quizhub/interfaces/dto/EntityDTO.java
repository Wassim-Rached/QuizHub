package org.wa55death405.quizhub.interfaces.dto;

public interface EntityDTO<T,K> {
    T toEntity(K additionalData);
}
