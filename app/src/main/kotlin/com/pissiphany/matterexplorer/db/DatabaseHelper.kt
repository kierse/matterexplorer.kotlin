package com.pissiphany.matterexplorer.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.pissiphany.matterexplorer.provider.contract.MatterContract

/**
 * Created by kierse on 16-05-08.
 */
class DatabaseHelper(val context: Context) :
      SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION)
{
    companion object {
        val DATABASE_NAME = "matter_explorer.db"
        val DATABASE_VERSION = 1;

        val DATABASE_CREATE = """
                CREATE TABLE ${MatterContract.TABLE}
                (
                    ${MatterContract.Columns._ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                    ${MatterContract.Columns.ID} INTEGER UNIQUE,
                    ${MatterContract.Columns.UPDATED_AT} TEXT,
                    ${MatterContract.Columns.CREATED_AT} TEXT,
                    ${MatterContract.Columns.DESCRIPTION} TEXT,
                    ${MatterContract.Columns.DISPLAY_NUMBER} TEXT,
                    ${MatterContract.Columns.STATUS} TEXT,
                    ${MatterContract.Columns.PENDING_DATE} TEXT,
                    ${MatterContract.Columns.OPEN_DATE} TEXT,
                    ${MatterContract.Columns.CLOSE_DATE} TEXT,
                    ${MatterContract.Columns.BILLING_METHOD} TEXT,
                    ${MatterContract.Columns.BILLABLE} INTEGER DEFAULT 1
                );
        """
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DATABASE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // DO NOTHING! No support for upgrading the database at this point
    }
}