package com.example.duanmemo.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.duanmemo.R
import com.example.duanmemo.adapter.NoteAdapter
import com.example.duanmemo.db.NoteDao
import Note
import PrefsUtil

class MainActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var noteAdapter: NoteAdapter
    private val noteDao by lazy { NoteDao(this) }
    private val notes = mutableListOf<Note>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 初始化控件
        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.recycler_view)
        tvEmpty = findViewById(R.id.tv_empty)

        // 设置Toolbar
        setSupportActionBar(toolbar)

        // 配置RecyclerView
        noteAdapter = NoteAdapter(notes) { note ->
            // 点击笔记跳转到详情页
            val intent = Intent(this, NoteDetailActivity::class.java)
            intent.putExtra("note_id", note.id)
            startActivity(intent)
        }

        // 设置长按删除事件
        noteAdapter.setOnLongClickListener { note ->
            showDeleteDialog(note)
            true
        }

        // 配置RecyclerView布局和适配器
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = noteAdapter
    }

    override fun onResume() {
        super.onResume()
        // 刷新笔记列表
        loadNotes()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_add -> {
                // 跳转到新建笔记页
                startActivity(Intent(this, NoteDetailActivity::class.java))
                true
            }
            R.id.action_logout -> {
                // 退出登录
                PrefsUtil.setLoggedIn(this, false)
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadNotes() {
        // 从数据库加载所有笔记
        notes.clear()
        notes.addAll(noteDao.getAllNotes())
        noteAdapter.notifyDataSetChanged()

        // 显示或隐藏空状态提示
        if (notes.isEmpty()) {
            tvEmpty.visibility = View.VISIBLE
        } else {
            tvEmpty.visibility = View.GONE
        }
    }

    private fun showDeleteDialog(note: Note) {
        AlertDialog.Builder(this)
            .setTitle("删除笔记")
            .setMessage("确定要删除这条笔记吗？")
            .setPositiveButton("确定") { _, _ ->
                // 删除笔记并刷新列表
                noteDao.deleteNote(note.id)
                loadNotes()
                Toast.makeText(this, "笔记已删除", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("取消", null)
            .show()
    }
}