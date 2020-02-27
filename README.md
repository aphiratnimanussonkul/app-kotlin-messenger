#### Add Firebase to your Android app
#####1. Create Firebase project
#####2. Add Firebase to your Android app
######2.1 Register app
...Open file build.gradle (Module: app) 
...Check at android.defaultConfig.applictionId "com.example.app_kotlin_messenger"
>Copy "com.example.app_kotlin_messenger" 
...At input "Android package name" paste "com.example.app_kotlin_messenger"
...At input "App nickname (optional)" and "Debug signing certificate SHA-1 (optional)" it's optional
...Hit next
#####2.2 Download config file
...Hit Download google-services.json file 
...Copy that file and paste at "Your_project_folder/app"
...Hit next
#####2.3 Add Firebase SDK
...Open file build.gradle(Project: App-Kotlin-Messenger) 
...Check at buildscript.repositories find "google()" if not add it
>Check at buildscript.dependencies add "classpath 'com.google.gms:google-services:4.3.3'"
...Check at allprojects.repositories find "google()" if not add it
...Open file build.gradle(Module: app)
>Add "apply plugin: 'com.google.gms.google-services'" at the top of file
>Check at dependencies add "implementation 'com.google.firebase:firebase-analytics:17.2.2'"
