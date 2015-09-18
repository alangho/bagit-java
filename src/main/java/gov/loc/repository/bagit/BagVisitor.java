package gov.loc.repository.bagit;

public interface BagVisitor {
	
	/**
	 * Called when first visiting a bag.
	 * @param bag The bag to visit.
	 */
	public void startBag(Bag bag);	
	
	/**
	 * Called before visiting a tag file.
	 */
	public void startTags();
	
	/**
	 * Called when visiting a tag file.
	 * @param bagFile The tag file to visit.
	 */
	public void visitTag(BagFile bagFile);
	
	/**
	 * Called after visiting a tag file.
	 */
	public void endTags();
	
	/**
	 * Called before visiting a payload file.
	 */
	public void startPayload();
	
	/**
	 * Called when visiting a payload file.
	 * @param bagFile The payload file to visit.
	 */
	public void visitPayload(BagFile bagFile);
	
	/**
	 * Called after visiting a payload file.
	 */
	public void endPayload();
	
	/**
	 * Called when finished visiting a bag.
	 */
	public void endBag();
	
}