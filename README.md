# andriod_call
android_call is an open source softphone for voice and video over IP calling.
It is fully SIP-based, for all calling..


**License**
Copyright © Bueno Technologies Inc
android_call is dual licensed, and is available either :


under a GNU/GPLv3 license, for free (open source). Please make sure that you understand and agree with the terms  of this license before using it (see LICENSE file for details).


under a proprietary license, for a fee, to be used in closed source applications. Contact Belledonne Communications for any question about costs and services.



**Documentation**
Refer this doc to understand how the app works. 
https://docs.google.com/document/d/15AJlwUH2ZMbFxaqJO5UqP8suuVEMsS9MsC5NRvk7UwA/edit?usp=sharing




**
Building the app**
If you have Android Studio, simply open the project, wait for the gradle synchronization and then build/install the app.
It will download the linphone library from its Maven repository as an AAR file so you don't have to build anything yourself.
If you don't have Android Studio, you can build and install the app using gradle:
./gradlew assembleDebug
will compile the APK file (assembleRelease to instead if you want to build a release package), and then
./gradlew installDebug
to install the generated APK in the previous step (use installRelease instead if you built a release package).
APK files are stored within ./app/build/outputs/apk/debug/ and ./app/build/outputs/apk/release/ directories.
When building a release AppBundle, use releaseAppBundle target instead of release.
Also make sure you have a NDK installed and that you have an environment variable named ANDROID_NDK_HOME that contains the path to the NDK.
This is to be able to include native libraries symbols into app bundle for the Play Store.

**Building a local SDK**

Clone the linphone-sdk repository from out gitlab:

git clone https://github.com/Vivektilva/andriod_call.git --recursive

Rebuild the app in Android Studio.

**
Native debugging**


Install LLDB from SDK Tools in Android-studio.


In Android-studio go to Run->Edit Configurations->Debugger.


Select 'Dual' or 'Native' and add the path to linphone-sdk debug libraries (build/libs-debug/ for example).


Open native file and put your breakpoint on it.


Make sure you are using the debug AAR in the app/build.gradle script and not the release one (to have faster builds by default the release AAR is used even for debug APK flavor).


Debug app.


**
Known issues**


If you encounter the couldn't find "libc++_shared.so" crash when the app starts, simply clean the project in Android Studio (under Build menu) and build again.
Also check you have built the SDK for the right CPU architecture using the -DLINPHONESDK_ANDROID_ARCHS=armv7,arm64,x86,x86_64 cmake parameter.


Push notification might not work when app has been started by Android Studio consecutively to an install. Remove the app from the recent activity view and start it again using the launcher icon to resolve this.



**Troubleshooting**

Behavior issue
When submitting an issue on our Github repository, please follow the template and attach the matching library logs:


To enable them, go to Settings -> Advanced and toggle Debug Mode. If they are already enabled, clear them first using the Reset logs button on the About page.


Then restart the app, reproduce the issue and upload the logs using the Send logs button on the About page.


Finally paste the link to the uploaded logs (link is already in the clipboard after a successful upload).





**Firebase** 
Now that Google Cloud Messaging has been deprecated and will be completely removed on April 11th 2019, the only official way of using push notifications is through Firebase.
However to make Firebase push notifications work, the project needs to have a app/google-services.json file that contains the configuration.
You can configure your firebase project and setup google-services.json file to get push notifiations and for incoming calls . 
Also you can use this firebase for the crash reporting . 





