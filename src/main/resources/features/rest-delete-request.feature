@rest @delete @api

Feature: Validate I can send a DELETE request to a REST endpoint

# Several of these should fail. My goal on this is to test the framework, not the API itself.
# Scenarios 1, 4, 6, and 9 are the only ones that should pass.

  Scenario: (1) Test data
    When a "post" request is sent to "http://dummy.restapiexample.com/api/v1/create" with the below data
      |type|key|value|
      |body|name|Soundwave|
      |body|salary|5000   |
      |body|age   |30     |

    Then save the value of "id" from the response

  Scenario: (2) DELETE request with missing segment
    When a "delete" request is sent to "http://dummy.restapiexample.com/api/v1/delete/{id}" with the below data
      ||

# This should fail. There's a bug in the API where it reports success even though there's no such
# employee record with the id of "banana"
  Scenario: (3) DELETE request with bad segment
    When a "delete" request is sent to "http://dummy.restapiexample.com/api/v1/delete/{id}" with the below data
      |type   |key |value |
      |segment|id  |banana|

  Scenario: (4) DELETE request with valid segment
    When a "delete" request is sent to "http://dummy.restapiexample.com/api/v1/delete/{id}" with the below data
      |type   |key |value |
      |segment|id  |((id))|

# Currently, a limitation of this framework is the inability to handled nested JSON structures
# The response coming back is {"success":{"text":"successfully! deleted Records"}}, which
# I can't evaluate right now.

  Scenario: (5) Verify the DELETE deleted. This one should fail.
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key |value |
      |segment|id  |((id))|

    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |Soundwave|
      |employee_salary|5000     |
      |employee_age   |30       |
      |profile_image  ||

  Scenario: (6) Test data for SoapUI scenarios
    When a "post" request is sent to "http://dummy.restapiexample.com/api/v1/create" with the below data
      |type|key   |value|
      |body|name  |Soundwave|
      |body|salary|5000   |
      |body|age   |30     |

    Then save the value of "id" from the response

  Scenario: (7) DELETE request from SoapUI with a missing segment
    When the request "Delete" in SoapUI project "dummy.xml" is sent with any below data
      ||

  Scenario: (8) DELETE request from SoapUI with a bad segment
    When the request "Delete" in SoapUI project "dummy.xml" is sent with any below data
      |type   |key |value |
      |segment|id  |banana|

  Scenario: (9) DELETE request from SoapUI with a valid segment
    When the request "Delete" in SoapUI project "dummy.xml" is sent with any below data
      |type   |key |value |
      |segment|id  |((id))|

  Scenario: (10) Verify the SoapUI DELETE deleted. This one should fail.
    When a "get" request is sent to "http://dummy.restapiexample.com/api/v1/employee/{id}" with the below data
      |type   |key |value |
      |segment|id  |((id))|

    Then the response comes back with the below values
      |id             |((id))|
      |employee_name  |Soundwave|
      |employee_salary|5000     |
      |employee_age   |30       |
      |profile_image  ||