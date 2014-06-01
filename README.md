WebAnalyzer
===========

Analyzing the internet.

Usage
=====

Run mvn clean install to compile the project.

Run mvn jetty:run to start the project.

Access the project at http://localhost:9090/.

Heroku commands
===============

Heroku link
  http://web-analyzer.herokuapp.com/

Tailing logs
  heroku logs -t

Re-scaling dyno
  heroku ps:scale web=1

To check running dyni
  heroku ps

In case cannot push after commit, use force push
  git remote origin master -f

