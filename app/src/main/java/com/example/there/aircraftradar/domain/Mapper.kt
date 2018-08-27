package com.example.there.aircraftradar.domain

interface Mapper<in E, out M> {
    fun fromEntity(entity: E): M
}