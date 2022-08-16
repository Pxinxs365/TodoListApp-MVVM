package com.pxinxs.todolistapp.presentation.utils.uuidgenerator

import java.util.UUID
import javax.inject.Inject

class UuidGenerator @Inject constructor() : IUuidGenerator {
    override fun getNewUuid() = UUID.randomUUID().toString()
}