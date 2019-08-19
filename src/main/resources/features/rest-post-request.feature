@rest @post @api
	
Feature: Create a new employee using POST in a REST web service

# The three types are:
# query, used in URI queries: www.whatever.com?key=value&key2=value2
# segment, used in URI param: www.whatever.com/{key} becomes www.whatever.com/value
# body, used in the body of a message: {"key":"value","key2":"value2"}
Scenario: POST request with values defined in this feature file
  When a "post" request is sent to "http://dummy.restapiexample.com/api/v1/create" with the below data
    |type|key   |value     |
    |body|name  |StarscreamA|
    |body|salary|9000      |
    |body|age   |50        |

# * means any value coming back is valid
  Then the response comes back with the below values
    |name  |StarscreamA|
    |salary|9000      |
    |age   |50        |
    |id    |*         |

Scenario: POST request with values defined in a JSON file
  When a "post" request is sent to "http://dummy.restapiexample.com/api/v1/create" using the JSON file "add-employee.json" and any below data
    ||

  Then the response comes back with the below values
    |name  |SkywarpA|
    |salary|9001   |
    |age   |51     |
    |id    |*      |

Scenario: POST request defined in a SoapUI file
  When the request "Create" in SoapUI project "dummy.xml" is sent with any below data
    ||

  Then the response comes back with the below values
    |name  |ThundercrackerA|
    |salary|9002    |
    |age   |52      |
    |id    |*       |

# This will come in handy later when testing GET
Scenario: POST request defined in a JSON file with values overwritten by a "body" entry
  When a "post" request is sent to "http://dummy.restapiexample.com/api/v1/create" using the JSON file "add-employee.json" and any below data
    |type|key |value|
    |body|name|DirgeA|

  Then the response comes back with the below values
    |name  |DirgeA  |
    |salary|9001   |
    |age   |51     |
    |id    |*      |

Scenario: POST request defined in a SoapUI file with values overwritten by a "body" entry
  When the request "Create" in SoapUI project "dummy.xml" is sent with any below data
    |type|key |value |
    |body|name|RamjetA|

  Then the response comes back with the below values
    |name  |RamjetA  |
    |salary|9002    |
    |age   |52      |
    |id    |*       |