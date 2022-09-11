package com.gerwalex.extensions

import androidx.fragment.app.Fragment

object FragmentExt {

    /**
     * Registriert PermissionRequest ohne ResultHandler.
     */
    @JvmStatic
    fun Fragment.registerforPermissionRequest(): PermissionUtil.Permission {
        return PermissionUtil.Permission(this)
    }
}