## Continuous Planning with Fact Changes

This specialist routing project has been made so that it can be deployed to the kie server and executed remotely.

#### Setup
For setup instructions see [Kie Server Integration](kieserver-integration.md)

##### Add continuous solver
URL:`http://localhost:8080/kie-server/services/rest/server/containers/specialist-routing/solvers/nonstopVRPSolver`

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
	<solver-config-file>org/redhat/vrp/solver/nonstopSolverConfig.xml</solver-config-file>
</solver-instance>
```

##### Post content to solver
URL:`http://localhost:8080/kie-server/services/rest/server/containers/specialist-routing/solvers/nonstopVRPSolver/state/solving`

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

##### Add job while solving
URL:`http://localhost:8080/kie-server/services/rest/server/containers/specialist-routing/solvers/nonstopVRPSolver/problemfactchanges`

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
  "org.redhat.vrp.domain.solver.realtime.AddJobProblemFactChange": {
    "job": {
      "jobId": "job-4",
      "location": {
        "@type": "AirLocation",
        "name": "job-4",
        "latitude": 41.85243358,
        "longitude": -88.12983749
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
  }
}
```

##### Check if fact change was processed
URL:`http://localhost:8080/kie-server/services/rest/server/containers/specialist-routing/solvers/nonstopVRPSolver/problemfactchanges/processed`

Method: `GET`

Headers:

| Key | Value |
|---|---|
| Content-Type | application/json |

Authorization: `Basic`

##### Get best solution
URL:`http://localhost:8080/kie-server/services/rest/server/containers/specialist-routing/solvers/nonstopVRPSolver/bestsolution`

Method: `GET`

Headers:

| Key | Value |
|---|---|
| Content-Type | application/json |

Authorization: `Basic`

##### Terminate Solving
URL:`http://localhost:8080/kie-server/services/rest/server/containers/specialist-routing/solvers/nonstopVRPSolver/state/terminating-early`

Method: `POST`

Authorization: `Basic`
