#CucumberMaven<br>
19Aug2019<br>
<br>
In addition to using Maven with Cucucmber, this can also test APIs using either JSON files or SoapUI project XML files.<br>
<br>
At this point the framework can:<br>
<ul>
  <li>Send GET, POST, PUT, and DELETE requests to a RESTful service.</li>
  <li>Requests can be defined in the feature file, a JSON file, or in a SoapUI XML project file. Requests in a SoapUI file have to have a unique name.</li>
  <li>Verify the responses using expected results defined in the feature file or a JSON file.</li>
  <li>Key-value pairs in a request or response using JSON or SoapUI file can be overwritten in a feature file. You can also add key-value pairs.</li>
  <li>Save the current value of a key-value pair from a JSON response that can be used for later scenarios in the same feature.
    Use null in either requests or responses when appropriate.</li>
</ul>
<br>
Currently, it can*not* handle JSONArrays or nested JSONObjects. Simple lists of key-value pairs only.<br>
<br>
