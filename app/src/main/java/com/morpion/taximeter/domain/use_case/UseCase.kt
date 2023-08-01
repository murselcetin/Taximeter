package com.morpion.taximeter.domain.use_case

import kotlinx.coroutines.flow.Flow

interface UseCaseWithFlow<in Input, out Output> {
    suspend operator fun invoke(input: Input): Flow<Output>
}

interface NoInputUseCaseWithFlow<out Output> {
    suspend operator fun invoke(): Flow<Output>
}

interface NoInputAndOutputUseCaseWithFlow {
    suspend operator fun invoke()
}

interface NoOutputUseCaseWithFlow<in Input> {
    suspend operator fun invoke(input: Input)
}
