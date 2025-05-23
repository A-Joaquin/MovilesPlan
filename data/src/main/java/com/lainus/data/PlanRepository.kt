package com.lainus.data

import com.lainus.data.plan.IPlanDataSource
import com.lainus.domain.Plan

class PlanRepository(
    val dataSource: IPlanDataSource
) {
    suspend fun getPlans(): NetworkResult<List<Plan>> {
        return dataSource.fetchPlans()
    }
}