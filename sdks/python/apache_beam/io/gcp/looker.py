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

The following is an example for configuring credentials sourced from Google Cloud Secret Manager,
where the `resource_id` is the path to the secret version and the format is the expected
`Looker .ini <https://github.com/looker-open-source/sdk-codegen/blob/main/python/README.rst#configuring-the-sdk>`
format:

    [Looker]
    base_url=https://yourlookerinstance.api.com
    client_id=YourClientID
    client_secret=YourClientSecret
    verify_ssl=True

    credentials = beam.io.looker.GoogleCloudSecretManagerCredentials(
        resource_id='projects/project/secrets/secret/versions/version'
    )

In addition to the requirement for the credentials when using the Beam Looker connector,
a developer may provide these credentials to instantiate the Looker SDK as shown in the following example.

    import looker_sdk

    sdk = looker_sdk.init40(config_settings=credentials)

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
    results = requests | beam.io.looker.RunInlineQuery(format='json', credentials=credentials)

The Looker API supports the
`creation of queries <https://cloud.google.com/looker/docs/reference/looker-api/latest/methods/Query/create_query>`
to be executed later. Using the Beam Looker connector, a developer may execute a stored query. The following
example illustrates a synchronyous query.

    import looker_sdk
    from looker_sdk import models40 as models

    sdk = looker_sdk.init40(config_settings=credentials)

    query = sdk.create_query(
        body=models.WriteQuery(
            model='thelook',
            view='inventory_items',
            fields=['category.name','inventory_items.days_in_inventory_tier','products.count']
        )
    )

    requests = p | beam.Create([query.id])
    results = requests | beam.io.looker.RunQueries(format='json', credentials=credentials)

The previous example illustrates a synchronous execution of a stored query. The following example illustrates
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
import abc
import contextlib
import logging
from typing import Union, cast, TypedDict

from looker_sdk.rtl.api_settings import SettingsConfig

# from apache_beam.io.requestresponse import Caller
# from apache_beam.io.requestresponse import RequestResponseIO
# from apache_beam.io.requestresponse import UserCodeExecutionException

_LOGGER = logging.getLogger(__name__)

try:
    import google.cloud.secretmanager_v1 as secretmanager
    import configparser
    import looker_sdk
    from looker_sdk import api_settings
except ImportError:
    _LOGGER.warning(
        'ImportError: GCP dependencies are not installed', exc_info=True)

__all__ = [
    'GoogleSecretManagerCredentials',
]


class _IniCredentials(api_settings.PApiSettings):
    _LOOKER = 'Looker'

    """Parses `Credentials` from an expected ini format."""

    def __exit__(self, __exc_type, __exc_value, __traceback):
        return None

    def __init__(self, data: str, *args, **kw_args):
        self.api_settings: Union[api_settings.SettingsConfig, None] = None
        self.data = data
        super().__init__(*args, **kw_args)

    def is_configured(self) -> bool:
        if self.api_settings is None:
            return False
        config = self.read_config()
        return all([k in config for k in ['base_url', 'client_id', 'client_secret']])

    def read_config(self) -> api_settings.SettingsConfig:
        if self.api_settings is not None:
            return self.api_settings

        _LOOKER = _IniCredentials._LOOKER

        parser = configparser.ConfigParser()

        parser.read_string(self.data)

        base_url = parser[_LOOKER]['base_url']
        verify_ssl = parser[_LOOKER]['verify_ssl']
        timeout = parser[_LOOKER]['timeout']
        client_id = parser[_LOOKER]['client_id']
        client_secret = parser[_LOOKER]['client_secret']
        if not client_id or not client_secret:
            raise ValueError('client_id or client_secret must be specified')

        result: SettingsConfig = {
            'client_id': client_id,
            'client_secret': client_secret,
            'base_url': base_url,
            'verify_ssl': verify_ssl,
            'timeout': timeout
        }

        return result


class GoogleSecretManagerCredentials(api_settings.ApiSettings):
    """Looker credentials stored in a Google Secret Manager Secret."""

    _CLIENT = secretmanager.SecretManagerServiceClient()

    def __init__(self, resource_id: str) -> None:
        self.request = secretmanager.AccessSecretVersionRequest({'name': resource_id})
        self.credentials: Union[_IniCredentials, None] = None
        super().__init__()
        """
        Args:
            resource_id: The Google Secret Manager Secret version path in the format:
                'projects/project/secrets/secret/versions/version'.
        """

    def is_configured(self) -> bool:
        if self.credentials is None:
            return False

        return self.credentials.is_configured()

    def read_config(self) -> SettingsConfig:
        if self.credentials is None:
            payload = self._read_secret_version()
            self.credentials = _IniCredentials(data=payload)

        return self.credentials.read_config()

    def _read_secret_version(self) -> str:
        response: secretmanager.AccessSecretVersionResponse \
            = GoogleSecretManagerCredentials._CLIENT.access_secret_version(self.request)
        return response.payload.data.decode('UTF-8')

    def __exit__(self, __exc_type, __exc_value, __traceback):
        return None
