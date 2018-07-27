#Orange trust badge Android Reference

![Orange trust badge : your privacy first](https://raw.githubusercontent.com/Orange-OpenSource/orange-trust-badge-android/master/orange_edo_demo_otb/src/main/res/mipmap-xxxhdpi/ic_launcher.png)

With the Orange trust badge, aka "Badge de confiance", give transparent information and user control on personal data and help users to identify if your application has any sensitive features.

## Features
Orange trust badge displays how are handled the following data :

System Permissions :

* Calendar
* Camera
* Contacts
* Location
* Microphone
* Phone
* Sensors
* SMS
* Storage

App data permissions :

* Account credentials
* Account info
* Advertise
* History
* Improvement program
* Notifications

* Custom data


It also displays the following informations :

* Help
* Privacy policy

It also :

* Works on Phones and Tablets with dedicated layout in portrait or landscape mode
* Is localized in 2 languages (English, French)
* has API hooks
* allows UI Customization
* allows to embed a web hosted video with a native player

## Requirements

* Android 4.0+ (API 14)

## Bug tracker

If you find any bugs, please submit a bug report through Github and/or a pull request.

## Example project

An example of integration is provided in OrangeTrustBadgeDemo project.
You can download the last version of the demo app in the release page: https://github.com/Orange-OpenSource/orange-trust-badge-android/releases
To run the example project, clone the repo, import it in Android Studio (versions above v1.0).

## Installation

> **Embedded frameworks require a minSdkVersion of 10**

### 1. Download the compiled library

To use the component in your own project, the fastest way is to download the latest `aar` release library in the release page:
https://github.com/Orange-OpenSource/orange-trust-badge-android/releases
You will then have to include the `aar` in the `libs` folder of your project

### 2. Update dependencies

Make sure to have the flatDir option for your `libs` folder in repositories 
```groovy
allprojects {
    repositories {
        mavenCentral()
        flatDir {
            dirs 'libs'
        }
    }
}
``` 
Add following dependency to the build.gradle file of the module that will use the Orange trust badge (adapt the name to the version you downloaded if necessary):
```groovy
dependencies {
    //OTB
    compile(name: 'Orange_trust_badge-2.0.0-release', ext: 'aar')
    //android support
    compile 'com.android.support:appcompat-v7:27.1.1'
    compile 'com.android.support:cardview-v7:27.1.1'
    compile 'com.android.support:recyclerview-v7:27.1.1'
}
```   
### 3. Manage languages

The SDK includes default text in english and add a translation in french in the french localized directory values-fr.
If you do not have french language in your app, and want to keep only engligh, exclude french resources using:
```groovy
android {
    ...
    defaultConfig {
        ...
        resConfigs "en"
    }
}
``` 
If you want to use another language than english as default language, please provide translation in that language for all strings in the `values/strings.xml` file. Then build only the locales you support (overriding is necessary because the default is always kept in the package). For example, if I support Italian and Spanish, provide translation for one of the two as default language and than add this to your Gradle configuration file:
```groovy
android {
    ...
    defaultConfig {
        ...
        resConfigs "it", "es"
    }
}
``` 

#### Alternative methods

You can also:
* either directly copy, from the source code, the otb module in your project,
* or import it as a new `aar` module using the Android Studio New Module menu.

In both cases you will have to add it to your dependencies as follow (supposing you named the new module `otb`):
```groovy
dependencies {
    //android support
    compile 'com.android.support:appcompat-v7:27.1.1'
    compile 'com.android.support:cardview-v7:27.1.1'
    compile 'com.android.support:recyclerview-v7:27.1.1'
    //OTB
    compile project(':otb') //if you named the new module `otb`
}
```  

## Usage

### Initialization of the SDK

#### 1. Create necessary elements (example)

Create a list of `TrustBadgeElement` and a list of `Term`. TrustBadgeElements can be easily created using the helper class `TrustBadgeElementFactory`, which provide convenient methods to create a badge.

Two types of Badges can be created :

* `ElementType.PERMISSIONS` : Those badges will reflect the phone system permissions needed by the app
* `ElementType.APP_DATA` : Those badges will reflect the data used or needed by the app to work

To easily create the default permissions, you can use these methods :

* `TrustBadgeElementFactory.getDefaultPermissionElements(mContext)` : this method will create a list of `TrustBadgeElement` that will reflect the permission declared in the manifest for the app
    * If your app needs a special way to declare the system permissions (not reflecting the manifest), you can still add badges manually, controlling every state of it
* `TrustBadgeElementFactory.getDefaultAppDataElements(mContext)` : this method will create a list of `TrustBadgeElement` that will reflect the default app data that should be declared by an app in the trust badge
    * If your app needs a different set of app data badges, you can still add badges manually, controlling every state of it

To easily create default Terms, you can use this method :

* `TrustBadgeElementFactory.getDefaultTermElements(mContext)` : this method will create a default list of Terms with labels verified and up to date regarding ORange's commitment
    * If your app needs a different set of Terms, you can still add terms manually, controlling every state of it

```java
    List<TrustBadgeElement> trustBadgeElements = new ArrayList<TrustBadgeElement>();
    /** SYSTEM PERMISSIONS */

    //Adds the permisssions declared by the app in the manifest
    trustBadgeElements.add(TrustBadgeElementFactory.getDefaultPermissionElements(mContext));

    //Adds the default app data that should be declared by an app in the trust badge
    trustBadgeElements.add(TrustBadgeElementFactory.getDefaultAppDataElements(mContext);

    /** Terms - You can add how many videos and terms section as you need, but you can also just use the default method to get default terms elements */
    List<Term> terms = new ArrayList<Term>();
    /** A video hosted on a third party provider such as Daily Motion has to be added as a text link */
    terms.add(new Term(TermType.TEXT, R.string.otb_terms_video_text_title, R.string.otb_terms_video_text_content));
    /** A video hosted on your servers or directly accessible can be embedded in the otb componenet */
    terms.add(new Term(TermType.VIDEO, "Video Title" , "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"));
    //Provide your own terms if you want terms to be exposed
    terms.add(new Term(TermType.TEXT, "Personal Data Usage Title", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
    terms.add(new Term(TermType.TEXT, "Help Title", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
    terms.add(new Term(TermType.TEXT, "More Info Title", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
    /** or use default method to get default terms elements */
    terms.add(TrustBadgeElementFactory.getDefaultTermElements(mContext));

```

#### 2. Initialize

Initialize TrustBadgeManager singleton with added items : a list of `TrustBadgeElement` and a list of `Terms`.

```java
    TrustBadgeManager.INSTANCE.initialize(getApplicationContext(), trustBadgeElements, terms);
```

#### 3. Launch activity

Launch the main activity named `OtbActivity` to access to the TrustBadge component main UI.

```java
    Intent intent = new Intent(MainActivity.this, OtbActivity.class);
    this.startActivity(intent);
```

### Implementation of optional features

#### Improvement program

The improvment program method has been deprecated and is no longer used by the Trust Badge. It will be removed in the near future

#### Custom badge element

Optionally you can have custom badge elements.

Add an element to your badge Elements List :

```java
    /** How to create a custom badge and/or set it as a toggable */
    TrustBadgeElement customBadge = TrustBadgeElementFactory.build(mContext, GroupType.CUSTOM_DATA, ElementType.APP_DATA, R.string.custom_badge_2_title,R.string.custom_badge_2_label);
    customBadge.setEnabledIconId(R.drawable.otb_ic_contacts_black_32dp);
    customBadge.setDisabledIconId(R.drawable.otb_ic_contacts_black_32dp);
    customBadge.setToggable(true);
    customBadge.setAppUsesPermission(AppUsesPermission.TRUE);
    customBadge.setUserPermissionStatus(UserPermissionStatus.NOT_GRANTED);
    trustBadgeElements.add(customBadge);
```

When you initialize the badge, if you have added Toggle switches, provide initialization if you want persistent state.
If you saved the value in the shared preferences:

#### Badge listener

Optionally provide a listener to listen to badges with toggles (such as improvement program).
One BadgeListener is enough to listen for all the toggles in your implementation: By checking the `element.getNameKey()` and/or the `element.getGroupType()` you can have different behaviors.

* NOTE: You need to make sure to do not have multiple copy of the same listener

```java

    //We can add a badge listener. Make sure to do not have multiple copy of the same listener
    //You can also clear existing listener using: TrustBadgeManager.INSTANCE.clearBadgeListeners();
    if (!TrustBadgeManager.INSTANCE.getBadgeListeners().contains(this)) {
        TrustBadgeManager.INSTANCE.addBadgeListener(this);
    }

    TrustBadgeManager.INSTANCE.addBadgeListener(new BadgeListener(){
        @Override
        public void onBadgeChange(TrustBadgeElement trustBadgeElement, boolean value, AppCompatActivity callingActivity) {
            Log.d(TAG, "onBadgeChange trustBadgeElement =" + trustBadgeElement + " value=" + value);
            if (null != trustBadgeElement) {
                //In this example we treat differently the improvement Program switch
                if (GroupType.IMPROVEMENT_PROGRAM.equals(trustBadgeElement.getGroupType())) {
                    if (!value) {
                        //if user is deactivating the program, we ask for confirmation
                        showDialog(callingActivity); //check demo app for a sample dialog implementation
                    } else {
                        //Activate again the improvement program
                        saveValueToPreferences(true, PREF_IMPROVEMENT);
                        TrustBadgeManager.INSTANCE.setUsingImprovementProgram(true);
                    }
                } else { //We do not use the dialog for other switches
                    //Save this new value to preferences to make it persistent
                    saveValueToPreferences(value, trustBadgeElement.getNameKey());
                    //Change the UserPermissionStatus as it has been switched by user
                    trustBadgeElement.setUserPermissionStatus(value ? UserPermissionStatus.GRANTED : UserPermissionStatus.NOT_GRANTED);
                }
            }
        }
    });
```

Please Note: `addTrustBadgeElementListener` has been deprecated in version G01R00C01 to allow managing custom toggable badges and remove in G02R00C00.


## Customization 

### Customized colors (overloading file `colors.xml`)

Orange trust badge use `colorAccent` to for links, toggle and text as follows in a color resource file.
The header background color can also be redefined.
User can change this colors by editing the `colors.xml` resource file.

```xml
    <color name="colorAccent">#AA0000</color>
    <color name="otb_header_color">#00DD00</color>
```
### Customized logo (overloading default drawable, file `otb_header_logo.xml`)

The header logo can be redefined by defining your own image. You should provide a drawable named `otb_header_logo.xml`. 
Please be aware that an incorrect image can lead to malformed header page.

```xml
    <?xml version="1.0" encoding="utf-8"?>
    <selector xmlns:android="http://schemas.android.com/apk/res/android">
        <item android:drawable="@drawable/otb_illustration_bleu"/>
    </selector>
```
### Customized icons and labels

User should define its own labels and icon for the different badges, but default values are available to provide a decent app.
Please be aware that string resources are localized, so overriding french values should be done in **values-fr** resource directory.

### Customized otb activity theme

You can override the otb activity theme directly in your manifest (see demo app for an exemple)

In your manifest:
```xml
    <activity
        android:name="com.orange.essentials.otb.OtbActivity"
        android:configChanges="orientation|screenSize"
        android:theme="@style/AppTheme.MyOtbTheme"
        tools:replace="android:theme" />
```

Then, in your styles.xml file you can for exemple override the toolbar colors by overriding primary and primaryDark color:
```xml
    <style name="AppTheme.MyOtbTheme" parent="Theme.AppCompat.Light.DarkActionBar">
        <item name="colorPrimary">@color/otb_black</item>
        <item name="colorPrimaryDark">@color/otb_black</item>
    </style>
```

## Release note

Version 2.0.0

## License

 Copyright (C) 2016 Orange

 This software is distributed under the terms and conditions of the 'Apache-2.0' license which can be found in the file 'License.txt' in this package distribution or at

 http://www.apache.org/licenses/LICENSE-2.0

### Third-Party Code

 This software includes the following third-party code:

#### AOSP

   Copyright (C) 2006 The Android Open Source Project

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

## Contacts
BOESCH Vincent, [vincent.boesch@orange.com](mailto://vincent.boesch@orange.com)
PENAULT Aurore, [aurore.penault@orange.com](mailto://aurore.penault@orange.com)
ACCETTA Giovanni Battista, [giovannibattista.accetta@orange.com](mailto://giovannibattista.accetta@orange.com)
