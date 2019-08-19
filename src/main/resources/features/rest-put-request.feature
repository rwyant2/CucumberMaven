@rest @put @api

Feature: Validate I can send a put request to a REST endpoint

  Scenario: (1) Someone set us up the test data
    When a "post" request is sent to "http://dummy.restapiexample.com/api/v1/create" with the below data
      |type|key   |value  |
      |body|name  |Megatron|
      |body|salary|9000   |
      |body|age   |40     |

    Then save the value of "id" from the response

  Scenario: (2) Validate PUT request by updating values using the feature file
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |
      |body   |name  |Megatron2|
      |body   |salary|9001    |
      |body   |age   |50      |

    Then the response comes back with the below values
      |name  |Megatron2|
      |salary|9001    |
      |age   |50      |

    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |

    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |Megatron2|
      |employee_salary|9001     |
      |employee_age   |50       |
      |profile_image  ||
    
# I would expect this to fail with "I can't find the id of banana", but it comes back as fine.
# This is a defect at the time of writing this. It does not update which is correct, but gives no error.
  Scenario: (3) Validate PUT request using the feature file but with a bad segment
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" with the below data
      |type   |key   |value    |
      |segment|id    |banana   |
      |body   |name  |Megatron3|
      |body   |salary|9002     |
      |body   |age   |51       |

    Then the response comes back with the below values
      |name  |Megatron3|
      |salary|9002     |
      |age   |51       |

    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |

# This will fail, it's still Megatron2, 9001, 50
    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |Megatron3|
      |employee_salary|9002     |
      |employee_age   |51       |
      |profile_image  ||    
    
# This should fail. The API requires an id
  Scenario: (4) Validate PUT request using the feature file but with a missing segment
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" with the below data
      |type   |key   |value    |
      |body   |name  |Megatron4|
      |body   |salary|9003     |
      |body   |age   |52       |

    Then the response comes back with the below values
      |name  |Megatron4|
      |salary|9003    |
      |age   |52      |
    
# Name is required and missing body elements are updated to null
  Scenario: (5) Validate PUT request using the feature file but with a missing body element
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" with the below data
      |type   |key   |value    |
      |segment|id    |((id))   |
      |body   |name  |Megatron5|
      |body   |age   |55       |

# ((null)) will always be a saved value so it can be used in situations like this
    Then the response comes back with the below values
      |name  |Megatron5|
      |age   |55      |
      |salary|((null))|

    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |

# behind the scenes, null = 0 when it comes to salary     
    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |Megatron5|
      |employee_salary|0     |
      |employee_age   |55       |
      |profile_image  ||    
    
# This should pass. "Banana" gets ignored, the rest are updated.
  Scenario: (6) Validate PUT request using the feature file but with an extra body element
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" with the below data
      |type   |key   |value  |
      |segment|id    |((id)) |
      |body   |name  |Megatron6|
      |body   |food  |banana |
      |body   |salary|9006   |
      |body   |age   |56     |

    Then the response comes back with the below values
      |name  |Megatron6|
      |food  |banana |
      |salary|9006   |
      |age   |56     |

    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |

    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |Megatron6|
      |employee_salary|9006     |
      |employee_age   |56       |
      |profile_image  ||    

  Scenario: (7) Validate PUT request using the feature file but with no real updates
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" with the below data
      |type   |key   |value    |
      |segment|id    |((id))   |
      |body   |name  |Megatron6|
      |body   |salary|9006   |
      |body   |age   |56     |

    Then the response comes back with the below values
      |name  |Megatron6|
      |salary|9006   |
      |age   |56     |

    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |

    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |Megatron6|
      |employee_salary|9006     |
      |employee_age   |56       |
      |profile_image  ||  
  
  Scenario: (8) Validate PUT request using a JSON file
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" using the JSON file "update-employee-request.json" and any below data
      |type   |key   |value   |
      |segment|id    |((id))  |

    Then the response matches the JSON in "update-employee-request.json" and any below data
      ||

    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |

    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |Megatron8|
      |employee_salary|10001     |
      |employee_age   |69       |
      |profile_image  ||

# Same thing as Scenario 3 above. It says it updates, when it doesn't and shouldn't.
  Scenario: (9) Validate PUT request using a JSON file with a bad segment
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" using the JSON file "update-employee-request.json" and any below data
      |type   |key   |value   |
      |segment|id    |banana  |
      |body   |name  |Megatron9|
      |body   |age   |50          |
      |body   |salary|9000        |

# I realize I can do this without the JSON, but I want to test in-feature value replacement
    Then the response matches the JSON in "update-employee-request.json" and any below data
      |name  |Megatron9|
      |age   |50          |
      |salary|9000        |

    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |

    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |Megatron9|
      |employee_salary|9000    |
      |employee_age   |50       |
      |profile_image  ||

# This should straight up fail
  Scenario: (10) Validate PUT request using a JSON file with a missing segment
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" using the JSON file "update-employee-request.json" and any below data
      ||

    Then the response matches the JSON in "update-employee-request.json" and any below data
      ||

  Scenario: (11) Validate PUT request using a JSON file with a missing body element
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" using the JSON file "update-employee-no-age.json" and any below data
      |type   |key   |value   |
      |segment|id    |((id))  |

    Then the response matches the JSON in "update-employee-no-age.json" and any below data
      |age|((null))|

    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |

    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |Megatron11|
      |employee_salary|10011     |
      |employee_age   |0       |
      |profile_image  ||

  Scenario: (12) Validate PUT request using a JSON file with an extra body element
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" using the JSON file "update-employee-extra-key.json" and any below data
      |type   |key   |value   |
      |segment|id    |((id))  |

    Then the response matches the JSON in "update-employee-extra-key.json" and any below data
      ||

    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |

    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |Megatron12|
      |employee_salary|10012     |
      |employee_age   |73       |
      |profile_image  ||
  
  Scenario: (13) Validate PUT request using a JSON file with no real updates
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" using the JSON file "update-employee-extra-key.json" and any below data
      |type   |key   |value   |
      |segment|id    |((id))  |

    Then the response matches the JSON in "update-employee-extra-key.json" and any below data
      ||

    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |

    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |Megatron12|
      |employee_salary|10012     |
      |employee_age   |73       |
      |profile_image  ||

  Scenario: (14) Validate PUT request using a SoapUI file
    When the request "Update" in SoapUI project "dummy.xml" is sent with any below data
      |type   |key |value |
      |segment|id  |((id))|

    Then the response comes back with the below values
      |name  |Megatron14|
      |salary|8001   |
      |age   |43     |

    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |

    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |Megatron14|
      |employee_salary|8001   |
      |employee_age   |43     |
      |profile_image  ||

# Same as Scenario 3, this should fail    
  Scenario: (15) Validate PUT request using a SoapUI file with a bad segment
    When the request "Update No Salary" in SoapUI project "dummy.xml" is sent with any below data
      |type   |key |value |
      |segment|id  |banana|

    Then the response comes back with the below values
      |name  |Megatron17|
      |salary|((null))|
      |age   |45     |

    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |

    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |Megatron17|
      |employee_salary|((null))|
      |employee_age   |45      |
      |profile_image  ||


  Scenario: (16) Validate PUT request using a SoapUI file with a missing segment
    When the request "Update" in SoapUI project "dummy.xml" is sent with any below data
      ||

    Then the response comes back with the below values
      |name  |Megatron14|
      |salary|8001   |
      |age   |43     |

  Scenario: (17) Validate PUT request using a SoapUI file with a missing body element
    When the request "Update No Salary" in SoapUI project "dummy.xml" is sent with any below data
      |type   |key |value |
      |segment|id  |((id))|

    Then the response comes back with the below values
      |name  |Megatron17|
      |salary|((null))|
      |age   |45     |

    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key   |value |
      |segment|id    |((id))|

    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |Megatron17|
      |employee_salary|0 |
      |employee_age   |45       |
      |profile_image  ||

  Scenario: (18) Validate PUT request using a SoapUI file with an extra body element
    When the request "Update Extra Element" in SoapUI project "dummy.xml" is sent with any below data
      |type   |key |value |
      |segment|id  |((id))|

    Then the response comes back with the below values
      |name  |Megatron18|
      |salary|8006   |
      |age   |47     |
      |hungry|banana |

    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |

    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |Megatron18|
      |employee_salary|8006   |
      |employee_age   |47     |
      |profile_image  ||

  Scenario: (19) Validate PUT request using a SoapUI file with no real updates
    When the request "Update" in SoapUI project "dummy.xml" is sent with any below data
      |type   |key   |value |
      |segment|id    |((id))|
      |body   |name  |Megatron18|
      |body   |salary|8006      |
      |body   |age   |47        |

    Then the response comes back with the below values
      |name  |Megatron18|
      |salary|8006   |
      |age   |47     |

    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |

    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |Megatron18|
      |employee_salary|8006   |
      |employee_age   |47     |
      |profile_image  ||