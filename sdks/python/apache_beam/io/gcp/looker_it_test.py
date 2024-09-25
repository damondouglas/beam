import os
import time
import unittest

import apache_beam as beam
from apache_beam.io.gcp.looker import GoogleSecretManagerCredentials
from apache_beam.io.gcp.looker import ResultFormat
from apache_beam.io.gcp.looker import RunInlineQueries
from apache_beam.io.gcp.looker import RunQueries
from apache_beam.io.gcp.looker import RunLooks

try:
    import google.cloud.secretmanager_v1 as secretmanager
    import looker_sdk
    from looker_sdk import api_settings
    from looker_sdk.sdk.api40 import models
except ImportError:
    unittest.SkipTest('google.cloud.secretmanager_v1 or looker_sdk not found')

_SECRET_MANAGER_VERSION_RESOURCE_ID = os.getenv('SECRET_MANAGER_VERSION_RESOURCE_ID')
_CREDENTIALS = GoogleSecretManagerCredentials(resource_id=_SECRET_MANAGER_VERSION_RESOURCE_ID)
_MODEL = 'apache-beam-testing_public_looker_explores_us'
_VIEW = 'public_us_io_performance_metrics'
_FIELDS = [
    'public_us_io_performance_metrics.io',
    'public_us_io_performance_metrics.sdk',
    'public_us_io_performance_metrics.read_or_write'
]
_QUERY = models.WriteQuery(
    model=_MODEL,
    view=_VIEW,
    fields=_FIELDS,
)


@unittest.skipIf(not _SECRET_MANAGER_VERSION_RESOURCE_ID,
                 'environment variable SECRET_MANAGER_VERSION_RESOURCE_ID is not set')
class TestGoogleSecretManagerCredentials(unittest.TestCase):
    def test_given_valid_yields_api_settings(self):
        sdk = looker_sdk.init40(config_settings=_CREDENTIALS)
        me = sdk.me()
        self.assertIsNotNone(me)
        self.assertNotEquals('', me.id)


class TestRunInlineQueries(unittest.TestCase):
    def test_run_inline_query(self):
        pipeline = beam.Pipeline()

        queries = pipeline | beam.Create([_QUERY])

        result = queries | RunInlineQueries(result_format=ResultFormat.JSON, credentials=_CREDENTIALS)
        pipeline_result = pipeline.run()
        pipeline_result.wait_until_finish()
        self.assertIsNotNone(result)


class TestRunQueries(unittest.TestCase):
    def test_run_query(self):
        sdk = looker_sdk.init40(config_settings=_CREDENTIALS)
        query = sdk.create_query(body=_QUERY)

        pipeline = beam.Pipeline()
        query_ids = pipeline | beam.Create([query.id])

        result = query_ids | RunQueries(result_format=ResultFormat.JSON, credentials=_CREDENTIALS)
        pipeline_result = pipeline.run()
        pipeline_result.wait_until_finish()
        self.assertIsNotNone(result)


class TestRunLooks(unittest.TestCase):
    def test_run_looks(self):
        sdk = looker_sdk.init40(config_settings=_CREDENTIALS)
        me = sdk.me()
        query = sdk.create_query(body=_QUERY)
        look = sdk.create_look(body=models.WriteLookWithQuery(
            title='TestRunLooks::test_run_looks-' + str(time.time()),
            description='Creating while testing TestRunLooks::test_run_looks',
            query_id=query.id,
            folder_id=me.personal_folder_id,
        ))
        self.assertIsNotNone(look)

        pipeline = beam.Pipeline()
        look_ids = pipeline | beam.Create([look.id])
        result = look_ids | RunLooks(result_format=ResultFormat.PNG, credentials=_CREDENTIALS)

        pipeline_result = pipeline.run()
        pipeline_result.wait_until_finish()
        self.assertIsNotNone(result)


if __name__ == '__main__':
    unittest.main()
