package com.pxinxs.todolistapp.presentation.utils.uuidgenerator

import java.util.UUID

class UuidGenerator : IUuidGenerator {
    override fun getNewUuid() = UUID.randomUUID().toString()
}