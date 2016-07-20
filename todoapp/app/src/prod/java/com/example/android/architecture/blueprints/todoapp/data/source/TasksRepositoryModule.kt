package com.example.android.architecture.blueprints.todoapp.data.source

import com.example.android.architecture.blueprints.todoapp.data.source.local.TasksLocalDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.remote.TasksRemoteDataSource
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.singleton

val tasksRepositoryModule = Kodein.Module {
    bind<TasksDataSource>("Local") with singleton { TasksLocalDataSource(instance()) }
    bind<TasksDataSource>("Remote") with singleton { TasksRemoteDataSource() }
    bind<TasksRepository>() with singleton { TasksRepository(instance("Remote"), instance("Local")) }
}
