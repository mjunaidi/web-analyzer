WebAnalyzer
===========

Analyzing the internet.

Usage
=====

Run `mvn clean install` to compile the project.

Run `mvn jetty:run` to start the project.

Default port number is `9090`. Update `jetty.port` property in `pom.xml` to change it.

Access the project at http://localhost:9090/

Heroku commands
===============

Heroku link
    http://web-analyzer.herokuapp.com/

Tailing logs
    `heroku logs -t`

Re-scaling dyno
    `heroku ps:scale web=1`

To check running dyno
    `heroku ps`

In case cannot push after commit, use force push
    `git remote origin master -f`

Find Bugs/Find Sec Bugs commands
================================

The project has been integrated with [Find Sec Bugs](https://github.com/find-sec-bugs/find-sec-bugs/wiki/Maven-configuration).

To scan
```
mvn compile
mvn findbugs:findbugs
```

Analyzing the result
```
mvn findbugs:gui
```

An XML report is also generated at `target/findbugsXml.xml`. This file format can be read by **Jenkins**.
