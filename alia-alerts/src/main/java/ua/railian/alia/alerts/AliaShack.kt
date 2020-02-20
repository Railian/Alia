package ua.railian.alia.alerts

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import ua.railian.alia.core.Alia

fun snack(view: View, builder: Snackbar.() -> Unit) = AliaSnack {
    Snackbar.make(view, "", Snackbar.LENGTH_SHORT).apply(builder)
}

class AliaSnack(val baker: () -> Snackbar) : AliaAlert<Snackbar>

infix fun AliaContextAlerts.make(snack: AliaSnack): Snackbar = snack.baker.invoke()
infix fun AliaContextAlerts.show(snack: AliaSnack): Snackbar = make(snack).apply { show() }

var Snackbar.text: CharSequence
    get() = throw RuntimeException("getter not supported")
    set(value) {
        setText(value)
    }

fun snackUsage(context: Context, view: View) {
    Alia.ALERTS[context] show snack(view) { text = "Hello, world"; duration = Snackbar.LENGTH_LONG }
}