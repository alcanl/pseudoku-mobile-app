package com.alcanl.sudoku.di.module.user

import com.alcanl.sudoku.entity.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserModule {
    @Provides
    @Singleton
    fun createUser() : User = User("Alican", "Keçici", 1, 0, 0, "alican.kecici@gmail.com", 0)
}