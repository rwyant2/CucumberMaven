@rest @put @api

Feature: Validate I can send a put request to a REST endpoint

  Scenario: Someone set us up the test data
    When send a "post" request to endpoint-resource "http://dummy.restapiexample.com/api/v1/create" with the below values
      |name  |Cyclonus|
      |salary|9000    |
      |age   |50      |
    Then save the id in the response
    Then the response comes back with the below values
      |name  |Cyclonus|
      |salary|9000    |
      |age   |50      |
      |id    |*       |

# For the purpose of testing my stepdefs, I'm hard-coding the id for these scenarios.
  Scenario: PUT request updating values using a feature file
    When send a "PUT" request to endpoint-resource "http://dummy.restapiexample.com/api/v1/update/" with the below values
      |id    |62876|
      |name  |Cyclonus|
      |salary|9001    |
      |age   |50      |

    Then the response comes back with the below values
      |name  |Cyclonus|
      |salary|9001    |
      |age   |50      |

  Scenario: PUT request updating values using a JSON file
    When send a "Put" request to endpoint-resource "http://dummy.restapiexample.com/api/v1/update/" with the JSON "update-employee.json"
    Then the response comes back with the below values
      |name  |Cyclonus2|
      |salary|9007    |
      |age   |52      |

  Scenario: PUT request updating values using a SoupUI project
    When send the request "Update" in SoapUI project "dummy.xml"
    Then the response comes back with the below values
      |name  |Scourge|
      |salary|8001   |
      |age   |43     |
      |id    |*      |


#  Scenario: PUT request updating all values with the same values
#    When send a "put" request to endpoint-resource "http://dummy.restapiexample.com/api/v1/update/" with the below values
#      |id             |59435  |
#      |employee_name  |Unicron|
#      |employee_salary|9002   |
#      |age            |52     |
#
#    Then the response comes back with the below values
#      |name  |Unicron |
#      |salary|9002    |
#      |age   |52      |
#      |id    |*       |
#
#
#  Scenario: PUT request with an invalid id
#    When send a "put" request to endpoint-resource "http://dummy.restapiexample.com/api/v1/update/" with the below values
#      |id             |59435  |
#      |employee_name  |Unicron|
#      |employee_salary|9002   |
#      |age            |52     |
#
#    Then the response comes back with the below values
#      |name  |Unicron |
#      |salary|9002    |
#      |age   |52      |
#      |id    |*       |
#
#  Scenario: PUT request with a missing id
#    When send a "put" request to endpoint-resource "http://dummy.restapiexample.com/api/v1/update/" with the below values
#      |id             |59435  |
#      |employee_name  |Unicron|
#      |employee_salary|9002   |
#      |age            |52     |
#
#    Then the response comes back with the below values
#      |name  |Unicron |
#      |salary|9002    |
#      |age   |52      |
#      |id    |*       |

  #  Scenario: PUT request with a missing value
#    When send a "put" request to endpoint-resource "http://dummy.restapiexample.com/api/v1/update/" with the below values
#      |id             |59435  |
#      |employee_name  |Unicron|
#      |employee_salary|9002   |
#      |age            |52     |
#
#    Then the response comes back with the below values
#      |name  |Unicron |
#      |salary|9002    |
#      |age   |52      |
#      |id    |*       |

  #  Scenario: PUT request with an unexpected value
#    When send a "put" request to endpoint-resource "http://dummy.restapiexample.com/api/v1/update/" with the below values
#      |id             |59435  |
#      |employee_name  |Unicron|
#      |employee_salary|9002   |
#      |age            |52     |
#
#    Then the response comes back with the below values
#      |name  |Unicron |
#      |salary|9002    |
#      |age   |52      |
#      |id    |*       |