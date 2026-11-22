package dev.dorfandiy.patches.heliboard

import app.morphe.patcher.patch.resourcePatch
import app.morphe.patcher.patch.stringOption
import dev.dorfandiy.patches.heliboard.Constants.COMPATIBILITY_HELIBOARD

private const val ORIGINAL_PACKAGE = "helium314.keyboard"
private const val ANDROID_NS = "http://schemas.android.com/apk/res/android"

@Suppress("unused")
val changePackageNamePatch = resourcePatch(
    name = "Change package name",
    description = "Installs the patched keyboard under another package name, so it can live next to the original HeliBoard.",
    default = false,
) {
    compatibleWith(COMPATIBILITY_HELIBOARD)

    val packageNameOption = stringOption(
        key = "packageName",
        default = "helium314.keyboard.blur",
        name = "Package name",
        description = "New package name, e.g. helium314.keyboard.blur. Must differ from the original.",
        required = true,
    )

    execute {
        val newPackage = packageNameOption.value!!.trim()
        require(Regex("[A-Za-z][A-Za-z0-9_]*(\\.[A-Za-z][A-Za-z0-9_]*)+").matches(newPackage)) {
            "Invalid package name: $newPackage"
        }
        if (newPackage == ORIGINAL_PACKAGE) return@execute

        document("AndroidManifest.xml").use { document ->
            val manifest = document.documentElement
            manifest.setAttribute("package", newPackage)

            // Provider authorities and custom permissions must be unique per package too.
            val all = document.getElementsByTagName("*")
            for (i in 0 until all.length) {
                val element = all.item(i) as org.w3c.dom.Element
                for (attribute in listOf("authorities", "name", "permission")) {
                    val value = element.getAttributeNS(ANDROID_NS, attribute)
                    if (value.startsWith("$ORIGINAL_PACKAGE.") &&
                        (element.tagName == "provider" && attribute == "authorities" ||
                            element.tagName == "permission" ||
                            element.tagName == "uses-permission" ||
                            attribute == "permission")
                    ) {
                        element.setAttributeNS(
                            ANDROID_NS,
                            "android:$attribute",
                            newPackage + value.removePrefix(ORIGINAL_PACKAGE),
                        )
                    }
                }
            }
        }
    }
}
