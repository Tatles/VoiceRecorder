package kakurin.voicerecorderapp.scenes.listRecord

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kakurin.voicerecorderapp.R
import kakurin.voicerecorderapp.database.RecordingItem
import kotlinx.android.synthetic.main.list_item_record.view.*
import java.util.concurrent.TimeUnit


class ListRecordAdapter(private val callback: (RecordingItem, Int) -> Unit) :
    RecyclerView.Adapter<ListRecordAdapter.ViewHolder>() {

    var data = listOf<RecordingItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_record, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    inner class ViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                callback.invoke(data[adapterPosition],1)
            }
            itemView.setOnLongClickListener {
                callback.invoke(data[adapterPosition],2)
                return@setOnLongClickListener true
            }
        }

        fun bind(recordingItem: RecordingItem) {
            val itemDuration: Long = recordingItem.length

            val minutes = TimeUnit.MILLISECONDS.toMinutes(itemDuration)
            val seconds =
                (TimeUnit.MILLISECONDS.toSeconds(itemDuration) - TimeUnit.MINUTES.toSeconds(minutes))

            with(itemView) {
                file_name_text.text = recordingItem.name
                file_length_text.text =
                    String.format("%02d:%02d", minutes, seconds)
            }
        }
    }
}