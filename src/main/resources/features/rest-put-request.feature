@rest @put @api

Feature: Validate I can send a put request to a REST endpoint

  Scenario: (1) Someone set us up the test data
    When a "post" request is sent to "http://dummy.restapiexample.com/api/v1/create" with the below data
      |type|key   |value  |
      |body|name  |FortyOne|
      |body|salary|9000   |
      |body|age   |40     |

    Then save the value of "id" from the response

  Scenario: (2) Validate PUT request by updating values using the feature file
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |
      |body   |name  |FortyOne2|
      |body   |salary|9001    |
      |body   |age   |50      |

    Then the response comes back with the below values
      |name  |FortyOne2|
      |salary|9001    |
      |age   |50      |

    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |

    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |FortyOne2|
      |employee_salary|9001     |
      |employee_age   |50       |
      |profile_image  ||
    
# I would expect this to fail with "I can't find the id of banana", but it comes back as fine.
# This is a defect at the time of writing this. It does not update which is correct, but gives no error.
  Scenario: (3) Validate PUT request using the feature file but with a bad segment
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" with the below data
      |type   |key   |value    |
      |segment|id    |banana   |
      |body   |name  |FortyOne3|
      |body   |salary|9002     |
      |body   |age   |51       |

    Then the response comes back with the below values
      |name  |FortyOne3|
      |salary|9002     |
      |age   |51       |

    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |

# This will fail, it's still FortyOne2, 9001, 50
    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |FortyOne3|
      |employee_salary|9002     |
      |employee_age   |51       |
      |profile_image  ||    
    
# This should fail. The API requires an id
  Scenario: (4) Validate PUT request using the feature file but with a missing segment
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" with the below data
      |type   |key   |value    |
      |body   |name  |FortyOne4|
      |body   |salary|9003     |
      |body   |age   |52       |

    Then the response comes back with the below values
      |name  |FortyOne4|
      |salary|9003    |
      |age   |52      |
    
# Name is required and missing body elements are updated to null
  Scenario: (5) Validate PUT request using the feature file but with a missing body element
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" with the below data
      |type   |key   |value    |
      |segment|id    |((id))   |
      |body   |name  |FortyOne5|
      |body   |age   |55       |

# ((null)) will always be a saved value so it can be used in situations like this
    Then the response comes back with the below values
      |name  |FortyOne5|
      |age   |55      |
      |salary|((null))|

    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |

# behind the scenes, null = 0 when it comes to salary     
    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |FortyOne5|
      |employee_salary|0     |
      |employee_age   |55       |
      |profile_image  ||    
    
# This should pass. "Banana" gets ignored, the rest are updated.
  Scenario: (6) Validate PUT request using the feature file but with an extra body element
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" with the below data
      |type   |key   |value  |
      |segment|id    |((id)) |
      |body   |name  |FortyOne6|
      |body   |food  |banana |
      |body   |salary|9006   |
      |body   |age   |56     |

    Then the response comes back with the below values
      |name  |FortyOne6|
      |food  |banana |
      |salary|9006   |
      |age   |56     |

    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |

    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |FortyOne6|
      |employee_salary|9006     |
      |employee_age   |56       |
      |profile_image  ||    

  Scenario: (7) Validate PUT request using the feature file but with no real updates
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" with the below data
      |type   |key   |value    |
      |segment|id    |((id))   |
      |body   |name  |FortyOne6|
      |body   |salary|9006   |
      |body   |age   |56     |

    Then the response comes back with the below values
      |name  |FortyOne6|
      |salary|9006   |
      |age   |56     |

    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |

    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |FortyOne6|
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
      |employee_name  |FortyOne8|
      |employee_salary|10001     |
      |employee_age   |69       |
      |profile_image  ||

# Same thing as Scenario 3 above. It says it updates, when it doesn't and shouldn't.
  Scenario: (9) Validate PUT request using a JSON file with a bad segment
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" using the JSON file "update-employee-request.json" and any below data
      |type   |key   |value   |
      |segment|id    |banana  |
      |body   |name  |FortyOne9|
      |body   |age   |50          |
      |body   |salary|9000        |

# I realize I can do this without the JSON, but I want to test in-feature value replacement
    Then the response matches the JSON in "update-employee-request.json" and any below data
      |name  |FortyOne9|
      |age   |50          |
      |salary|9000        |

    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key   |value   |
      |segment|id    |((id))  |

    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |FortyOne9|
      |employee_salary|9000    |
      |employee_age   |50       |
      |profile_image  ||

#  Scenario: (10) Validate PUT request using a JSON file with a missing segment
#  Scenario: (11) Validate PUT request using a JSON file with a missing body element
#  Scenario: (12) Validate PUT request using a JSON file with an extra body element
#  Scenario: (13) Validate PUT request using a JSON file with no real updates
#  Scenario: (14) Validate PUT request using a SoapUI file
#  Scenario: (15) Validate PUT request using a SoapUI file with a bad segment
#  Scenario: (16) Validate PUT request using a SoapUI file with a missing segment
#  Scenario: (17) Validate PUT request using a SoapUI file with a missing body element
#  Scenario: (18) Validate PUT request using a SoapUI file with an extra body element
#  Scenario: (19) Validate PUT request using a SoapUI file with no real updates