# Retrofit
-keepattributes *Annotation*
-keep class retrofit.** { *; }
-keepclassmembernames interface * {
    @retrofit.http.* <methods>;
}
-dontwarn com.google.appengine.**

# Retrofit - can be removed when updating to v2
-dontwarn retrofit.client.ApacheClient$GenericEntityHttpRequest
-dontwarn retrofit.client.ApacheClient$GenericHttpRequest
-dontwarn retrofit.client.ApacheClient$TypedOutputEntity
-dontwarn org.apache.http.**
-dontwarn android.net.http.AndroidHttpClient

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

# Support libraries
-dontwarn android.support.**
-keep class android.support.** { *; }
-keep interface android.support.** { *; }
-keep public class * extends android.support.**

-keep public class com.ataulm.wutson.view.AppBarWidget$** {
    public *;
}
