@rest @get @api

Feature: Validate I can send a get request to a REST endpoint

  Scenario: (1) GET request with no values
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employees" with the below data
      ||

    Then the response comes back with the below keys
      |id             |
      |employee_name  |
      |employee_salary|
      |employee_age   |
      |profile_image  |

  Scenario: (2) GET request with no values fails when greater than expected keys
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employees" with the below data
      ||

    Then the response comes back with the below keys
      |employee_name  |
      |employee_salary|
      |employee_age   |
      |profile_image  |

  Scenario: (3) GET request with no values fails when less then than expected keys
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employees" with the below data
      ||

    Then the response comes back with the below keys
      |id             |
      |employee_name  |
      |banana         |
      |employee_salary|
      |employee_age   |
      |profile_image  |

  Scenario: (4) GET request when response had unexpected key
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employees" with the below data
      ||

    Then the response comes back with the below keys
      |id             |
      |employee_name  |
      |employee_salary|
      |employee_age   |
      |banana         |

  Scenario: (5) Set up test data and validate GET response against dynamic test data
    When a "post" request is sent to "http://dummy.restapiexample.com/api/v1/create" with the below data
       |type|key   |value    |
       |body|name  |GalvatronN|
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
      |employee_name  |GalvatronN|
      |employee_salary|1000     |
      |employee_age   |40       |
      |profile_image  ||

  Scenario: (6) GET request with values verified against a JSON file.
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key |value |
      |segment|id  |((id))|

    Then the response matches the JSON in "get-single-employee.json" and any below data
      |employee_name|GalvatronN|
      |id  |((id))   |

  Scenario: (7) GET request with values verified against a JSON file with less than expected keys.
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key |value |
      |segment|id  |((id))|

    Then the response matches the JSON in "get-single-employee.json" and any below data
      |employee_name|GalvatronN|
      |id  |((id))   |
      |food|banana   |

  Scenario: (8) GET request with values verified against a JSON file with greater than expected keys.
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key |value |
      |segment|id  |((id))|

    Then the response matches the JSON in "get-single-employee.json" and any below data
      |id  |((id))   |

  Scenario: (9) GET request with values verified against a JSON file and an invalid value
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key |value |
      |segment|id  |((id))|

    Then the response matches the JSON in "get-single-employee.json" and any below data
      |employee_name|GalvatronN|
      |id  |banana    |

  Scenario: (10) GET request with values verified against a JSON file and an with invalid key
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key |value |
      |segment|id  |((id))|

    Then the response matches the JSON in "get-single-employee.json" and any below data
      |employee_name|GalvatronN|
      |banana|((id))  |

  Scenario: (11) GET request with values verified against a JSON file and an invalid query param
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key |value |
      |segment|id  |banana|

    Then the response matches the JSON in "get-single-employee.json" and any below data
      |employee_name|GalvatronN|
      |id  |((id))  |

  Scenario: (12) GET request with values verified against a JSON file replacing one of the values
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key |value |
      |segment|id  |((id))|

    Then the response matches the JSON in "get-single-employee.json" and any below data
      |employee_name|GalvatronN|
      |id  |((id))  |
      |employee_salary|9000|
  
  
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
#Scenario: () GET request with values defined in a SoapUI xml project file
#  When the request "GET single employee" in SoapUI project "dummy.xml" is sent
#  Then the response comes back with the below values
#    |id             |((id))   |
#    |employee_name  |Galvatron|
#    |employee_salary|1000     |
#    |employee_age   |40       |
#    |profile_image  ||
##
##Scenario: () Validate GET response against a JSONObject
##  When send the request "GET single employee" in SoapUI project "dummy.xml"
##  Then the response matches the JSON in "get-single-employee.json"

