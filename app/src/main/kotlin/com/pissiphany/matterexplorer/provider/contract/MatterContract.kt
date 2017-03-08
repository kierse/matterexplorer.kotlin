package com.pissiphany.matterexplorer.provider.contract

/**
 * Created by kierse on 16-05-09.
 */
object MatterContract : BaseContract {
    val TABLE = "matters"

    val CONTENT_TYPE = BaseContract.BASE_CONTENT_TYPE + TABLE;
    val CONTENT_ITEM_TYPE = BaseContract.BASE_CONTENT_TYPE + TABLE;

    object Columns {
        val _ID = "_id"
        val ID = "id"
        val UPDATED_AT = "updated_at"
        val CREATED_AT = "created_at"
        val DESCRIPTION = "description"
        val DISPLAY_NUMBER = "display_number"
        val STATUS = "status"
        val PENDING_DATE = "pending_date"
        val OPEN_DATE = "open_date"
        val CLOSE_DATE = "close_date"
        val BILLING_METHOD = "billing_method"
        val BILLABLE = "billable"
    }
}