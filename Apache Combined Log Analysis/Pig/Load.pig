/*
@author James Caldwell
@version December, 2018 

Pig script used to load Apache log data into HBase. The date time is broken down 
into separate fields and so is ip address. geo location is determined by ip address
using external jar files.  Fields are checked for empty values and replaced with 
appropriate values instead. The result is stored into HBase so that it is indexed into Solr. 

*/
-- Jar files containing the UDFs to determine location by ip
-- Newest version of akela 0.6 was corrupt and has to find the previous version
REGISTER 'akela-0.5.jar';
REGISTER 'geoip-1.2.8.jar';

-- Define the UDF that uses a local database file for locations
DEFINE GeoIpLookup com.mozilla.pig.eval.geoip.GeoIpLookup('GeoLiteCity.dat');

-- Load the log file that is stored in HDFS that has been converted to csv format by a Python script
a = LOAD  '/user/jcaldwell2/7days_out.csv' using PigStorage(',')as(id:int,ip:chararray,identd:chararray,userid:chararray,timestamp:chararray,client_request_line:chararray,status_code:chararray,bytes:int,referer:chararray,user_agent:chararray,mu:chararray);

-- The csv contains headers that we do not need to operate on
b = FILTER a by $0>0;

-- Split the ip address using dot as the delimiter and generate columns
c = foreach b generate FLATTEN(STRSPLIT(ip,'\\u002E')) as (ip1:chararray, ip2:chararray,ip3:chararray,ip4:chararray),
                        ToDate(timestamp,'dd/MMM/yyyy:HH:mm:ss Z') as timestamp2,
                        id,ip,identd,userid,timestamp,client_request_line,status_code,bytes,referer,user_agent,mu;

-- c and d could potentially be combined, but separated for testing purposes
-- Generate date time fields from timestamp2
d = FOREACH c GENERATE id,
                      GetDay(timestamp2) as Day,
                      GetMonth(timestamp2) as Month,
                      GetYear(timestamp2) as Year,
                      GetHour(timestamp2) as Hour,
                      GetMinute(timestamp2) as Minute,
                      GetSecond(timestamp2) as Second,
                      ip,                    
                      identd,userid,timestamp2,client_request_line,status_code,bytes,referer,user_agent,mu;                  


-- Generate various locations fields using the GeoIpLookup UDF
geo = FOREACH c GENERATE ip, FLATTEN(GeoIpLookup(ip)) AS (country:chararray, country_code:chararray, region:chararray, city:chararray, postal_code:chararray, metro_code:int);

-- Check fields for missing values and replace with appropriate value to indicate it is missing
geoip = FOREACH geo GENERATE ip, ((country IS NULL) ? 'Uknown':country) as country,((country_code IS NULL) ? 'N/A':country_code) as country_code,((region IS NULL) ? 'N/A':region) as region,((city IS NULL) ? 'N/A':city) as city,((postal_code IS NULL) ? 'N/A':postal_code) as postal_code,((metro_code IS NULL) ? 1337:metro_code) as metro_code;

-- Join the data from d and the geolocation data from geoip 
final =  JOIN d BY ip, geoip BY ip; 

-- Keep only the distinct results
distinct_final = DISTINCT final;

-- Generate the final results and name the columns appropriately
result = FOREACH distinct_final GENERATE id as id, Day as Day, Month as Month, Year as Year, Hour as Hour, Minute as Minute, Second as Second,geoip::ip as ip, geoip::country as country, geoip::country_code as Country_Code, geoip::region as Region, geoip::city as City,geoip::postal_code as Postal_Code, geoip::metro_code as Metro_Code, identd as Identd, userid as Userid, timestamp2 as Time_Stamp, client_request_line as Client_Request_Line, status_code as Status_Code, bytes as Bytes, referer as Referer, user_agent as User_Agent,mu as mu;

-- Store the result into HBase
STORE result INTO 'hbase://ruweb' USING org.apache.pig.backend.hadoop.hbase.HBaseStorage(
'
id:Day,id:Month,id:Year,id:Hour,id:Minute,id:Second,id:ip,id:Country,id:country_code,id:Region,id:city,id:Postal_Code,id:metro_code,id:identd,id:userid,id:Time_Stamp,id:client_request_line,id:status_code,id:bytes,id:referer,id:user_agent, id:mu
');					  					  
		
--dump used to verify the results				  
--dump result;

