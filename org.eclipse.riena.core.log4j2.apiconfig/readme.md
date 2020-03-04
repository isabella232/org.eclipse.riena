Log4j 2 problems with OSGI
==========================

Using Log4j 2 out of the box within OSGI/Equinox still fails (version 2.13.0 of Log4j 2).
When doing so startup fails with the following error:

    ERROR StatusLogger Log4j2 could not find a logging implementation. Please add log4j-core to the classpath. Using SimpleLogger to log to the console

Unfortunately there exist several Log4j2 issues but they did not solve the problem although they have been closed as resolved.

Searching for this has revealed the following (helpful) links:  
* [Getting log4j2 to work in an osgi context](https://craftsmen.nl/getting-log4j2-to-work-in-an-osgi-context/)
* [log4j2 api cannot find log4j2 core in osgi environment](https://stackoverflow.com/questions/30045873/log4j2-api-cannot-find-log4j2-core-in-osgi-environment)

With the information in these links this fragment has been created which seems (!) to solve that problem.
A key to the solution is the log4j-provider.properties file in this fragment. See [Extending Log4j - LoggerContextFactory](https://logging.apache.org/log4j/log4j-2.3/manual/extending.html)
