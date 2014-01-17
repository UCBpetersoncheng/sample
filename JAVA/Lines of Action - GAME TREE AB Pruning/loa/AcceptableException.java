package loa;

/** Represents errors that occur that our LOA simulator should ignore,
  * it should continue and ask for correct input.
  * @author Peterson Cheng. 
  */
public class AcceptableException extends Exception
{
	/** Constructs an AcceptableException with MESSAGE. */
	public AcceptableException(String message) {
		super(message);
	}
}