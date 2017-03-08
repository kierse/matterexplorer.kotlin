package com.pissiphany.matterexplorer.di

/**
 * Created by kierse on 16-05-01.
 */
interface HasComponent<C> {
    fun getComponent(): C
}