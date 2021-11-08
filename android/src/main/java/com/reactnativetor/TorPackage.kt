package com.reactnativetor

import java.util.Arrays
import java.util.Collections

import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ViewManager
import com.facebook.react.bridge.JavaScriptModule
import android.content.pm.PackageManager
import java.io.File

class TorPackage : ReactPackage {

  override fun createNativeModules(reactContext: ReactApplicationContext): List<NativeModule> {
    val manager = reactContext.getPackageManager();
    val ai = manager.getApplicationInfo(reactContext.packageName, PackageManager.GET_META_DATA);
    
    // React native some times reports the wrong native library folder
    // This is a quick hack to ensure the app does not crash if this happens.
    // I admit it is not a correct fix but after many days searching. 
    // This worked perfectly well and I could move on from this frustration.
    
    val arch = System.getProperty("os.arch");

    if (File("${ai.nativeLibraryDir}/libsifir_android.so").exists()) {

      System.load("${ai.nativeLibraryDir}/libsifir_android.so");

    } else {
      if (arch == 'arm64' or arch == "aarch64") {

        if (File("${ai.nativeLibraryDir}/../${arch}/libsifir_android.so").exists()) {

          System.load("${ai.nativeLibraryDir}/../${arch}/libsifir_android.so");

        } else if (File("${ai.nativeLibraryDir}/../arm64-v8a/libsifir_android.so").exists()) {

          System.load("${ai.nativeLibraryDir}/../arm64-v8a/libsifir_android.so");

        } else
          System.loadLibrary("sifir_android")

      } else {

        if (File("${ai.nativeLibraryDir}/../armeabi-v7a/libsifir_android.so").exists()) {

          System.load("${ai.nativeLibraryDir}/../armeabi-v7a/libsifir_android.so");

        } else
          System.loadLibrary("sifir_android")

      }
    }

    return Arrays.asList<NativeModule>(TorModule(reactContext))
  }

  override fun createViewManagers(reactContext: ReactApplicationContext): List<ViewManager<*, *>> {
    return emptyList<ViewManager<*, *>>()
  }
}
