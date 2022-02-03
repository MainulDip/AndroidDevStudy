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