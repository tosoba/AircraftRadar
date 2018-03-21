package com.example.there.aircraftradar.data.base.mapper

interface TwoWayMapper<E, M>: OneWayMapper<E, M> {
    fun toEntity(model: M): E
}