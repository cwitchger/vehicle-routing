# Vehicle Routing with Time Windows (VRPTW) Plus Skill Sets

This solution is based off of the Open Source OptaPlanner examples [Vehicle Routing](https://github.com/kiegroup/optaplanner/tree/master/optaplanner-examples/src/main/java/org/optaplanner/examples/vehiclerouting) and [Task Assignment](https://github.com/kiegroup/optaplanner/tree/master/optaplanner-examples/src/main/java/org/optaplanner/examples/taskassigning).

In this solution, we use Business Optimizer to assign a set of Jobs to Specialist based on the following criteria:
* Specialists must arrive in the requested time window
* Specialists should arrive close to the start window
* Total travel distance should be minimized
* A Specialists skills should match or be close to the job requirements
* Specialists should not be scheduled to work overtime

The project is split in to two projects
1. [specialist-routing](specialist-routing/README.md) which can be deployed for remote execution or run locally
2. [routing-demo](routing-demo/README.md) which can be used for running the demo locally


## Kie Server Integration
For instructions on deploying the specialist routing solution to the kie server for remote execution, see [Kie Server Integration](docs/kieserver-integration.md)

## Continuous Planning
For an example on continuous planning with fact changes for the specialist routing solution, see [Continuous Planning](docs/continuous-planning.md)

## Real Road Distance
For an example on using real road distances, see [Real Road Distances](docs/road-distance.md)
