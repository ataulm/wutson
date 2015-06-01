# Retrofit
-keepattributes *Annotation*
-keep class retrofit.** { *; }
-keep class com.ataulm.wutson.tmdb.gson.** { *; }
-keepclassmembernames interface * {
    @retrofit.http.* <methods>;
}
-dontwarn com.google.appengine.**

# Okio (OkHTTP)
-dontwarn java.nio.file.**
-dontwarn org.codehaus.mojo.animal_sniffer.**

# RxJava
-dontwarn sun.misc.Unsafe
-keepattributes Signature

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
