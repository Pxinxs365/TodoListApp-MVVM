package com.pxinxs.todolistapp.presentation.di

import com.pxinxs.todolistapp.presentation.utils.dateprovider.DateProvider
import com.pxinxs.todolistapp.presentation.utils.dateprovider.IDateProvider
import com.pxinxs.todolistapp.presentation.utils.uuidgenerator.IUuidGenerator
import com.pxinxs.todolistapp.presentation.utils.uuidgenerator.UuidGenerator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelComponentsModule {

    @Binds
    abstract fun provideDataProvider(dataProvider: DateProvider): IDateProvider

    @Binds
    abstract fun provideUuidGenerator(uuidGenerator: UuidGenerator): IUuidGenerator
}