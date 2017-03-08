package com.pissiphany.matterexplorer

import dagger.Component
import javax.inject.Singleton

/**
 * Created by kierse on 16-05-01.
 */
@Singleton
@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {
    fun inject(app: App)
}