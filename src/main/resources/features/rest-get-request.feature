@rest @get @api

Feature: Validate I can send a get request to a REST endpoint

  Scenario: GET request with no values
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employees" with the values listed below
      ||

    Then the response comes back with the below keys
      |id             |
      |employee_name  |
      |employee_salary|
      |employee_age   |
      |profile_image  |

  Scenario: GET request with no values fails when less than expected keys
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employees" with the values listed below
      ||

    Then the response comes back with the below keys
      |employee_name  |
      |employee_salary|
      |employee_age   |
      |profile_image  |

  Scenario: GET request with no values fails when greater then than expected keys
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employees" with the values listed below
      ||

    Then the response comes back with the below keys
      |id             |
      |employee_name  |
      |banana         |
      |employee_salary|
      |employee_age   |
      |profile_image  |

  Scenario: GET request when response had unexpected key
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employees" with the values listed below
      ||

    Then the response comes back with the below keys
      |id             |
      |employee_name  |
      |employee_salary|
      |employee_age   |
      |banana         |

 Scenario: Set up test data and validate GET response against dynamic test data
   When a "post" request is sent to "http://dummy.restapiexample.com/api/v1/create" with the values listed below
     |type|name  |value|
     |body|name  |GalvatronF|
     |body|salary|1000     |
     |body|age   |40       |
   Then save the value of "id" from the response

# new hotness
  Then a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/((id))" with the values listed below
    ||
#    |type |name|value | this would be as a query (i.e.: endpoint.com/?id=12345
#    |param|id  |((id))|

# This checks for anything as an id
  Then the response comes back with the below values
    |id             |*|
    |employee_name  |GalvatronF|
    |employee_salary|1000     |
    |employee_age   |40       |
    |profile_image  ||

# This checks for the saved value of "id"
   Then the response comes back with the below values
     |id             |((id))|
     |employee_name  |GalvatronF|
     |employee_salary|1000     |
     |employee_age   |40       |
     |profile_image  ||

## The example API I'm using can only look up by id even though names must be unique.
## If this were in the real world, I could implement something like this scenario to
## use any id and verify the parts of the response I care about.

#
## The purpose of this is to verify my stepdefs more than it is to test the API itself.
## To that end, these id values are hard-coded to ensure I can verify using this feature
## file, a JSON file, or a SoapUI file.
#
#Scenario: GET request with values defined in the feature file
#  When send a "get" request to endpoint-resource "http://dummy.restapiexample.com/api/v1/employee" with the below values
#    |id|59435|
#
#  Then the response comes back with the below values
#    |id             |59435    |
#    |employee_name  |Galvatron|
#    |employee_salary|1000     |
#    |employee_age   |40       |
#    |profile_image  ||
#
#Scenario: GET request with invalid value
#  When send a "get" request to endpoint-resource "http://dummy.restapiexample.com/api/v1/employee" with the below values
#    |id|banana|
#
#  Then the response comes back with the below values
#    |id             |banana   |
#    |employee_name  |Galvatron|
#    |employee_salary|1000     |
#    |employee_age   |40       |
#    |profile_image  ||
#
#Scenario: GET request with values defined in a SoapUI xml project file
#  When send the request "GET single employee" in SoapUI project "dummy.xml"
#  Then the response comes back with the below values
#    |id             |59435    |
#    |employee_name  |Galvatron|
#    |employee_salary|1000     |
#    |employee_age   |40       |
#    |profile_image  ||
#
#Scenario: Validate GET response against a JSONObject
#  When send the request "GET single employee" in SoapUI project "dummy.xml"
#  Then the response matches the JSON in "get-single-employee.json"

