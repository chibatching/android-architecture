package com.example.android.architecture.blueprints.todoapp.statistics

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance

/**
 * This is a Dagger module. We use this to pass in the View dependency to the
 * [StatisticsPresenter].
 */
fun statisticsPresenterModule(view: StatisticsContract.View) = Kodein.Module {
    bind<StatisticsContract.View>() with instance(view)
}
