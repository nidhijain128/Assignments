## datasetNationalGallery.json

### PyTransforms

### Semantic Types
| Column | Property | Class |
|  ----- | -------- | ----- |
| _Acquisition Credit_ | `rdfs:label` | `schema:Person2`|
| _Acquisition Received_ | `dcterms:dateAccepted` | `schema:Person2`|
| _Artist_ | `rdfs:label` | `schema:Person1`|
| _Artist dates_ | `dcterms:date` | `schema:Person1`|
| _Date made_ | `schema:dateCreated` | `schema:Painting1`|
| _Full title_ | `rdfs:label` | `schema:Painting1`|
| _Group_ | `rdfs:label` | `schema:Organization1`|
| _Image Name_ | `rdfs:label` | `schema:URL1`|
| _Image URL_ | `uri` | `schema:URL1`|
| _Inscription summary_ | `schema:citation` | `schema:Painting1`|
| _Inventory number_ | `schema:serialNumber` | `schema:Painting1`|
| _Location in Gallery_ | `rdfs:label` | `schema:CreativeWork1`|
| _Medium and support_ | `dcterms:description` | `schema:Painting1`|
| _Painting Height_ | `schema:height` | `schema:Painting1`|
| _Painting Width_ | `schema:width` | `schema:Painting1`|


### Links
| From | Property | To |
|  --- | -------- | ---|
| `schema:Painting1` | `schema:accountablePerson` | `schema:Person2`|
| `schema:Painting1` | `schema:contentLocation` | `schema:CreativeWork1`|
| `schema:Painting1` | `schema:creator` | `schema:Person1`|
| `schema:Painting1` | `schema:primaryImageOfPage` | `schema:URL1`|
| `schema:Painting1` | `schema:provider` | `schema:Organization1`|
