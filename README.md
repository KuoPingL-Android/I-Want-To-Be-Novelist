![banner](https://github.com/KuoPingL-Android/I-Want-To-Be-Novelist/blob/master/images/cover.png)
<p align="center">A book-creating app that allows writers to write and share their self-customized books.</p>
<p align="center">
<a href="https://play.google.com/store/apps/details?id=studio.saladjam.iwanttobenovelist"><img width = "170" height="70" src="https://github.com/KuoPingL-Android/I-Want-To-Be-Novelist/blob/master/images/google.svg"/></a>
 </p>
<br></br>

## Feaures
* Create, Customize and Share you books worldwide 
* Read, Like and Follow books you enjoy
* Easy to use editor, allow you to customize your contents by adding draggable and resizable images
* Search books by popularity, published date, and recommendation
<br></br>

## Screenshots
<p align="justify">
  <img width="270" height="480" src="https://github.com/KuoPingL-Android/I-Want-To-Be-Novelist/blob/master/images/login_01.png"/>
  <img width="270" height="480" src="https://github.com/KuoPingL-Android/I-Want-To-Be-Novelist/blob/master/images/home_01.png"/>
  <img width="270" height="480" src="https://github.com/KuoPingL-Android/I-Want-To-Be-Novelist/blob/master/images/home_02.png"/>
</p>
<br>
<p align="justify">
  <img width="270" height="480" src="https://github.com/KuoPingL-Android/I-Want-To-Be-Novelist/blob/master/images/search_01.png"/>
  <img width="270" height="480" src="https://github.com/KuoPingL-Android/I-Want-To-Be-Novelist/blob/master/images/profile_01.png"/>
  <img width="270" height="480" src="https://github.com/KuoPingL-Android/I-Want-To-Be-Novelist/blob/master/images/profile_02.png"/>
</p>

<br>
<p align="justify">
  <img width="270" height="480" src="https://github.com/KuoPingL-Android/I-Want-To-Be-Novelist/blob/master/images/profile_03.png"/>
  <img width="270" height="480" src="https://github.com/KuoPingL-Android/I-Want-To-Be-Novelist/blob/master/images/editor_01.png"/>
  <img width="270" height="480" src="https://github.com/KuoPingL-Android/I-Want-To-Be-Novelist/blob/master/images/editor_02.png"/>
  </p>

<br>
<p align="justify">
  <img width="270" height="480" src="https://github.com/KuoPingL-Android/I-Want-To-Be-Novelist/blob/master/images/editor_03.png"/>
</p>

<br></br>

## Implementation
### Architecture
* MVVM

### Design Pattern
* Observer
* Singleton
* Factory

### Core Function
* Editor with words surround image effect
* Use Firestore to store info on book contents, including image location
* Handled touch events, allowing images to be resizable and draggable
* Facebook & Google login

### UI
* ViewGroup customization (Editor and Add-on Image)
* Activity & Fragment
* Jetpack Navigation
* Fragments & Dialogs
* RecyclerView
* CardView
* Floating Action Button

### Network
* Retrofit2
* Firestore & Firebase Storage
* Facebook SDK
* Google SDK

### Analysis
* Crashlytics

### Testing
* JUnit
* Espresso

### Versions
* [release] 1.1.2 - 2020/03/14<br>
Update: App Icon<br><br>
FIX: Text not shown when reading  <br>
* [release] 1.1.1 - 2020/03/13<br> 
FIX: Editor unable to display URL strings correctly. <br>
* [release] 1.1.0 - 2020/03<br> 
First release<br>
UPDATE : Editor with a more intuitive design Fixed : Allowing words that maxed out the editor width to be displayed correctly.
* [beta] 1.0.0 - 
<br>Initial Version</br> 

## Requirements
* Android 7+
* Android Studio 3.6.1
* Gradle Version 5.4.1

## Contact
Jimmy Liu <br>
kuopingl@gmail.com
