package kakurin.voicerecorderapp.scenes.listRecord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import kakurin.voicerecorderapp.R
import kakurin.voicerecorderapp.database.RecordDatabase
import kakurin.voicerecorderapp.database.RecordingItem
import kakurin.voicerecorderapp.databinding.FragmentListRecordBinding
import kakurin.voicerecorderapp.scenes.MainActivity

class ListRecordFragment : Fragment() {

    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentListRecordBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_list_record, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = RecordDatabase.getInstance(application).recordDatabaseDao
        val viewModelFactory = ListRecordViewModelFactory(dataSource)

        val listRecordViewModel: ListRecordViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ListRecordViewModel::class.java)

        binding.listRecordViewModel = listRecordViewModel

        val adapter = ListRecordAdapter { itemOnClick(it) }
        binding.recyclerView.adapter = adapter

        listRecordViewModel.records.observe(
            viewLifecycleOwner,
            Observer { it?.let { adapter.data = it } })

        binding.lifecycleOwner = this

        mainActivity = activity as MainActivity


        return binding.root
    }

    private fun createExoPlayer(filePath: String) {
        val exoPlayer = ExoPlayer.Builder(mainActivity).build()
        val mediaItem = MediaItem.Builder().setUri(filePath).build()
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
        exoPlayer.play()
    }

    private fun itemOnClick(item: RecordingItem) {
        val filePath = item.filePath
        createExoPlayer(filePath)
    }
}




