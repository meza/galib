Google Authenticator Lib for java
=================================

##Installation
###Maven
```xml
<dependency>
	<groupId>hu.meza.tools</groupId>
	<artifactId>galib</artifactId>
	<version>1.0.0</version>
</dependency>
```

###sbt
```
"hu.meza.tools" % "galib" % "1.0.0"
```

##Usage
```java
GoogleAuthenticator ga = new GoogleAuthenticator("your secret key");
String passCode = ga.getCode();
```

##Obtaining your secret key for google logins
Open your security settings and start setting up a new android device for 2 step verification.

When prompted to scan a barcode, scan it with a regular barcode scanner. It will hold a URI with a query
parameter called "secret". You're looking for the value of that key.
