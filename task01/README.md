# Задание 01 Тестирование Integer.decode (Задача 12.1)

## Условие

Протестируйте статический метод `Integer.decode(String nm)` из стандартной библиотеки Java.
1. Изучите документацию к методу и код метода.
2. Найдите все возможные ветвления в коде метода и какие аргументы приводят к заходу в эти ветвления.
3. Напишите тесты (не менее 8) так, чтобы покрыть ими все строки кода метода. То есть чтобы при выполнении тестов каждая строка метода была выполнена хотя бы 1 раз.

---

## 1. Документация и исходный код метода `Integer.decode`

### Назначение метода:
Метод декодирует строковое представление числа в объект `Integer`. Поддерживает десятичные, шестнадцатеричные (префиксы `0x`, `0X`, `#`) и восьмеричные (префикс `0`) числа, а также необязательный знак (`+` или `-`) в начале строки перед префиксом основания.

### Исходный код метода (из OpenJDK):

```java
public static Integer decode(String nm) throws NumberFormatException {
    int radix = 10;
    int index = 0;
    boolean negative = false;
    int result;

    if (nm.isEmpty())
        throw new NumberFormatException("Zero length string");
    char firstChar = nm.charAt(0);
    // Handle sign, if present
    if (firstChar == '-') {
        negative = true;
        index++;
    } else if (firstChar == '+')
        index++;

    // Handle radix specifier, if present
    if (nm.startsWith("0x", index) || nm.startsWith("0X", index)) {
        index += 2;
        radix = 16;
    }
    else if (nm.startsWith("#", index)) {
        index ++;
        radix = 16;
    }
    else if (nm.startsWith("0", index) && nm.length() > 1 + index) {
        index ++;
        radix = 8;
    }

    if (nm.startsWith("-", index) || nm.startsWith("+", index))
        throw new NumberFormatException("Sign character in wrong position");

    try {
        result = parseInt(nm, index, nm.length(), radix);
        result = negative ? -result : result;
    } catch (NumberFormatException e) {
        // If number is Integer.MIN_VALUE, we'll end up here. The next line
        // handles this case, and causes any genuine format error to be
        // rethrown.
        String constant = negative ? ("-" + nm.substring(index))
                                   : nm.substring(index);
        result = parseInt(constant, radix);
    }
    return result;
}
```

---

## 2. Анализ всех ветвлений и тестовые аргументы

| № | Условие / Ветвление в коде | Описание ситуации | Пример аргумента | Ожидаемый результат |
|---|-----------------------------|-------------------|-------------------|---------------------|
| 1 | `nm == null` | Передан `null` (вызов `nm.isEmpty()` падает) | `null` | `NullPointerException` |
| 2 | `if (nm.isEmpty())` | Пустая строка | `""` | `NumberFormatException: Zero length string` |
| 3 | `if (firstChar == '-')` | Отрицательное число со знаком `-` | `"-123"` | `result = -123` |
| 4 | `else if (firstChar == '+')` | Положительное число с явным знаком `+` | `"+123"` | `result = 123` |
| 5 | Ни `'-'`, ни `'+'` (ветка по умолчанию) | Положительное число без знака | `"123"` | `result = 123` |
| 6 | `nm.startsWith("0x", index)` | Шестнадцатеричный префикс `0x` в нижнем регистре | `"0x1a"`, `"-0x10"` | `result = 26`, `result = -16` |
| 7 | `nm.startsWith("0X", index)` | Шестнадцатеричный префикс `0X` в верхнем регистре | `"0X1A"` | `result = 26` |
| 8 | `else if (nm.startsWith("#", index))` | Шестнадцатеричный префикс `#` | `"#1a"`, `"-#10"` | `result = 26`, `result = -16` |
| 9 | `else if (nm.startsWith("0", index) && nm.length() > 1 + index)` | Восьмеричный префикс `0` (длина строки больше 1) | `"017"` | `result = 15` |
| 10 | `nm.startsWith("0", index)` ложно для длины > 1 | Ноль `"0"` (длина равна 1, остаётся radix 10) | `"0"` | `result = 0` |
| 11 | `if (nm.startsWith("-", index) \|\| nm.startsWith("+", index))` | Знак в неверной позиции (после знака или префикса) | `"--10"`, `"+-10"`, `"0x-5"`, `"#+5"` | `NumberFormatException: Sign character in wrong position` |
| 12 | `try { result = parseInt(...) }` успешный парсинг | Корректное число любого поддерживаемого формата | `"42"`, `"-42"`, `"0x2A"`, `"052"` | возвращает `42` или `-42` |
| 13 | `result = negative ? -result : result;` (ветка `false`) | Положительное число без знака или с `+` | `"42"` | `result = 42` |
| 14 | `result = negative ? -result : result;` (ветка `true`) | Отрицательное число | `"-42"` | `result = -42` |
| 15 | `catch (NumberFormatException e)` при `negative == true` | Граничное значение `Integer.MIN_VALUE` (`"-2147483648"`), magnitude `2147483648` не влезает в положительный `int`, поэтому `parseInt` падает в try, но парсится с минусом в catch | `"-2147483648"`, `"-0x80000000"`, `"-#80000000"`, `"-020000000000"` | `result = Integer.MIN_VALUE` |
| 16 | `catch (NumberFormatException e)` при `negative == false` | Некорректные символы или переполнение положительного числа | `"abc"`, `"0xZZ"`, `"2147483648"` | `NumberFormatException` |
| 17 | `constant = negative ? ...` в catch (ветка `true`) с переполнением вниз | Число меньше `Integer.MIN_VALUE` | `"-2147483649"` | `NumberFormatException` |

---

## 3. Покрытие строк тестами (100% Statement & Branch Coverage)

В классе `Task01MainTest` реализовано **более 20 модульных тестов**, покрывающих:
1. `testNullString()` — проверка обработки `null`.
2. `testEmptyString()` — строка `""` (выброс исключения `"Zero length string"`).
3. `testPositiveDecimal()` — обычные десятичные числа (`negative = false`, radix 10).
4. `testNegativeDecimal()` — отрицательные десятичные числа (`negative = true`, `firstChar == '-'`).
5. `testExplicitPositiveDecimal()` — знак `+` в начале (`firstChar == '+'`).
6. `testHexPrefixLower()` — префикс `"0x"` (radix 16).
7. `testHexPrefixUpper()` — префикс `"0X"` (radix 16).
8. `testHexPrefixHash()` — префикс `"#"` (radix 16).
9. `testNegativeHex()` — префиксы `"0x"` и `"#"` с отрицательным знаком.
10. `testPositiveHex()` — префиксы `"0x"` и `"#"` со знаком `+`.
11. `testOctal()` — префикс `"0"` при длине > 1 (radix 8).
12. `testZero()` — строка `"0"`, `"-0"`, `"+0"`.
13. `testSignCharacterInWrongPositionAfterSign()` — знаки `"--"`, `"+-"`, `"-+"`, `"++"`.
14. `testSignCharacterInWrongPositionAfterRadix()` — знаки `"0x-"`, `"0x+"`, `"#+"`, `"-#+"`.
15. `testIntegerMinValue()` — вход в блок `catch` с `negative == true` и успешный парсинг `Integer.MIN_VALUE` в десятичном, шестнадцатеричном и восьмеричном форматах.
16. `testIntegerMaxValue()` — проверка граничного значения `Integer.MAX_VALUE`.
17. `testInvalidCharactersInDecimal()` — вход в блок `catch` с `negative == false` и повторный выброс исключения.
18. `testInvalidCharactersInHex()` — недопустимые символы в шестнадцатеричной записи.
19. `testInvalidCharactersInOctal()` — недопустимые цифры в восьмеричной записи (8, 9).
20. `testOverflowPositive()` — превышение максимального значения `Integer.MAX_VALUE`.
21. `testUnderflowNegative()` — превышение минимального значения `Integer.MIN_VALUE` в отрицательную сторону.
