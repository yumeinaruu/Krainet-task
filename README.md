Tracker system for company


My `Tracker System` is the most secure and useful application for compamies to track the project work time.

Developed for macOS, Windows, Linux, iOS and Android.

**Usage**
---

```
Usage: By endpoints in ui made by Swagger framework
  Developed by Lisavy Stanislau -> (Github: yumeinaruu)


Options:
  1          Endpoints dedicated to security start with /security
  2          Endpoints dedicated to users start with /user
  3          Endpoints dedicated to subjects start with /project
  4          Endpoints dedicated to speciality start with /record

  Some endpoints are protected with JWT security and you'll not be able to reach them without a token
```

**Installation Options**
---
 On local machine:
  1. Install docker
  2. git clone https://github.com/yumeinaruu/Krainet-task
  3. docker-compose up -d
  4. go to http://localhost:8080/swagger-ui/index.html#/
  5. go to security/token endpoint and get your JWT token
  6. insert token on swagger page in form
  
 On remote server:
 1. create a droplet on DigitalOcean
  2. connect to server by root@ip_adress_of_droplet through concole using wsl (or just concole if using linux)
  3. curl -fsSL https://get.docker.com -o get-docker.sh
  4. sudo sh get-docker.sh
  5. git clone https://github.com/yumeinaruu/Krainet-task
  6. docker-compose up -d
  7. go to http://{YOUR_DROPLET_IP}:8080/swagger-ui/index.html#/
  8. go to security/token endpoint and get your JWT token
  9. insert token on swagger page in form



**What's the project about?**
---

1. What You Need to Know Before Executing
    + Where info for .properties file is?
        - In .env file.
2. What Technologies Were Used?
    + PostgreSQL as Database
    + Spring Framework 
        - Spring Boot
        - Spring MVC
        - Spring Security
        - Spring AOP(Transactional)
    + Slf4g for logging
    + Swagger
    + Docker
3. How To Create A Super User?
    + go into docker container with db (docker exec)
    + psql -U postgres
    + \c db_name
    + And there you can create your first user

**How to Contribute**
---
1. Clone repo and create a new branch: `$ git checkout https://github.com/yumeinaruu/Krainet-task -b name_for_new_branch`.
2. Make changes and test
3. Submit Pull Request with comprehensive description of changes
