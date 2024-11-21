# TODO
## tasks to carry
    - [ ] finish UML
        - [ ] add usermodel to the UML


# JSON structure


## Users
```json
{
    "_id": ObjectId(),
    "username": "username",
    "email": "email",
    "organization": "organization",
    "password": "hashed password",
    "ownedInventories": [invtory IDs...]
    "logs":{
        "id": ObjectId(),
        "timestamp": timestamp,
        "loginTime": timestamp,
        "description": "description",
    }
}

```

## Inventories
```json
{
    "_id": ObjectId(),
    "inventoryName": "inventory name",
    "ownerID": ownerId,
    "sharedTo": [usersIds of users...],
    "items": {
        {
            "itemId": objectId(),
            "itemName": "name",
            "units": 0,
            "description": null,
            "pricePerUnit": 0.00,
            "standardProfitPerUnit": 0.1,
            "dateAdded": timestamp,
            "dataUpdated": timestamp,
            "isDeleted": false,
            "history": {
                { 
                    "timestamp": timestamp, "pricePerUnit": 0.00, 
                    "standardProfitPerUnit": 0.00
                }
            }
        }
    }
    "logs": [
        {
            "itemId": xxxx,
            "activity": xxxx,
            "sales": null,
            "timestamp": timestamp,
            "salesPerUnit": null,
        }
    ]
}

```

## Additional restraints
```json
>db.Users.createIndex({ "username": 1 }, {unique: true})
>db.Inventories.createIndex({ "items.itemName": 1 }, {unique: true})
```
