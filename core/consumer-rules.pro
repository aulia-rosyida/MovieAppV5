# Mengapa kita menggunakan consumer-rules.pro, bukan proguard-rules.pro seperti yang ada di teori?
# Hal ini karena library terletak di module core yang merupakan Android Library.
# Jika module library tersebut digunakan oleh module lain,
# maka Anda harus menambahkannya ke dalam consumer-rules.pro.
#Jika tidak maka akan muncul eror JNI DETECTED ERROR IN APPLICATION

##---------------Begin: proguard configuration for SQLCipher  ----------
# konfigurasi ProGuard untuk library SQLCipher.

# keep : mempertahankan suatu class supaya tidak ikut ter-obfuscate.
# Modifier includedescriptorclasses digunakan supaya fungsi dan field yang ada di dalam class tersebut
# juga dipertahankan.
-keep,includedescriptorclasses class net.sqlcipher.** { *; }
-keep,includedescriptorclasses interface net.sqlcipher.** { *; }


##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.

# keepattributes : mempertahankan attributes seperti annotation, source file name, dan line number.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

#dontwarn : untuk menghilangkan warning jika ada masalah saat proses obfuscation.
# Gunakan ini jika Anda benar-benar tahu apa yang Anda lakukan.
# Beberapa library mungkin memerlukan ini supaya aplikasi tetap berjalan tanpa menimbulkan eror.
# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * extends com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

#keepclassmembers : merupakan salah satu variasi dari option keep,
# bedanya yaitu yang dipertahankan hanya member class-nya saja, class utamanya tidak.
# Modifier allowobfuscation digunakan supaya proses obfuscation tetap dijalankan namun hanya sebatas mengubah nama,
 #tidak sampai dihapus atau dioptimasi.
# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}


##---------------Begin: proguard configuration for Retrofit  ----------
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn java.lang.instrument.ClassFileTransformer
-dontwarn sun.misc.SignalHandler

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

-dontwarn kotlinx.**


##---------------Begin: proguard configuration for Glide  ----------
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

# Uncomment for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule


##---------------Begin: proguard configuration for RxJava  ----------
# Uncomment if you use RxJava
#-dontwarn java.util.concurrent.Flow*