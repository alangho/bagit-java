package gov.loc.repository.bagit;

public interface BagVisitor {
	
	/**
	 * Starts the cycle of a visitor visiting a bag.
	 * @param bag The bag for the visitor to visit.
	 */
	public void startBag(Bag bag);	
	
	/**
	 * Starts the tag cycle of a visitor.
	 */
	public void startTags();
	
	/**
	 * The visitor visits the tags in the BagFile.
	 * @param bagFile The BagFile tags to visit.
	 */
	public void visitTag(BagFile bagFile);
	
	/**
	 * Ends the tag cycle of a visitor.
	 */
	public void endTags();
	
	/**
	 * Starts the payload cycle of a visitor.
	 */
	public void startPayload();
	
	/**
	 * The visitor visits the payload in the BagFile.
	 * @param bagFile The BagFile payload to visit.
	 */
	public void visitPayload(BagFile bagFile);
	
	/**
	 * Ends the payload cycle of a visitor.
	 */
	public void endPayload();
	
	/**
	 * Ends the cycle of a visitor visiting a bag.
	 */
	public void endBag();
	
}