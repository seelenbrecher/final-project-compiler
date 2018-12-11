/** 
 * @class: Context
 * This class constructs Context object that has attributes : 
 * 1. lexicalLevel    : current lexical level
 * 2. orderNumber     : current order number
 * 3. symbolHash      : hash table of symbols
 * 4. symbolStack     : stack to keep symbol's name
 * 4. typeStack       : stack to keep symbol's type
 * 4. printSymbols    : choice of printing symbols
 * 4. errorCount      : error counter of context checking
 *
 * @author: DAJI Group (Dalton E. Pelawi & Jimmy)
 */
import java.util.LinkedList;
import java.util.Stack;

class Context
{
    public Context()
    {
        lexicalLevel = -1;
        orderNumber = 0;
        symbolHash = new Hash(HASH_SIZE);
        typeStack = new Stack();
        printSymbols = false;
        errorCount = 0;
        orderNumberStack = new Stack<Integer>();
        numberOfParamsStack = new Stack<Integer>();
    }

    /**
     * This method chooses which action to be taken
     * @input : ruleNo(type:int)
     * @output: -(type:void)
     */
    public void C(int ruleNo)
    {
        boolean error = false;

        // System.out.println("C" + ruleNo + " " + Generate.cell + " " + currentStr);
        switch(ruleNo)
        {
            case 0:
                lexicalLevel++;
                orderNumber = 0;
                break;
            case 1:
                if (printSymbols)
                    symbolHash.print(lexicalLevel);
                break;
            case 2:
                symbolHash.delete(lexicalLevel);
                lexicalLevel--;
                break;
            case 3:
                if (symbolHash.isExist(currentStr, lexicalLevel))
                {
                    System.out.println("Variable declared at line " + currentLine + ": " + currentStr);
                    errorCount++;
                    System.err.println("\nProcess terminated.\nAt least " + (errorCount + parser.yylex.num_error)
                                       + " error(s) detected.");
                    System.exit(1);
                }
                else
                {
                    symbolHash.insert(new Bucket(currentStr));
                }
                symbolStack.push(currentStr);
                break;
            case 4:
                symbolHash.find(currentStr).setLLON(lexicalLevel, orderNumber);
                break;
            case 5:
                symbolHash.find(currentStr).setIdType(((Integer)typeStack.peek()).intValue());
                break;
            case 6:
                if (!symbolHash.isExist(currentStr))
                {
                    System.out.println("Variable undeclared at line " + currentLine + ": " + currentStr);
                    errorCount++;
                    System.err.println("\nProcess terminated.\nAt least " + (errorCount + parser.yylex.num_error)
                                       + " error(s) detected.");
                    System.exit(1);
                }
                else
                {
                    symbolStack.push(currentStr);
                }
                break;
            case 7:
                symbolStack.pop();
                break;
            case 8:
                typeStack.push(new Integer(symbolHash.find(currentStr).getIdType()));
                break;
            case 9:
                typeStack.push(new Integer(Bucket.INTEGER));
                break;
            case 10:
                typeStack.push(new Integer(Bucket.BOOLEAN));
                break;
            case 11:
                typeStack.pop();
                break;
            case 12:
                switch (((Integer)typeStack.peek()).intValue())
                {
                    case Bucket.BOOLEAN:
                        System.out.println("Type of integer expected at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                    case Bucket.UNDEFINED:
                        System.out.println("Undefined type at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                }
                break;
            case 13:
                switch (((Integer)typeStack.peek()).intValue())
                {
                    case Bucket.INTEGER:
                        System.out.println("Type of boolean expected at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                    case Bucket.UNDEFINED:
                        System.out.println("Undefined type at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                }
                break;
            case 14:
                int temp = ((Integer)typeStack.pop()).intValue();
                if (temp != ((Integer)typeStack.peek()).intValue())
                {
                    System.out.println("Unmatched type at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                typeStack.push(new Integer(temp));
                break;
            case 15:
                temp = ((Integer)typeStack.pop()).intValue();
                if ((temp != Bucket.INTEGER) && ((Integer)typeStack.peek()).intValue() != Bucket.INTEGER)
                {
                    System.out.println("Unmatched type at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                typeStack.push(new Integer(temp));
                break;
            case 16:
                temp = symbolHash.find((String)symbolStack.peek()).getIdType();
                if (temp != ((Integer)typeStack.peek()).intValue())
                {
                    System.out.println("Unmatched type at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 17:
                temp = symbolHash.find((String)symbolStack.peek()).getIdType();
                if (temp != Bucket.INTEGER)
                {
                    System.out.println("Type of integer expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 18:
                symbolHash.find(currentStr).setIdKind(Bucket.SCALAR);
                orderNumber++;
                break;
            case 19:
                symbolHash.find(currentStr).setIdKind(Bucket.ARRAY);
                orderNumber += 3;
                break;
            case 20:
                switch (symbolHash.find((String)symbolStack.peek()).getIdKind())
                {
                    case Bucket.UNDEFINED:
                        System.out.println("Variable not fully defined at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                    case Bucket.ARRAY:
                        System.out.println("Scalar variable expected at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                }
                break;
            case 21:
                switch (symbolHash.find((String)symbolStack.peek()).getIdKind())
                {
                    case Bucket.UNDEFINED: 
                        System.out.println("Variable not fully defined at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                    case Bucket.SCALAR:
                        System.out.println("Array variable expected at line " + currentLine + ": " + currentStr);
                        errorCount++;
                        break;
                }
                break;
            case 22:
                // Initiate lexic level and order number of proc and func to hash table
                symbolHash.find(currentStr).setLLON(lexicalLevel, orderNumber);
                break;
            case 23:
                // Set type identifier
                symbolHash.find(currentStr).setIdType(((Integer)typeStack.peek()).intValue());
                break;
            case 24:
                // store procedure to symbol table
                symbolHash.find(currentStr).setIdKind(Bucket.PROCEDURE);
                // add parameters
                symbolHash.find(currentStr).setParameters(new LinkedList<Bucket>());
                subRoutineNamesStack.push(currentStr);
                break;
            case 25:
                // add proc and func params
                Bucket currentParam = symbolHash.find(currentStr);
                String currentSubroutineName = subRoutineNamesStack.peek();
                symbolHash.find(currentSubroutineName).addParameter(currentParam);
                break;
            case 26:
                // store function to symbol table
                symbolHash.find(currentStr).setIdKind(Bucket.FUNCTION);
                // add parameters
                symbolHash.find(currentStr).setParameters(new LinkedList<Bucket>());
                subRoutineNamesStack.push(currentStr);
                break;
            case 27:
                // keluar dari scope yang mengandung parameter. 
                // balikin order number(case 51) dan decrease lexicallevel(case 2)
                C(51);
                C(2);
                break;
            case 28:
                // cek apakah identifier nama prosedur atau bukan
                if (symbolHash.find((String)symbolStack.peek()).getIdKind() == Bucket.UNDEFINED) {
                    System.out.println("Procedure is not fully defined at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                else if (symbolHash.find((String)symbolStack.peek()).getIdKind() != Bucket.PROCEDURE) {
                    System.out.println("Procedure expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 29: {
                // cek func dan proc yang tidak punya param
                LinkedList<Bucket> parameters =  symbolHash.find((String)symbolStack.peek()).getParameters();
                Integer expectedNumberOfParam;
                if (parameters == null) {
                    expectedNumberOfParam = 0;
                } else {
                    expectedNumberOfParam = parameters.size();
                }
                if (expectedNumberOfParam != 0) {
                    System.out.println("Procedure or function expected zero parameters at line " + currentLine + ": " + currentStr);
                    System.out.println("But " + expectedNumberOfParam + " paramaeters was found");
                    errorCount++;
                }
                break;
            }
            case 30:
                // push jumlah argument pertama kali
                numberOfParamsStack.push(0);
                break;
            case 31:{
                // check argument based on parameter
                Integer currNumberOfParam = numberOfParamsStack.peek();
                String subroutineName = (String)symbolStack.peek();
                Integer expectedNumberOfParam = symbolHash.find(subroutineName).getParameters().size();
                // check number of params
                if (currNumberOfParam > expectedNumberOfParam) {
                    System.out.println("Number of parameters exceeded for " + subroutineName + ", found:" + currNumberOfParam + " expected: " + expectedNumberOfParam);
                    errorCount++;
                } else {
                    // check param type
                    Integer paramType = symbolHash.find(subroutineName).getParameters().get(currNumberOfParam-1).getIdType();
                    Integer expType = ((Integer)typeStack.peek()).intValue();

                    if (paramType != expType) {
                        System.out.println("Type mismatch at parameter number: " + currNumberOfParam + " at line:" + currentLine + ": " + currentStr);
                        errorCount++;
                    }
                }
                break;
            }
            case 32:{
                Integer currNumberOfParam = numberOfParamsStack.pop();
                String subroutineName = (String)symbolStack.peek();
                Integer expectedNumberOfParam = symbolHash.find(subroutineName).getParameters().size();
                if (currNumberOfParam != expectedNumberOfParam) {
                    System.out.println("Number of parameters mismatched for " + subroutineName + ", found:" + currNumberOfParam + " expected: " + expectedNumberOfParam);
                    errorCount++;
                }
                break;
            }
            case 33:
                // cek apakah identifier nama fungsi atau bukan
                if (symbolHash.find((String)symbolStack.peek()).getIdKind() == Bucket.UNDEFINED) {
                    System.out.println("Function is not fully defined at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                if (symbolHash.find((String)symbolStack.peek()).getIdKind() != Bucket.FUNCTION) {
                    System.out.println("Function expected at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                break;
            case 34:{
                // increment top of number of params
                Integer currNumberOfParam = numberOfParamsStack.pop();
                currNumberOfParam++;
                numberOfParamsStack.push(currNumberOfParam);
                break;
            }
            case 35:
                // store number of argument to hash table
                // we do not need to do this since we already have list of params. Just need to call parameters.size() to get number of arguments
                break;
            case 36:{
                // check return type of func with expression inside func
                Integer funcType = symbolHash.find((String)symbolStack.peek()).getIdType();
                Integer expType = ((Integer)typeStack.peek()).intValue();
                if (funcType != expType) {
                    System.out.println("Unmatched type of expression to function at line " + currentLine + ": " + currentStr);
                    errorCount++;
                }
                typeStack.push(new Integer(expType));
                break;
            }
            case 37:{
                // if identifier type is function C(33), else C(20)
                Integer identifierType = symbolHash.find(currentStr).getIdKind();
                if (identifierType == Bucket.FUNCTION) {
                    C(33);
                } else {
                    C(20);
                }
                break;
            }
            case 50:
                // push current order number ke stack
                orderNumberStack.push(orderNumber);
                break;
            case 51:
                // setelah keluar dari scope parameter, balikin order numbernya
                orderNumber = orderNumberStack.pop();
                break;
            case 52: {
                symbolHash.find(currentStr).setBaseAddress(Generate.cell);
                break;
            }
        }
    }

    /**
     * This method sets the current token and line
     * @input : str(type:int), line(type:int)
     * @output: -(type:void)
     */
    public void setCurrent(String str, int line)
    {
        currentStr = str;
        currentLine = line;
    }

    /**
     * This method sets symbol printing option
     * @input : bool(type:boolean)
     * @output: -(type:void)
     */
    public void setPrint(boolean bool)
    {
        printSymbols = bool;
    }

    private final int HASH_SIZE = 211;

    public static int lexicalLevel;
    public static int orderNumber;
    public static Hash symbolHash;
    public static Stack symbolStack = new Stack();
    private Stack typeStack;
    public static String currentStr;
    public static int currentLine;
    private boolean printSymbols;
    public int errorCount;

    // name of procedure and function, needed to add params
    public static Stack<String> subRoutineNamesStack = new Stack<String>();
    public static Stack<Integer> numberOfParamsStack;
    
    // menyimpan order number pada suatu lexic level sebelum berpindah level
    public static Stack<Integer> orderNumberStack;
}