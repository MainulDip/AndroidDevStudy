
### Deep Linking With NavigationUI:
* Deep Link and Linking : Deep link a request from outside of the app to a specific/exact destination/page of the app. Deep-Linking means, making an app ready for receiving such requests and launching the exact thing of the app.

Ex: tapping a notification can open an app's exact destination/page. Or tapping youtube videos can open youtube-app other than on the browser, etc.

NavigationUI has a `NavDeepLinkBuilder` class to construct a `PendingIntent` that will take the user to a specific destination.

### A Widget With DeepLinked:
Building a Widget that can be used by user to launch the app's specific destination:

To do, first add necessary tag in manifest.xml. The receiver class declared in manifest is the entrypoint which will setup a Widget that will be available to use by user and tapping on it will open the exact destination of the app. Signature => `class DeepLinkAppWidgetProvider : AppWidgetProvider() {...}`
```xml
<manifest>
    <application>
        <activity>...</activity>

        <receiver android:name=".DeepLinkAppWidgetProvider"
            android:exported="true">
            <intent-filter>
                <action android:name="android.appwidget.action.APPWIDGET_UPDATE" />
            </intent-filter>

            <meta-data
                android:name="android.appwidget.provider"
                android:resource="@xml/deep_link_appwidget_info" />
        </receiver>

    </application>
</manifest>
```
### DeepLink Handler Class With AppWidgetProviders:
The steps are
- Build `RemoteViews`
- Build a `PendingIntent` using `NavDeepLinkBuilder` and pass it to the remoteView using `setOnClickPendingIntent` along with click button
- then call appWidgetManager.updateAppWidget(appWidgetIds, remoteViews), that comes with the override fn onUpdate

```kotlin
/**
* This class will 
*/
class DeepLinkAppWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val remoteViews = RemoteViews(
            context.packageName,
            R.layout.deep_link_appwidget
        )

        val args = Bundle()
        args.putString("myarg", "From Widget")
        val pendingIntent = NavDeepLinkBuilder(context)
                .setGraph(R.navigation.mobile_navigation)
                .setDestination(R.id.deeplink_dest)
                .setArguments(args)
                .createPendingIntent()

        remoteViews.setOnClickPendingIntent(R.id.deep_link_button, pendingIntent)

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews)

    }
}
```

Note

`setGraph` includes the navigation graph.
- `setDestination` specifies where the link goes to.
- `setArguments` includes any arguments you want to pass into your deep link.

* By default NavDeepLinkBuilder will start your launcher Activity. This can be override using NavDeepLinkBuilder's `setComponentName()`

### DeepLink BackStack:
The backstack for a deep link is determined using the navigation graph that had been injected. If that activity has parent activity, it will also be taken.

More complicated navigation can include nested navigation graphs. The app:startDestination at each level of the nested graphs determines the backstack. 

Docs: https://developer.android.com/guide/navigation/principles

### Web Link to Deep Link:
to allow a web link to open an activity of an app, a intent filter and associated URL can be used. Docs: https://developer.android.com/training/app-links/deep-linking#adding-filters

This can be done with Navigation Library. by allowing to map URLs directly to destinations in the navigation graph using `<deepLink>` attribute.
```xml
<fragment ...>
    <argument android:name="myarg" />
    <deepLink app:uri="www.example.com/{myarg}" />
</fragment>
```
Note: without scheme name will support both http and https, placeholder is the argument

Also Manifest should have the `nav-graph` listed. Which will generate all the necessary `intent-filter` regarding deep-link listed in the navigation graph
```xml
<activity>
    <intent-filter></intent-filter>
    <nav-graph android:value="@navigation/mobile_navigation" />
</activity>
```

The generated manifest will look
```xml
<!-- Generated Manifest.xml -->
<activity>
    <intent-filter></intent-filter>
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />

        <category android:name="android.intent.category.DEFAULT" />

        <category android:name="android.intent.category.BROWSABLE" />

        <data android:scheme="http" />

        <data android:scheme="https" />

        <data android:host="www.example.com" />

        <data android:pathPrefix="/" />
    </intent-filter>
</activity>
```
adb command to launch this
```sh
adb shell am start -a android.intent.action.VIEW -d "http://www.example.com/urlTest"
```

* Android 12 and ahead, app needs to manually configured to open a specific link by default. Or need to be verified. Otherwise will open in Chrome by default
