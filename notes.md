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
    "columns": ["itemName", "units", "description", "capitalPerUnit"]
    "items": {
        {
            "itemId": objectId(),
            "itemName": "name",
            "units": 0,
            "description": null,
            "capiterPer": 0, 
            "dateAdded": timestamp,
            "dataUpdated": timestamp,
            "isDeleted": false,
            "history": {
                { 
                    "timestamp": timestamp, "capitalPerUnit": 0.00, 
                }
            }
        }
    }
    "logs": [
        {
            "itemId": xxxx,
            "activity": xxxx, // either be addColumns, deleteColumns, createInventory, deleteInventory, accessedInventory, 
            "username": username;
            "sales": null,
            "timestamp": timestamp,
            "unitsSold": ewan,
        }
    ]
}

```

## Additional restraints
```json
>db.Users.createIndex({ "username": 1 }, {unique: true})
>db.Inventories.createIndex({ "items.itemName": 1 }, {unique: true})
```
