package com.vasberc.domain.di

import com.vasberc.domain.bookspaging.BooksRemoteMediator
import com.vasberc.domain.bookspaging.BooksRemoteMediatorImpl
import com.vasberc.domain.repo.BooksLocalRepo
import com.vasberc.domain.repo.BooksRemoteRepo
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.vasberc.domain")
class DomainModule
