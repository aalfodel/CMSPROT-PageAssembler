import java.io.File;
import java.io.IOException;

public class PageAssemblerTest {
	
	public static void main(String[] args) throws IOException{
		
		String basePath 		= "/home/aalfodel/git/CMSPROT-PageAssembler/CMSPROT-PageAssembler/html/";
		String inPath1 			= basePath + "mainPage1.html";
		String inPath2 			= basePath + "mainPage2.html";
		String fragmentPath1 	= basePath + "fragmentPage1.html";
		String fragmentPath2	= basePath + "fragmentPage2.html";
		String fragmentPath3 	= basePath + "fragmentPage3.html";
		String outPath1 		= basePath + "outputPage1.html";
		String outPath2 		= basePath + "outputPage2.html";
		String outPath3 		= basePath + "outputPage3.html";
		String outPath4 		= basePath + "outputPage4.html";
		String outPath5 		= basePath + "outputPage5.html";
		
		
		

		System.out.print("\n ## -------MULTIPLE STEPS TESTS------- ## \n");
		
		System.out.print("\n-----Add test-----\n");
		PageAssembler paAdd1 = new PageAssembler(new File(inPath1), new File(outPath1));
		paAdd1.addFragment(new File(fragmentPath1), "father", 1);
		
		System.out.print("\n-----Add test 2-----\n");
		PageAssembler paAdd2 = new PageAssembler(new File(outPath1), new File(outPath2));
		paAdd2.addFragment(new File(fragmentPath2), "father", 1);
		
		System.out.print("\n-----Remove test-----\n");
		PageAssembler paRemove = new PageAssembler(new File(outPath2), new File(outPath3));
		paRemove.removeFragment("1");
		
		System.out.print("\n-----Move test-----\n");
		PageAssembler paMove = new PageAssembler(new File(outPath3), new File(outPath4));
		paMove.moveFragment("2", "father", 0);
		
		System.out.print("\n-----Update test-----\n");
		PageAssembler paUpdate = new PageAssembler(new File(outPath4), new File(outPath5));
		paUpdate.updateFragment(new File(fragmentPath3), "2");
		
		
		
		
		System.out.print("\n ## -------SINGLE STEP TEST------- ## \n");
		
		File f = new File(inPath2);
		
		System.out.print("\n-----Add test-----\n");
		PageAssembler pa2Add1 = new PageAssembler(f);
		pa2Add1.addFragment(new File(fragmentPath1), "father", 1);
		
		System.out.print("\n-----Add test 2-----\n");
		PageAssembler pa2Add2 = new PageAssembler(f);
		pa2Add2.addFragment(new File(fragmentPath2), "father", 1);
		
		System.out.print("\n-----Remove test-----\n");
		PageAssembler pa2Remove = new PageAssembler(f);
		pa2Remove.removeFragment("1");
		
		System.out.print("\n-----Move test-----\n");
		PageAssembler pa2Move = new PageAssembler(f);
		pa2Move.moveFragment("2", "father", 0);
		
		System.out.print("\n-----Update test-----\n");
		PageAssembler pa2Update = new PageAssembler(f);
		pa2Update.updateFragment(new File(fragmentPath3), "2");
		 
	}

}
