---------------------------------
--Query 1
---------------------------------


      
SELECT a.country, a.name, a.age, b.AverageDrive
FROM pga.golfers a
INNER JOIN (
    SELECT country, MAX(avgdrive) as AverageDrive
    FROM pga.golfers
    GROUP BY country
) b 
ON a.country = b.country AND a.avgdrive = b.AverageDrive
ORDER BY AverageDrive desc;      
  
      
      
---------------------------------
--Query 2
---------------------------------

SELECT *
FROM
   (SELECT a.country, a.name, b.AvgEarnings, a.earnings, 
       (a.earnings - b.AvgEarnings) as OverAvg
   FROM   pga.golfers a
   INNER JOIN    
        (SELECT country,  AVG(earnings) as AvgEarnings
         FROM pga.golfers 
         GROUP BY country) b
   ON a.country = b.country) 
WHERE OverAvg > 0   
ORDER BY OverAvg desc;


---------------------------------
--Query 3
---------------------------------

SELECT  agentid, lname, fname, COUNT(NULLIF(Price, 0)) AS Listings, SUM(Value) 
AS TotalValue
FROM
  (SELECT mls.agents.agentid, fname, lname, NVL(listingnum, null) as Price,
       NVL(price, 0) as Value
   FROM mls.agents
   LEFT JOIN (SELECT mls.homes.agentid, price, listingnum from mls.homes) a
   ON mls.agents.agentid = a.agentid)
GROUP BY agentid, fname, lname
ORDER BY listings desc;



---------------------------------
--Query 4
---------------------------------

SELECT agentid, lname, fname, listings, TotalValue, Percentage
FROM
  (SELECT agentid, lname, fname, listings, TotalValue, Percentage, 
          rank() over(order by TotalValue DESC) AS Rank
   FROM
    (SELECT agentid, lname, fname, Listings, TotalValue,    
      to_char(round(100 * (TotalValue / (select sum(price) from mls.homes)))) 
            || '%' AS Percentage
    FROM
    (SELECT agentid, lname, fname, COUNT(NULLIF(Price, 0)) as Listings,
         SUM(Value) AS TotalValue
     FROM
      (SELECT mls.agents.agentid,fname, lname, NVL(listingnum, null) AS Price, 
           NVL(price, 0) AS Value
       FROM mls.agents
       LEFT JOIN (SELECT mls.homes.agentid, listingnum, price FROM mls.homes) a
       ON mls.agents.agentid = a.agentid)
GROUP BY agentid, fname, lname
ORDER BY listings desc)))
WHERE Rank <= 5;



---------------------------------
--Query 5
---------------------------------

   
CREATE TABLE Transactions 
AS (SELECT * 
    FROM
       (SELECT acctno,tdate,description,credit,charge,
        SUM(NVL(credit,0) - NVL(charge,0)) OVER 
       (partition by acctno ORDER BY tdate ) as RUNNINGBALANCE
   FROM bank.trans           
   ORDER BY acctno,tdate));
---------------------------------
--Query 6
---------------------------------

CREATE TABLE TopAccounts 
AS 
  (SELECT custid, lastname, firstname, balance
     FROM
     (SELECT *
      FROM
      (SELECT bank.customers.CustID, LastName, FirstName, bank.accounts.balance, 
         rank() OVER (ORDER BY bank.accounts.balance desc) AS Rank
       FROM bank.customers
   INNER JOIN bank.accounts on bank.customers.custid = bank.accounts.custid)
   WHERE Rank <= 3));

---------------------------------
--Query 7
---------------------------------

SELECT ticker, close, NVL((close - lag(close,1) OVER(ORDER BY tradedate)), 0) 
     AS change, volume
FROM marketdata
WHERE tradedate >= to_date('01-Jan-15', 'DD-MON-YY')
AND tradedate <= to_date('31-Jan-15', 'DD-MON-YY')
AND ticker = 'DCC' OR ticker='OBO';
---------------------------------
--Query 8
---------------------------------

SELECT mother AS Grandmother, father AS Grandfather
FROM people
WHERE level = 2
START WITH name = 'Sue'
CONNECT BY PRIOR mother = name
UNION 
SELECT mother, father
FROM people 
WHERE level = 2
START WITH name = 'Sue'
CONNECT BY PRIOR father = name;

---------------------------------
--Query 9
---------------------------------

SELECT  name
FROM   people
WHERE level = 4
START WITH mother = 'Marge'
CONNECT BY PRIOR Name = father
ORDER SIBLINGS BY name, mother, father;

---------------------------------
--Query 10
---------------------------------
SELECT name FROM people 
WHERE level = 2 
AND name != 'Frank'
START WITH name in 
   (SELECT mother FROM people
    WHERE name = 'Frank')
connect by prior name = mother;






