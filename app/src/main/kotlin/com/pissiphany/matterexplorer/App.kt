package com.pissiphany.matterexplorer

import android.app.Application
import android.net.Uri
import com.pissiphany.matterexplorer.di.HasComponent
import net.danlew.android.joda.JodaTimeAndroid

/**
 * Created by kierse on 16-05-01.
 */
class App() : Application(), HasComponent<ApplicationComponent> {
    private lateinit var graph: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        /**
         * Note: a resource file must exist at res/values/themis_api.xml and contain the following info:
         *
         * <resources>
         *     <string name="scheme">https</string>
         *     <string name="authority">some.url.com</string>
         *     <string name="token">oauth token value</string>
         * </resources>
         *
         */
        val rootUri : Uri = Uri.parse(resources.getString(R.string.api_root))
        val token : String = resources.getString(R.string.token)

        graph = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this, rootUri, token))
                .build()
        graph.inject(this)

        JodaTimeAndroid.init(this);
    }

    override fun getComponent(): ApplicationComponent = graph
}