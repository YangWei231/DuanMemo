data class Note(
    val id: Long = 0,          // 主键，自增
    val title: String,         // 标题
    val content: String,       // 内容
    val time: Long = System.currentTimeMillis() // 创建时间
)