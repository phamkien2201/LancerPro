# LancerPro

<p align="center">
  A freelancing platform connecting professionals with job opportunities.
</p>

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Requirements](#requirements)
- [Quick Start](#quick-start)
- [Testing](#testing)
- [API](#api)
- [Acknowledgements](#acknowledgements)

## Introduction

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

LancerPro is a job posting and freelancing platform designed to connect professionals with job opportunities. Businesses can post projects, and freelancers can bid for work. The system supports real-time updates, secure authentication, and seamless communication.

## Features

- **User Authentication & Authorization** (Identity Service)
- **User Profiles & Portfolio Management** (Profile Service)
- **Project Posting & Categorization** (Project Service)
- **Freelancer Bidding System** (Bid Service)
- **Real-time Chat for Collaboration** (Chat Service)
- **Real-time Notifications** (Notification Service)
- **API Gateway for Client Communication**
- **Full-text Search & Filtering with Elasticsearch**

## Requirements

### Local Development
- Java 17
- Maven
- MongoDB
- Docker
- Kafka

### Docker
- [Docker](https://www.docker.com/get-docker)

## Quick Start

### Run Locally
```bash
mvn spring-boot:run
```
Application runs on port `8080` (configurable in `application.yml`).

### Run with Docker
```bash
docker-compose up --build
```

## Testing

To run tests:
```bash
mvn test
```

## API

- **Authentication & Authorization**: Login, Logout, Register.
- **Profile Management**: CRUD operations on user profiles.
- **Project Management**: Create, Update, Delete, Search projects.
- **Bidding System**: Freelancers can place bids on projects.
- **Real-time Chat**: Users can communicate instantly.
- **Notifications**: Users receive updates about projects and bids.

## Acknowledgements

Special thanks to all contributors and the open-source community.

