# Real Road Distances

This specialist routing project has been made so that it can be deployed to the kie server and executed remotely.

## Setup
For setup instructions see [Kie Server Integration](kieserver-integration.md)

### Add solver
Add a solver following the instructions here: [Kie Server Integration](kieserver-integration.md)

### Post content to solver
URL:`http://localhost:8080/kie-server/services/rest/server/containers/specialist-routing/solvers/vrpSolver/state/solving`

Method: `POST`

Headers:

| Key | Value |
|---|---|
| Content-Type | application/json |
| X-KIE-ContentType | JSON |

Authorization: `Basic`

Body:
Use the JSON file [located here](../generate-data/data/output/json-examples/output.json).

### Get best solution
Instructions here: [Kie Server Integration](kieserver-integration.md)

### Terminate Solving
Instructions here: [Kie Server Integration](kieserver-integration.md)
