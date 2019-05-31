@rest @get @api

Feature: Validate I can send a get request to a REST endpoint

  Scenario: GET request with no values
    When send a "get" request to endpoint-resource "http://dummy.restapiexample.com/api/v1/employees"

    Then the response comes back with the below keys
      |id             |
      |employee_name  |
      |employee_salary|
      |employee_age   |
      |profile_image  |

  Scenario: GET request with no values fails when less than expected keys
    When send a "get" request to endpoint-resource "http://dummy.restapiexample.com/api/v1/employees"

    Then the response comes back with the below keys
      |employee_name  |
      |employee_salary|
      |employee_age   |
      |profile_image  |

  Scenario: GET request with no values fails when greater then than expected keys
    When send a "get" request to endpoint-resource "http://dummy.restapiexample.com/api/v1/employees"

    Then the response comes back with the below keys
      |id             |
      |employee_name  |
      |banana         |
      |employee_salary|
      |employee_age   |
      |profile_image  |

  Scenario: GET request when response had unexpected key
    When send a "get" request to endpoint-resource "http://dummy.restapiexample.com/api/v1/employees"

    Then the response comes back with the below keys
      |id             |
      |employee_name  |
      |employee_salary|
      |employee_age   |
      |banana         |

# These are going to require editing. The samples I'm using don't have a way to look up by name
# even though names have to be unique.  While I'd build dynamic requests in reality,
# the purpose of this feature is to make sure my stepdefs work.
Scenario: Set up test data
  When send a "post" request to endpoint-resource "http://dummy.restapiexample.com/api/v1/create" with the below values
    |name  |Galvatron |
    |salary|1000      |
    |age   |40        |

Scenario: GET request with values defined in the feature file
  When send a "get" request to endpoint-resource "http://dummy.restapiexample.com/api/v1/employees" with the below values
    |id|4502|
#
#  Then the response comes back with the below values
#    |id             |4502     |
#    |employee_name  |Galvatron|
#    |employee_salary|1000     |
#    |employee_age   |40       |
#    |profile_image  ||
#
#Scenario: GET request with values defined in JSONObject
#  When send a "Get" request to endpoint-resource "http://dummy.restapiexample.com/api/v1/create" with the JSON "get-employee.json"
#
#  Then the response comes back with the below values
#    |name  |Skywarp |
#    |salary|9001    |
#    |age   |51      |
#    |id    |*       |
#
#Scenario: GET request with values defined in a SoapUI xml project file
#  When send the request "Create" in SoapUI project "dummy.xml"
#
#  Then the response comes back with the below values
#    |name  |Thundercracker|
#    |salary|9002    |
#    |age   |52      |
#    |id    |*       |
#
#Scenario: GET request with invalid value
#Scenario: Validate GET response against a JSONObject
#Scenario: GET request using dynamic value