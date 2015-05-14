package planet.scribe;
/**
*		Interface for isRoot call response events
*/
public interface IsRootListener {

/**
*		This method is the standard callback provided for isRoot calls.
*		@return response of the call to method isRoot.
*/
  public boolean responseArrived();
		
}