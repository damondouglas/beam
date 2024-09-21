#
# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

"""
Looker connector
================

This module supports reading from and writing to the Google Cloud Platform Looker API 4.0
using the `official Python SDK <https://github.com/looker-open-source/sdk-codegen>`.
See the `Looker API reference <https://cloud.google.com/looker/docs/reference/available-apis>` for more details.

Security
--------

The Beam Looker connector acquires credentials via a :class:`~apache_beam.io.looker.Credentials`
To instantiate Credentials, currently the connector supports the use of the
`Google Cloud Secret Manager <https://cloud.google.com/security/products/secret-manager>`.
If a developer, needs alternative sources of credentials, they may simply extend the
:class:`~apache_beam.io.looker.Credentials` abstract base class.

The following is an example for configuring credentials sourced from Google Cloud Secret Manager,
where the `resource_id` is the path to the secret version and the format is the expected
`Looker .ini <https://github.com/looker-open-source/sdk-codegen/blob/main/python/README.rst#configuring-the-sdk>`
format:

    [Looker]
    base_url=https://yourlookerinstance.api.com
    client_id=YourClientID
    client_secret=YourClientSecret
    verify_ssl=True

    credentials = beam.io.looker.credentials.GoogleCloudSecretManager(
        resource_id='projects/project/secrets/secret/versions/version',
        format=beam.io.looker.credentials.FormatToMl
    )

In addition to the requirement for the credentials when using the Beam Looker connector,
a developer may provide these credentials to instantiate the Looker SDK as shown in the following example.

    import looker_sdk

    sdk = looker_sdk.init40(config_settings=credentials.ApiSettings())

Querying Data
-------------
This module supports running queries against Looker configured database connections.
Looker normalizes across numerous SQL dialects and provides a single platform for access administration.
Using the Beam Looker connector, database queries may be performed as shown in the following examples.
Looker distinguishes between "inline" queries and queries stored for later execution. The following
shows the use of the Beam Looker connector for executing an "inline" query.

    from looker_sdk import models40 as models

    requests = p | beam.Create([
        models.Query(
            model = 'thelook',
            view = 'inventory_items',
            fields = ['category.name','inventory_items.days_in_inventory_tier','products.count'],
            filters = {'category.name': 'socks'},
            sorts = ['products.count desc 0'],
            limit = 500,
            query_timezone = 'America/Los_Angeles'
        )
    ])
    results = requests | beam.io.looker.RunInlineQuery(format='json')

The Looker API supports the
`creation of queries <https://cloud.google.com/looker/docs/reference/looker-api/latest/methods/Query/create_query>`
to be executed later. Using the Beam Looker connector, a developer may execute a stored query. The following
example illustrates a synchronyous query.

    import looker_sdk
    from looker_sdk import models40 as models

    sdk = looker_sdk.init40(config_settings=credentials.ApiSettings())

    query = sdk.create_query(
        body=models.WriteQuery(
            model='thelook',
            view='inventory_items',
            fields=['category.name','inventory_items.days_in_inventory_tier','products.count']
        )
    )

    requests = p | beam.Create([query.id])
    results = requests | beam.io.looker.RunQueries(format='json')

The previous example illustrates a synchronyous execution of a stored query. The following example illustrates
the same as an asynchronous operation. This is useful when one expects larger amounts of data.

    import looker_sdk
    from looker_sdk import models40 as models

    sdk = looker_sdk.init40()

    query = sdk.create_query(
        body=models.WriteQuery(
            model='thelook',
            view='inventory_items',
            fields=['category.name','inventory_items.days_in_inventory_tier','products.count']
        )
    )

    query_task = sdk.create_query_task(
        query_id=query.id
    )

    requests = p | beam.Create([query_task.id])
    results = requests | beam.io.looker.RunQueriesAsync(format='json', credentials=credentials)

Generating Visualizations
-------------------------

Looker supports data visualizations via its `Look <https://cloud.google.com/looker/docs/saving-and-editing-looks>`
model type. The Beam Looker connector supports running a Look to generate a visualization based on the
current state of the data.


    import looker_sdk
    from looker_sdk import models40 as models

    sdk = looker_sdk.init40()

    query = sdk.create_query(
        body=models.WriteQuery(
            model='thelook',
            view='inventory_items',
            fields=['category.name','inventory_items.days_in_inventory_tier','products.count']
        )
    )

    look = sdk.create_look(
        body=models.WriteLookWithQuery(
            title='Inventory Items',
            description='Inventory items by category',
            query_id=query.id,
        )
    )

    requests = p | beam.Create([look.id])
    results = requests | beam.io.looker.RunLooks(format='png', credentials=credentials)
"""

