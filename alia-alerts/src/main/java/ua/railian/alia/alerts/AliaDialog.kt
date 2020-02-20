package ua.railian.alia.alerts

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import ua.railian.alia.core.Alia

fun dialog(builder: AlertDialog.Builder.() -> Unit) = AliaDialog { context ->
    AlertDialog.Builder(context).apply(builder).create()
}

class AliaDialog(val baker: (Context) -> AlertDialog) : AliaAlert<AlertDialog>

infix fun AliaContextAlerts.make(dialog: AliaDialog): AlertDialog = dialog.baker.invoke(context)
infix fun AliaContextAlerts.show(dialog: AliaDialog): AlertDialog = make(dialog).apply { show() }

var AlertDialog.Builder.title: CharSequence
    get() = throw RuntimeException("getter not supported")
    set(value) {
        setTitle(value)
    }

var AlertDialog.Builder.message: CharSequence
    get() = throw RuntimeException("getter not supported")
    set(value) {
        setMessage(value)
    }

infix fun <T : DialogFragment> AliaActivityAlerts.show(dialog: T): T {
    dialog.show(activity.supportFragmentManager, null)
    return dialog
}

fun dialogUsage(context: Context, activity: AppCompatActivity) {

    Alia.ALERTS[context] show dialog {
        title = "Hello, World!"
        message = "Are you ready for Alia?"
        setPositiveButton("Sure!", null)
    }

    Alia.ALERTS[activity] show CustomDialogFragment.newInstance(
        title = "Hello, World!",
        message = "Are you ready for Alia?"
    )
}