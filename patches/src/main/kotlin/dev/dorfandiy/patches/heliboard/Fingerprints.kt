package dev.dorfandiy.patches.heliboard

import app.morphe.patcher.Fingerprint

private const val PKG = "Lhelium314/keyboard/latin"
private const val VIEW = "Landroid/view/View;"
private const val COLOR_TYPE = "Lhelium314/keyboard/latin/common/ColorType;"
private const val DRAWABLE = "Landroid/graphics/drawable/Drawable;"

/** HeliBoard release builds keep the names of its own classes, so these match by name. */
object SetInputViewFingerprint : Fingerprint(
    definingClass = "$PKG/LatinIME;",
    name = "setInputView",
    returnType = "V",
    parameters = listOf(VIEW),
)

object WindowShownFingerprint : Fingerprint(
    definingClass = "$PKG/LatinIME;",
    name = "onWindowShown",
    returnType = "V",
    parameters = listOf(),
)

object AllColorsSetBackgroundFingerprint : Fingerprint(
    definingClass = "$PKG/common/AllColors;",
    name = "setBackground",
    returnType = "V",
    parameters = listOf(VIEW, COLOR_TYPE),
)

object DefaultColorsSetBackgroundFingerprint : Fingerprint(
    definingClass = "$PKG/common/DefaultColors;",
    name = "setBackground",
    returnType = "V",
    parameters = listOf(VIEW, COLOR_TYPE),
)

object DynamicColorsSetBackgroundFingerprint : Fingerprint(
    definingClass = "$PKG/common/DynamicColors;",
    name = "setBackground",
    returnType = "V",
    parameters = listOf(VIEW, COLOR_TYPE),
)

object AllColorsSetColorFingerprint : Fingerprint(
    definingClass = "$PKG/common/AllColors;",
    name = "setColor",
    returnType = "V",
    parameters = listOf(DRAWABLE, COLOR_TYPE),
)

object DefaultColorsSetColorFingerprint : Fingerprint(
    definingClass = "$PKG/common/DefaultColors;",
    name = "setColor",
    returnType = "V",
    parameters = listOf(DRAWABLE, COLOR_TYPE),
)

object DynamicColorsSetColorFingerprint : Fingerprint(
    definingClass = "$PKG/common/DynamicColors;",
    name = "setColor",
    returnType = "V",
    parameters = listOf(DRAWABLE, COLOR_TYPE),
)
