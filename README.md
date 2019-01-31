# ktorserver
A REST API web server written in Kotlin using Ktor. This server provides for lookup of device manufacturers based on a MAC address.

## Installation
 

1. Download or clone the project
2. Build and Run
3. Browse to the local IP: 127.0.0.1:8080

See usage details below

## Usage


### Get OUIs for Manufacturer

To get a list of OUIs assigned to a specific manufacturer enter a URL like: 
    
`127.0.0.1:8080/mac/id`

where id is the manufacturer name.
    
Example: 
`127.0.0.1:8080/mac/freebox`
    
This will return all the OUIs allocated to FREEBOX SAS like this:
    
[00:07:CB, 34:27:92, 68:A3:78, 14:0C:76, F4:CA:E5, 00:24:D4, E4:9E:12]
    
Note you don't have to enter the entire manufacturer name, just a beginning matching substring (e.g., 'free', 'freeb', etc.)
    
### Get Manufacturer for an OUI

To get the name of the manufacturer who has been assigned a specific OUI enter a URL like:
    
`127.0.0.1:8080/manuf/id`

where id is the OUI (first 3 bytes of a MAC address)
    
Example: 
`127.0.0.1:8080/manuf/00:07:3d`

This will return the manufacturer assigned the 00:07:3d OUI like this:
    
Nanjing Postel Telecommunications Co., Ltd.
    
### Get Manufacturers List

To see a list of all manufacturers enter the following URL:
    
`127.0.0.1:/manuf`
