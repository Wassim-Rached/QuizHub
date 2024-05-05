package org.wa55death405.quizhub.interfaces;

public interface EntityDTO<T,K> {
    T toEntity(K additionalData);
}
