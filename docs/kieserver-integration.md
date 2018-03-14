## Deploying Business Optimizer Rules to Kie Server

This kjar has been made so that it can be deployed to the kie server and can be executed remotely.

#### Requirements
* JBoss EAP 7.1
* Decision Manager 7

#### Setup
* Perform a default installation of JBoss EAP and Decision Manager
* Execute a `mvn clean install` to compile the specialist-routing kjar and install it to your local m2 folder

#### Executing Requests
In the following section we will use a series of ReST requests to create a container and execute the solver. You can use curl, Postman, SoapUI, or another tool to execute these requests.

##### Create container
URL:`http://localhost:8080/kie-server/services/rest/server/containers/specialist-routing`

Method: `Put`

Headers:

| Key | Value |
|---|---|
| Content-Type | application/json |

Authorization: `Basic`

Body:
```
{
	"release-id" : {
    	"artifact-id": "specialist-routing",
    	"group-id": "org.redhat.vrp",
    	"version" : "1.0.0"
    }
}
```

##### Add solver to container
URL:`http://localhost:8080/kie-server/services/rest/server/containers/specialist-routing/solvers/vrpSolver`

Method: `Put`

Headers:

| Key | Value |
|---|---|
| Content-Type | application/xml |
| X-KIE-ContentType | xstream |

Authorization: `Basic`

Body:
```
<solver-instance>
	<solver-config-file>org/redhat/vrp/solver/solverConfig.xml</solver-config-file>
</solver-instance>
```

##### Post content to solver
URL:`http://localhost:8080/kie-server/services/rest/server/containers/specialist-routing/solvers/vrpSolver/state/solving`

Method: `POST`

Headers:

| Key | Value |
|---|---|
| Content-Type | application/json |
| X-KIE-ContentType | JSON |

Authorization: `Basic`

Body:
```
{
  "org.redhat.vrp.domain.SpecialistRoutingSolution": {
    "distanceType": "AIR_DISTANCE",
    "distanceUnitOfMeasurement": "miles",
    "specialistList": [
      {
        "home": {
          "location": {
            "@type": "AirLocation",
            "name": "driver-1",
            "latitude": 41.62221343,
            "longitude": -88.00098373
          },
          "windowStart": 1514818800000,
          "windowEnd": 1514851200000
        },
        "specialistId": "driver-1",
        "skills": [
          {
            "name": "skill-f"
          }
        ],
        "location": {
          "@type": "AirLocation",
          "name": "driver-1",
          "latitude": 41.62221343,
          "longitude": -88.00098373
        },
        "totalWorkingHours": 0
      },
      {
        "home": {
          "location": {
            "@type": "AirLocation",
            "name": "driver-2",
            "latitude": 41.82189461,
            "longitude": -88.3035307
          },
          "windowStart": 1514818800000,
          "windowEnd": 1514851200000
        },
        "specialistId": "driver-2",
        "skills": [
          {
            "name": "skill-f"
          }
        ],
        "location": {
          "@type": "AirLocation",
          "name": "driver-2",
          "latitude": 41.82189461,
          "longitude": -88.3035307
        },
        "totalWorkingHours": 0
      }
    ],
    "jobList": [
      {
        "jobId": "job-1",
        "location": {
          "@type": "AirLocation",
          "name": "job-1",
          "latitude": 41.95243358,
          "longitude": -88.22983749
        },
        "windowStart": 1514818800000,
        "windowEnd": 1514851200000,
        "serviceDuration": 3600000,
        "requiredSkills": [
          {
            "name": "skill-f"
          }
        ]
      },
      {
        "jobId": "job-2",
        "location": {
          "@type": "AirLocation",
          "name": "job-2",
          "latitude": 42.00404441,
          "longitude": -88.27373586
        },
        "windowStart": 1514818800000,
        "windowEnd": 1514851200000,
        "serviceDuration": 3600000,
        "requiredSkills": [
          {
            "name": "skill-f"
          }
        ]
      },
      {
        "jobId": "job-3",
        "location": {
          "@type": "AirLocation",
          "name": "job-3",
          "latitude": 41.87056981,
          "longitude": -87.90780939
        },
        "windowStart": 1514818800000,
        "windowEnd": 1514851200000,
        "serviceDuration": 3600000,
        "requiredSkills": [
          {
            "name": "skill-f"
          }
        ]
      }
    ]
  }
}
```

##### Get best solution
URL:`http://localhost:8080/kie-server/services/rest/server/containers/specialist-routing/solvers/vrpSolver/bestsolution`

Method: `GET`

Headers:

| Key | Value |
|---|---|
| Content-Type | application/json |

Authorization: `Basic`

##### Add job while solving
URL:`http://localhost:8080/kie-server/services/rest/server/containers/specialist-routing/solvers/vrpSolver/bestsolution`

Method: `GET`

Headers:

| Key | Value |
|---|---|
| Content-Type | application/json |

Authorization: `Basic`
