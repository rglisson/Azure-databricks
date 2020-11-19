/* 1st */
CREATE DATABASE IF NOT EXISTS sampledatabase;
USE sampledatabase

/* 2nd */
DESCRIBE sampledatabase

/* 3rd */
SELECT location, date, total_cases, total_deaths
FROM covid

/* 4th */
SELECT location, date, total_cases, total_deaths
FROM covid
WHERE total_cases > 10000000 AND total_deaths > 240000
