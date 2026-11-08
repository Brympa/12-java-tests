package com.example.task01;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class Task01MainTest {

    @Test
    @DisplayName("null argument throws NullPointerException")
    public void testNullString() {
        Assertions.assertThatThrownBy(() -> Integer.decode(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Empty string throws NumberFormatException with message 'Zero length string'")
    public void testEmptyString() {
        Assertions.assertThatThrownBy(() -> Integer.decode(""))
                .isInstanceOf(NumberFormatException.class)
                .hasMessage("Zero length string");
    }

    @Test
    @DisplayName("Positive decimal numbers without sign")
    public void testPositiveDecimal() {
        Assertions.assertThat(Integer.decode("12345")).isEqualTo(12345);
        Assertions.assertThat(Integer.decode("1")).isEqualTo(1);
    }

    @Test
    @DisplayName("Negative decimal numbers with leading '-'")
    public void testNegativeDecimal() {
        Assertions.assertThat(Integer.decode("-12345")).isEqualTo(-12345);
        Assertions.assertThat(Integer.decode("-1")).isEqualTo(-1);
    }

    @Test
    @DisplayName("Positive decimal numbers with leading '+'")
    public void testExplicitPositiveDecimal() {
        Assertions.assertThat(Integer.decode("+12345")).isEqualTo(12345);
        Assertions.assertThat(Integer.decode("+1")).isEqualTo(1);
    }

    @Test
    @DisplayName("Hexadecimal numbers with '0x' prefix")
    public void testHexPrefixLower() {
        Assertions.assertThat(Integer.decode("0x1a")).isEqualTo(26);
        Assertions.assertThat(Integer.decode("0x7f")).isEqualTo(127);
        Assertions.assertThat(Integer.decode("0xFF")).isEqualTo(255);
    }

    @Test
    @DisplayName("Hexadecimal numbers with '0X' prefix")
    public void testHexPrefixUpper() {
        Assertions.assertThat(Integer.decode("0X1A")).isEqualTo(26);
        Assertions.assertThat(Integer.decode("0X7F")).isEqualTo(127);
        Assertions.assertThat(Integer.decode("0Xff")).isEqualTo(255);
    }

    @Test
    @DisplayName("Hexadecimal numbers with '#' prefix")
    public void testHexPrefixHash() {
        Assertions.assertThat(Integer.decode("#1a")).isEqualTo(26);
        Assertions.assertThat(Integer.decode("#7F")).isEqualTo(127);
        Assertions.assertThat(Integer.decode("#FF")).isEqualTo(255);
    }

    @Test
    @DisplayName("Negative hexadecimal numbers with '0x', '0X' and '#' prefixes")
    public void testNegativeHex() {
        Assertions.assertThat(Integer.decode("-0x10")).isEqualTo(-16);
        Assertions.assertThat(Integer.decode("-0X10")).isEqualTo(-16);
        Assertions.assertThat(Integer.decode("-#10")).isEqualTo(-16);
    }

    @Test
    @DisplayName("Positive hexadecimal numbers with explicit '+' and '0x', '0X', '#' prefixes")
    public void testPositiveHex() {
        Assertions.assertThat(Integer.decode("+0x10")).isEqualTo(16);
        Assertions.assertThat(Integer.decode("+0X10")).isEqualTo(16);
        Assertions.assertThat(Integer.decode("+#10")).isEqualTo(16);
    }

    @Test
    @DisplayName("Octal numbers with '0' prefix and length > 1")
    public void testOctal() {
        Assertions.assertThat(Integer.decode("017")).isEqualTo(15);
        Assertions.assertThat(Integer.decode("077")).isEqualTo(63);
        Assertions.assertThat(Integer.decode("-017")).isEqualTo(-15);
        Assertions.assertThat(Integer.decode("+017")).isEqualTo(15);
    }

    @Test
    @DisplayName("Zero representations: '0', '+0', '-0'")
    public void testZero() {
        Assertions.assertThat(Integer.decode("0")).isEqualTo(0);
        Assertions.assertThat(Integer.decode("+0")).isEqualTo(0);
        Assertions.assertThat(Integer.decode("-0")).isEqualTo(0);
        Assertions.assertThat(Integer.decode("0x0")).isEqualTo(0);
        Assertions.assertThat(Integer.decode("#0")).isEqualTo(0);
    }

    @ParameterizedTest
    @ValueSource(strings = {"--10", "++10", "-+10", "+-10"})
    @DisplayName("Sign character in wrong position directly after sign")
    public void testSignCharacterInWrongPositionAfterSign(String input) {
        Assertions.assertThatThrownBy(() -> Integer.decode(input))
                .isInstanceOf(NumberFormatException.class)
                .hasMessage("Sign character in wrong position");
    }

    @ParameterizedTest
    @ValueSource(strings = {"0x-1", "0x+1", "0X-1", "0X+1", "#-1", "#+1", "-#+1", "+0x-1"})
    @DisplayName("Sign character in wrong position after radix specifier")
    public void testSignCharacterInWrongPositionAfterRadix(String input) {
        Assertions.assertThatThrownBy(() -> Integer.decode(input))
                .isInstanceOf(NumberFormatException.class)
                .hasMessage("Sign character in wrong position");
    }

    @Test
    @DisplayName("Integer.MIN_VALUE boundary condition (triggers catch block with negative == true)")
    public void testIntegerMinValue() {
        Assertions.assertThat(Integer.decode("-2147483648")).isEqualTo(Integer.MIN_VALUE);
        Assertions.assertThat(Integer.decode("-0x80000000")).isEqualTo(Integer.MIN_VALUE);
        Assertions.assertThat(Integer.decode("-0X80000000")).isEqualTo(Integer.MIN_VALUE);
        Assertions.assertThat(Integer.decode("-#80000000")).isEqualTo(Integer.MIN_VALUE);
        Assertions.assertThat(Integer.decode("-020000000000")).isEqualTo(Integer.MIN_VALUE);
    }

    @Test
    @DisplayName("Integer.MAX_VALUE boundary condition")
    public void testIntegerMaxValue() {
        Assertions.assertThat(Integer.decode("2147483647")).isEqualTo(Integer.MAX_VALUE);
        Assertions.assertThat(Integer.decode("+2147483647")).isEqualTo(Integer.MAX_VALUE);
        Assertions.assertThat(Integer.decode("0x7fffffff")).isEqualTo(Integer.MAX_VALUE);
        Assertions.assertThat(Integer.decode("0X7FFFFFFF")).isEqualTo(Integer.MAX_VALUE);
        Assertions.assertThat(Integer.decode("#7fffffff")).isEqualTo(Integer.MAX_VALUE);
        Assertions.assertThat(Integer.decode("017777777777")).isEqualTo(Integer.MAX_VALUE);
    }

    @Test
    @DisplayName("Invalid characters in decimal string (triggers catch block with negative == false)")
    public void testInvalidCharactersInDecimal() {
        Assertions.assertThatThrownBy(() -> Integer.decode("abc"))
                .isInstanceOf(NumberFormatException.class);
        Assertions.assertThatThrownBy(() -> Integer.decode("12a34"))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("Invalid characters in hexadecimal string")
    public void testInvalidCharactersInHex() {
        Assertions.assertThatThrownBy(() -> Integer.decode("0xXYZ"))
                .isInstanceOf(NumberFormatException.class);
        Assertions.assertThatThrownBy(() -> Integer.decode("#GHI"))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("Invalid characters in octal string (digits 8 and 9 are illegal in octal)")
    public void testInvalidCharactersInOctal() {
        Assertions.assertThatThrownBy(() -> Integer.decode("089"))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("Positive overflow beyond Integer.MAX_VALUE")
    public void testOverflowPositive() {
        Assertions.assertThatThrownBy(() -> Integer.decode("2147483648"))
                .isInstanceOf(NumberFormatException.class);
        Assertions.assertThatThrownBy(() -> Integer.decode("0x80000000"))
                .isInstanceOf(NumberFormatException.class);
        Assertions.assertThatThrownBy(() -> Integer.decode("99999999999999999999"))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("Negative underflow beyond Integer.MIN_VALUE")
    public void testUnderflowNegative() {
        Assertions.assertThatThrownBy(() -> Integer.decode("-2147483649"))
                .isInstanceOf(NumberFormatException.class);
        Assertions.assertThatThrownBy(() -> Integer.decode("-0x80000001"))
                .isInstanceOf(NumberFormatException.class);
        Assertions.assertThatThrownBy(() -> Integer.decode("-99999999999999999999"))
                .isInstanceOf(NumberFormatException.class);
    }

    @Test
    @DisplayName("Main method execution smoke test")
    public void testMain() {
        Task01Main.main(new String[0]);
    }
}
