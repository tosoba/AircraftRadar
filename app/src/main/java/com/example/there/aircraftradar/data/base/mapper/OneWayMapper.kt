package com.example.there.aircraftradar.data.base.mapper

interface OneWayMapper<in E, out M> {
    fun fromEntity(entity: E): M
}