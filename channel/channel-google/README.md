# Google Content API for Shopping
See: <https://developers.google.com/shopping-content/v2/quickstart>

## Prerequisites

### ChannelInstance Configuration
| Parameter | Explanation | Default |
| -------- | -------- | -------- |
| dryRun | Run the requests in dry-run mode. (Only validation, no actions on serverside) | false |
| merchantId | The ID of the managing account. |  |
| oAuthEmail | The address you signed up your service account with |  |
| p12File | The P12 certficate store located in the projects resources folder. You get this file while setting up the service account |  |

Example:
```
channel:
  GOOG:
    FooShop:
      dryRun: true
      merchantId: 123456
      oAuthEmail: xxx@yyy.gserviceaccount.com
      p12File: FooShop.p12
```

## Jobs
### TaxonomyImport
Import Googles taxonomy as category tree. This is needed to categorize your articles.
Google supplies the taxonomy in many languages, but the structure and keys are always the same.
So choose your preffered locale (language and country) and schedule the job only once and not per every channel instance.

| Parameter | Explanation | Default |
| -------- | -------- | -------- |
| languageCode | Language of the taxonomy | en |
| countryCode | Country of the taxonomy | US |


Example configuration
```
jobs:
  google-taxonomy-import:
    class: google.job.TaxonomyImport
    schedule:
      minute: 5
      hour: 4
    params:
      languageCode: en
      countryCode: US
```

### AttributeImport
Initially used to import some attribute fields defined in
<https://developers.google.com/shopping-content/v2/reference/v2/products/insert>

Does not need to run more than one time when setting up you project or when attributes change, since they are currently hardcoded.

Example configuration
```
jobs:
  google-attribute-import:
    class: google.job.AttributeImport
    schedule:
      minute: 5
      hour: 4
      day: 1
      month: 8
      year: 2018
```

### ProductExport
Uploads products to your Merchant Center account. Relates to <https://developers.google.com/shopping-content/v2/reference/v2/products/>.

Should be scheduled for every channel instance.

| Parameter | Explanation | Default |
| -------- | -------- | -------- |
| languageCode | Language to retrieve the article data for (ISO 639-1).  |  |
| countryCode | The CLDR territory code for the item. |  |
| channelInstance | The channel instance you want this job to handle. |  |

Example configuration
```
  google-product-export-FooShop:
    class: google.job.ProductExport
    schedule:
      minute: 20
      hour: "*"
    params:
      languageCode: en
      countryCode: US
      channelInstance: FooShop
```

### InventoryExport
Updates price and availability of products in your Merchant Center account. Relates to <https://developers.google.com/shopping-content/v2/reference/v2/inventory/>.

Should be scheduled for every channel instance.

| Parameter | Explanation | Default |
| -------- | -------- | -------- |
| channelInstance | The channel instance you want this job to handle |  |

Example configuration
```
  google-inventory-export-FooShop:
    class: google.job.InventoryExport
    schedule:
      minute: 30
      hour: "*"
    params:
      channelInstance: FooShop
```