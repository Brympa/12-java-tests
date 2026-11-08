# 12. Тестирование

Задания по модульному тестированию в Java (Unit Testing, JUnit 5, AssertJ, белое тестирование, покрытие кода).

## Список задач

* [Задание 01 Тестирование Integer.decode (Задача 12.1)](task01/README.md)

## Сборка и запуск тестов

Требуется JDK 21 и Maven 3.8+.

```bash
mvn test              # прогнать тесты всех заданий
mvn -pl task01 test   # прогнать тесты одного задания
```

## Структура модуля

```
task01/
├── pom.xml
├── README.md                                            условие задания и анализ ветвлений
└── src
    ├── main/java/com/example/task01/Task01Main.java      демонстрационный класс
    └── test/java/com/example/task01/Task01MainTest.java  набор тестов с 100% покрытием всех строк и ветвлений
```
