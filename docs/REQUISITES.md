Project Pair
PW manager
holds items

item types:
    web account
    personal information
    payment card

every item type can have extra key value pairs
each pair can have a flag that says whether value is sensitive or not, sensitive means encrypted
---

API
base path `/item/v1/item
Create ~~POST~~PUT  /item/v1/item
Update POST         /item/v1/item/{guid} weird choice, i dont understand why not put. include in comments
Delete DELETE       /item/v1/item/{guid}
Search POST         /item/v1/item/search (filters go in the request body: the search string, the offset, the page size and the sort order) **no secrets in the search results.**
Detail GET          /item/v1/item/{guid}

Search returns a paginated list. 
For each row the list returns the VaultItem object which contains:
    the item title
    the associated **url** when there is one
    the **identifier** and the **type**
Filters: a search string, offset, page size, sort order
size: ~~25~~20 max, reject anything above 20, else validation error 

response: HTTP 200 ALWAYS. real output travels in status.code

GenericResponse {
 status: {
    code: integer // business/outcome code/error code
    message: string // human readable message/predefined message/underlying error description
    traceId: string // correlation id for logs/always populated
 }
}

GenericPagedResponse extends GenericResponse {
 total: integer // total number of matching rows
 ... payload ...
}

200 Success
400 Validation error
~~404~~418 Item not found - when item does not exist on detail, update or delete
500 Generic error
---

Sensitive data encrypted:
    Passwords
    card numbers
    CVV
    any other flagged extra value

Use **authenticated encryption — AES-GCM**

---

Validation for create/update
1. title required (non blank)
2. type required (one of three known types)
    web account
        1. username + password required
    personal information
    payment card
        1. cardHolder required
        2. cardNumber required + valid card number (Luhn Checksum) + 13-19 digits
        3. CVV 3-4 digits
        4. expiry MM/YY format + must not be in the past

3. extra list is optional, either null or non empty, every entry must have non empty key, validation error otherwise
4. search offset >= 0
5. sort order, one of the allowed options (ASC DESC?)


---

Delivery
1. Javadoc on relevant classes and method
2. swagger /OpenAPI
3. at least 1 success test case
4. at least 1 validation error test case
5. must boot on h2
6. must load up with ~30 items across different types


---
AI Help
1. helped explain the record and use cases
2. helped understand traces, and I discovered the TraceProvider as it was an unfamiliar concept for me when logging errors.
3. provided an implementation of CommandLineRunner to use as startup seeder, populating the db with initial data