package com.pissiphany.matterexplorer

import android.app.Application
import android.net.Uri
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by kierse on 16-05-08.
 */
@Module
class ApplicationModule(
        private val application: Application, private val rootUri: Uri, private val apiToken: String
) {
    companion object {
        const val API_ROOT: String = "api_root"
        const val API_TOKEN: String = "api_token"
    }

    @Provides
    @Singleton
    fun providesApplication(): Application = application

    @Provides
    @Named(API_ROOT)
    fun providesRootUri(): Uri = rootUri

    @Provides
    @Named(API_TOKEN)
    fun providesApiToken(): String = apiToken
}
