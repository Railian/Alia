package ua.railian.alia.alerts

import android.content.Context
import android.widget.Toast
import ua.railian.alia.core.Alia

fun toast(builder: Toast.() -> Unit) = AliaToast { context ->
    Toast.makeText(context, "", Toast.LENGTH_SHORT).apply(builder)
}

class AliaToast(val baker: (Context) -> Toast) : AliaAlert<Toast>

infix fun AliaContextAlerts.make(toast: AliaToast): Toast = toast.baker.invoke(context)
infix fun AliaContextAlerts.show(toast: AliaToast): Toast = make(toast).apply { show() }

var Toast.text: CharSequence?
    get() = throw RuntimeException("getter not supported")
    set(value) = setText(value)

fun toastUsage(context: Context) {
    Alia.ALERTS[context] show toast { text = "Hello, World"; duration = Toast.LENGTH_LONG }
}