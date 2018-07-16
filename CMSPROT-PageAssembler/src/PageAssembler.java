
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;	//TODO remove this dependency?

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

public class PageAssembler {

	private final File IN_PAGE;
	private final File OUT_PAGE;
	private Document docTree;
	
	//CONSTRUCTORS
	
	public PageAssembler(File inPage) throws IOException {
		this(inPage, inPage, true);
	}
	
	public PageAssembler(File inPage, File outPage) throws IOException {
		this(inPage, outPage, true);
	}
	
	public PageAssembler(File inPage, File outPage, boolean indentation) throws IOException {
		this.IN_PAGE = inPage;
		this.OUT_PAGE = outPage;
		
		this.docTree = Jsoup.parse(IN_PAGE, null);
		if(indentation) {
			docTree.outputSettings().prettyPrint(true).indentAmount(4);	//sets output indentation
		}
	}
	
	//DOM MANIPULATION PUBLIC METHODS
	
	public void addFragment(File fragmentPage, String fragmentParentId, int fragmentPosition) throws IOException {
		
		Element fragmentTree = getFragmentTreeFromFile(fragmentPage);
		addFragment(fragmentTree, fragmentParentId, fragmentPosition);
		saveTreeToFile();
	}
	
	public void removeFragment(String fragmentId) throws IOException {
		
		removeFragment(docTree.getElementById(fragmentId));
		saveTreeToFile();
	}
	
	public void moveFragment(String fragmentId, String newParentId, int newFragmentPosition) throws IOException {
		
		moveFragment(docTree.getElementById(fragmentId), newParentId, newFragmentPosition);
		saveTreeToFile();
	}
	
	public void updateFragment(File newFragmentPage, String oldFragmentId) throws IOException {
		
		Element newFragmentTree = getFragmentTreeFromFile(newFragmentPage);
		updateFragment(newFragmentTree, oldFragmentId);
		saveTreeToFile();
	}
	
	//UTILITIES IN/OUT
	
	private Element getFragmentTreeFromFile(File fragmentInputFile) throws IOException {
		
		String fragmentHtml = FileUtils.readFileToString(fragmentInputFile, "UTF-8");
		Element fragmentTree = getFragmentTreeFromHtml(fragmentHtml);
		return fragmentTree;
	}
	
	//Jsoup automatically wraps a complete html tree around a fragment when parseBodyFragment(), so I had to strip it with this workaround
	private Element getFragmentTreeFromHtml(String fragmentHtml) {
			return Jsoup.parseBodyFragment(fragmentHtml).body().child(0);	//TODO this is just a stub(?)
	}
	
	private void saveTreeToFile() throws IOException {
		FileUtils.writeStringToFile(OUT_PAGE, docTree.html(), "UTF-8"); 
	}
	
	//DOM MANIPULATION
	
	private void addFragment(Element fragmentTree, String fragmentParentId, int fragmentPosition) throws IOException {
		
		System.out.println("\n+IN TREE (add)+\n");
		printNodeRecursive(docTree);
		
		System.out.println("\n+FRAGMENT TREE (add)+\n");
		printNodeRecursive(fragmentTree);
		
		Element fragmentParent = docTree.getElementById(fragmentParentId);
		int siblingsSize = fragmentParent.children().size();
		if( siblingsSize == 0 || fragmentPosition > siblingsSize-1) {
			fragmentTree.appendTo(fragmentParent);
		} else {
			//NOTE: do NOT use "fragmentParent.insertChildren(fragmentPosition, fragmentTree)" because it considers all child Nodes and not only child Elements
			//NOTE: do NOT use "fragmentParent.children().add(fragmentPosition, fragmentTree)" because children() gives a COPY
			Element fragmentPreviousSibling = fragmentParent.child(fragmentPosition);	
			fragmentPreviousSibling.before(fragmentTree);
		}
		System.out.println("\n+OUT TREE (add)+\n");
		printNodeRecursive(docTree);
	
	}
	
	private void removeFragment(Element fragmentTreeToRemove) throws IOException {
			
			System.out.println("\n+IN TREE (remove)+\n");
			printNodeRecursive(docTree);
			
			fragmentTreeToRemove.remove();
			System.out.println("\n+OUT TREE (remove)+\n");
			printNodeRecursive(docTree);
	}
	
	private void moveFragment(Element fragmentTreeToMove, String newParentId, int newFragmentPosition) throws IOException {

		System.out.println("\n+IN TREE (move)+\n");
		printNodeRecursive(docTree);
		
		removeFragment(fragmentTreeToMove);
		addFragment(fragmentTreeToMove, newParentId, newFragmentPosition);
		
		System.out.println("\n+OUT TREE (move)+\n");
		printNodeRecursive(docTree);
		
	}
	
	private void updateFragment(Element newFragmentTree, String oldFragmentId) throws IOException {
			
			System.out.println("\n+IN TREE (update)+\n");
			printNodeRecursive(docTree);
			
			System.out.println("\n+FRAGMENT TREE (update)+\n");
			printNodeRecursive(newFragmentTree);
			
			Element oldFragmentTree = docTree.getElementById(oldFragmentId);
			oldFragmentTree.replaceWith(newFragmentTree);
			System.out.println("\n+OUT TREE (update)+\n");
			printNodeRecursive(docTree);
	}
	
	//DEBUGGING UTILITIES
		
	private void printNodeRecursive(Node n) {
		printNodeRecursive(n,"");
	}
	
	private void printNodeRecursive(Node n, String space) {
			
			if (n == null) return;
			
			else if (n instanceof Element) {
				System.out.println( space + n.nodeName() + " (" + n.attributes() + " )");
			}
	
			else if (n instanceof TextNode) {
				if (! ((TextNode)n).isBlank())	//is needed  otherwise spaces and newlines (by the indentation) would be considered
					System.out.println( space + n.nodeName() + " : " + n );
			}
			
			else {
				System.out.println( space + n.nodeName());
			}
			
			List<Node> children = n.childNodes();
			for(int i=0; i<children.size(); i++)
					printNodeRecursive(children.get(i), space + " - ");		
	}

}
