# Reporting-Application


This is the solution about the scope statement [here](http://dev.axel-chemin.fr/reporting-application/dl/MaintenanceReportTestReqDocV0_1.pdf)

**Note** : links are available up to the 19th January 2021

## How to use

### Production simulation

In order to test the program, you will need to use [this](http://dev.axel-chemin.fr/reporting-application/dl/machine-event-simulator-master.zip) simulator. A step-by-step guide is present in the README.

Maybe you will have problems with obsolete dependencies, there is a [compiled](http://dev.axel-chemin.fr/reporting-application/dl/machine-event-simulator-compiled.zip) version with openjdk15.

Once the server is listening to `61616` 

### Main program

You can use this program with these arguments :
.. : launch CLI program
.. : launch website
myProgram index <Workspace> : reindex files and close, should not be used
myProgram index <Workspace> <stdin|logfile> : reindex and process stream, stdin or the text file






