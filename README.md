[![Spring Test](https://github.com/LaPauseClope/pause-clope-server/actions/workflows/maven.yml/badge.svg)](https://github.com/LaPauseClope/pause-clope-server/actions/workflows/maven.yml)
[![Latest Release](https://img.shields.io/github/v/release/LaPauseClope/pause-clope-server)](https://github.com/LaPauseClope/pause-clope-server/releases)

## Présentation

Pause Clope est un projet API Java Spring Boot avec un frontend HTML simple et une base de données PostgreSQL. Il s’inscrit dans une démarche d’industrialisation DevOps avec un pipeline CI/CD complet sous Azure DevOps. L’infrastructure est déployée sur Azure à l’aide de Terraform et configurée avec Ansible. Le frontend HTML sert également à tester un scénario de release/rollback.

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

Accès :

    API Spring Boot : http://localhost:8080

    Base PostgreSQL : localhost:5432 (user: postgres / password: password)

Tests

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

Rollback

Le projet propose un mécanisme de rollback **manuel et simulé** :

- Le fichier `frontend/index.html` contient un message différencié selon la version, permettant de visualiser un changement après déploiement.
- Un retour arrière peut être effectué en :
  - Faisant un `git checkout` vers un commit ou un tag antérieur
  - Rebuildant le projet (`./mvnw package`)
  - Re-déployant la version précédente via le playbook Ansible (`deploy.yml`)

Ce mécanisme illustre un cas typique de rollback basé sur versioning Git + déploiement contrôlé.


Sécurité

    Connexion à la VM via clé SSH RSA (générée par Terraform)

    Variables sensibles stockées de manière sécurisée dans Azure DevOps

    L’utilisateur adminuser est utilisé sans accès root direct

```

<img width="407" height="123" alt="1 PipelineBack" src="https://github.com/user-attachments/assets/75dba2ba-c4d2-4859-bc66-b5033947fa29" />
