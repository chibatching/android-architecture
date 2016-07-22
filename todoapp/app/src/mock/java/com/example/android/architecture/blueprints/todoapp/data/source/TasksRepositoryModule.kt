package com.example.android.architecture.blueprints.todoapp.data.source

import com.example.android.architecture.blueprints.todoapp.data.FakeTasksRemoteDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.local.TasksLocalDataSource
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton

/**
 * This is used by Dagger to inject the required arguments into the [TasksRepository].
 */
val tasksRepositoryModule = Kodein.Module {
    bind<TasksDataSource>("Local") with singleton { TasksLocalDataSource(instance()) }
    bind<TasksDataSource>("Remote") with singleton { FakeTasksRemoteDataSource() }
}
