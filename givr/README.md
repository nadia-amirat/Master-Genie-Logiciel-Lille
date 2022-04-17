# GivR

**The platform to help students by giving them donations**

[![CI](https://gitlab.univ-lille.fr/liticia.medjoudj.etu/givr/badges/master/pipeline.svg)](https://gitlab.univ-lille.fr/liticia.medjoudj.etu/givr)

## Contributors

* Ali Abidar
* Nadia Amirat
* Alexandre Bonvoisin
* Mohamed-Said Hammoudi
* Anthony Hanson
* Liticia Medjoudj  

## How to run ?

To run the application, please open a command terminal in the project's root folder then execute the following command:
```
mvn spring-boot:run
```  

To run tests, please open a command terminal in the project's root folder then execute the following command:
```
mvn test
```  

## Common issues

### Compilation fail

On compilation, you get a message like:
```
[ERROR] javac: invalid target release: 11
[ERROR] Usage: javac <options> <source files>
[ERROR] use -help for a list of possible options
```  

There are two solutions :
1. Change the `fork` option in `pom.xml` for the `compiler` goal:  
    Update:
    ```
    <fork>true</fork>
    ```  
    To:
    ```
    <fork>false</fork>
    ```  
2. Set `JAVA_HOME` in environment variable (depend on targeted OS)