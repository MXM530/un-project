package symbol;

/**
 * 操作数类型枚举
 */
public enum OperandType {
    // 字符（ASC码表中的序号）
    CHAR,
    // 字符集（字符集的id）
    CHARSET,
    // 正则表达式（正则运算式的id,即regularId）
    REGULAR
}
// 对应operandId