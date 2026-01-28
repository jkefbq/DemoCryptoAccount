# AssetTracker (Telegram-bot)
Телеграм бот **демо-счет** криптовалютной биржи. CRUD операции над активами, просмотр статистики по активам, торговля с нейросетью и возможность писать в поддержку с дальнейшей обработкой этих вопросов администратором через защищенный Spring Security сайт.
<details>
<summary>подробнее</summary>
подробная инфа
</details>

## ⚙Технологии
- Spring Boot (Security, Web, Data, Cache)
- PostgreSQL
- Redis
- MapStruct
- Lombok
- TelegramBots
### тесты
- JUnit
- Mockito
- TestContainers
- Liquibase

## Архитектура


## Панель администратора
![Image alt](https://github.com/jkefbq/AssetTrackerTelegramBot/raw/main/src/main/resources/static/admin-support-panel.png)
>Панель для обработки пользовательских вопросов или жалоб, ответ сразу отправляется нужному пользователю в чат. Реализован поиск всех вопросов какого-то юзера по его ID или по содержанию.

✅ Сайт защищен через Spring Security не только на уровне URL, но и на уровне **методов**.  
✅ Реализована защита от **CSRF**  

##Бот
