package kakurin.voicerecorderapp.scenes.listRecord

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kakurin.voicerecorderapp.R
import kakurin.voicerecorderapp.database.RecordingItem
import kotlinx.android.synthetic.main.list_item_record.view.*
import java.lang.Exception

import java.util.concurrent.TimeUnit


class ListRecordAdapter(private val callback: (RecordingItem) -> Unit) : RecyclerView.Adapter<ListRecordAdapter.ViewHolder>() {

    var data = listOf<RecordingItem>()
        set (value) { field = value
            notifyDataSetChanged () }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val context = holder.itemView.context
        val recordingItem = data[position]

        val itemDuration: Long = recordingItem.length
        val minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration)
        val seconds =
            (TimeUnit.MILLISECONDS.toSeconds(itemDuration) - TimeUnit.MINUTES.toSeconds(minutes))

        holder.vName.text = recordingItem.name
        holder.vLength.text = String.format("%02d:%02d", minutes, seconds)
        holder.itemView.setOnClickListener{
                callback.invoke(recordingItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent, callback)
    }

    class ViewHolder(
        itemView: View,
        callback: (RecordingItem) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        var vName: TextView = itemView.file_name_text
        var vLength: TextView = itemView.file_length_text
        var cardView: View = itemView.card_view

        companion object {
            fun from(parent: ViewGroup, callback: (RecordingItem) -> Unit): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_item_record, parent, false)

                return ViewHolder(view,callback)
            }
        }
    }
}