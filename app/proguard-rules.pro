# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

#-dontwarn java.util.concurrent.Flow*

#for sweet alert

 -keep class cn.pedant.SweetAlert.Rotate3dAnimation {
    public <init>(...);
 }

# gms
-keep class com.google.android.gms.** { *; }


#huawei
-keep class com.huawei.hms.ads.** { *; }
-keep interface com.huawei.hms.ads.** { *; }

#greendao
 -keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties {*;}
-dontwarn okhttp3.internal.platform.ConscryptPlatform
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform
# If you do not use SQLCipher:
-dontwarn net.sqlcipher.database.**
# If you do not use RxJava:
-dontwarn rx.**
-keep class rx.internal.util.unsafe.** {
    *;
 }

# this kills the release issue
-keep class com.google.** { *; }
-keep class com.github.** { *; }
-keep class org.apache.** { *; }
-keep class com.android.** { *; }
-keep class junit.** { *; }
-keep class android.support.v7.widget.SearchView { *; }
-keep class com.myproject.model.** { *; }
-keep interface android.support.** { *; }
-keep class android.support.** { *; }

-dontwarn java8.util.**
-dontwarn jnr.posix.**
-dontwarn com.kenai.**

#-keep class org.bouncycastle.**
-dontwarn org.bouncycastle.jce.provider.X509LDAPCertStoreSpi
-dontwarn org.bouncycastle.x509.util.LDAPStoreHelper

# Web3j
-keep class org.** { *; }
-keep class * extends org.web3j.abi.TypeReference
-keep class * extends org.web3j.abi.datatypes.Type
#-dontwarn java.lang.SafeVarargs
-dontwarn org.slf4j.**


# eventbus
-keepattributes *Annotation*
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# And if you use AsyncExecutor:
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}