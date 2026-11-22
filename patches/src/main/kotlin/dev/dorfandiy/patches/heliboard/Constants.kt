package dev.dorfandiy.patches.heliboard

import app.morphe.patcher.patch.ApkFileType
import app.morphe.patcher.patch.AppTarget
import app.morphe.patcher.patch.Compatibility

object Constants {
    val COMPATIBILITY_HELIBOARD = Compatibility(
        name = "HeliBoard",
        packageName = "helium314.keyboard",
        apkFileType = ApkFileType.APK,
        appIconColor = 0x2F7BE0,
        targets = listOf(
            AppTarget(version = "4.2"),
            AppTarget(version = "4.3-beta2", isExperimental = true),
        )
    )
}
