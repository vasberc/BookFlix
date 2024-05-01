package com.vasberc.domain.model

interface Domainable<DOMAIN_MODEL: Any> {
    fun toDomain(vararg args: Any): DOMAIN_MODEL
}