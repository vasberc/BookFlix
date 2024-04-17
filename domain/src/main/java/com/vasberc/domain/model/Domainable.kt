package com.vasberc.domain.model

interface Domainable<DOMAIN_MODEL: Any> {
    fun asDomain(vararg args: Any): DOMAIN_MODEL
}