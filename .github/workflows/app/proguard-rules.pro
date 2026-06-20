-keep class com.guardmonitor.** { *; }
-keep class androidx.compose.** { *; }
-keep class androidx.security.** { *; }
-assumenosideeffects class android.util.Log {
    public static *** d(...);
}
