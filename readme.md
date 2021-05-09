## Portfolio Rebalancing Service
### Overview
> Subsriber model used to udpate portfolios
---
 - `Market` has a list of instances of `Portfolio` that must be updated on a monthly basis.<br>
 - `Portfolio` has a an instance of `Statement`,the desired `Allocation` and an instance of `SIP`.<br>
 - `Statement` contains a list of instances of `Record` which are in turn identified by the month to which it corresponds.<br>
 - `Record` contains instances of `Allocation`.<br>
Each of the above classes come with builders that help in instantiating for different scenarios. They are additionally equipped with expansive API.
---
### Run project
```
mvn clean install -DskipTests -q assembly:single
java -jar <path_to>/geektrust.jar <absolute_path_to_input_file>
```
### Run tests
```
mvn test
```
