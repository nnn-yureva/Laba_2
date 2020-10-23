import java.util.Stack;

public class Calculator {
    /**
     * выражение, содержащие числа, операторы(+,-,*,/), скобки, пробелы, мусор
     */
    private String str = "";

    /**
     * удаление пробелов в выражении
     */
    private void delSpace()
    {
        String newstr = "";
        for(int pos = 0; pos < str.length(); pos++)
            if(str.charAt(pos) != ' ')
            {
                newstr += str.charAt(pos);
            }
        str = newstr;
    }

    /**
     * проверка выражения на корректность
     * @return true - если выраж. верно, иначе false
     */
    private boolean check()
    {
        if(str.isEmpty()) return false;
        else{
            delSpace();//удаляем пробелы
            //смотрим за скобками, если встретили например: ( и )) bracket = -1 это ошибка
            int bracket = 0;

            for(int pos = 0; pos < str.length(); pos++)
            {
                if(bracket >= 0)
                {
                   switch (str.charAt(pos))
                   {
                       case '+':
                           case '-':
                               case '*':
                                   case '/':
                                   { if(pos == 0 || pos == str.length() - 1) return false;
                                       else if(str.charAt(pos+1) == '+' || str.charAt(pos+1) == '-' || str.charAt(pos+1) == '*' || str.charAt(pos+1) == '/' || str.charAt(pos+1) == ')')
                                           return false;
                                       break;
                                   }

                                       case '(':
                                       {
                                           bracket++;
                                           if(str.charAt(pos+1) == '+' || str.charAt(pos+1) == '-' || str.charAt(pos+1) == '*' || str.charAt(pos+1) == '/' ||  str.charAt(pos+1) == ')')
                                               return false;
                                           else if(pos == str.length() - 1) return false;
                                           break;
                                       }


                                           case ')':
                                           {
                                               bracket--;
                                               if(pos == 0) return false;
                                               else  if(str.charAt(pos-1) == '+' || str.charAt(pos-1) == '-' || str.charAt(pos-1) == '*' || str.charAt(pos-1) == '/' || str.charAt(pos-1) == '(' )
                                                   return false;
                                               break;
                                           }

                       default:
                           if(str.charAt(pos) >= '0' && str.charAt(pos) <= '9')
                           {   // "5(" || ")5" - некорректно
                               if (pos != 0)
                                   if(str.charAt(pos-1) == ')' )
                                       return false;
                               if(pos != str.length()-1)
                                       if(str.charAt(pos+1) == '(' )
                                           return false;
                           }
                           else return false;
                   }
                } else return false;
            }
            return true;
        }
    }

    /**
     * определение приоритета
     * @param ch символ
     * @return приоритет символа
     */
    private int typeChar(char ch) {
        if (ch == '*' || ch == '/')
            return 3;
        else if (ch == '+' || ch == '-')
            return 2;
        else if (ch == '(')
            return 1;
        else if (ch == ')')
            return -1;
        return 0;
    }

    /**
     * постфиксная запись выражения
     * @return true - если удалось, иначе false
     */
    private boolean postfixNotation() {

        if(!check() || str.isEmpty())
            return false;
        else{
            Stack<Character> charstack = new Stack<Character>();
            String newstr = "";

            for(int pos = 0; pos < str.length(); pos++)
            {
                //приоритет оператора
                int typeoper = typeChar(str.charAt(pos));

                if (typeoper == 0) newstr += str.charAt(pos); //не оператор, помещаем в новую строку
                else if (typeoper == 1) charstack.push(str.charAt(pos)); // если ( , помещаем в стек
                else if (typeoper > 1) //если + - * /
                {
                    newstr += ' ';
                    while (!charstack.empty())
                    {
                        if (typeChar(charstack.peek()) >= typeoper) //если приоритет верх. эл. стека >= текущ.
                            newstr += charstack.pop();
                        else break;
                    }
                    charstack.push(str.charAt(pos)); //помещаем в стек
                }
                else if (typeoper == -1)  // если ) , переписываем все до ( в строку
                {
                    newstr += ' ';
                    while (typeChar(charstack.peek()) != 1)
                        newstr += charstack.pop();
                    charstack.pop();
                }
            }
            while (!charstack.empty()) newstr += charstack.pop();
           // str = "";
            str = newstr;
            return true;
        }
    }

    /**
     * считает значения выражения, записанного в постфиксной записи, и записывает результат в поле str
     * @return true - если удалось посчитать, иначе false
     */
    public boolean count()
    {
        boolean t = postfixNotation();
        if ( !t ) return false;
        else
            {
                String res = "";
                Stack<Double> st = new Stack<Double>();

                for(int pos = 0;pos < str.length(); pos++)
                {
                    if (str.charAt(pos) == ' ') continue;
                    if (typeChar(str.charAt(pos)) == 0)//число
                    {
                            while (str.charAt(pos) != ' ' && typeChar(str.charAt(pos)) == 0)
                            {
                                res += str.charAt(pos++);
                                if (pos == str.length()) break;
                            }
                            st.push(Double.parseDouble(res));
                            res = "";
                    }
                    if (typeChar(str.charAt(pos)) > 1)// + - * /
                    {
                            double num1 = st.pop();
                            double num2 = st.pop();

                            if (str.charAt(pos) == '+')
                                st.push(num2 + num1);

                            if (str.charAt(pos) == '-')
                                st.push(num2 - num1);

                            if (str.charAt(pos) == '*')
                                st.push(num2 * num1);

                            if (str.charAt(pos) == '/')
                                st.push(num2 / num1);
                    }
                }
                str = Double.toString(st.pop());
                return true;
            }
    }

    /**
     * вывод значения поля str на экран
     */
    public void printExp ()
    {
        System.out.println(str + "\n");
    }

    /**
     * инициализация поля str
     * @param str выражение
     */
    public void setStr(String str) {
        this.str = str;
    }

    /**
     *
     * @return значение поля str
     */
    public String getStr() { return str;}
}
