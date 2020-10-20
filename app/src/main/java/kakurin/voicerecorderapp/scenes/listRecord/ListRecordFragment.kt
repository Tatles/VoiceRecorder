package kakurin.voicerecorderapp.scenes.listRecord

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kakurin.voicerecorderapp.R
import kakurin.voicerecorderapp.database.RecordDatabase
import kakurin.voicerecorderapp.database.RecordDatabaseDao
import kakurin.voicerecorderapp.database.RecordingItem
import kakurin.voicerecorderapp.databinding.FragmentListRecordBinding
import kotlinx.android.synthetic.main.fragment_list_record.*
import kotlinx.coroutines.*

class ListRecordFragment : Fragment() {
    private var exoPlayer: SimpleExoPlayer? = null
    private lateinit var currentItem: RecordingItem
    private lateinit var dataSource: RecordDatabaseDao
    private val mJob = Job()
    private val mUiScope = CoroutineScope(Dispatchers.Main + mJob)
    private val listRecordDeleteDialog = ListRecordDeleteDialog(::removeRecord)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentListRecordBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_list_record, container, false)

        val application = requireNotNull(this.activity).application

        dataSource = RecordDatabase.getInstance(application).recordDatabaseDao

        val viewModelFactory = ListRecordViewModelFactory(dataSource)

        val listRecordViewModel: ListRecordViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ListRecordViewModel::class.java)

        binding.listRecordViewModel = listRecordViewModel

        val adapter = ListRecordAdapter(::onClick)
        binding.recyclerView.adapter = adapter

        listRecordViewModel.records.observe(
            viewLifecycleOwner,
            Observer { it?.let { adapter.data = it } })

        binding.lifecycleOwner = this

        return binding.root
    }

    private fun onClick(item: RecordingItem, mode: Int) {
        currentItem = item
        if (mode == 1) {
            createExoPlayer()
        } else {
            listRecordDeleteDialog.show(requireActivity().supportFragmentManager,"ListRecordDeleteDialog")
        }
    }

    private fun createExoPlayer() {
        val filePath = currentItem.filePath
        val mediaItem = MediaItem.Builder().setUri(Uri.parse(filePath)).build()
        val dataSourceFactory =
            DefaultDataSourceFactory(
                requireContext(),
                Util.getUserAgent(requireContext(), "VoiceRecorderApp")
            )
        val mediaSource =
            ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(mediaItem)
        if (exoPlayer == null) {
            exoPlayer = SimpleExoPlayer.Builder(requireContext()).build()
        }
        exoPlayer!!.setMediaSource(mediaSource)
        exo_player_view.player = exoPlayer
        exoPlayer!!.prepare()
        exoPlayer!!.play()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (exoPlayer != null) {
            exoPlayer!!.stop()
            exoPlayer!!.release()
        }
    }

    private fun removeRecord() {
        try {
            mUiScope.launch { withContext(Dispatchers.IO) { dataSource.removeRecord(currentItem.id) } }
        } catch (e: Exception) {
            Log.e("RecordService", "exception", e)
        }
    }
}




