package kakurin.voicerecorderapp.scenes.listRecord

import androidx.lifecycle.ViewModel
import kakurin.voicerecorderapp.database.RecordDatabaseDao

class ListRecordViewModel(dataSource: RecordDatabaseDao) : ViewModel() {

    val database = dataSource
    val records = database.getAllRecords()
}