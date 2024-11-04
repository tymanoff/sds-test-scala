# Базовый образ с OpenJDK для сборки
FROM openjdk:11-slim AS builder

# Устанавливаем SBT
RUN apt-get update && \
    apt-get install -y curl gnupg && \
    echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list && \
    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x99E82A75642AC823" | apt-key add && \
    apt-get update && \
    apt-get install -y sbt

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы проекта
COPY . .

# Собираем проект
RUN sbt clean compile stage

# Финальный образ с OpenJDK
FROM openjdk:11-jre-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранные файлы из стадии сборки
COPY --from=builder /app/target/universal/stage/ /app

# Открываем порт приложения
EXPOSE 9000

# Запускаем приложение
CMD ["bin/sds-task-scala", "-Dplay.http.secret.key=your_secret_key", "-Dconfig.file=conf/application.conf"]
