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

```
.. : launch CLI program
.. : launch website (not yet implemented)
myProgram index <Workspace> <stdin|logfile> : reindex and process stream, stdin or the text file
myProgram data <Workspace> <FT> <FM> <FpM> <FE> <FP>
```

For example : `myProgram data C:\WORKSPACE 50 MACHINE_86,MACHINE_127 true * 15 `

You can see explaination of the filter chain below.

#### Filter chain

Generate a report, interface :

settings presets buttons \[REQ004\] \[REQ005\] \[REQ006\]

* (FT) *Filter Time* : 90days or less (can use a calendar in HTML AND countbox)

* (FM) *Filter Machine*: select (or not /disable/) a machine (should make a list of machine)

* (FpM) *Filter per Machine* : count errors per machine OR globally, like one machine (no sense if only one machine, true in this case)

* (FE) *Filter Error code* : select (or not /disable/) a list of codes (should make a list of error code)

* [(Re)GENERATE]

* (FP) *Precision/Number of table entry* : select (or not /disable/) the number of entry of the table (can be changed after processing), 10  default

* [Export PDF ; CSV]

For example :

* REQ,004:
FT *; FM NOT; FpM TRUE; FE NOT ; FP 10 ou FT *; FM *; FpM TRUE ; FE NOT ; FP 10

* REQ,005:
FT *; FM *; FpM TRUE; FE NOT ; FP 10

* REQ,006:
FT *; FM *; FpM TRUE; FE NOT ; FP NOT

