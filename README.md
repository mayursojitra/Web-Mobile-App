# Web to Android Mobile App

## This is simple webview application with basic functionalities

# Demo

Download here : [Demo Apk](https://github.com/mayursojitra/Web-Mobile-App/raw/master/app-debug.apk)

# Screenshots

![ANDROID WEBVIEW](https://raw.githubusercontent.com/mayursojitra/Web-Mobile-App/master/screenshots/screenshot-1.png) ![ANDROID NAVIGATION DRAWER](https://raw.githubusercontent.com/mayursojitra/Web-Mobile-App/master/screenshots/screenshot-2.png)

# Configurations

## Change Package name
- Change package name in  ```app/build.gradle```
```json
applicationId "murait.the.android.mania"
```
Replace with your package name
```json
applicationId "your.package.name"
```

## Replace Logo
- Replace logo **logo.png** in ```app/src/main/res/drawable/``` directory

## Website URL
- Change url variable in ```app/src/main/java/murait/the/android/mania/HomeActivity.java```  
```java
private String url = "https://www.theandroid-mania.com/";
```

## App Credentials
- Change App Name and Other Credentials ```app/src/main/res/values/strings.xml```
```xml
<string name="app_name">The Android Mania</string>
<string name="header_title">Murait Technologies</string>
<string name="title_activity_home">The Android Mania</string>
<string name="email">hello@murait.com</string>
<string name="phone">+919999998888</string>
```

## Push Notifications

- First thing you need to do is go to **(https://firebase.google.com/)** and make an account to gain access to their console. After you gain access to the console you can start by creating your first project.

- Give the package name of your project (mine is **murait.the.android.mania**) in which you are going to integrate the Firebase. Here the **google-services.json** file will be downloaded when you press add app button.
![FIREBASE NEW PROJECTS](https://raw.githubusercontent.com/mayursojitra/Web-Mobile-App/master/screenshots/screenshot-3.png)
![FIREBASE NEW ANDROID APP](https://raw.githubusercontent.com/mayursojitra/Web-Mobile-App/master/screenshots/screenshot-4.png)
![FIREBASE NEW ANDROID APP](https://raw.githubusercontent.com/mayursojitra/Web-Mobile-App/master/screenshots/screenshot-5.png)
![FIREBASE NEW ANDROID APP](https://raw.githubusercontent.com/mayursojitra/Web-Mobile-App/master/screenshots/screenshot-6.png)

- Copy this **Server Key** and **Sender ID** and paste it in Onesignal Account
![Onesignal Project](https://raw.githubusercontent.com/mayursojitra/Web-Mobile-App/master/screenshots/screenshot-7.png)

## AdMob Integration

- Replace your ads id in ```app/src/main/res/values/strings.xml```
```xml
<!-- AdMob ad unit IDs -->
    <string name="admob_app_id">ca-app-pub-XXXXXXXX~XXXXXXXXXXX</string>
    <string name="banner_home_footer">ca-app-pub-3940256099942544/6300978111</string>
    <string name="interstitial_full_screen">ca-app-pub-3940256099942544/1033173712</string>
```
## No Action Bar WebView
Change intent filter in `AndroidManifest.xml`
```xml
    <activity
    android:name=".NoActionBarActivity"
    android:theme="@style/AppTheme.NoActionBar">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>
    <activity
    android:name=".SplashActivity"
    android:theme="@style/AppTheme.NoActionBar">
    </activity>
```

## WebView With Navigation Drawer
Change intent filter in `AndroidManifest.xml`
```xml
    <activity
    android:name=".SplashActivity"
    android:theme="@style/AppTheme.NoActionBar">
        <intent-filter>
            <action android:name="android.intent.action.MAIN" />
            <category android:name="android.intent.category.LAUNCHER" />
        </intent-filter>
    </activity>
```

_**You Are DONE**_


# Blog

- [The Android Mania](https://www.theandroid-mania.com/)

# Hire Me

- [People per hour](http://pph.me/mayursojitra)
- [UpWork](https://www.upwork.com/freelancers/~019d3db2c3f08414b8)

# Network Profiles

- [Linkedin](https://www.linkedin.com/in/mayursojitra/)
- [Twitter](https://twitter.com/mayur_sojitra)
- [Instagram](https://www.instagram.com/mayursojitra/)
- [Quora](https://www.quora.com/profile/Mayur-Sojitra)

# Contact Me

- [**Mail me**](mailto:hello@murait.com)
- [**Contact Us**](https://www.murait.com/contact-us/)

# License

GNU GENERAL PUBLIC LICENSE
Version 3, 29 June 2007
Copyright (C) 2007 Free Software Foundation, Inc. <https://fsf.org/> Everyone is permitted to copy and distribute verbatim copies of this license document, but changing it is not allowed.

## Buy a cup of coffee
If you found this project helpful or you learned something from the source code and want to thank me, consider buying me a cup of ☕️ [Buy Me a Coffee](https://www.buymeacoffee.com/mayur)

### Do not forget to give star If its useful to you. :)


:+1: **Happy Coding!!!**
