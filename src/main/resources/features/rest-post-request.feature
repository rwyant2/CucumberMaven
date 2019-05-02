@rest @post @api
	
Feature: Create a new employee using POST in a REST web service

Scenario: POST request with values defined in feature file
  When send a "post" request to endpoint-resource "http://dummy.restapiexample.com/api/v1/create" with the below values
    |name  |Starscream|
    |salary|9000      |
    |age   |50        |

  Then the response comes back with the below values
    |name  |Starscream|
    |salary|9000      |
    |age   |50        |
    |id    |*         |

Scenario: POST request with values defined in JSONObject
  When send a "Post" request to endpoint-resource "http://dummy.restapiexample.com/api/v1/create" with the JSON "add-employee.json"

  Then the response comes back with the below values
    |name  |Skywarp |
    |salary|9001    |
    |age   |51      |
    |id    |*       |

Scenario: POST request defined in a SoapUI xml project file
  When send the request "Create" in SoapUI project "dummy.xml"
  Then the response comes back with the below values
    |name  |Thundercracker|
    |salary|9002    |
    |age   |52      |
    |id    |*       |

#Scenario: Test for an appropriate fail for a duplicate employee name
#Scenario: Test for an appropriate fail for an unexpected value in the response
#Scenario: Test for an appropriate fail for different number of values in the response
#Scenario: Test for an appropriate fail for

