package com.pissiphany.matterexplorer.model

import org.joda.time.DateTime

/**
 * Created by kierse on 16-05-01.
 */
data class Matter(
        val id: Int,
        val createdAt: DateTime,
        val updatedAt: DateTime,

        val description: String,
        val displayNumber: String,
        val status: String,
        val billingMethod: String,

        val billable: Boolean,

        val pendingDate: DateTime,
        val openDate: DateTime,
        val closeDate: DateTime
)
