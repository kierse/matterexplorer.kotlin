package com.pissiphany.matterexplorer.provider.contract

/**
 * Created by kierse on 16-05-09.
 */
interface BaseContract {
    companion object {
        val AUTHORITY = "com.pissiphany.matterexplorer"

        val BASE_CONTENT_TYPE = "vnd.android.cursor.dir/vnd.pissiphany.";
        val BASE_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.pissiphany.";
    }
}