package polish;

public class Main {


	public static void main(String[] args) {
		String parserExpression = Calculate.expressionParser("sin(-x)*12+17+(-12+x)");
		System.out.println(parserExpression);
		System.out.println(Calculate.calculate(5, parserExpression));
	}
}
