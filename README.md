Google Authenticator Lib for java
=================================
[![Build Status](https://travis-ci.org/meza/galib.png?branch=verify)](https://travis-ci.org/meza/galib)

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
boolean isValid = ga.isValidCode("your secret key", "code to test for validity");
```

###Using custom time
Sometimes it comes in handy to be able to use your own implementation of time. Especially when you're aiming for
testability and maintainability.
For this reason, you can tell the library where to get the time from, by implementing a class to the
```hu.meza.tools.galib.Clock``` interface. The only thing you should be aware of, that you need to return EPOCH
time. In java terms this means you need to DIVIDE the timeInMillis by 1000;

Once you have yor clock implementation, you can pass it in as a constructor parameter:

```java
Clock myClockImpl = new MyOwnClockImplementation();
GoogleAuthenticator ga = new GoogleAuthenticator(myClockImpl);
```

##Obtaining your secret key for google logins
Open your security settings and start setting up a new android device for 2 step verification.

When prompted to scan a barcode, scan it with a regular barcode scanner. It will hold a URI with a query
parameter called "secret". You're looking for the value of that key.

More [here](http://www.meza.hu/2014/02/havig-difficulties-testing-sites-with-2.html)
