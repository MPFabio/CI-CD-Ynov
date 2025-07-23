[![Spring Test](https://github.com/LaPauseClope/pause-clope-server/actions/workflows/maven.yml/badge.svg)](https://github.com/LaPauseClope/pause-clope-server/actions/workflows/maven.yml)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/MPFabio/CI-CD-Ynov)

## Présentation
Ceci est un projet API Java Spring Boot avec un frontend HTML simple et une base de données PostgreSQL. Il s’inscrit dans une démarche d’industrialisation DevOps avec un pipeline CI/CD complet sous Azure DevOps. L’infrastructure est déployée sur Azure à l’aide de Terraform et configurée avec Ansible. Le frontend HTML sert également à tester un scénario de release/rollback.

---

## Objectifs

- Conteneurisation de l’application backend (Spring Boot)
- Déploiement avec Docker Compose
- Provisionnement automatique d’une VM Azure avec Terraform
- Configuration automatique via Ansible
- Mise en place d’un pipeline Azure DevOps CI/CD avec :
  - Tests automatiques Maven
  - Plan et Apply Terraform
  - Déploiement avec Ansible
- Simulation d’un rollback avec version frontend différente

---

## Stack technique

| Composant     | Technologie                       |
|---------------|-----------------------------------|
| Backend       | Java 21, Spring Boot 3.4.4        |
| Base de données | PostgreSQL (via Docker)         |
| Frontend      | HTML statique                     |
| CI/CD         | Azure DevOps                      |
| Provisionnement | Terraform                       |
| Configuration serveur | Ansible                   |
| Conteneurisation | Docker, Docker Compose         |


---

## Lancer l’application en local

### Prérequis
- Docker + Docker Compose

### Commandes

```bash
docker-compose up --build
```

Accès :

    API Spring Boot : http://localhost:8080

    Base PostgreSQL : localhost:5432 (user: postgres / password: password)

## Tests

Le projet inclut des tests unitaires d’API à l’aide de **Spring Boot Test** et **MockMvc**.  
Ils valident notamment le bon fonctionnement du contrôleur `ClickerController` :

- Vérification du `POST /clicker/{nickname}` avec payload JSON
- Vérification du `GET /clicker/{nickname}` si l’utilisateur existe
- Gestion du cas où l’utilisateur est introuvable (`404`)

Ces tests sont exécutés automatiquement dans la pipeline Azure DevOps via :

mvn clean verify

Le pipeline principal azure-pipelines.yml comporte les étapes suivantes :

    Terraform Plan

        Vérifie Java et Maven

        Exécute les tests

        Publie les résultats JUnit

        Initialise Terraform

        Affiche le Plan

    Terraform Apply

        Crée une VM Azure Ubuntu avec réseau privé, IP publique, sécurité SSH

    Configuration avec Ansible

        Transfert dynamique de la clé SSH et IP via artefacts

        Exécution du playbook deploy.yml depuis WSL

        Installe Docker, Docker Compose et lance l’application sur la VM




Les variables sensibles (identifiants Azure, mot de passe VM...) sont gérées via un groupe de variables sécurisé

## Monitoring & Alertes – Azure Monitor

Le projet est surveillé via **Azure Monitor**, avec métriques visibles dans l’interface Insights :

- Usage CPU, mémoire, trafic réseau
- Collecte en temps réel via agent AMA
- Stockage dans un Log Analytics Workspace

Une alerte est configurée dans un Azure Monitor Action Group, notifiant l'équipe en cas de dépassement de seuil critique.

## Rollback

Le fichier `frontend/index.html` contient un message différencié par version.  
Cela permet de **visualiser le rollback** après un déploiement.

Exemple :

- Version 1 : “Ceci est une page de test pour la release et le rollback”
- Version 2 : “Version modifiée pour simuler un rollback”
- Rollback → Version 1 réaffichée

Le retour arrière est réalisé manuellement via :

- `git checkout` d’un ancien tag
- Rebuild Maven (`./mvnw package`)
- Redeploiement avec `deploy.yml`


## Sécurité

    Connexion à la VM via clé SSH RSA (générée par Terraform)

    Variables sensibles stockées de manière sécurisée dans Azure DevOps

    L’utilisateur adminuser est utilisé sans accès root direct


<img width="821" height="246" alt="1 PipelineBack" src="https://github.com/user-attachments/assets/75dba2ba-c4d2-4859-bc66-b5033947fa29" />
<img width="821" height="691" alt="untitled" src="https://github.com/user-attachments/assets/599a6c44-5277-4f62-b96d-4105711a5c88" />
<img width="821" height="417" alt="12 Monitor" src="https://github.com/user-attachments/assets/77c540ed-3b84-4e6b-8803-eb93ac5438b2" />











