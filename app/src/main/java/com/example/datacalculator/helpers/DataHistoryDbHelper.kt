package com.example.datacalculator.helpers

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.datacalculator.model.DataHistoryModel

class DataHistoryDbHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_USER_ACTIVITY_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_TIME + " TEXT,"
                + COLUMN_FROM + " TEXT,"
                + COLUMN_TO + " TEXT,"
                + COLUMN_BYTES + " INTEGER"
                + ")")
        db.execSQL(CREATE_USER_ACTIVITY_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }

    fun addUsageHistory(dataHistoryModel: DataHistoryModel) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_DATE, dataHistoryModel.getDate())
        values.put(COLUMN_TIME, dataHistoryModel.getTime())
        values.put(COLUMN_FROM, dataHistoryModel.getFrom())
        values.put(COLUMN_TO, dataHistoryModel.getTo())
        values.put(COLUMN_BYTES, dataHistoryModel.getBytes())
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun getDataHistoryList() : List<DataHistoryModel> {
        val dataHistoryList: MutableList<DataHistoryModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_DATE DESC, $COLUMN_TIME DESC"
        val db = this.writableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val userActivity = DataHistoryModel()
                userActivity.setId(cursor.getString(0).toInt())
                userActivity.setDate(cursor.getString(1))
                userActivity.setTime(cursor.getString(2))
                userActivity.setFrom(cursor.getString(3))
                userActivity.setTo(cursor.getString(4))
                userActivity.setBytes(cursor.getString(5).toDouble())
                dataHistoryList.add(userActivity)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return dataHistoryList
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "db_activity"
        private const val TABLE_NAME = "table_usage_history"
        private const val COLUMN_ID = "id"
        private const val COLUMN_DATE = "history_date"
        private const val COLUMN_TIME= "history_time"
        private const val COLUMN_FROM = "history_from"
        private const val COLUMN_TO = "history_to"
        private const val COLUMN_BYTES = "history_data_usage"
    }
}
