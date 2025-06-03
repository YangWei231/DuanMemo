package com.example.duanmemo.adapter // 替换为项目实际包名（如：包名+.adapter）

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import Note
import com.example.duanmemo.R
import java.text.SimpleDateFormat
import java.util.*

/**
 * 备忘录列表适配器
 * @param notes 笔记数据列表
 * @param onItemClick 条目点击回调（跳转到详情页）
 */
class NoteAdapter(
    private val notes: List<Note>,
    private val onItemClick: (Note) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    // 长按事件回调（可选，返回 true 表示消费事件）
    private var onLongClickListener: ((Note) -> Boolean)? = null

    /**
     * 设置长按事件监听器
     */
    fun setOnLongClickListener(listener: (Note) -> Boolean) {
        onLongClickListener = listener
    }

    /**
     * 创建 ViewHolder（加载条目布局）
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val context = parent.context
        val itemView = LayoutInflater.from(context)
            .inflate(R.layout.item_note, parent, false) // 加载 item_note.xml 布局
        return NoteViewHolder(itemView)
    }

    /**
     * 绑定数据到 ViewHolder
     */
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position] // 获取当前笔记

        // 绑定标题和时间
        holder.tvTitle.text = note.title
        val timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        holder.tvTime.text = timeFormat.format(note.time)

        // 条目点击事件：触发外部回调
        holder.itemView.setOnClickListener {
            onItemClick(note)
        }

        // 长按事件：触发外部回调（如果已设置）
        holder.itemView.setOnLongClickListener {
            onLongClickListener?.invoke(note) ?: false
        }
    }

    /**
     * 返回数据总数
     */
    override fun getItemCount(): Int = notes.size

    /**
     * ViewHolder：缓存条目控件
     */
    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tv_title)   // 笔记标题
        val tvTime: TextView = itemView.findViewById(R.id.tv_time)     // 笔记时间
    }
}