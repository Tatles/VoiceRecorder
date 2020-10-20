package kakurin.voicerecorderapp.scenes.listRecord


import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class ListRecordDeleteDialog(private val callback: () -> Unit) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Хотите ли вы удалить эту аудиодорожку?")
            .setPositiveButton("Да") { _, _ ->
                callback.invoke()
            }.setNegativeButton("Нет") { _, _ ->
            dialog!!.dismiss()
        }
        return builder.create()
    }
}