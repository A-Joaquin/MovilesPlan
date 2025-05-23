package com.lainus.examen.di



import com.lainus.data.PlanRepository
import com.lainus.data.plan.IPlanDataSource
import com.lainus.usecases.GetPlans
import com.lainus.framework.plan.PlanDataSource


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ppModule {




    @Provides
    @Singleton
    fun providePlanDataSource(): IPlanDataSource {
        return PlanDataSource()
    }

    @Provides
    @Singleton
    fun providePlanRepository(dataSource: IPlanDataSource): PlanRepository {
        return PlanRepository(dataSource)
    }

    @Provides
    @Singleton
    fun provideGetPlans(planRepository: PlanRepository): GetPlans {
        return GetPlans(planRepository)
    }

}
