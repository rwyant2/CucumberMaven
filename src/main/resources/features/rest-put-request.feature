@rest @put @api

Feature: Validate I can send a put request to a REST endpoint

  Scenario: (1) Someone set us up the test data
    When a "post" request is sent to "http://dummy.restapiexample.com/api/v1/create" with the below data
      |type|key   |value   |
      |body|name  |Thirteen|
      |body|salary|9000    |
      |body|age   |40      |

    Then save the value of "id" from the response

  Scenario: (2) Validate PUT request by updating values using the feature file
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" with the below data
      |type   |key   |value    |
      |segment|id    |((id))   |
      |body   |name  |Thirteen2|
      |body   |salary|9001     |
      |body   |age   |50       |

    Then the response comes back with the below values
      |name  |Thirteen2|
      |salary|9001     |
      |age   |50       |

# I would expect this to fail with "I can't find the id of banana", but it comes back as fine.
  Scenario: (3) Validate PUT request using the feature file but with a bad segment
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" with the below data
      |type   |key   |value    |
      |segment|id    |banana   |
      |body   |name  |Thirteen3|
      |body   |salary|9002     |
      |body   |age   |51       |

    Then the response comes back with the below values
      |name  |Thirteen3|
      |salary|9002     |
      |age   |51       |

  Scenario: (4) Validate PUT request using the feature file but with a missing segment
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" with the below data
      |type   |key   |value    |
      |body   |name  |Thirteen3|
      |body   |salary|9002     |
      |body   |age   |51       |

    Then the response comes back with the below values
      |name  |Thirteen3|
      |salary|9002     |
      |age   |51       |

# Name is required and missing body elements are updated to null
  Scenario: (5) Validate PUT request using the feature file but with a missing body element
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" with the below data
      |type   |key   |value    |
      |segment|id    |((id))   |
      |body   |name  |Thirteen4|
      |body   |age   |50       |

# ((null)) wil always be a saved value so it can be ued in situations like this    
    Then the response comes back with the below values
      |name  |Thirteen4|
      |age   |50  |
      |salary|((null))|

  Scenario: (6) Validate PUT request using the feature file but with an extra body element

  Scenario: (7) Validate PUT request using the feature file but with no real updates
    When a "put" request is sent to "http://dummy.restapiexample.com/api/v1/update/{id}" with the below data
      |type   |key   |value    |
      |segment|id    |((id))   |
      |body   |name  |Thirteen2|
      |body   |salary|9002     |
      |body   |age   |50       |

    Then the response comes back with the below values
      |name  |Thirteen2|
      |salary|9002     |
      |age   |50       |

  Scenario: (7) Validate PUT request using a JSON file
  Scenario: (8) Validate PUT request using a JSON file with a bad segment
  Scenario: (9) Validate PUT request using a JSON file with a missing segment
  Scenario: (10) Validate PUT request using a JSON file with a missing body element
  Scenario: (11) Validate PUT request using a JSON file with an extra body element
  Scenario: (12) Validate PUT request using a JSON file with no real updates
  Scenario: (13) Validate PUT request using a SoapUI file
  Scenario: (14) Validate PUT request using a SoapUI file with a bad segment
  Scenario: (15) Validate PUT request using a SoapUI file with a missing segment
  Scenario: (16) Validate PUT request using a SoapUI file with a missing body element
  Scenario: (17) Validate PUT request using a SoapUI file with an extra body element
  Scenario: (18) Validate PUT request using a SoapUI file with no real updates