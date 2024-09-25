# SpringBoot File Upload, Save, Retreive

### Saving File to DB
To save file in a database the entity needs to have a Blob type, this indicates that the entity is associated with a file/image/video etc. This also has to be communicated to springboot which uses the annotation @Lob and helps to parse it cleanly.

### Saving File to Disk
Saving a File to disk also can be very helpful if at all the file has to be shared. It also reduces the load on the database by removing unnecessary un-queryable data. But has the risk of being deleted on the file system by accident(doesnt happen on servers though). To track the files associated with a particular entity the path of where it is stored needs to be tracked. This is done by having an extra field in the entity for tracking all the files.


### Pre-requisites

0. Go through the springboot_unit1_2_hellopeople
1. Go through the springboot_unit2_1_onetoone example
2. Go through the springboot_unit2_2_onetomany example
3. Maven has to be installed on command line OR your IDE must be configured with maven
4. 
5. Java version 1.8 - 1.10 (Some versions of springboot are really unhappy with java 11)

### To Run the project 
1. Command Line (Make sure that you are in the folder containing pom.xml)</br>
<code> mvn clean package</code></br>
<code>java -jar target/onetoone-1.0.0.jar</code>
2. IDE : Right click on Application.java and run as Java Application

### Available End points from POSTMAN: CRUDL
1. CREATE requests - 
POST request: 
    1. /laptops - Create a new Laptop. The request has to be of type application/JSON input format 
    {
        "cpuClock" : "1.7",
        "cpuCores"  : "4",
        "ram"   : "4",
        "manufacturer" : "apple",
        "cost" : "1000000"
    }
    2. /users - Create user without laptop. Request Format : form-data
    input format : [ key -> user, value -> {
        "name" : "John",
        "email"  : "johndoe@somemail.com"
    }, 
    key -> avatar, value -> upload file ]
    4. /phones - Create a new Phone. cannot create a new user(no cascade option), but you can associate an existing user to the phone 
    {
        "name" : "iPhone X",
        "price" : "2000",
        "cameraQuality": "13",
        "manufacturer: : "apple",
        "battery" : "3000",
        "user" : {
            "id" : 3
        }
    }
2. READ requests -
GET request:
    1. /laptops/{id} - Get laptop object for provided id
    2. /users/{id} - Get user object with associated laptop and phones for provided user id
    3. /phones/{id} - Get phone objects with provided id
    4. /users/{id}/avatar - Get the image uploaded for a user
    5. /laptops/{id}/invoice - Get the invoice uploaded for a laptop

3. UPDATE requests -
PUT request : 
    1. /laptops/{id} - Update info on a laptop whose id is provided on the URL. The modified info is sent in the body of the message
    {
        "cpuClock" : "2.7",
        "cpuCores"  : "4",
        "ram"   : "4",
        "manufacturer" : "apple",
        "cost" : "2000000"
    }
    2. /users/{id} - Update User and optionally laptop and phone associated with him/her for id provided in the url
    {
        "name" : "John",
        "email"  : "johndoe@newmail.com",
        laptop   : {
            "id" : "5"
            "cpuClock" : "3.3",
            "cpuCores"  : "8",
            "ram"   : "16",
            "manufacturer" : "new Laptop",
            "cost" : "1000"
        },
        "phones" : [{"id":1},{"id":2} ]
    }.
    3. /phones/{id} - Update the Phone for given id and optionally modify the user_id associated with phone
     {
        "name" : "iPhone X",
        "price" : "2000",
        "cameraQuality": "13",
        "manufacturer: : "apple",
        "battery" : "3000",
        "user" : {
            "id" : 5
        }
    }
    4. /laptops/{id}/invoice - update the invoice for a given laptop
    input format - form-data
    key -> invoice, value -> upload file

4. DELETE a record - 
 DELETE request:
    1. /laptops/{id} - Delete the entry with id in te url
    2. /users/{id} - delete a user with the id provided in the url
    3. /phones/{id} - delete the phone for the given id

5. LIST all -  
GET request
    1. /laptops - Get/List all the laptops stored in the db
    2. /users - Get/List all the users along with their associated laptops
    3. /phones = Get/List all the phones stores in the database

#

### Note :
### 1. /laptops APIs are defined in Laptops/LaptopController, 
### 2. /users APIs are defined in the Users/UserController
### 3. /phones APIs are defined in the Phones/PhoneController

# 
## Some helpful links:
[SpringBoot Entity](https://www.baeldung.com/jpa-entities)   
[SpringBoot OneToOne](https://www.baeldung.com/jpa-one-to-one)    
[SpringBoot OneToMany](https://www.baeldung.com/hibernate-one-to-many)

