# ISG Sousse - Inscription Master (Spring Boot)

## Prérequis
- JDK 17+  - Maven (ou utilise l'IDE : IntelliJ / Eclipse / VS Code)
- PostgreSQL + pgAdmin, base `isg_sousse` créée

## Lancer
1. Crée la base `isg_sousse` dans pgAdmin (les tables se créent toutes seules au démarrage).
2. Configure `src/main/resources/application.properties` (ou des variables d'environnement) :
   DB_USER, DB_PASSWORD, MAIL_USER, MAIL_PASSWORD, MAIL_DEST
3. Dans ce dossier : `mvn spring-boot:run`
4. Ouvre http://localhost:8080  et  http://localhost:8080/inscription-master.html

## Structure
- `src/main/resources/static/` : le site (index.html, css/, js/, images/)
- `controller/` : API POST /api/candidatures
- `service/` : logique, fichiers, e-mails
- `model/` + `repository/` : tables PostgreSQL
- `uploads/` : pièces jointes (créé automatiquement, non public)
