Google Authenticator Lib for java
=================================

Some context at [this blogpost](http://www.meza.hu/2014/02/havig-difficulties-testing-sites-with-2.html).

##Installation
###Maven
```xml
<dependency>
	<groupId>hu.meza.tools</groupId>
	<artifactId>galib</artifactId>
	<version>1.1.0</version>
</dependency>
```

###sbt
```
"hu.meza.tools" % "galib" % "1.1.0"
```

##Usage

###Generate passcode
```java
GoogleAuthenticator ga = new GoogleAuthenticator();
String passCode = ga.getCode("your secret key");
```

###Verify passcode
```java
GoogleAuthenticator ga = new GoogleAuthenticator();
boolean isValid = isValidCode("your secret key", "code to test for validity");
```

##Obtaining your secret key for google logins
Open your security settings and start setting up a new android device for 2 step verification.

When prompted to scan a barcode, scan it with a regular barcode scanner. It will hold a URI with a query
parameter called "secret". You're looking for the value of that key.

More [here](http://www.meza.hu/2014/02/havig-difficulties-testing-sites-with-2.html)
