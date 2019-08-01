@rest @get @api

Feature: Validate I can send a get request to a REST endpoint

  Scenario: GET request with no values
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employees" with the below data
      ||

    Then the response comes back with the below keys
      |id             |
      |employee_name  |
      |employee_salary|
      |employee_age   |
      |profile_image  |

  Scenario: GET request with no values fails when less than expected keys
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employees" with the below data
      ||

    Then the response comes back with the below keys
      |employee_name  |
      |employee_salary|
      |employee_age   |
      |profile_image  |

  Scenario: GET request with no values fails when greater then than expected keys
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employees" with the below data
      ||

    Then the response comes back with the below keys
      |id             |
      |employee_name  |
      |banana         |
      |employee_salary|
      |employee_age   |
      |profile_image  |

  Scenario: GET request when response had unexpected key
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employees" with the below data
      ||

    Then the response comes back with the below keys
      |id             |
      |employee_name  |
      |employee_salary|
      |employee_age   |
      |banana         |

 Scenario: Set up test data and validate GET response against dynamic test data
   When a "post" request is sent to "http://dummy.restapiexample.com/api/v1/create" with the below data
     |type|key   |value    |
     |body|name  |Galvatron7|
     |body|salary|1000     |
     |body|age   |40       |

   Then save the value of "id" from the response

# "((id))" refers to the value saved from the response in the above step
# the "id" under name refers to the {id} in the url below
   Then a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
    |type   |key |value |
    |segment|id  |((id))|

   Then the response comes back with the below values
    |id             |((id))|
    |employee_name  |Galvatron7|
    |employee_salary|1000     |
    |employee_age   |40       |
    |profile_image  ||
#
## This checks for the saved value of "id"
#    Then the response comes back with the below values
#      |id             |((id))|
#      |employee_name  |Galvatron1|
#      |employee_salary|1000     |
#      |employee_age   |40       |
#      |profile_image  ||
#
#Scenario: GET request with values verified against a JSON file.
#  When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/((id))" with the values listed below
#    ||
#
#  Then the response matches the JSON in "get-single-employee.json"
#
#
#Scenario: GET request with invalid value
#  When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/" with the values listed below
#    |type |name|value|
#    |param|id  |banana|
#
#  Then the response comes back with the below values
#    |id             |banana   |
#    |employee_name  |Galvatron|
#    |employee_salary|1000     |
#    |employee_age   |40       |
#    |profile_image  ||
#
#Scenario: GET request with values defined in a SoapUI xml project file
#  When the request "GET single employee" in SoapUI project "dummy.xml" is sent
#  Then the response comes back with the below values
#    |id             |((id))   |
#    |employee_name  |Galvatron|
#    |employee_salary|1000     |
#    |employee_age   |40       |
#    |profile_image  ||
##
##Scenario: Validate GET response against a JSONObject
##  When send the request "GET single employee" in SoapUI project "dummy.xml"
##  Then the response matches the JSON in "get-single-employee.json"

