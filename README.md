# Maps

**Successfully getting direction from one place to another**

## Dependencies

### Gradle Configuration: 

Add the following codes to `build.gradle(module)`

```
dependencies {
    compile fileTree(dir: 'libs', include: ['*.jar'])
    testCompile 'junit:junit:4.12'
    compile 'com.android.support:appcompat-v7:23.1.0'
    compile 'com.google.android.gms:play-services:8.4.0'
    compile 'com.akexorcist:googledirectionlibrary:1.0.3'
    compile 'com.android.support:design:23.1.0'
}
```

### ManiFest Configuration:

```xml
<!-- Permission -->
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
    <uses-permission android:name="android.permission.ACCESS_LOCATION_EXTRA_COMMANDS" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>

<!-- Meta Data -->
<meta-data
            android:name="com.google.android.geo.API_KEY"
            android:value="@string/google_maps_key"/>

<meta-data
            android:name="com.google.android.gms.version"
            android:value="@integer/google_play_services_version" />
```

## Demo

![demo_app](http://i.imgur.com/TeF1xKt.png)
