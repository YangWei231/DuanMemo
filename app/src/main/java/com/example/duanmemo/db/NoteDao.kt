package com.example.duanmemo.db // 替换为你的项目实际包名（如：包名+.db）

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import Note
import com.example.duanmemo.db.NoteDatabaseHelper // 导入数据库帮助类

/**
 * 数据访问对象（DAO）：封装备忘录的数据库操作
 * 依赖：NoteDatabaseHelper（创建/管理数据库）
 */
class NoteDao(private val context: Context) {

    // 数据库帮助类实例（懒加载，避免重复创建）
    private val dbHelper by lazy { NoteDatabaseHelper(context) }


    // ====================== 增（Insert） ======================
    /**
     * 插入一条笔记
     * @param note 笔记对象（id会被自动忽略，数据库自增）
     * @return 插入成功返回记录的ID（>0），失败返回-1
     */
    fun insertNote(note: Note): Long {
        val db = dbHelper.writableDatabase // 获取可写数据库
        val values = ContentValues().apply {
            put(NoteDatabaseHelper.COLUMN_TITLE, note.title)   // 标题
            put(NoteDatabaseHelper.COLUMN_CONTENT, note.content) // 内容
            put(NoteDatabaseHelper.COLUMN_TIME, note.time)     // 时间戳
        }
        return db.insert(NoteDatabaseHelper.TABLE_NOTE, null, values)
    }


    // ====================== 改（Update） ======================
    /**
     * 更新一条笔记
     * @param note 笔记对象（必须包含有效id）
     * @return 受影响的行数（1表示成功，0表示失败）
     */
    fun updateNote(note: Note): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(NoteDatabaseHelper.COLUMN_TITLE, note.title)
            put(NoteDatabaseHelper.COLUMN_CONTENT, note.content)
            put(NoteDatabaseHelper.COLUMN_TIME, note.time)
        }
        // WHERE 条件：通过id定位记录
        val whereClause = "${NoteDatabaseHelper.COLUMN_ID} = ?"
        val whereArgs = arrayOf(note.id.toString())

        return db.update(
            NoteDatabaseHelper.TABLE_NOTE,
            values,
            whereClause,
            whereArgs
        )
    }


    // ====================== 删（Delete） ======================
    /**
     * 根据ID删除笔记
     * @param noteId 笔记的唯一ID
     * @return 受影响的行数（1表示成功，0表示失败）
     */
    fun deleteNote(noteId: Long): Int {
        val db = dbHelper.writableDatabase
        val whereClause = "${NoteDatabaseHelper.COLUMN_ID} = ?"
        val whereArgs = arrayOf(noteId.toString())

        return db.delete(
            NoteDatabaseHelper.TABLE_NOTE,
            whereClause,
            whereArgs
        )
    }


    // ====================== 查（Query） ======================
    /**
     * 查询所有笔记（按时间倒序排列，最新的在前）
     * @return 笔记列表（空列表表示无数据）
     */
    fun getAllNotes(): List<Note> {
        val db = dbHelper.readableDatabase // 获取只读数据库
        val notes = mutableListOf<Note>()

        // 查询语句：SELECT * FROM note ORDER BY time DESC
        val cursor: Cursor = db.query(
            NoteDatabaseHelper.TABLE_NOTE,          // 表名
            null,                                   // 查询所有列（等价于 *）
            null,                                   // WHERE 条件（空表示无过滤）
            null,                                   // WHERE 参数
            null,                                   // GROUP BY（空表示不分组）
            null,                                   // HAVING（空表示无筛选）
            "${NoteDatabaseHelper.COLUMN_TIME} DESC" // 排序：时间倒序
        )

        // 遍历Cursor，转换为Note对象
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(NoteDatabaseHelper.COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabaseHelper.COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(NoteDatabaseHelper.COLUMN_CONTENT))
            val time = cursor.getLong(cursor.getColumnIndexOrThrow(NoteDatabaseHelper.COLUMN_TIME))

            notes.add(Note(id, title, content, time))
        }

        cursor.close() // 关闭Cursor，避免内存泄漏
        return notes
    }
}