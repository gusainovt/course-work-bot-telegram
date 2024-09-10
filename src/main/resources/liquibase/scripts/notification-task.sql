-- liquibase formatted sql

-- changeset tgusainov:1
CREATE TABLE notification_task (
        id SERIAL8,
        chat_id SERIAL8,
        text_notification TEXT,
        data_time TIMESTAMP
)

