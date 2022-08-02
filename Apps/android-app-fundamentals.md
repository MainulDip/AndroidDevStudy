## Overview:
Personalized Jump Start Docs regarding android app's API, Fundamental Concepts And Communication between components

<br>

> ### There are 4 types of app components:
___
| Component | Class | AndroidManifest.xml | Methods |
| --- | --- | --- | --- |
| Activity | Intent | <activity> elements for activities | startActivity(), startActivityForResult()
| Service |  Intent, JobScheduler | <service> elements for services. | bindService()
| BroadcastRecever | Intent | <receiver> elements for broadcast receivers. | sendBroadcast(), sendOrderedBroadcast(), sendStickyBroadcast().
| ContentProvider | ContentResolver | query()): <provider> elements for content providers.
---

### Test (Unit-test) and Android-Test (instrumentation-test/UI):
There are 2 directory inside app's src directory. The "test" directory is for Unit testing and "androidTest" is for instrumentation-test or UI test.

### UI, View and ViewGroup:
The user interface (UI) of an app like text, images, buttons, etc are called Views. Each view has relationship with other views. A ViewGroup (ConstraintLayout, RelativeLayout, etc) is a container that View objects can sit in, and is responsible for arranging the Views inside it. LayoutEditor helps to customize layout elements (Views).

Margin can be used to position a view from an edge of its container.

- constraint layout: in the context of the Layout Editor represents a connection or alignment of a view to another view, the parent layout, or an invisible guideline. For constraint position mistake, use "Clear Constraints of the Selection" by right clicking the view/viewGroup.

### Views behind/top on other views (overlap):
In the constraint layout, the views (xml) will be drawn from first to last. The first sibling will drawn first and last will be drawn last. Same way the last sibling is the top most element, Other views element will be behind of it as positioned.

### Adding Images and icons:
From AndroidStudio go to View > Tool Windows > Resource Manager and then "Import Drawables" and select. Imported images and icons will be on the app > res > drawable.

To add a image: LayoutEditor >Palette then drag/add an ImageView, then use "Pick a Resource" dialog. use the image listed under the Drawable tab.

ImageView attributes:
- scaleType: to scale the image. centerCrop value for filling the screen without distortion.

### Folder and File Structure:
- app: contains most of the files for the app to change.
- res: contains all the resources.
- layout: is for screen layouts.
- activity_main.xml: it contains a description of your screen layout (res > layout > activity_main.xml)
- drawable (app > res > drawable): This Directory includes all images and icons for the app
- 

### Measurement Units (dp,sp):
- dp (Density-independent Pixels): An abstract unit that is based on the physical density of the screen. These units are relative to a 160 dpi screen, so one dp is one pixel on a 160 dpi screen. The ratio of dp-to-pixel will change with the screen density, but not necessarily in direct proportion. "dip" and "dp" are same.

- sp (Scale-independent Pixels, for Font-Size): Similar to dp unit, but also scaled by the user's font size preference. 

- px (Pixels) - Actual pixels or dots on the screen.
- in (Inches) - Physical size of the screen in inches.
- mm (Millimeters) - Physical size of the screen in millimeters.
- pt (Points) - 1/72 of an inch.

> Always use sp for font sizes and dip for everything else.