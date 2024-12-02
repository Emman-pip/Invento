# TODO

- Analytics page
1. Sort by date range
2. Generate pie chart for item storage reference
3. generate an area plot
4. generate central tendencies and measures variance statistics

- Alerts
1. when lower than threshhold, raise an alert!

- Report generation
1. generate reports with charts, records (in pdf format)

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
