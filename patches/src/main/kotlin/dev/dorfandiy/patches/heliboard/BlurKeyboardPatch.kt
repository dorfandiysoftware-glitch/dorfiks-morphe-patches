package dev.dorfandiy.patches.heliboard

import app.morphe.patcher.extensions.InstructionExtensions.addInstructions
import app.morphe.patcher.patch.patch
import app.morphe.patcher.util.proxy.mutableTypes.MutableMethod
import com.android.tools.smali.dexlib2.Opcode
import dev.dorfandiy.patches.heliboard.Constants.COMPATIBILITY_HELIBOARD

private const val EXTENSION_CLASS = "Ldev/dorfandiy/extension/heliboard/BlurKeyboard;"

/** Runs [smali] right before every return-void of the method. */
private fun MutableMethod.beforeEveryReturn(smali: String) {
    val returns = implementation!!.instructions
        .withIndex()
        .filter { it.value.opcode == Opcode.RETURN_VOID }
        .map { it.index }
        .reversed()
    for (index in returns) {
        addInstructions(index, smali)
    }
}

@Suppress("unused")
val blurKeyboardPatch = patch(
    name = "Translucent blurred keyboard",
    description = "Makes the keyboard see-through and blurs what is behind it (Android 12+).",
    default = true,
) {
    compatibleWith(COMPATIBILITY_HELIBOARD)

    extendWith("extensions/extension.mpe")

    execute {
        // The keyboard window: translucent, with background blur.
        val window = "invoke-static {p0}, $EXTENSION_CLASS->applyWindow(Landroid/inputmethodservice/InputMethodService;)V"
        SetInputViewFingerprint.method.addInstructions(0, window)
        WindowShownFingerprint.method.addInstructions(0, window)

        // Frame and suggestion strip backgrounds.
        val background = "invoke-static {p1, p2}, $EXTENSION_CLASS->afterBackground(Landroid/view/View;Ljava/lang/Object;)V"
        listOf(
            AllColorsSetBackgroundFingerprint,
            DefaultColorsSetBackgroundFingerprint,
            DynamicColorsSetBackgroundFingerprint,
        ).forEach { it.method.beforeEveryReturn(background) }

        // Key backgrounds.
        val keys = "invoke-static {p1, p2}, $EXTENSION_CLASS->afterKeyColor(Landroid/graphics/drawable/Drawable;Ljava/lang/Object;)V"
        listOf(
            AllColorsSetColorFingerprint,
            DefaultColorsSetColorFingerprint,
            DynamicColorsSetColorFingerprint,
        ).forEach { it.method.beforeEveryReturn(keys) }
    }
}
