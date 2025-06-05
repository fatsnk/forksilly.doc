# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /usr/local/Cellar/android-sdk/24.3.3/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# react-native-reanimated
-keep class com.swmansion.reanimated.** { *; }
-keep class com.facebook.react.turbomodule.** { *; }

# Add any project specific keep options here:

# For expo-file-system
-keep class expo.modules.filesystem.** { *; }

# For expo-asset
-keep class expo.modules.asset.** { *; }

# For react-native-fs
-keep class com.rnfs.** { *; }
-keep interface com.rnfs.** { *; }

# For @react-native-documents/picker
-keep class com.reactnativedocumentpicker.** { *; }
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-dontwarn com.reactnativedocumentpicker.**

# For react-native-share
-keep class cl.json.** { *; }
-dontwarn cl.json.**

# General rules that are often helpful for React Native apps
-keepattributes Signature
-keepattributes *Annotation*
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep custom native modules (if any)
# -keep class com.yourpackage.YourNativeModule { *; }

# Keep Hermes related classes if you are using Hermes and encounter issues
# (Though usually Hermes is handled well by default)
# -keep class com.facebook.hermes.unicode.** { *; }
# -keep class com.facebook.jni.** { *; }

# If using OkHttp (common for networking in React Native)
-if class okhttp3.internal.platform.Platform {
    static java.lang.String getAndroidApiLevel() {
        return java.lang.System.getProperty("java.specification.version");
    }
}
-dontwarn okio.**
-dontwarn okhttp3.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Keep GSON classes if used for JSON serialization/deserialization by native modules
# -keep class com.google.gson.** { *; }
