package polish;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Stack;
import java.util.StringTokenizer;

public class Calculate {
	private static final String OPERATORS = "+-*/^%";
	private static final String[] FUNCTIONS = { "sin", "cos", "tan", "sqr", "sqrt" };

	private static enum FunctionMath {
		PLUS, MINUS, MULTIPLY, DIVIDE, REMAINS, POW, SQR, SQRT, COS, SIN, TAN;

	}

	// добавляет 0 для отрицательных операндов
	private static String preparingExpresion(String expression) {
		String expresion = new String();
		for (int i = 0; i < expression.length(); i++) {
			char symbol = expression.charAt(i);
			if (symbol == '-') {
				if (i == 0)
					expresion += '0';
				else if (expression.charAt(i - 1) == '(')
					expresion += '0';
			}
			expresion += symbol;
		}

		return expresion;
	}

	// Преобразовать строку в обратную польскую нотацию
	public static String expressionParser(String expression) {
		Stack<String> stack = new Stack<String>();
		StringBuilder sbOut = new StringBuilder("");
		StringTokenizer stringTokenizer = new StringTokenizer(preparingExpresion(expression), OPERATORS + " ()", true);

		String cTmp;

		while (stringTokenizer.hasMoreTokens()) {
			String token = stringTokenizer.nextToken();
			if (isOperator(token)) {

				while (!stack.empty()) {
					if (isOperator(stack.lastElement()) && (priority(token) <= priority(stack.lastElement()))) {
						sbOut.append(" ").append(stack.pop()).append(" ");
					} else {
						sbOut.append(" ");
						break;
					}
				}

				stack.push(token);

			} else if (token.equals("(")) {
				stack.push(token);
			} else if (token.equals(")")) {
				cTmp = stack.lastElement();
				while (!cTmp.equals("(")) {
					if (stack.size() < 1) {
						throw new NumberFormatException("Error parsing");
					}
					sbOut.append(" ").append(stack.pop()).append(" ");

					cTmp = stack.lastElement();

				}

				stack.remove(cTmp);
				if (stack.size() != 0) {
					if (isFunction(stack.lastElement())) {
						sbOut.append(" ").append(stack.lastElement()).append(" ");
						stack.remove(stack.lastElement());
					}
				}
			}

			else if (isFunction(token)) {
				stack.push(token);
			} else {
				// Если символ не оператор - добавляем в выходную последовательность
				sbOut.append(" ").append(token).append(" ");
			}

		}
		while (!stack.empty()) {
			sbOut.append(" ").append(stack.pop());
		}

		return sbOut.toString();
	}

	// Функция проверяет, является ли текущий символ оператором
	private static boolean isOperator(String c) {
		if (c.length() != 1)
			return false;
		char ch = c.charAt(0);

		switch (ch) {
		case '-':
		case '+':
		case '*':
		case '/':
		case '^':
		case '%':
			return true;
		}
		return false;
	}

	// Возвращает приоритет операции
	private static byte priority(String op) {
		if (op.length() != 1)
			return 1;
		char ch = op.charAt(0);
		switch (ch) {
		case '^':
			return 3;
		case '*':
		case '/':
		case '%':
			return 2;
		}
		return 1;
	}

	private static boolean isFunction(String token) {
		for (String item : FUNCTIONS) {
			if (item.equals(token)) {
				return true;
			}
		}
		return false;
	}

	// Считает выражение, записанное в обратной польской нотации
	public static double calculate(double x, String sIn) {
		String sTmp;
		double old = x;
		Deque<Double> stack = new ArrayDeque<Double>();
		StringTokenizer st = new StringTokenizer(sIn);
		while (st.hasMoreTokens()) {

			sTmp = st.nextToken().trim();
			if (sTmp.equals("x"))
				sTmp = String.valueOf(old);

			if ((1 == sTmp.length() && isOperator(sTmp)) || isFunction(sTmp)) {
				if (isFunction(sTmp)) {
					executeFunc(stack, sTmp);
				}

				if (1 == sTmp.length() && isOperator(sTmp)) {
					processOperator(stack, sTmp);
				}
			} else
				stack.push(Double.parseDouble(sTmp));
		}
		return stack.pop();
	}

	private static void processOperator(Deque<Double> stack, String operator) {
		double dB = stack.pop();
		double dA = stack.pop();
		switch (getOperation(operator)) {
		case PLUS:
			stack.push(dA + dB);
			break;
		case MINUS:
			stack.push(dA - dB);
			break;
		case DIVIDE:
			stack.push(dA / dB);
			break;
		case MULTIPLY:
			stack.push(dA * dB);
			break;
		case REMAINS:
			stack.push(dA % dB);
			break;
		case POW:
			stack.push(Math.pow(dA, dB));
			break;
		default:
			break;
		}
	}

	private static void executeFunc(Deque<Double> stack, String func) {
		double dA = stack.pop();
		switch (getOperation(func)) {
		case SIN:
			stack.push(Math.sin(dA));
			break;
		case COS:
			stack.push(Math.cos(dA));
			break;
		case TAN:
			stack.push(Math.tan(dA));
			break;
		case SQR:
			stack.push(Math.pow(dA, 2));
			break;
		case SQRT:
			stack.push(Math.sqrt(dA));
			break;
		default:
			break;
		}
	}

	private static FunctionMath getOperation(String operation) {
		if (operation.equals("+"))
			return FunctionMath.PLUS;
		if (operation.equals("-"))
			return FunctionMath.MINUS;
		if (operation.equals("*"))
			return FunctionMath.MULTIPLY;
		if (operation.equals("/"))
			return FunctionMath.DIVIDE;
		if (operation.equals("%"))
			return FunctionMath.REMAINS;
		if (operation.equals("^"))
			return FunctionMath.POW;
		if (operation.equals("cos"))
			return FunctionMath.COS;
		if (operation.equals("tan"))
			return FunctionMath.TAN;
		if (operation.equals("sin"))
			return FunctionMath.SIN;
		if (operation.equals("sqr"))
			return FunctionMath.SQR;
		if (operation.equals("sqrt"))
			return FunctionMath.SQRT;
		throw new NumberFormatException("Wrong function");
	}

}