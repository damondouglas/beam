import os
import unittest
from typing import cast

from apache_beam.io.gcp.looker import GoogleSecretManagerCredentials

try:
    import google.cloud.secretmanager_v1 as secretmanager
    import looker_sdk
    from looker_sdk import api_settings
except ImportError:
    unittest.SkipTest('google.cloud.secretmanager_v1 or looker_sdk not found')

_SECRET_MANAGER_VERSION_RESOURCE_ID = os.getenv('SECRET_MANAGER_VERSION_RESOURCE_ID')
_CREDENTIALS = GoogleSecretManagerCredentials(resource_id=_SECRET_MANAGER_VERSION_RESOURCE_ID)


@unittest.skipIf(not _SECRET_MANAGER_VERSION_RESOURCE_ID,
                 'environment variable SECRET_MANAGER_VERSION_RESOURCE_ID is not set')
class TestGoogleSecretManagerCredentials(unittest.TestCase):
    def test_given_valid_yields_api_settings(self):
        sdk = looker_sdk.init40(config_settings=_CREDENTIALS)
        me = sdk.me()
        self.assertIsNotNone(me)
        self.assertNotEquals('', me.id)


if __name__ == '__main__':
    unittest.main()
