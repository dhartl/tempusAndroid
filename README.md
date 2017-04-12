# Tempus-Android
Eine Beispielapplikation für die Zeiterfassung.

## Überblick über die verwendeten Frameworks
* HTTP-Kommunikation
    * [Retrofit](http://square.github.io/retrofit/) für die Kommunikation mit der API.
    * [Apache OLTU](https://oltu.apache.org/) für die Anmeldung am OpenID-Connect-Server.
* Dependency-Injection
    * [Dagger 2](https://google.github.io/dagger/users-guide) für Dependency-Injection der Services.
    * [Butterknife](http://jakewharton.github.io/butterknife/) für die Injection der Android-Views.
* Datenbankzugriff
    * [GreenDAO](http://greenrobot.org/greendao/) für den Zugriff auf die Datenbank.
* RxJava
    * [RxJava2](https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0) für asynchrone Verarbeitung in Kombination mit [RxJava2-Adaper für Retrofit](https://github.com/square/retrofit/tree/master/retrofit-adapters/rxjava2)
* MVP-Pattern
    * [Nucleus](https://github.com/konmik/nucleus) für die Aufteilung im MVP-Pattern

## Entwicklerinformationen
Dieses Projekt verwendet die neuen [Java 8 Android Features](https://developer.android.com/guide/platform/j8-jack.html).
Daher muss derzeit für die Entwicklung die ["Dev"-Variante](https://developer.android.com/studio/preview/index.html) von Android Studio installiert werden

