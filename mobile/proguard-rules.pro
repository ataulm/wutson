# Retrofit
-keepattributes *Annotation*
-keep class retrofit.** { *; }
-keepclassmembernames interface * {
    @retrofit.http.* <methods>;
}
-dontwarn com.google.appengine.**

# Okio (OkHTTP)
-dontwarn java.nio.file.**
-dontwarn org.codehaus.mojo.animal_sniffer.**

# RxJava
-dontwarn sun.misc.Unsafe
