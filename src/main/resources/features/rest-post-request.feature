@rest @post @api
	
Feature: Create a new employee using POST in a REST web service

Scenario: POST request with values defined in feature file
  When a "post" request is sent to "http://dummy.restapiexample.com/api/v1/create" with the values listed below
    |name  |Starscream|
    |salary|9000      |
    |age   |50        |

  Then the response comes back with the below values
    |name  |Starscream|
    |salary|9000      |
    |age   |50        |
    |id    |*         |

Scenario: POST request with values defined in a JSONObject
  When a "post" request is sent to "http://dummy.restapiexample.com/api/v1/create" with the values listed below

  Then the response comes back with the below values
    |name  |Skywarp |
    |salary|9001    |
    |age   |51      |
    |id    |*       |

Scenario: POST request defined in a SoapUI xml project file
  When the request "Create" in SoapUI project "dummy.xml" is sent
  Then the response comes back with the below values
    |name  |Thundercracker|
    |salary|9002    |
    |age   |52      |
    |id    |*       |




