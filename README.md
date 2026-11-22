# Dorfik Morphe Patches

Patches for use with [Morphe](https://github.com/MorpheApp) (this project is not made by the Morphe project).

## Translucent blurred keyboard (HeliBoard)

Makes the [HeliBoard](https://github.com/HeliBorg/HeliBoard) keyboard see-through and blurs whatever is behind it.

- Works on Android 12 or newer. The blur needs a phone that allows window blur (some phones turn it off, and battery saver turns it off too). Without blur the keyboard is only see-through.
- Tested target: HeliBoard 4.2 (APK from GitHub).
- Strengths of blur and see-through are constants at the top of
  `extensions/extension/src/main/java/dev/dorfandiy/extension/heliboard/BlurKeyboard.java`.

License: GPLv3 (see LICENSE and NOTICE).
