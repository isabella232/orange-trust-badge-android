#Orange trust badge Android Reference

![Orange trust badge : your privacy first](https://raw.githubusercontent.com/Orange-OpenSource/orange-trust-badge-android/master/orange_edo_demo_otb/src/main/res/mipmap-xxxhdpi/ic_launcher.png)

With the Orange trust badge, aka "Badge de confiance", give transparent informations and user control on personal data and help users to identify if your application has any sensitive features.

## Features
Orange trust badge displays how are handled the following data :

* Identity of the User
* Location
* Photos, medias and files
* Contacts
* Usage data
* Camera
* Calendar
* SMS
* Microphone
* Phone
* Body sensors / Connected objects
* Social Sharing
* In-app purchase
* Advertising

It also displays the following informations :

* Application's rating
* Data usage general description
* Help
* Privacy policy

It also :

* Works on Phones and Tablets with dedicated layout
* Is Localized in 2 languages (English,French)
* has API hooks
* allows UI Customization
* allows to embed a web hosted video with a native player

## Requirements

* Android 2.3.3 (API 10)
* Embedded videos will be shown on android 4.0+ (API 14)

## Bug tracker

If you find any bugs, please submit a bug report through Github and/or a pull request.

## Example project

An example of integration is provided in OrangeTrustBadgeDemo project.
To run the example project, clone the repo, import it in Android Studio (versions above v1.0).

## Installation

> **Embedded frameworks require a minSdkVersion of 10**

### Importing library

To use the component in your own project, import the lib `.aar` to your project as a new module (Do a clean and rebuild after that).

### Dependencies

Add following depedencies to the build.gradle file of the module that will use the Orange trust badge:
```groovy
        compile 'com.android.support:appcompat-v7:23.1.1'
        compile 'com.android.support:cardview-v7:23.1.1'
        compile 'com.android.support:recyclerview-v7:23.1.1'
        compile project(':otb')
```   
## Usage

### Initialization of the SDK

Create a list of TrustBadgeElement and a list of Term. TrustBadgeElements can be easily created using the helper class TrustBadgeElementFactory, which provide convenient methods to create a badge.

#### Create necessary elements (example)

```java
    List<TrustBadgeElement> trustBadgeElements = new ArrayList<TrustBadgeElement>();
    /** MAIN BADGES - They Will appear on the main dashboard */
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.IDENTITY, ElementType.MAIN, AppUsesPermission.TRUE));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.LOCATION, ElementType.MAIN));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.STORAGE, ElementType.MAIN));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.CONTACTS, ElementType.MAIN));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.IMPROVEMENT_PROGRAM, ElementType.MAIN, AppUsesPermission.TRUE));

    /** OTHERS BADGES - They Will appear when entering More Data info*/
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.CAMERA, ElementType.OTHERS));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.AGENDA, ElementType.OTHERS));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.SMS, ElementType.OTHERS));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.MICROPHONE, ElementType.OTHERS));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.PHONE, ElementType.OTHERS));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.SENSORS, ElementType.OTHERS));

    /** USAGE BADGES */
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, RatingType.TWELVE));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.BILLING, ElementType.USAGE));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.ADVERTISE, ElementType.USAGE));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.SOCIAL_INFO, ElementType.USAGE));

    /** Terms - You can add how many videos and terms section as you need */
    List<Term> terms = new ArrayList<Term>();
    //Provide your own terms if you want terms to be exposed
    terms.add(new Term(TermType.VIDEO, "Video Title" , "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"));
    terms.add(new Term(TermType.TEXT, "Personal Data Usage Title", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
    terms.add(new Term(TermType.TEXT, "Help Title", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
    terms.add(new Term(TermType.TEXT, "More Info Title", "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."));
```

Initialize TrustBadgeManager singleton with added items : a list of TrustBadgeElement and a list of Terms.

#### Initialize

```java
    TrustBadgeManager.INSTANCE.initialize(getApplicationContext(), trustBadgeElements, terms);
```

Launch the main activity named OtbActivity to access to the TrustBadge component main UI.

#### Launch activity

```java
    Intent intent = new Intent(MainActivity.this, OtbActivity.class);
    this.startActivity(intent);
```

### Implementation of optional features

Optionally provide a listener to listen to badges with toggles

#### Badge listener

```java
    TrustBadgeManager.INSTANCE.addTrustBadgeElementListener(new TrustBadgeElementListener(){
        @Override
        public void onBadgeChange(GroupType groupType, boolean value, AppCompatActivity callingActivity) {
            //Do your own work : example, what to do if badge improvement program change
        }
    });
```
Optionally set a value definig if your app currently uses improvement program badge (default true, if value not provided)

#### Improvement program

```java
    TrustBadgeManager.INSTANCE.setUsingImprovementProgram(isImprovementEnabled);
```
## Customization 

### Customized colors

Orange trust badge use `colorAccent` to for links, toggle and text as follows in a color resource file.
The header background color can also be redefined.
User can change this colors by editing the `colors.xml` resource file.

#### Color to define (overloading file `colors.xml`)

```xml
    <color name="colorAccent">#AA0000</color>
    <color name="otb_header_color">#00DD00</color>
```
### Customized logo

The header logo can be redefined by defining your own image. You should provide a drawable named `header_logo`. 
Please be aware that an incorrect image can lead to malformed header page

#### Image logo (overloading default drawable, file `header_logo.xml`)
```xml
    <?xml version="1.0" encoding="utf-8"?>
    <selector xmlns:android="http://schemas.android.com/apk/res/android">
        <item android:drawable="@drawable/otb_illustration_bleu"/>
    </selector>
```
### Customized icons and labels
User should define its own labels and icon for the different badges, but default values are available to provide a decent app.
Please be aware that string resources are localized, so overriding french values should be done in **values-fr** resource directory.

## Release note

Version 1.0

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
