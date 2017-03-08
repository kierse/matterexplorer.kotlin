package com.pissiphany.matterexplorer.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.text.TextUtils

import com.pissiphany.matterexplorer.db.DatabaseHelper
import com.pissiphany.matterexplorer.provider.contract.BaseContract
import com.pissiphany.matterexplorer.provider.contract.MatterContract

import java.util.ArrayList
import java.util.Arrays

/**
 * Created by kierse on 15-08-23.
 */
class MatterExplorerContentProvider : ContentProvider() {
    private lateinit var databaseHelper: DatabaseHelper
    private val NO_ID = 0L

    override fun onCreate(): Boolean {
        databaseHelper = DatabaseHelper(context)
        return true
    }

    override fun bulkInsert(uri: Uri, values: Array<ContentValues>): Int {
        var count = 0
        val table = getTable(uri)

        val db = databaseHelper.writableDatabase
        try {
            db.beginTransaction()
            values.forEach {
                val rowId = upsert(table, it)
                if (rowId > -1) count++
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }

        if (count > 0) notifyChange(uri)

        return count
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val table = getTable(uri) // this checks for unknown uri first
        if (values != null) {
            val rowId = upsert(table, values)

            var rowPath: Uri? = null
            if (rowId > 0) {
                context!!.contentResolver.notifyChange(uri, null)
                rowPath = ContentUris.withAppendedId(uri, rowId)
            }

            return rowPath
        }

        return null
    }

    override fun update(
            uri: Uri,
            values: ContentValues?,
            selection: String?,
            selectionArgs: Array<String>
    ): Int {
        val builder = SelectionBuilder().add(selection, *selectionArgs)

        val id = extractRecordId(uri)
        if (id > NO_ID) builder.add("id = ?", id.toString())

        val result = databaseHelper.writableDatabase.update(getTable(uri), values, builder.selection, builder.selectionArgs)
        if (result > 0) context!!.contentResolver.notifyChange(uri, null)

        return result
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>): Int {
        val builder = SelectionBuilder().add(selection, *selectionArgs)

        val id = extractRecordId(uri)
        if (id > NO_ID) builder.add("id = ?", id.toString())

        val result = databaseHelper.writableDatabase.delete(getTable(uri), builder.selection, builder.selectionArgs)
        if (result > 0) context!!.contentResolver.notifyChange(uri, null)

        return result
    }

    private fun upsert(table: String, values: ContentValues): Long {
        return databaseHelper.writableDatabase.insertWithOnConflict(table, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    override fun query(
            uri: Uri,
            projection: Array<String>,
            selection: String?,
            selectionArgs: Array<String>,
            sortOrder: String?
    ): Cursor? {
        val builder = getQueryBuilder(uri)

        val db = databaseHelper.writableDatabase
        val cursor = builder.query(db, projection, selection, selectionArgs, null, null, sortOrder)
        cursor?.setNotificationUri(context.contentResolver, uri)

        return cursor
    }

    override fun getType(uri: Uri): String {
        return when (uriMatcher.match(uri)) {
            MATTERS -> MatterContract.CONTENT_TYPE
            MATTER_ID -> MatterContract.CONTENT_ITEM_TYPE

            else -> throw IllegalArgumentException("unknown type: " + uri)
        }
    }

    private fun getTable(uri: Uri): String {
        return when (uriMatcher.match(uri)) {
            MATTERS, MATTER_ID -> MatterContract.TABLE
            else -> throw UnsupportedOperationException("unknown table: " + uri)
        }
    }

    private fun extractRecordId(uri: Uri): Long {
        if (Regex("[^\\d]").matches(uri.lastPathSegment)) {
            return try { uri.lastPathSegment.toLong() } catch (e: NumberFormatException) { NO_ID }
        }

        return NO_ID
    }

    private fun getQueryBuilder(uri: Uri): SQLiteQueryBuilder {
        val builder = SQLiteQueryBuilder()
        builder.tables = getTable(uri)

        // TODO comment on this
        builder.setStrict(true)

        //        switch (sUriMatcher.getMatchCode(uri)) {
        //            default:
        //
        //        }

        val id = extractRecordId(uri)
        if (id > NO_ID) builder.appendWhere("id = " + id.toString())

        return builder
    }

    private fun notifyChange(uri: Uri) {
        context!!.contentResolver.notifyChange(uri, null)
    }

    companion object {

        private val MATTERS = 100
        private val MATTER_ID = 101

        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        init {
            uriMatcher.addURI(BaseContract.AUTHORITY, MatterContract.TABLE, MATTERS)
            uriMatcher.addURI(BaseContract.AUTHORITY, MatterContract.TABLE + "/#", MATTER_ID)
        }
    }

    private class SelectionBuilder {
        private val _selection: MutableList<String>
        private val _selectionArgs: MutableList<String>

        init {
            _selection = ArrayList<String>()
            _selectionArgs = ArrayList<String>()
        }

        val selection: String?
            get() = if (_selection.size > 0) TextUtils.join(" AND ", _selection)
            else null

        val selectionArgs: Array<String>?
            get() {
                var args: Array<String>? = null
                if (_selectionArgs.size > 0) {
                    args = _selectionArgs.toTypedArray();
                }

                return args
            }

        fun add(selection: String?, vararg args: String): SelectionBuilder {
            if (selection != null && !selection.isEmpty()) {
                _selection.add(selection)
                if (args.size > 0) _selectionArgs.addAll(Arrays.asList(*args))
            }

            return this
        }
    }
}
