# Fulfillment Center - PropVUE LLC Backend Engineer Challenge

## Technologies

- Lombok
- Spring Web 
- Spring Data JPA 
- PostgreSQL Driver
- JUnit, Mockito -> (Unit Testing)

## Цель задания (Goals):

Создать REST API для управления продуктами в различных центрах выполнения (Fulfillment Centers) и поддержания данных о состоянии запасов. (исходные данные)

Дано: таблица содержит следующие данные:

- Product: Идентификатор продукта (например, p1, p2).
- Status: Состояние продукта (например, Sellable, Unfulfillable).
- Fulfillment Center: Центр выполнения, в котором находится продукт (например, fc5, fc3).
- Qty: Количество единиц.
- Value: Стоимость.

1. Основные требования:

   1.1. Создать REST API для выполнения CRUD-операций с продуктами.

   1.2. Должны быть эндпоинты для получения списка продуктов, добавления, обновления и удаления продуктов.

   1.3. Поля продукта:
   - productId (например, p1, p2).
   - status (например, Sellable, Unfulfillable, Inbound).
   - fulfillmentCenter (например, fc5, fc3).
   - quantity — количество единиц.
   - value — стоимость единиц.


2. Логика работы API:

   2.1. Реализовать фильтрацию продуктов по статусу (Sellable, Unfulfillable, Inbound).

   2.2. Реализовать возможность запроса общего значения (value) всех продуктов с состоянием Sellable.


3. Работа с базой данных:

   3.1. Настроить подключение к PostgreSQL и создать таблицу products для хранения информации о продуктах.

   3.2. SQL-запросы должны учитывать обновления данных, такие как изменение количества или статуса продукта.


4. Требования к документации:
   
   4.1. Добавить документацию API (например, с использованием Swagger).


5. Опциональные требования (для дополнительных баллов):
   
   5.1. Создать метод для вычисления общего значения (value) всех продуктов для конкретного Fulfillment Center.

   5.2. Написать unit-тесты для основных операций API.


6. Инструкции по запуску:

   6.1. Приложить README.md с описанием шагов для запуска проекта и выполнения основных операций API.



## API-документация (API Documentation)

Вы также можете использовать коллекции и методы API DocumentaAPI в своем собственном Postman WorkSpace, создав их ответвление.
[![Run in Postman](https://run.pstmn.io/button.svg)](https://god.gw.postman.com/run-collection/5231798-95d3e224-ec16-40d2-898c-e4460e948112?action=collection%2Ffork&source=rip_markdown&collection-url=entityId%3D5231798-95d3e224-ec16-40d2-898c-e4460e948112%26entityType%3Dcollection%26workspaceId%3Dacd71577-82db-475f-be01-2352ece0701a)


## Product API

В этом разделе содержатся определения и примеры использования методов REST API, которые будут использоваться для операций, выполняемых с Продуктом.

This section contains the definitions and usage examples of REST API methods to be used for operations performed on the Product. 


### Validation Rules

Следующие правила проверки применяются ко всем запросам API: / The following validation rules are applied to all API requests:

1. `productId`: Не может быть пустым или нулевым / Cannot be empty or null
2. `status`: Должно быть одно из значений «SELLABLE», «UNFULFILLABLE» или «INBOUND». / Must be one of the values: "SELLABLE", "UNFULFILLABLE" or "INBOUND"
3. `fulfillmentCenter`: Boş veya null olamaz
4. `quantity`: Negatif olamaz
5. `value`: Negatif olamaz

---

![image](https://github.com/user-attachments/assets/cb170003-b0b4-445b-b0cb-57429f4b25d9)

Swagger / OpenAPI : http://localhost:8080/swagger-ui/swagger-ui/index.html

> **CreateProduct**

Создает новый продукт. / Creates a new product.

| Type | Method |
|------| ------ |
| POST | /api/v1/products/create |

Request Body:
```json
{
    "productId": "P121",
    "status": "INBOUND",
    "fulfillmentCenter": "FC4",
    "quantity": 30,
    "value": 721.4
}
```

Response:
```json
{
    "id": 1,
    "productId": "P121",
    "status": "INBOUND",
    "fulfillmentCenter": "FC4",
    "quantity": 30,
    "value": 721.4
}
```

---

> **UpdateProduct**

Обновляет существующий продукт. / Updates an existing product.

| Type | Method |
|------| ------ |
| PATCH | /api/v1/products/update/{id} |

Request Body:
```json
{
    "quantity": 25,
    "value": 800.0
}
```

Response:
```json
{
    "id": 1,
    "productId": "P121",
    "status": "INBOUND",
    "fulfillmentCenter": "FC4",
    "quantity": 25,
    "value": 800.0
}
```

Примечание. В теле запроса вам нужно отправить только те поля, которые вы хотите обновить. / Note: You can send the fields you want to update in the request body.

---

> **DeleteProduct**

Удаляет товар с указанным идентификатором. / Deletes the product with the specified ID. 

| Type | Method |
|------| ------ |
| DELETE | /api/v1/products/delete/{id} |

Response:
```json
"Product with id: 1 deleted successfully"
```
---

> **GetProductById**

Возвращает объект Product, `id` которого указан. / Returns the Product object given its `id`.

| Type | Method |
|------| ------ |
| GET  | /api/v1/products/{id} |

Response:
```json
{
    "id": 1,
    "productId": "P121",
    "status": "SELLABLE",
    "fulfillmentCenter": "FC1",
    "quantity": 10,
    "value": 100.0
}
```

---

> **GetAllProducts**

Перечисляет все продукты. / Lists all products

| Type | Method |
|------| ------ |
| GET  | /api/v1/products/all |

Response:
```json
[
    {
        "id": 1,
        "productId": "P121",
        "status": "INBOUND",
        "fulfillmentCenter": "FC4",
        "quantity": 30,
        "value": 721.4
    },
    {
        "id": 2,
        "productId": "P122",
        "status": "SELLABLE",
        "fulfillmentCenter": "FC1",
        "quantity": 10,
        "value": 100.0
    }
]
```

---

---

> **GetProductsByFilters**

Перечисляет все продукты. / Lists all products

| Type | Method                  |
|------|-------------------------|
| GET  | /api/v1/products/filter |

Parametres

- productId (optional): product id (example: P123)
- status (optional): product status (example: SELLABLE, UNFULFILLABLE, INBOUND)
- fulfillmentCenter (optional): fulfillment center (example: FC1, FC2)
- minQuantity (optional): Minimum amount (example: 10)
- maxQuantity (optional): Maxsimum amount (example: 100)
- minValue (optional): Minimum value (example: 50.0)
- maxValue (optional): Maksimum value (example: 500.0)

Example requests:

1. Only product id: 

> http://localhost:8080/api/v1/products/filter?productId=P123


2. Only Status and quantity

>http://localhost:8080/api/v1/products/filter?status=INBOUND&minQuantity=5&maxQuantity=20


3. All parameters

> http://localhost:8080/api/v1/products/filter?productId=P123&status=SELLABLE&fulfillmentCenter=FC1&minQuantity=10&maxQuantity=100&minValue=50.0&maxValue=500.0


Response:
```json
[
    {
        "id": 1,
        "productId": "P121",
        "status": "INBOUND",
        "fulfillmentCenter": "FC4",
        "quantity": 30,
        "value": 721.4
    },
    {
        "id": 2,
        "productId": "P122",
        "status": "SELLABLE",
        "fulfillmentCenter": "FC1",
        "quantity": 10,
        "value": 100.0
    }
]
```

---

> **GetProductByStatus**

Перечисляет все продукты с указанным статусом. / Lists all products with the specified status.

| Type | Method |
|------| ------ |
| GET  | /api/v1/products/status/{status} |

Status values: SELLABLE, UNFULFILLABLE, INBOUND / Status values: SELLABLE, UNFULFILLABLE, INBOUND

Response:
```json
[
    {
        "id": 1,
        "productId": "P122",
        "status": "SELLABLE",
        "fulfillmentCenter": "FC1",
        "quantity": 10,
        "value": 100.0
    },
    {
        "id": 2,
        "productId": "P123",
        "status": "SELLABLE",
        "fulfillmentCenter": "FC2",
        "quantity": 10,
        "value": 134.0
    }
]
```

---

> **GetTotalValueByStatus**

Возвращает общую стоимость товаров с указанным статусом / Returns the total value of products with the specified status.

| Type | Method |
|------| ------ |
| GET  | /api/v1/products/total-value/status/{status} |

Response:
```json
{
   "value": 1500.0
}

```

## Test Coverage:

![image](https://github.com/user-attachments/assets/037aed59-ab93-45ae-9179-1e8e16821095)






