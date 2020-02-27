package ua.railian.alia.permissions

import android.Manifest
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ua.railian.alia.core.Alia
import ua.railian.alia.permissions.AliaPermissionResult.Denied
import ua.railian.alia.permissions.AliaPermissionResult.Granted

object AliaPermissions

private var AliaPermissionBuilder.requestCode: Int
    get() = TODO("not implemented")
    set(value) {
        TODO("not implemented")
    }

val Alia.PERMISSIONS get() = AliaPermissions

private fun AliaPermissionBuilder.rationale(
    evenAtFirstTime: Boolean = false,
    function: () -> Nothing
): Alia.Element = TODO("not implemented")

infix fun AliaPermissionResult.handleResult(handler: (AliaPermissionResult) -> Unit): AliaPermissionResult =
    TODO("not implemented")

class AliaContextPermissions

operator fun AliaPermissions.get(activity: AppCompatActivity): AliaContextPermissions =
    TODO("not implemented")

class AliaPermission

fun permission(
    permission: String,
    builder: AliaPermissionBuilder.() -> Unit = {}
): AliaPermission = TODO("not implemented")

sealed class AliaPermissionResult {
    sealed class Granted
    sealed class Denied
}

infix fun AliaContextPermissions.request(permission: AliaPermission): AliaPermissionResult =
    TODO("not implemented")

infix fun AliaContextPermissions.check(permission: AliaPermission): AliaPermissionResult =
    TODO("not implemented")

class AliaPermissionBuilder : Alia.Builder<AliaPermission>(Alia.Core(), { AliaPermission() })

class AliaPermissionsResult

fun rawPermissionsResult(
    requestCode: Int,
    permissions: Array<out String>,
    grantResults: IntArray
): AliaPermissionsResult = TODO("not implemented")

infix fun AliaPermissions.handle(permissionsResult: AliaPermissionsResult): Unit =
    TODO("not implemented")

infix fun AliaContextPermissions.handle(permissionsResult: AliaPermissionsResult): Unit =
    TODO("not implemented")


class MainActivity : AppCompatActivity() {

    companion object {
        private const val ACCESS_FINE_LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Alia.PERMISSIONS[this] check permission(
            Manifest.permission.ACCESS_FINE_LOCATION
        ) handleResult {
            when (it) {
                is Granted -> TODO("not implemented")
                is Denied -> TODO("not implemented")
            }
        }

        Alia.PERMISSIONS[this] request permission(
            Manifest.permission.ACCESS_FINE_LOCATION
        ) {
            requestCode = ACCESS_FINE_LOCATION_PERMISSION_REQUEST_CODE
            +rationale { TODO("not implemented") }
        } handleResult {
            when (it) {
                is Granted -> TODO("not implemented")
                is Denied -> TODO("not implemented")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Alia.PERMISSIONS handle rawPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}


class MainActivity2 : AppCompatActivity() {

    companion object {
        private const val ACCESS_FINE_LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private val locationPermission = permission(Manifest.permission.ACCESS_FINE_LOCATION) {
        requestCode = ACCESS_FINE_LOCATION_PERMISSION_REQUEST_CODE
        +rationale { TODO("not implemented") }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Alia.PERMISSIONS[this] check locationPermission handleResult {
            when (it) {
                is Granted -> TODO("not implemented")
                is Denied -> TODO("not implemented")
            }
        }

        Alia.PERMISSIONS[this] request locationPermission handleResult {
            when (it) {
                is Granted -> TODO("not implemented")
                is Denied -> TODO("not implemented")
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Alia.PERMISSIONS handle rawPermissionsResult(requestCode, permissions, grantResults)
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}