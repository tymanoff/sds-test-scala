# Базовый образ с OpenJDK для сборки
FROM openjdk:11-slim AS builder

# Устанавливаем SBT
RUN apt-get update && \
    apt-get install -y curl gnupg && \
    echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" | tee /etc/apt/sources.list.d/sbt.list && \
    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x99E82A75642AC823" | apt-key add - && \
    apt-get update && \
    apt-get install -y sbt

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы конфигурации SBT в контейнер
COPY build.sbt ./
COPY project project

# Загружаем зависимости
RUN sbt update

# Копируем остальные файлы проекта
COPY . .

# Собираем проект
RUN sbt clean compile stage

# Финальный образ с OpenJDK для выполнения
FROM openjdk:11-jre-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранные файлы из стадии сборки
COPY --from=builder /app/target/universal/stage/ /app

# Проверяем содержимое директории
RUN ls -la /app/bin/

# Открываем порт приложения
EXPOSE 9000

# Запускаем приложение
ENTRYPOINT ["bin/app", "-Dconfig.file=conf/application.conf", "-Dplay.http.secret.key=@GjJO0EHhJ9d^6XfkZ`Hla21nYmkxe6uJ=/a^L0i0yHRRej1iXHTrm7D`=fpr><G"]