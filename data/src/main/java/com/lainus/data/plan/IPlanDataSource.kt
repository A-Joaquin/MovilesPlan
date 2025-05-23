package com.lainus.data.plan


import com.lainus.data.NetworkResult
import com.lainus.domain.Plan

interface IPlanDataSource {
    suspend fun fetchPlans(): NetworkResult<List<Plan>>
}
