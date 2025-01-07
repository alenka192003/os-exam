# Микросервисная Архитектура для Управления Заказами

Этот проект реализует микросервисную архитектуру для управления заказами с использованием различных технологий и инструментов для обеспечения производительности, мониторинга и логирования. Система включает несколько сервисов, которые взаимодействуют через REST API, RabbitMQ для асинхронных сообщений и Redis для кэширования. Для мониторинга используются Prometheus и Grafana, а для логирования — стек ELK (ElasticSearch, Logstash, Kibana).

## Стек технологий

- **Java** и **Spring Boot** для разработки сервисов.
- **gRPC** для взаимодействия между сервисами.
- **RabbitMQ** для асинхронного обмена сообщениями.
- **Redis** для кэширования данных.
- **MongoDB** для хранения данных о заказах.
- **Prometheus** и **Grafana** для мониторинга метрик.
- **ElasticSearch**, **Logstash**, **Kibana** (ELK) для централизованного логирования.
- **Docker** для контейнеризации сервисов и удобства их развертывания.

## Описание архитектуры

Проект состоит из нескольких микросервисов, каждый из которых отвечает за выполнение отдельных задач в системе:

- **Gateway** — маршрутизирует HTTP-запросы от клиента к внутренним сервисам.
- **Domain** — сервис для выполнения CRUD операций с заказами, работает с базой данных MongoDB.
- **MongoDB** — база данных для хранения заказов.
- **Redis** — кэширование данных для ускорения доступа к часто запрашиваемой информации.
- **RabbitMQ** — используется для обработки событий создания, обновления и удаления заказов асинхронно.
- **Prometheus** и **Grafana** — используются для мониторинга и визуализации метрик.
- **ELK Stack (ElasticSearch, Logstash, Kibana)** — для централизованного логирования.

## Структура репозитория

```plaintext
├── gateway                # API Gateway для обработки HTTP-запросов
├── domain                 # Сервис для работы с заказами
├── docker-compose.yml     # Файл для запуска всех сервисов через Docker Compose
├── monitoring             # Конфигурации для Prometheus и Grafana
└── logging                # Конфигурации для ELK
