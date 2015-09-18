package gov.loc.repository.bagit;

import gov.loc.repository.bagit.BagFactory.Version;
import gov.loc.repository.bagit.Manifest.Algorithm;
import gov.loc.repository.bagit.transformer.Completer;
import gov.loc.repository.bagit.transformer.HolePuncher;
import gov.loc.repository.bagit.utilities.SimpleResult;
import gov.loc.repository.bagit.verify.FailModeSupporting.FailMode;
import gov.loc.repository.bagit.verify.Verifier;
import gov.loc.repository.bagit.writer.Writer;

import java.io.Closeable;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>This is the core interface of the BagIt Library, representing
 * a bag from the BagIt spec.
 * Methods are available for creating, manipulating, writing, validating,
 * and verifying bags.</p>
 * 
 * <p>You should not create a Bag instance directly.  Instead, use an
 * appropriate method on the {@link BagFactory} class.</p>
 *
 * @see BagFactory
 */
public interface Bag extends Closeable {
	
	/**
	 * <p>The format of a bag.  Bags may be serialized (such
	 * as "zip") or they may simply be directories on
	 * the filesystem (such as "file").</p>
	 * 
	 * <table>
	 * <tbody>
	 * <tr><th>Format</th><th>Scheme</th><th>Extension</th><th>Serialized?</th></tr>
	 * <tr><td>{@link #FILESYSTEM}</td><td>file</td><td>&lt;none&gt;</td><td>false</td></tr>
	 * <tr><td>{@link #ZIP}</td><td>zip</td><td>.zip</td><td>true</td></tr>
	 * </tbody>
	 * <caption>Supported BagIt formats and extensions</caption>
	 * </table>
	 */
	enum Format {
		ZIP ("zip", true, ".zip"), FILESYSTEM ("file", false, "");
		
		/**
		 * The URI scheme for the format.
		 */
		public String scheme;
		
		/**
		 * Whether or not the format is a serialized bag format.
		 */
		public boolean isSerialized;
		
		/**
		 * The file extension typically appended to a bag name
		 * in the given format when it is written to disk.
		 */
		public String extension;
		
		/**
		 * Format constructor.
		 * @param scheme The URI scheme for the format.
		 * @param isSerialized Whether or not the format is a serialized bag format.
		 * @param extension The file extension typically appended to a bag name.
		 */
		Format(String scheme, boolean isSerialized, String extension) {
			this.scheme = scheme;
			this.isSerialized = isSerialized;
			this.extension = extension;
		}
		
	};
	
	/**
	 * Gets the version of the BagIt spec to which the bag conforms.
	 * @return The version of the bag.  Will never be null.
	 */
	Version getVersion();
	
	/**
	 * Gets a BagFile.
	 * @return A BagFile.
	 */
	File getFile();

	/**
	 * Sets the BagFile to the incoming File. 
	 * @param file The file to set as a BagFile.
	 */
	void setFile(File file);
	
	/**
	 * Gets a list of manifests from a payload to which the bag conforms.
	 * @return A manifest list that belongs to a payload from the current bag.
	 */
	List<Manifest> getPayloadManifests();
	
	/**
	 * Gets an algorithm manifest from a payload to which the bag conforms. 
	 * @param algorithm The type of algorithm used.
	 * @return An algorithm manifest that belongs to a payload from the current bag.
	 */
	Manifest getPayloadManifest(Algorithm algorithm);

	/**
	 * Gets a list of manifests from the tags to which the bag conforms.
	 * @return A manifest list that belongs to the tags from the current bag.
	 */
	List<Manifest> getTagManifests();
	
	/**
	 * Gets an algorithm manifest from the tags to which the bag conforms.
	 * @param algorithm The type of algorithm used.
	 * @return An algorithm manifest that belongs to the tags from the current bag.
	 */
	Manifest getTagManifest(Algorithm algorithm);
	
	/**
	 * Gets a collection of BagFiles that contains tags from which the bag conforms.
	 * @return A collection of BagFiles that contains tags from the current bag.
	 */
	Collection<BagFile> getTags();

	/**
	 * Gets a collection of BagFiles that contains the payload from which the bag conforms.
	 * @return A collection of BagFiles that contains the payload from the current bag.
	 */
	Collection<BagFile> getPayload();
	
	/**
	 * Removes a BagFile from located at the filepath from which the bag conforms.
	 * @param filepath Location of the BagFile to be removed from the current bag.
	 */
	void removeBagFile(String filepath);

	/**
	 * Removes a tag directory located at the filepath from which the bag conforms.
	 * @param filepath Location of the tag directory to be removed from the current bag.
	 */
	void removeTagDirectory(String filepath);
	
	/**
	 * Removes a payload directory located at the filepath from which the bag conforms.
	 * @param filepath Location of the payload directory to be removed from the current bag.
	 */
	void removePayloadDirectory(String filepath);
	
	/**
	 * Gets a BagFile from the located at the filepath from which the bag conforms.
	 * @param filepath Location of the BagFile to retrieve from the current bag.
	 * @return A BagFile located at the filepath from that belongs to the current bag.
	 */
	BagFile getBagFile(String filepath);
	
	/**
	 * Puts a BagFile in the current bag.
	 * @param bagFile The BagFile to put in the current bag.
	 */
	void putBagFile(BagFile bagFile);

	/**
	 * Puts a collection of BagFiles in the current bag.
	 * @param bagFiles The collection of BagFiles to put in the current bag.
	 */
	void putBagFiles(Collection<BagFile> bagFiles);
	
	/**
	 * Adds a file to the payload from which the bag conforms.
	 * @param file The file to add to the payload of the current bag.
	 */
	void addFileToPayload(File file);
	
	/**
	 * Adds a list of files to the payload from which the bag conforms.
	 * @param files The list of files to add to the payload of the current bag.
	 */
	void addFilesToPayload(List<File> files);
	
	/**
	 * Adds a file as a tag to the current bag.
	 * @param file The file that is added as a tag to the current bag.
	 */
	void addFileAsTag(File file);

	/**
	 * Adds a list of files as tags to the current bag.
	 * @param files The list of files that is added as tags to the current bag.
	 */
	void addFilesAsTag(List<File> files);
	
	/**
	 * Finds checksums in all manifests for a file.
	 * @param filepath The file to get checksums for.
	 * @return A map for each algorithm to each checksum for the given file.
	 */
	Map<Algorithm, String> getChecksums(String filepath);
	
	/**
	 * Gets the bag declaration text to which the bag conforms.
	 * @return The declaration text of the current bag.
	 */
	BagItTxt getBagItTxt();
	
	/**
	 * Gets a "bag-info.txt" file that contains metadata elements describing
	 * the bag and payload in name-value pairs.
	 * @return A set of name-value pairs from a "bag-info.txt" file.
	 */
	BagInfoTxt getBagInfoTxt();
	
	/**
	 * Gets a "fetch.txt" file that contains a list of files to be fetched
	 * and added to the payload.
	 * @return A list of files to be fetched and added to the payload.
	 */
	FetchTxt getFetchTxt();
	
	/**
	 * Gets the progress of the files that are being fetched from 
	 * the "fetch.txt" file.
	 * @return The progress of the files that being fetched.
	 */
	FetchTxt getFetchProgressTxt();
	
	/**
	 * Gets the format of a bag.  Bags may be serialized or they may simply be
	 * directories on the filesystem.
	 * @return The format of the current bag.
	 */
	Format getFormat();

	/**
	 * Determines whether the bag is valid according to the BagIt Specification.
	 * @return A {@link SimpleResult} representing the validity of the bag
	 */		
	SimpleResult verifyValid();

	/**
	 * Determines whether the bag is valid according to the BagIt Specification
	 * and sets a verifier's mode to a FailMode.
	 * @param failMode Describes which stage a verifier fails on.
	 * @return A {@link SimpleResult} representing the validity of the bag.
	 */
	SimpleResult verifyValid(FailMode failMode);

	/**
	 * Determines whether the bag is complete according to the BagIt Specification.
	 * @return A {@link SimpleResult} representing the completeness of the bag.
	 */		
	SimpleResult verifyComplete();

	/**
	 * Determines whether the bag is complete according to the BagIt Specification
	 * and sets a verifier's mode to a FailMode.
	 * @param failMode Describes which stage a verifier fails on.
	 * @return A {@link SimpleResult} representing the completeness of the bag.
	 */
	SimpleResult verifyComplete(FailMode failMode);

	/**
	 * Invokes a Verifier to verify a bag.
	 * @param verifier The {@link Verifier} implementation to use.
	 * @return A {@link SimpleResult} representing the verification result for the bag.
	 */	
	SimpleResult verify(Verifier verifier);
		
	/**
	 * Verify that each checksum in every payload manifest can be verified against
	 * the appropriate contents.
	 * @return A {@link SimpleResult} representing the verification of the payload manifests for the bag.
	 */
	SimpleResult verifyPayloadManifests();

	/**
	 * Verify that each checksum in every payload manifest can be verified against
	 * the appropriate contents and sets a verifier's mode to a FailMode.
	 * @param failMode Describes which stage a verifier fails on.
	 * @return A {@link SimpleResult} representing the verification of the payload manifests for the bag.
	 */
	SimpleResult verifyPayloadManifests(FailMode failMode);

	/**
	 * Determines whether the bag is valid according to the BagIt Specification
	 * while adding Progress Listeners to a verifier and sets a verifier's mode to a FailMode.
	 * @param failMode Describes which stage a verifier fails on.
	 * @param progressListeners Receives progress reports from other components.
	 * @return A {@link SimpleResult} representing the validity of the bag.
	 */
	SimpleResult verifyValid(FailMode failMode, List<ProgressListener> progressListeners);
	
	/**
	 * Verify that each checksum in every tag manifest can be verified against
	 * the appropriate contents.
	 * @return A {@link SimpleResult} representing the verification of the tag manifests for the bag.
	 */	
	SimpleResult verifyTagManifests();

	/**
	 * Verify that each checksum in every tag manifest can be verified against
	 * the appropriate contents and sets a verifier's mode to a FailMode.
	 * @param failMode Describes which stage a verifier fails on.
	 * @return A {@link SimpleResult} representing the verification of the tag manifests for the bag.
	 */
	SimpleResult verifyTagManifests(FailMode failMode);

	
	/**
	 * Loads a bag based on the tag files found on disk and the payload files listed in the payload manifests.
	 */
	void loadFromManifests();

	/**
	 * Loads a bag based on the tag files and payload files found on disk.
	 */
	void loadFromFiles();

	/**
	 * Loads a bag based on the tag files and payload files found on disk while ignoring 
	 * files found in specified directories.
	 * @param ignoreAdditionalDirectories List of Strings that contain directories to ignore.
	 */
	void loadFromFiles(List<String> ignoreAdditionalDirectories);
	
	/**
	 * Invokes a BagVisitor.
	 * @param visitor The visitor for the bag.
	 */
	void accept(BagVisitor visitor);
	
	/**
	 * Writes to a new bag and new file.
	 * @param writer A FileSystemWriter that writes to the bag and file.
	 * @param file New file to be written to.
	 * @return A new bag.
	 */
	Bag write(Writer writer, File file);

	/**
	 * Makes a bag holey by creating a fetch.txt and removing payload files.
	 * 
	 * @param baseUrl The url part to prepend to create the payload url
	 * @param includePayloadDirectoryInUrl Whether to include the payload directory ("data") in the payload url
	 * @param includeTags Whether to include the tags in the fetch.txt.  If true then includePayloadDirectory will be true.
	 * @param resume True to indicate that the process should be resumed.
	 * @return the newly holey bag
	 * @see HolePuncher#makeHoley(Bag, String, boolean, boolean, boolean, boolean)
	 */
	Bag makeHoley(String baseUrl, boolean includePayloadDirectoryInUrl, boolean includeTags, boolean resume);

	/**
	 * Invokes a HolePuncher to make a bag holey.
	 * 
	 * @param holePuncher The {@link HolePuncher} implementation to use
	 * @param baseUrl The url part to prepend to create the payload url
	 * @param includePayloadDirectoryInUrl Whether to include the payload directory ("data") in the payload url
	 * @param includeTags Whether to include the tags in the fetch.txt.  If true then includePayloadDirectory will be true.
	 * @param resume True to indicate that the process should be resumed.
	 * @return the newly holey bag
	 * @see HolePuncher#makeHoley(Bag, String, boolean, boolean, boolean, boolean)
	 */	
	Bag makeHoley(HolePuncher holePuncher, String baseUrl, boolean includePayloadDirectoryInUrl, boolean includeTags, boolean resume);
	
	/**
	 * Makes a bag complete by filling in any pieces necessary to satisfy the BagIt Specification.
	 * @return the completed bag
	 */
	Bag makeComplete();

	/**
	 * Invokes a Completer to make a bag complete.
	 * @param completer The {@link Completer} implementation to use.
	 * @return The completed bag.
	 */
	Bag makeComplete(Completer completer);
	
	/**
	 * Gets the names of constants associated with a bag.
	 * @return The names of constants associated with the current bag.
	 */
	BagConstants getBagConstants();
	
	/**
	 * Gets various parts of a bag for the version and underlying implementation of the bag.
	 * @return Various parts of the current bag.
	 */
	BagPartFactory getBagPartFactory();
	
	/**
	 * <p>Contains names for constants associated with a bag.
	 * BagIt defines and reserves several names, and some of those names
	 * change between versions of the specification.  This interface
	 * abstracts away those constants so they can be examined on a
	 * per-version basis.</p>
	 * 
	 * <p>For example, the <code>bag-info.txt</code> file was called
	 * <code>package-info.txt</code> in earlier versions of the spec.
	 * The correct name can be determined by using the
	 * {@link #getBagInfoTxt()} method.</p>
	 * 
	 * <p>You should never reference BagIt constants by name directly
	 * in your code, as they may change from version to version.  Instead,
	 * obtain an instance of this interface and its values as the
	 * constants.
	 * Constants for the current bag's version may be obtained by
	 * calling the {@link Bag#getBagConstants()} method.
	 * Constants for a particular BagIt version may be obtained
	 * by calling the {@link BagFactory#getBagConstants(Version)}
	 * method.</p>
	 * 
	 * @see Bag#getBagConstants()
	 * @see BagFactory#getBagConstants()
	 * @see BagFactory#getBagConstants(Version)
	 */
	public interface BagConstants {

		/**
		 * Get the prefix for a payload manifest, "manifest-"
		 * in the latest version.
		 * @return The constant.
		 */
		String getPayloadManifestPrefix();

		/**
		 * Get the prefix for a payload manifest, "tagmanifest-"
		 * in the latest version.
		 * @return The constant.
		 */
		String getTagManifestPrefix();

		/**
		 * Get the prefix for a payload manifest, ".txt"
		 * in the latest version.
		 * @return The constant.
		 */
		String getPayloadManifestSuffix();

		/**
		 * Get the prefix for a payload manifest, ".txt"
		 * in the latest version.
		 * @return The constant.
		 */
		String getTagManifestSuffix();

		/**
		 * Get the text encoding required for the
		 * {@link #getBagItTxt() bagit.txt} file, "UTF-8" in the latest
		 * version.
		 * @return The constant.
		 */
		String getBagEncoding();

		
		/**
		 * Get the name of the bag declaration file, "bagit.txt"
		 * in the latest version.
		 * @return The constant.
		 */
		String getBagItTxt();

		/**
		 * Get the name of the payload directory, "data"
		 * in the latest version.
		 * @return The constant.
		 */
		String getDataDirectory();

		/**
		 * Get the name of the standard bag metadata file, "bag-info.txt"
		 * in the latest version.
		 * @return The constant.
		 */
		String getBagInfoTxt();

		/**
		 * Get the name of the fetch file, "fetch.txt"
		 * in the latest version.
		 * @return The constant.
		 */
		String getFetchTxt();
		
		/**
		 * Get the name of the fetch progress file, "fetch-progress.txt"
		 * in the latest version.
		 * @return The constant.
		 */
		String getFetchProgressTxt();

		/**
		 * Get the version of the spec these constants are for.
		 * @return The version.
		 */
		Version getVersion();
	}

	/**
	 * <p>Creates various parts of a bag, as appropriate for the
	 * version and underlying implementation of the {@link Bag} interface.</p>
	 * 
	 * <p>You should never create implementations for the various components
	 * of a bag directly.  Instead, you should obtain a parts factory through
	 * {@link Bag#getBagPartFactory()} and then create the desired component
	 * through the factory.</p>
	 * 
	 * <p>The components created are not already "bound" to a bag.  They
	 * must still be added to the bag using methods such as
	 * {@link Bag#putBagFile(BagFile)}.</p>
	 * 
	 * @see Bag
	 */
	public interface BagPartFactory {
		
		/**
		 * Creates a ManifestReader to read a bag's manifest file. 
		 * @param in The InputStream that contains a manifest to read.
		 * @param encoding Checks for the contents of the manifest file or a file name.
		 * @return A ManifestReader that contains a bags manifest contents.
		 */
		ManifestReader createManifestReader(InputStream in, String encoding);
		
		/**
		 * Creates a ManifestReader to read a bag's manifest file. 
		 * @param in The InputStream that contains a manifest to read.
		 * @param encoding Checks for the contents of the manifest file or a file name.
		 * @param treatBackSlashAsPathSeparator Determines if backslash is a path separator.
		 * @return A ManifestReader that contains a bags manifest contents.
		 */
		ManifestReader createManifestReader(InputStream in, String encoding, boolean treatBackSlashAsPathSeparator);
		
		/**
		 * Creates a ManifestWriter that writes data to other files.
		 * @param out The OutputStream that writes to a file.
		 * @return A ManifestWriter that writes to an OutputStream.
		 */
		ManifestWriter createManifestWriter(OutputStream out);
		
		/**
		 * Creates a ManifestWriter that writes data to other files.
		 * @param out The OutputStream that writes to a file.
		 * @param manifestSeparator A String that separates manifests.
		 * @return A ManifestWriter that writes to an OutputStream.
		 */
		ManifestWriter createManifestWriter(OutputStream out, String manifestSeparator);
		
		/**
		 * Creates a manifest with a given name.
		 * @param name The name for the new manifest.
		 * @return A Manifest with a given name.
		 */
		Manifest createManifest(String name);
		
		/**
		 * Creates a manifest from the sourceBagFile with a given name.
		 * @param name The name for the new manifest.
		 * @param sourceBagFile The BagFile used to create a manifest.
		 * @return A manifest created from the sourceBagFile with a given name.
		 */
		Manifest createManifest(String name, BagFile sourceBagFile);
		
		/**
		 * Creates a BagItTxtReader that reads from a file.
		 * @param encoding The BagItTxt character encoding type.
		 * @param in The InputStream that reads an incoming file.
		 * @return A BagItTxtReader that reads an incoming file.
		 */
		BagItTxtReader createBagItTxtReader(String encoding, InputStream in);
		
		/**
		 * Creates a BagItTxtWriter that writes the character encoding type,
		 * and specifies a line length and space indentation to a BagItTxt tag file.
		 * @param out The OutputStream that writes to a file.
		 * @param encoding The BagItTxt character encoding type.
		 * @param lineLength Specifies a line length while writing to a BagItTxt.
		 * @param indentSpaces Specifies an amount of spaces to indent while writing.
		 * @return A BagItTxtWriter that writes to a file conforming to it's given parameters.
		 */
		BagItTxtWriter createBagItTxtWriter(OutputStream out, String encoding, int lineLength, int indentSpaces);
		
		/**
		 * Creates a BagItTxtWriter that writes the character
		 * encoding type to a BagItTxt tag file.
		 * @param out The OutputStream that writes to a BagItTxt tag file.
		 * @param encoding The BagItTxt character encoding type.
		 * @return A BagItTxtWriter that writes to a BagItTxt tag file.
		 */
		BagItTxtWriter createBagItTxtWriter(OutputStream out, String encoding);
		
		/**
		 * Creates a BagItTxt tag file and adds it to a BagFile.
		 * @param bagFile The BagFile that adds this BagItTxt tag file.
		 * @return A BagItTxt tag file that belongs to a BagFile.
		 */
		BagItTxt createBagItTxt(BagFile bagFile);
		
		/**
		 * Creates a BagItTxt tag file that MUST consist of exactly two lines:
		 * <ul>
		 * 		<li>BagIt-Version: M.N</li>
		 * 			<ul>
		 * 				<li>Where M.N identifies the BagIt major (M) and minor (N) version numbers.</li>
		 * 			</ul>
		 * 		<li>Tag-File-Character-Encoding: UTF-8</li>
		 * 			<ul>
		 * 				<li>UTF-8 identifies the character set encoding of tag files.</li>
		 * 			</ul>
		 * </ul>
		 * The bag declaration MUST be encoded in UTF-8, and MUST NOT
		 * contain a byte-order mark (BOM).
		 * @return A BagItTxt tag file that consists of two lines.
		 */
		BagItTxt createBagItTxt();
		
		/**
		 * Creates a BagInfoTxtReader that reads the metadata elements describing
		 * the bag and the payload with a character encoding.
		 * @param encoding The BagInfoTxt character encoding type.
		 * @param in The InputStream that reads an incoming file.
		 * @return A BagInfoTxtReader that reads metadata elements.
		 */
		BagInfoTxtReader createBagInfoTxtReader(String encoding, InputStream in);
		
		/**
		 * Creates a BagInfoTxtWriter that writes the metadata elements describing 
		 * the bag and the payload to an OutputStream with a character encoding 
		 * while specifying the line length and indent spaces.
		 * @param out The OutputStream that writes to a file.
		 * @param encoding The BagInfoTxt character encoding type. 
		 * @param lineLength Specifies a line length while writing to a BagInfoTxt.
		 * @param indentSpaces Specifies an amount of spaces to indent while writing.
		 * @return A BagInfoTxtWriter that writes to a file conforming to it's given parameters.
		 */
		BagInfoTxtWriter createBagInfoTxtWriter(OutputStream out, String encoding, int lineLength, int indentSpaces);
		
		/**
		 * Creates a BagInfoTxtWriter that writes the metadata elements describing 
		 * the bag and the payload to an OutputStream with a character encoding.
		 * @param out The OutputStream that writes to a file.
		 * @param encoding The BagInfoTxt character encoding type.
		 * @return A BagInfoTxtWriter that writes the metadata elements.
		 */
		BagInfoTxtWriter createBagInfoTxtWriter(OutputStream out, String encoding);
		
		/**
		 * Creates a BagInfoTxt that contains metadata elements describing the bag
		 * and the payload and adds it to a BagFile.
		 * @param bagFile The BagFile that adds this BagInfoTxt.
		 * @return A BagInfoTxt that belongs to a BagFile.
		 */
		BagInfoTxt createBagInfoTxt(BagFile bagFile);
		
		/**
		 * Creates a BagInfoTxt that contains metadata elements describing
		 * the bag and the payload.
		 * @return A BagInfoTxt that contains metadata elements.
		 */
		BagInfoTxt createBagInfoTxt();
		   
		/**
		 * Creates a FetchTxtReader that reads in a list of files to be fetched
		 * and added to the payload before a bag can be checked for completeness.
		 * @param in The InputStream that reads an incoming file.
		 * @param encoding The FetchTxt character encoding type.
		 * @return A FetchTxtReader that reads an incoming list of files to be fetched.
		 */
		FetchTxtReader createFetchTxtReader(InputStream in, String encoding);
		
		/**
		 * Creates a FetchTxtWriter that writes a list of files that need to be fetched
		 * and added to the payload before a bag can be checked for completeness.
		 * @param out The OutputStream that writes to a file.
		 * @return A FetchTxtWriter that writes a list of filed to be fetched.
		 */
		FetchTxtWriter createFetchTxtWriter(OutputStream out);
		
		/**
		 * Creates a FetchTxt that contains a list of files that need to be fetched
		 * and added to the payload before a bag can be checked for completeness.
		 * @return A FetchTxt that contains a list of files to be fetched.
		 */
		FetchTxt createFetchTxt();
		
		/**
		 * Creates a FetchTxt that contains a list of files that need 
		 * to be fetched and added to the payload before a bag can be checked 
		 * for completeness and adds it to a BagFile.
		 * @param sourceBagFile The BagFile that adds this FetchTxt.
		 * @return A FetchTxt that contains a list of files to be fetched.
		 */
		FetchTxt createFetchTxt(BagFile sourceBagFile);
		
		/**
		 * Creates a FetchTxtReader that reads a progress of the files that 
		 * are currently being fetched.
		 * @param in The InputStream that reads an incoming file.
		 * @param encoding The FetchTxt character encoding type.
		 * @return A FetchTxtReader that returns the progress of files being fetched.
		 */
		FetchTxtReader createFetchProgressTxtReader(InputStream in, String encoding);
		
		/**
		 * Creates a FetchTxtWriter that writes the progress of the files that
		 * are currently being fetched.
		 * @param out The OutputStream that writes to a file.
		 * @return A FetchTxtWriter that writes the progress of files being fetched.
		 */
		FetchTxtWriter createFetchProgressTxtWriter(OutputStream out);
		
		/**
		 * Creates a FetchTxt that fetches the progress of the files that
		 * are currently being fetched.
		 * @return A FetchTxt that fetches the current progress of fetched files.
		 */
		FetchTxt createFetchProgressTxt();
		
		/**
		 * Creates a FetchTxt that fetches the progress of the files that
		 * are currently being fetched ands adds it to a BagFile.
		 * @param sourceBagFile The BagFile that adds this FetchTxt.
		 * @return A FetchTxt that fetches the current progress of fetched files.
		 */
		FetchTxt createFetchProgressTxt(BagFile sourceBagFile);
		
		/**
		 * Gets the version of this BagPartFactory.
		 * @return The version of this BagPartFactory.
		 */
		Version getVersion();	
	}
		
}