# Commvault API Demo

		This project demonstrate how to use Commvault's Rest APIs to :
		Perform backup, restore and other CommCell operations from the Command Line, or through the use of REST APIs.
		Automate business processes by putting together a set of tasks in a specific order using Workflow.

## REST API Overview
Commvault REST (Representational State Transfer) APIs represent operations that are performed in the CommCell Console. You can use the REST APIs to create custom interfaces that focus on the operations your users need. The REST APIs are implemented on the HTTP protocol, so you can use them with your preferred programming language and tools.

## Required Capabilities

A REST API client should support the following:

* HTTPS and basic authentication
> For information about configuring HTTPS, see [Configuring Secured Access on a Web Service](https://documentation.commvault.com/commvault/v11_sp20/article?p=119525.htm).

* HTTP GET, POST, and DELETE requests
* Ability to set and read request and response headers
* XML or JSON responses

## Supported HTTP Methods

GET: Lists objects and includes the information needed to perform additional operations on the object. Sample use: list all of the users in the CommCell.

POST: Creates or updates objects. Most requests using the POST method require XML in the request body. XML template files are provided. Sample use: create a client group.

DELETE: Removes objects. Sample use: delete a storage policy.

## Responses

Responses to these API requests are served either in XML or JSON formats. To request a format, use the Accept request header, for example:

Accept: application/xml

Accept: application/json

For a detailed explanation on how things work, check out the [guide](http://documentation.commvault.com/commvault/) and [doc of Commvault Rest API](https://api.commvault.com/?version=latest).
