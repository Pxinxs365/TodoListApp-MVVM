package com.pxinxs.todolistapp.presentation.di

import com.pxinxs.todolistapp.data.repositories.ITasksRepository
import com.pxinxs.todolistapp.data.repositories.TasksRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideTasksRepository(): ITasksRepository = TasksRepository()
}