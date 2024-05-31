-- liquibase formatted sql

-- changeset tgusainov:1
CREATE TABLE notification_task (
        id SERIAL,
        chat_id SERIAL,
        text_notification TEXT,
        data_time TIMESTAMP
)

