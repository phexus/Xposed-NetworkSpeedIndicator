Network Speed Indicator for H2OS
=======================

Displays network speeds on the status bar. Requires a rooted phone with Xposed Module installed.

This is a continuous work from [Dzakus'](http://repo.xposed.info/module/pl.com.android.networkspeedindicator) and [FatMinMin's](http://repo.xposed.info/module/tw.fatminmin.xposed.networkspeedindicator) great work. Many thanks to the contributors and translators!

**Translators,** please see notes below.

To install, please go to [oneplus bbs](http://www.oneplusbbs.com/thread-2881051-1-1.html) download the newest-version apk.(I'll put it on coolapk.com someday.)

安装这个模块请前往[一加社区](http://www.oneplusbbs.com/thread-2881051-1-1.html)下载最新版本。(后续我可能会将apk发布到酷市场)


Notice
--------
This module had been different from [this one](https://github.com/chiehmin/Xposed-NetworkSpeedIndicator).

I had modified this module for doing H2OS supporting, if you are not using H2OS, please don't try to use this module.

注意
--------
这个模块已经与[原模块](https://github.com/chiehmin/Xposed-NetworkSpeedIndicator)不同。

我修改了原模块来支持一加的氢OS，如果你并不是氢OS用户，请不要尝试使用这个模块。

Features
--------
 * Shows upload and download speeds
 * Works for both Mobile and Wi-Fi networks
 * Highly customizable
  * Update interval and speed display
  * Positioning, display and suffix
  * Unit choice and formatting
  * Font styles, size and color

Translating
-----------
Thank you for helping us provide localization of the app. It is very easy to do so on the GitHub website (no need to clone locally) or by using GitHub software (no need to mess with the command line).

When contributing translations (fork, modify and send pull request) please keep the following in mind:
 * Translate the original `strings.xml` in your fork (original source code).
 * Do not translate `strings.xml` or `arrays.xml` extracted from the APK file.
 * Do not translate `values.xml` or `dimens.xml`.
 * Application name (`app_name`) should ideally remain in English, unless your locale specifically requires a translated name.
 * Do not translate or change the value of the "name" attribute.
 * Follow the capitalization and style of the default strings.
 * Only include strings that were translated. Non-translated strings must fall back to defaults in `/res/values/strings.xml`.
 * For string arrays, always include all items in the correct order (regardless of how many items were translated).
 * Place translations in the correct locale folder (for example `/res/values-de/strings.xml`).
 * Remove any obsolete strings that are not present in the default file.
