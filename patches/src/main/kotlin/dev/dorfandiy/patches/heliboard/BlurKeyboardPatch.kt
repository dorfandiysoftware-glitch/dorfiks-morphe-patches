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
        val rememberBg = "invoke-static {p1, p2}, $EXTENSION_CLASS->rememberBackground(Ljava/lang/Object;Ljava/lang/Object;)V"
        val background = "invoke-static {}, $EXTENSION_CLASS->afterBackground()V"
        listOf(
            AllColorsSetBackgroundFingerprint,
            DefaultColorsSetBackgroundFingerprint,
            DynamicColorsSetBackgroundFingerprint,
        ).forEach {
            it.method.beforeEveryReturn(background)
            it.method.addInstructions(0, rememberBg)
        }

        // Key backgrounds.
        val rememberKey = "invoke-static {p1, p2}, $EXTENSION_CLASS->rememberKey(Ljava/lang/Object;Ljava/lang/Object;)V"
        val keys = "invoke-static {}, $EXTENSION_CLASS->afterKeyColor()V"
        listOf(
            AllColorsSetColorFingerprint,
            DefaultColorsSetColorFingerprint,
            DynamicColorsSetColorFingerprint,
        ).forEach {
            it.method.beforeEveryReturn(keys)
            it.method.addInstructions(0, rememberKey)
        }
    }
}
