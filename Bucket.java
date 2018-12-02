import java.util.LinkedList;

class Bucket
{
    public Bucket()
    {
    }

    public Bucket(String idName)
    {
        this.idName = idName;
        this.lexicLev = UNDEFINED;
        this.orderNum = UNDEFINED;
        this.idType = UNDEFINED;
        this.idKind = UNDEFINED;
        this.nextBucket = null;
        this.parameters = null;
    }

    public void setIdName(String idName)
    {
        this.idName = idName;
    }

    public void setLexicLev(int lexicLev)
    {
        this.lexicLev = lexicLev;
    }

    public void setOrderNum(int orderNum)
    {
        this.orderNum = orderNum;
    }

    public void setLLON(int lexicLev, int orderNum)
    {
        this.lexicLev = lexicLev;
        this.orderNum = orderNum;
    }

    public void setIdType(int idType)
    {
        this.idType = idType;
    }

    public void setIdKind(int idKind)
    {
        this.idKind = idKind;
    }

    public void setNextBucket()
    {
        nextBucket = null;
    }

    public void setNextBucket(Bucket next)
    {
        nextBucket = next;
    }

    public String getIdName()
    {
        return idName;
    }

    public int getLexicLev()
    {
        return lexicLev;
    }

    public int getOrderNum()
    {
        return orderNum;
    }

    public int getIdType()
    {
        return idType;
    }

    public int getIdKind()
    {
        return idKind;
    }

    public String getIdTypeStr()
    {
        String type = null;

        switch (idType)
        {
            case INTEGER:
                type = "integer";
                break;
            case BOOLEAN:
                type = "boolean";
                 break;
            case UNDEFINED:
                type = "undefined";
                break;
        }

        return type;
    }

    public String getIdKindStr()
    {
        String kind = null;

        switch (idKind)
        {
            case SCALAR:
                kind = "scalar";
                break;
            case ARRAY:
                kind = "array";
                break;
            case PROCEDURE:
                kind = "procedure";
                break;
            case FUNCTION:
                kind = "function";
                break;
            case UNDEFINED:
                kind = "undefined";
                break;
        }

        return kind;
    }


    public Bucket getNextBucket()
    {
        return nextBucket;
    }

    // set parameters
    public void setParameters(LinkedList<Bucket> parameters) {
        this.parameters = parameters;
    }

    // add parameter
    public void addParameter(Bucket parameter) {
        this.parameters.add(parameter);
    }

    // get parameters
    public LinkedList<Bucket> getParameters() {
        return this.parameters;
    }

    public static final int INTEGER = 0;
    public static final int BOOLEAN = 1;
    public static final int UNDEFINED = -1;

    public static final int SCALAR = 0;
    public static final int ARRAY = 1;
    public static final int PROCEDURE = 2;
    public static final int FUNCTION = 3;

    private String idName;
    private int orderNum;
    private int lexicLev;
    private int idType;
    private int idKind;
    private Bucket nextBucket;

    // function or procedure parameters
    private LinkedList<Bucket> parameters;
}