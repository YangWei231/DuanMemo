package com.example.duanmemo.db // 替换为你的实际包名（如项目包名+".db"）

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * 数据库帮助类：负责创建、升级备忘录数据库
 * 表结构：note 表包含字段：_id（主键）、title（标题）、content（内容）、time（时间戳）
 */
class NoteDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    // 数据库常量（推荐通过 companion object 定义，方便外部引用）
    companion object {
        // 数据库名称
        const val DB_NAME = "note_database.db"
        // 数据库版本（升级时需递增）
        const val DB_VERSION = 1

        // 表名
        const val TABLE_NOTE = "note"
        // 列名
        const val COLUMN_ID = "_id"          // 主键，自增
        const val COLUMN_TITLE = "title"     // 笔记标题（非空）
        const val COLUMN_CONTENT = "content" // 笔记内容
        const val COLUMN_TIME = "time"       // 时间戳（非空，记录创建/修改时间）
    }

    /**
     * 数据库首次创建时调用：执行建表 SQL
     */
    override fun onCreate(db: SQLiteDatabase) {
        val createTableSql = """
            CREATE TABLE $TABLE_NOTE (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_CONTENT TEXT,
                $COLUMN_TIME INTEGER NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTableSql)
    }

    /**
     * 数据库版本升级时调用（如 DB_VERSION 递增）
     * 此处为简化实现，直接删除旧表并重建（实际项目需处理数据迁移！）
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // 1. 删除旧表
        val dropTableSql = "DROP TABLE IF EXISTS $TABLE_NOTE"
        db.execSQL(dropTableSql)
        // 2. 重建新表
        onCreate(db)
    }
}