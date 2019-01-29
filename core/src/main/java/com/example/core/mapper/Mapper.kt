package com.example.core.mapper

interface Mapper<in E, out M> {
    fun fromEntity(entity: E): M
}