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

## Запуск

Все указанные выше docker-compose компоненты объединены в `docker-compose.yaml`
и поднять их все можно одной командой:
```shell
docker compose up -d
```
Не забудем про последний шаг настройки логирования:
```shell
cd kibana
cd init
./init.sh
```
И всё - система готова к работе!

## Тестирование
 * [http://localhost:8080/api/orders](http://localhost:8080/api/orders) - покидать запросы на шлюз через Postman, 
бразуер или как [тут](testing/gateway.http), через особый файл;
 * http://localhost:8081/ - посмотреть работу MongoDB (логин: admin, пароль: pass);
 * http://localhost:15672/#/ - посмотреть работу RabbitMQ (логин: guest, пароль: guest);
 * http://localhost:8080/actuator/metrics - посмотреть, какие метрики отдаёт шлюз;
 * http://localhost:8080/actuator/prometheus - посмотреть сами метрики;
 * http://localhost:9090/query - поделать запросы к Prometheus;
 * http://localhost:3000/dashboards - посмотреть дашборды в Grafana (логин: admin, пароль: admin);
 * http://localhost:5601/app/discover - - посмотреть логи в Kibana;
