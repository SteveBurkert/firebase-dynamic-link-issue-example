# firebase-dynamic-link-issue-example

This repo only intents to showcase a problem related to the firebase android sdk analytics part and has no further usage.

Dependencies:

platform     : 'com.google.firebase:firebase-bom:29.1.0', // Updated to 30.1.0
analytics    : 'com.google.firebase:firebase-analytics', // before update resolves to 20.1.0 - after to 21.0.0

We updated the bom from 29.1.0 to 30.1.0 and released a new version.
Since then we did not receive `dynamic_link_first_open` events anymore.

After downgrading the bom back to 29.1.0 for the next release,
the event is being tracked again.

## HowTo

In order to reproduce, make sure: 
- do not open the application on install
- install first with an old version of bom (see above) and enable debug log
- see that `dynamic_link_first_open` is tracked with old version 


Console

`adb shell setprop log.tag.FA VERBOSE`

`adb shell setprop log.tag.FA-SVC VERBOSE`

Open logcat:

`adb logcat -v time -s FA FA-SVC`

Next Step is to open the application using the dynamic link `https://repoexmaple.page.link/test` <-

`dynamic_link_first_open` will get logged or not (Logged when a user opens the app for the first time via a Dynamic Link.) 



