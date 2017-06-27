import edu.duke.*;

public class Scratch {
    final String fileName = "brca1line.fa";

    public int findStopCodon(String dna, int startIndex, String stopCodon) {
	int currIndex = dna.indexOf(stopCodon, startIndex);
	if (currIndex % 3 == 0) {
	    return currIndex;
	}
	while (currIndex % 3 != 0) {
	    if (currIndex == -1) {
		break;
	    }
	    currIndex++;
	    currIndex = dna.indexOf(stopCodon, currIndex); //
	    if (currIndex % 3 == 0) {
		return currIndex;
	    }

	}
	return dna.length();
    }

    public String findGene(String dna) {
	int startPosition = dna.indexOf("ATG");
	if (dna.indexOf("ATG") == -1) {
	    return "";
	} // End if no valid startPosition

	int taaIndex = findStopCodon(dna, startPosition, "TAA");
	int tagIndex = findStopCodon(dna, startPosition, "TAG");
	int tgaIndex = findStopCodon(dna, startPosition, "TGA");

	int tempMinIndex = Math.min(taaIndex, tagIndex);
	int minIndex = Math.min(tgaIndex, tempMinIndex);

	if (minIndex == dna.length()) {
	    return "";
	}
	String validGene = dna.substring(startPosition, minIndex + 3);
	return validGene;
    } // end findGene

    public StorageResource getAllGenes(String dna) {
	StorageResource geneList = new StorageResource();
	int count = 0;
	while (true) {
	    String validGene = findGene(dna);
	    if (dna.length() == 0) {
		break;
	    }
	    geneList.add(validGene);
	    count++;
	    dna = dna.substring(dna.indexOf(validGene) + 1);
	} // end while
	System.out.println("Number of genes is: " + count);
	return geneList;	
    } // end getAllGenes

    public static int countOccurrences(String haystack, char needle) {
	int count = 0;
	for (int i = 0; i < haystack.length(); i++) {
	    if (haystack.charAt(i) == needle) {
		count++;
	    }
	}
	return count;
    }// end countOccurrences

    public float cgRatio(String dna) {
	float countC = countOccurrences(dna, 'C');
	float countG = countOccurrences(dna, 'G');
	float len = dna.length();
	float cgRatio = (countC + countG) / len;
	return cgRatio;
    } // end cgRatio

    /*
     * Write a method countCTG that has one String parameter dna and returns the
     * number of times the codon CTG appears in dna.
     */
    public int countCTG(String dna) {
	int ctgCount = 0;
	int startIndex = 0;
	int currentIndex = dna.indexOf("CTG");
	if (currentIndex != -1) {
	    while (true) {
		if (currentIndex == -1 || dna.length() == 0) {
		    // System.out.println("Breaking");
		    break;
		}
		ctgCount++;
		startIndex = dna.indexOf("CTG", startIndex + 1);
		dna = dna.substring(dna.indexOf("CTG") + 3);
	    }
	}
	return ctgCount;
    } // end countCTG

    public void testOn(String dna) {
	System.out.println("Testing getAllGense on " + dna);
	StorageResource genes = getAllGenes(dna);
	for (String g : genes.data()) {
	    System.out.println(g);
	}
    } // end testOn

    public void test() {
	// ATGv TAAv ATG v v TGA
	// testOn("ATGATCTAATTTATGCTGCAACGGTGAAGA");
	// testOn("");
	// ATGv v v v TAAv v v ATGTAA
	// testOn("ATGATCATAAGAAGATAATAGAGGGCCATGTAA");

	float ratio = cgRatio("ATGATAGGG");
	System.out.println("The ratio is: " + ratio + " Cs to Gs");

	int ctgCount = countCTG("CTGZZZCSEGGGGTAACTGCTZCTDCTBCTGZZZCTGCTG");
	System.out.println("The number of CTG codons in the dna is: " + ctgCount);
    } // end test

    public void processGenes(StorageResource sr) {
	int maxLen = 0;
	int lenCount = 0;
	float cgRat = 0;

	// Print number of genes greater than 60 in length
	for (String s : sr.data()) {
	    if (s.length() > 60) {
		lenCount++;
		// System.out.println("Gene greater than 60 in length: " + s);
	    } // end if
	    if (s.length() > maxLen) {
		maxLen = s.length();
	    } // end if
	} // end for loop
	System.out.println("The number of genes > 60 in length: " + lenCount);
	System.out.println("The longest gene in sr is: " + maxLen);

	// print genes with C/G ratio of greater than 0.35	
	for (String s : sr.data()) {
	    if (cgRatio(s) > .35) {
		System.out.println("s currently is: " + s);
		cgRat = cgRatio(s);
		System.out.println("C/G ratio is: " + cgRat + " s is: " + s);
		//System.out.println("genes w/cgRatio > 0.35: " + s);
	    } // end if	    
	} // end for loop
	


	// print the number of genes with C/G ratio > 0.35
	int count = 0;
	for (String s : sr.data()) {
	    if (cgRatio(s) > .35) {
		count++;
	    } // end if
	} // end for loop
	System.out.println("The number of genes with C/G ratio > 0.35: " + count);

	// ++print number of Strings in sr > 9 characters
	// ++print Strings in sr if (C-G-ratio > 0.35)
	// ++print number of strings in sr C-G-ratio > 0.35
	// ++print the length of the longest gene in sr

    } // end processGenes

    public void testProcessGenes() {
	System.out.println("\ntestProcessGenes called, begin tests:\n");
	
	StorageResource sr = new StorageResource();
	/*
	sr.add("ATGGTAATTTGTTGATT");
	sr.add("ATGGGGTAA");
	sr.add("ATGCCCTGGTAA");
	sr.add("ATGTATTGA");
	sr.add("ATGATAGGG");
	*/
	FileResource fr = new FileResource("brca1line.fa");
	String dna = fr.asString();
	dna = dna.toUpperCase();
	sr = getAllGenes(dna);

	processGenes(sr);

	/*
	 * call processGenes method on different test cases. Think of five DNA
	 * strings to use as test cases. These should include: 1) one DNA string
	 * that has some genes longer than 9 characters 2) one DNA string that
	 * has no genes longer than 9 characters 3) one DNA string that has some
	 * genes whose C-G-ratio is higher than 0.35 4) one DNA string that has
	 * some genes whose C-G-ratio is lower than 0.35
	 * 
	 * Write code in testProcessGenes to call processGenes five times with
	 * StorageResources made from each of your five DNA string test cases.
	 */
	System.out.println("\ntestProcessGenes testing compete");
    } // testProcessGenes

    public static void main(String[] args) {
	Scratch s = new Scratch();
	//s.test();
	s.testProcessGenes();

    } // end main
} // end class scratch