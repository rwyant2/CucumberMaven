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
       |body|name  |GalvatronP|
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
      |employee_name  |GalvatronP|
      |employee_salary|1000     |
      |employee_age   |40       |
      |profile_image  ||

  Scenario: (6) GET request using a JSON file
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key |value |
      |segment|id  |((id))|

    Then the response matches the JSON in "get-single-employee.json" and any below data
      |employee_name|GalvatronP|
      |id  |((id))   |

  Scenario: (7) GET request using a JSON file with less than expected keys
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key |value |
      |segment|id  |((id))|

    Then the response matches the JSON in "get-single-employee.json" and any below data
      |employee_name|GalvatronP|
      |id           |((id))   |
      |food         |banana   |

  Scenario: (8) GET request using a JSON file with greater than expected keys
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key |value |
      |segment|id  |((id))|

    Then the response matches the JSON in "get-single-employee.json" and any below data
      |id  |((id))   |

  Scenario: (9) GET request using a JSON file and an invalid value
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key |value |
      |segment|id  |((id))|

    Then the response matches the JSON in "get-single-employee.json" and any below data
      |employee_name|GalvatronP|
      |id  |banana    |

  Scenario: (10) GET request using a JSON file and an invalid key
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key |value |
      |segment|id  |((id))|

    Then the response matches the JSON in "get-single-employee.json" and any below data
      |employee_name|GalvatronP|
      |banana       |((id))    |

  Scenario: (11) GET request using a JSON file and an invalid segment
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key |value |
      |segment|id  |banana|

    Then the response matches the JSON in "get-single-employee.json" and any below data
      |employee_name|GalvatronP|
      |id           |((id))    |

  Scenario: (12) GET request using a JSON file replacing one of the values
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key |value |
      |segment|id  |((id))|

    Then the response matches the JSON in "get-single-employee.json" and any below data
      |employee_name  |GalvatronP|
      |id             |((id))    |
      |employee_salary|9000      |

#  Scenario: (13) GET request using a SoapUI file with less than expected keys
#  Scenario: (14) GET request using a SoapUI file with greater than expected keys
#  Scenario: (15) GET request using a SoapUI file and an invalid value
#  Scenario: (16) GET request using a SoapUI file and an invalid key
#  Scenario: (17) GET request using a SoapUI file and an invalid segment
#  Scenario: (18) GET request using a SoapUI file replacing one of the values  