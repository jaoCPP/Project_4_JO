public class RecursiveMaxOfArray
{

    
    /**
     * Compute the maximum of a range of values in an array recursively.
     *
     * @param data   An array of integers.
     * @param from  The low index for searching for the maximum.
     * @param to    The high index for searching for the maximum.
     * 
     * preconditions: from LTE Zero to, from LTE 0, to LT length, length GT 0
     *                
     * @return     The maximum value in the array.
     */
    
    public  int max(int data[], int from, int to)
    {
        int result = 0;
        
        // ADD YOUR CODE HERE
//vvvvvvvvv ADDED CODE vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv        

        if (data == null || data.length == 0 || from < 0 || to >= data.length || from > to){
            throw new BadArgumentsForMaxException("Invalid arguments for max");
        }
        if(from == to){
            return data[from];
        }

        int current = data[from];
        int maxOfRest = max(data, from + 1, to);
        result = Math.max(current, maxOfRest);

        return result;
    }
    
    
}
