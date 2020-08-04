# PhoneStartUpPromo
Imagine a client giving out some television or phone and what they want is that whenever the tv/phone is switched on it shows their promotional logo or video for certain amount of time and then launches the default launcher.
So, let me explain,

Imagine a client giving out some television or phone and what they want is that whenever the tv/phone is switched on it shows their promotional logo or video for certain amount of time and then launches the default launcher.

Here, check my blog at https://www.oblate.in/post/making-a-android-mobile-tv-app-starting-on-reboot-and-never-again



I tried flutter but flutter cant have this done therefore i had to make in android.


This was a hard challenge.

Steps include:

Setting the app as an launcher in AndroidManifest.xml

<category android:name="android.intent.category.HOME" />
<category android:name="android.intent.category.DEFAULT" />

Making an mediaController to play the video stored in assets or in a file adding a VideoView in layouts activity_main.xml 

<VideoView
    android:id="@+id/videoPlay"
    android:layout_width="match_parent"
    android:layout_height="0dp"
    android:layout_weight="9"
    />
 In MainActivity.java make an autplay function to plav the video and to close the app programmatically use  

 finish(); and System.exit(0);

The trickiest part is aetting the app as default launcher.

Because when the device is rebooted the default launcher is not loaded but our app is loaded and when the app closes itself , having nowhere to go it goes back to default home activity which is our app and it re runs and loops. 

To counter that I tried to clear the default that the app is launcher by using the code below but what this did it cleared the default and then every time we have to select the default launcher.

pm.clearPackagePreferredActivities(getPackageName());
Using INTENT,  i tried that this app could call the the default home launcher and run it, but i found that most devices have different package names for default launchers and i couldn't find a way to programatically find the package name.

_intent.setClassName("com.android.launcher", "Launcher");
startActivity(_intent);
this was better option
Intent _intent = new Intent(Intent.ACTION_MAIN);
_intent.addCategory(Intent.CATEGORY_HOME);
_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
startActivity(_intent);
Then I tried enabling and disabling the launcher setting using 


pm.setComponentEnabledSetting(name,PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
/// enables 
pm.setComponentEnabledSetting(name,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,PackageManager.DONT_KILL_APP);
///disables

but since it disabled the component It wouldn't launch on startup/reboot

But the thing that worked is that the app has a condition if the device is rebooted i found the time from reboot from SystemClock.elapsedRealTime() and check if it was below 40sec then the app run time should be 10 sec or if not then it should be 1 millisec

Since the app is set at home whenever home button is clicked the app runs and I dont want that there it will run only a millisecond which the user cant notice.



        public void autoPlay() {
    String videoPath = "android.resource://com.oblate.tvappvideo/" + R.raw.oblate;
    Uri uri = Uri.parse(videoPath);
    videoPlayed.setVideoURI(uri);
    //no mediaController needed
    videoPlayed.start();
    long time;
    time = SystemClock.elapsedRealtime();
    int apprunTime=0;
    if( time <40000){
        apprunTime = 10000;
    }

final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {


                PackageManager pm = getPackageManager();
                ComponentName name = new ComponentName(MainActivity.this, MainActivity.class);
       

                Intent i = new Intent("android.intent.action.MAIN");
                i.addCategory("android.intent.category.HOME");
                List<ResolveInfo> lst = pm.queryIntentActivities(i, 0);
                if (lst != null) {
                    for (ResolveInfo resolveInfo : lst) {
                        try {
                            Intent home = new Intent("android.intent.action.MAIN");
                            home.addCategory("android.intent.category.HOME");
                            home.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
                            startActivity(home);
                            break;
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                    }
                }

                //stoping the app, Must not show Dialog
                finish();
                System.exit(0);

            }
        }, apprunTime);
            }
