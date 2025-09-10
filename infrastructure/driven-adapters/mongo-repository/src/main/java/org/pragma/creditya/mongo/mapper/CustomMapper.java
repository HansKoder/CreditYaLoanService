package org.pragma.creditya.mongo.mapper;

public interface CustomMapper<E, D> {
    D toData(E entity);
    E toEntity(D data);
}