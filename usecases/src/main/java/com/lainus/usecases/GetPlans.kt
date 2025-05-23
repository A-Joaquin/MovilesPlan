package com.lainus.usecases

import com.lainus.data.NetworkResult
import com.lainus.data.PlanRepository
import com.lainus.domain.Plan

class GetPlans(
    private val planRepository: PlanRepository
) {
    suspend fun invoke(): NetworkResult<List<Plan>> {
        return planRepository.getPlans()
    }
}