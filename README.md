# OTB

## Introduction

The OrangeTrustBadge Component can be used to present Legal Information, privacy policy, terms of use or any other information about an application.

The OrangeTrustBadge Component is compatible with Android 10 to 23 and is build for phones and tablet in portrait or landscape mode.

The OrangeTrustBadge Component provides a GUI to display informations regarding major system permissions and host app features.


## Usage

* To run the example project, clone the repo, import it in Android Studio (versions above v1.0).

* To use the component in your own project, add the lib .aar to to your project.

#### Customized colors
User should define its own color named colorAccent to change default color for links, toggle and text as follows in a color resource file.
The header background color can also be redefined
##### Color to define (red exemple)
    <color name="colorAccent">#AA0000</color>
    <color name="otb_header_color">#00DD00</color>

#### Customized logo
The header logo can be redefined by defining your own image. You should provide a drawable named header_logo 
Please be aware that an incorrect image can lead to malformed header page
##### Image logo (surcharging default drawable, file header_logo.xml)
    <?xml version="1.0" encoding="utf-8"?>
    <selector xmlns:android="http://schemas.android.com/apk/res/android">
        <item android:drawable="@drawable/otb_illustration_bleu"/>
    </selector>

#### Customized icons and labels
User should define its own labels and icon for the different badges, but default values are available to provide a decent app.
Please be aware that string resources are localized, so overriding french values should be done in **values-fr** resource directory.

#### Configure the component and add in your project
To configure the component follow these steps

1. Create a list of TrustBadgeElement and a list of Term. TrustBadgeElements can be easily created using the helper class TrustBadgeElementFactory, which provide convenient methods to create a badge
##### Create necessary elements (example)
    List<TrustBadgeElement> trustBadgeElements = new ArrayList<TrustBadgeElement>();
    /** MANDATORY : MAIN BADGE */
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.IDENTITY, ElementType.MAIN, AppUsesPermission.TRUE));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.LOCATION, ElementType.MAIN));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.STORAGE, ElementType.MAIN));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.CONTACTS, ElementType.MAIN));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.IMPROVEMENT_PROGRAM, ElementType.MAIN, AppUsesPermission.TRUE));

    /** NOT MANDATORY : OTHERS BADGES */
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.CAMERA, ElementType.OTHERS));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.AGENDA, ElementType.OTHERS));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.SMS, ElementType.OTHERS));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.MICROPHONE, ElementType.OTHERS));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.PHONE, ElementType.OTHERS));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.SENSORS, ElementType.OTHERS));

    /** MANDATORY : USAGE BADGES */
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, RatingType.TWELVE));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.BILLING, ElementType.USAGE));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.ADVERTISE, ElementType.USAGE));
    trustBadgeElements.add(TrustBadgeElementFactory.build(mContext, GroupType.SOCIAL_INFO, ElementType.USAGE));

    /** Terms */
    List<Term> terms = new ArrayList<Term>();
    //Provide your own terms if you want terms to be exposed
    terms.add(new Term(TermType.VIDEO, R.string.otb_terms_video_title, R.string.otb_terms_video_content));
    terms.add(new Term(TermType.TEXT, R.string.otb_terms_usage_title, R.string.otb_terms_usage_content));
    terms.add(new Term(TermType.TEXT, R.string.otb_terms_help_title, R.string.otb_terms_help_content));
    terms.add(new Term(TermType.TEXT, R.string.otb_terms_more_info_title, R.string.otb_terms_more_info_content));


2. Initialize TrustBadgeManager singleton with Mandatory items : a list of TrustBadgeElement and a list of Terms.
##### Initialise
    TrustBadgeManager.INSTANCE.initialize(getApplicationContext(), trustBadgeElements, terms);


3. Optionally provide a listener to listen to badges with toggles
##### Badge listener
    TrustBadgeManager.INSTANCE.addTrustBadgeElementListener(new TrustBadgeElementListener(){
        @Override
        public void onBadgeChange(GroupType groupType, boolean value, AppCompatActivity callingActivity) {
            //Do your own work : example, what to do if badge improvement program change
        }
    });

4. Optionaly set a value definig if your app currently uses improvement program badge (default true, if value not provided)
##### Improvement program
    TrustBadgeManager.INSTANCE.setUsingImprovementProgram(improvement);


5. Launch the main activity named OtbActivity to access to the TrustBadge component main UI.
##### Launch activity
    Intent intent = new Intent(MainActivity.this, OtbActivity.class);
    this.startActivity(intent);


## Release note

Version 1.0

## License

 Copyright (C) 2016 Orange

 This software is distributed under the terms and conditions of the 'Apache-2.0' license which can be found in the file 'License.txt' in this package distribution or at

 http://www.apache.org/licenses/LICENSE-2.0

### Third-Party Code

 This software includes the following third-party code.

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


