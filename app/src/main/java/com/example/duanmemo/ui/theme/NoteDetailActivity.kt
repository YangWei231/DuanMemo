package com.example.duanmemo.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.duanmemo.R
import com.example.duanmemo.db.NoteDao
import Note

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var etContent: EditText
    private lateinit var btnSave: Button
    private lateinit var noteDao: NoteDao
    private var noteId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.memo_detail)

        etTitle = findViewById(R.id.et_title)
        etContent = findViewById(R.id.et_content)
        btnSave = findViewById(R.id.btn_save)
        noteDao = NoteDao(this)

        // 获取传递的笔记 ID
        noteId = intent.getLongExtra("note_id", 0)

        if (noteId != 0L) {
            // 编辑模式，加载笔记信息
            val note = noteDao.getAllNotes().find { it.id == noteId }
            note?.let {
                etTitle.setText(it.title)
                etContent.setText(it.content)
            }
        }

        btnSave.setOnClickListener {
            saveNote()
        }
    }

    private fun saveNote() {
        val title = etTitle.text.toString().trim()
        val content = etContent.text.toString().trim()

        if (title.isEmpty()) {
            Toast.makeText(this, "标题不能为空", Toast.LENGTH_SHORT).show()
            return
        }

        if (noteId != 0L) {
            // 更新笔记
            val note = Note(noteId, title, content)
            val rowsAffected = noteDao.updateNote(note)
            if (rowsAffected > 0) {
                Toast.makeText(this, "笔记已更新", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "更新失败", Toast.LENGTH_SHORT).show()
            }
        } else {
            // 新增笔记
            val note = Note(title = title, content = content)
            val newNoteId = noteDao.insertNote(note)
            if (newNoteId > 0) {
                Toast.makeText(this, "笔记已保存", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show()
            }
        }

        // 返回主页面
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}