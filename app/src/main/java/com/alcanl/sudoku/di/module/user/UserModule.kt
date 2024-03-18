package com.alcanl.sudoku.di.module.user

import com.alcanl.sudoku.repository.entity.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {
    @Provides
    @Singleton
    fun createUser() : User = User("alcanl","1234","Alican", "Keçici", eMail = "alican.kecici@gmail.com")
}