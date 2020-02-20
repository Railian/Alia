package ua.railian.alia.alerts

import android.content.Context
import android.widget.Toast
import ua.railian.alia.core.Alia

val Alia.ALERTS get() = AliaAlertsSelector

object AliaAlertsSelector

operator fun AliaAlertsSelector.get(context: Context) = AliaAlerts(context)

class AliaAlerts(val context: Context)

infix fun AliaAlerts.makeShortToast(text: CharSequence) =
    Toast.makeText(context, text, Toast.LENGTH_SHORT)

infix fun AliaAlerts.makeLongToast(text: CharSequence) =
    Toast.makeText(context, text, Toast.LENGTH_LONG)

infix fun AliaAlerts.showShortToast(text: CharSequence) =
    makeShortToast(text).show()

infix fun AliaAlerts.showLongToast(text: CharSequence) =
    makeLongToast(text).show()