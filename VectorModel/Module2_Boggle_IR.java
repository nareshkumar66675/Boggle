import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Pushkar Singh Negi
 * Student Id: 2946319
 * Created Date: 23 April 2018
 * Modified Date: 3 May 2018
 * Information Retrieval Final Project: Boogle ( Module 2: Indexing, Vector Space Model and Term proximity)
 *
 */
public class Module2_Boogle_IR {

	
	
	
	public final static void main(String[] args) throws FileNotFoundException, IOException
	{
		
	
	String line;

	try {
	
		
		long lStartTime = System.currentTimeMillis();
		
		//String tempFileName="document_";
		
		File folder = new File("C:\\Users\\Sony\\Desktop\\Google Drive Sync\\workspaces_IR\\18_march_ir\\test\\parsedDocs");
        File[] listOfFiles = folder.listFiles();
System.out.println("listOfFiles---"+listOfFiles.length);
		
		String tempFileName="Doc_";
		TreeSet <String> listUniqueTokens = new TreeSet <String>(); // to store the unique tokens in all the documents
		
		/*Map<String, Integer> collected 
		= Arrays.stream(str)
		        .collect(groupingBy(Function.identity(), 
		                    collectingAndThen(counting(), Long::intValue)));*/
		
		/***************** START code to find unique tokens(word from) all the documents*****************/
		int intNoOfDocuments=listOfFiles.length; // change to total number of file generated dynamically
	//	int intNoOfDocuments=5; // change to total number of file generated dynamically
		LinkedHashMap<String, TreeSet <String>> lhmEachDocUniqueToken=new LinkedHashMap<String, TreeSet <String>>(); // list that contains eaach document and its respective token list
		
		LinkedHashMap<String, ArrayList<String>> lhmEachDocAllTokenForProximity=new LinkedHashMap<String, ArrayList <String>>();
		// lhm that contains each document and all the words(only alphabets) in it, will be used in term proximity 
		
		for(int i=1;i<=intNoOfDocuments;i++)
		{
		
			TreeSet <String> listForEachDocUniqueToken = new TreeSet <String>();
			ArrayList<String> listForEachDocAllWordsProxTemp=new ArrayList<String>();
	    InputStream fis = new FileInputStream("C:\\Users\\Sony\\Desktop\\Google Drive Sync\\workspaces_IR\\18_march_ir\\test\\parsedDocs\\"+tempFileName+ String.valueOf(i)+".htm"); // change the file name 
	    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
	    BufferedReader br = new BufferedReader(isr);
	    
	    
	    
	  /*  while ((line = br.readLine()) != null) {
	       // System.out.println(line);
	        listUniqueTokens.add(line);// this listUniqueTokens contains unique tokens from all the documents
	    }*/
	    
	    while ((line = br.readLine()) != null) {
		      //  System.out.println("line---"+line);
		        //listUniqueTokens.add(line);// this listUniqueTokens contains unique tokens from all the documents
	    	String[] strSpaceSeparated = line.split("\\s+");  
	    	String regex = "^[a-zA-Z]+$";
	    	Pattern pattern = Pattern.compile(regex);
	    //	Matcher matcher = pattern.matcher(name);
	    //	Matcher m=pattern.matcher(st);
	    	for (String strTemp : strSpaceSeparated) {
	    		
	    		
	    		if(strTemp.length()<50 && strTemp.matches(regex))
	    		{
	    		listUniqueTokens.add(strTemp);// this listUniqueTokens contains unique tokens from all the documents
	    		listForEachDocUniqueToken.add(strTemp);//contains unique terms(tokens) for each doc, to optimize the code
	    		
	    		listForEachDocAllWordsProxTemp.add(strTemp);//contains all terms(words) for each doc, for term proximity
	    		}
	    		}
	    
	    }
	    
	  /*  while ((line = br.readLine()) != null)
        {
            // you need only this for loop in you code.
            for (String value : line.split(" ")) {  
                 if(!value.equals(""))             // ignore space                      
                	 listUniqueTokens.add(value);  // add to list
            }

        }*/
	    
	    lhmEachDocUniqueToken.put(tempFileName+ String.valueOf(i)+".htm", listForEachDocUniqueToken);
	    lhmEachDocAllTokenForProximity.put(tempFileName+ String.valueOf(i)+".htm", listForEachDocAllWordsProxTemp);
	    
		}
		System.out.println("List of unique Tokens--->");
		System.out.println(listUniqueTokens);
		System.out.println("List of unique tokens SIZE is---"+listUniqueTokens.size());
		
		System.out.println("New list for each doc unique token list data structure---"+lhmEachDocUniqueToken); 
		
		 for (Map.Entry<String, TreeSet <String>> entry : lhmEachDocUniqueToken.entrySet()) {
		        System.out.println(entry.getKey()+" : "+entry.getValue());
		    }
		
		/***************** END  code to find unique tokens(word from) all the documents*****************/
		/* Iterator<String> itr = listUniqueTokens.iterator();
		while (itr.hasNext()) { 
	    	System.out.println(itr.next()); }*/

		 /**********Start code to find the term proximity for each word in each document---lhmEachDocUniqueToken & lhmEachDocAllTokenForProximity --contains documents and respective tokens list**********/
		 
		 LinkedHashMap<String, LinkedHashMap<String,List<Integer>>> lhmTermProximity=new LinkedHashMap<String, LinkedHashMap<String,List<Integer>>>();
		 			//Doc Number             //words, index where it occurs
		 /*for (Map.Entry<String, ArrayList <String>> entry : lhmEachDocAllTokenForProximity.entrySet()) {
			 
			 
		        System.out.println(entry.getKey()+" : "+entry.getValue());
		    }*/
		 
		 
		 for(int i=1;i<=intNoOfDocuments;i++)
			{
		
			 ArrayList<String> listTempAllWordsInDoc=lhmEachDocAllTokenForProximity.get(tempFileName+ String.valueOf(i)+".htm");// list of all words in this doc;
			 
			 System.out.println("listTempAllWordsInDoc---");
			 /*for(String strTemp:listTempAllWordsInDoc)
			 {
				 System.out.println(strTemp);
			 }*/
			 
			 
			 TreeSet <String> listTempUniqueTokenInDoc = lhmEachDocUniqueToken.get(tempFileName+ String.valueOf(i)+".htm");// list of unique tokens iin this doc
			 LinkedHashMap<String,List<Integer>> lhmTemp=new LinkedHashMap<String,List<Integer>>();
			 for(String strTempUniqueToken:listTempUniqueTokenInDoc)
			 {
				 
				 List<Integer> allIndexes =
					        IntStream.range(0, listTempAllWordsInDoc.size()).boxed()
					                .filter(j -> listTempAllWordsInDoc.get(j).equals(strTempUniqueToken))
					                .collect(Collectors.toList());
				 
				 for(int k=0;k<allIndexes.size();k++)
				 {
					 allIndexes.set(k, allIndexes.get(k)+1);
				 }
				 
				 
				 
				 lhmTemp.put(strTempUniqueToken, allIndexes) ;
			 }
			 
			 lhmTermProximity.put(tempFileName+ String.valueOf(i)+".htm", lhmTemp) ;
			}
		 
		 System.out.println("lhmTermProximity-----");
		 for (Map.Entry<String, LinkedHashMap<String,List<Integer>>> entry : lhmTermProximity.entrySet()) {
			 
			 
		        System.out.println(entry.getKey()+" : "+entry.getValue());
		    }
		 /**********END code to find the term proximity for each word in each document---lhmEachDocUniqueToken--contains documents and respective tokens list**********/
		 
		/***************** START  code to find DF and TF(inverted index) for unique tokens(word from) all the documents*****************/
		 
		    int int_term_freq=0;
		    int int_doc_freq=0;
		    
		    LinkedHashMap<String, LinkedHashMap<String,String>> hmToken=new LinkedHashMap<String, LinkedHashMap<String,String>>(); // stores the inverted index matrix
		    LinkedHashMap<String, String> hmDocIdTF=null;
		    
LinkedHashMap<String,String> lhmDocumentFrequencyToken=new LinkedHashMap<String,String>();// to store the doc freq of terms, i.e. in how many docs particular terms occurs
		    int counter=0;
		    for (String temp : listUniqueTokens) {
		    	
		    	counter+=1;
		    	int_term_freq=0;
		    	int_doc_freq=0;
		    	hmDocIdTF=new LinkedHashMap<String, String>();  	
		for(int i=1;i<=intNoOfDocuments;i++)
		{   
			
			/*if(lhmEachDocUniqueToken.get(tempFileName+ String.valueOf(i)+".htm").contains(temp))  // added so that only those documents get traversed that have the unique token in their list in this lhm
			{*/
			int_term_freq=0;
		
	    InputStream fis = new FileInputStream("C:\\Users\\Sony\\Desktop\\Google Drive Sync\\workspaces_IR\\18_march_ir\\test\\parsedDocs\\"+tempFileName+ String.valueOf(i)+".htm"); // here change the file name to dynamic... 2nd place
	    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
	    BufferedReader br = new BufferedReader(isr);
		
	//	Scanner file=new Scanner("tempFileName+ String.valueOf(i)"+".htm");
		
	   // System.out.println(temp);	    
	  //while(file.hasNext())	
	    			while ((line = br.readLine()) != null)
	    		
		{	    		//String word=	file.next();	
	    				//System.out.println(line);
	    				
	    				/*if(temp.equals(line))
	    					{
	    						int_term_freq++;	    						
	    					}*/
	    				
	    				if(line.contains(temp)) //latest one with 20 docs and 87 secs
	    				{
	    				String[] strSpaceSeparated = line.split("\\s+");  
	    		    	for (String strTemp : strSpaceSeparated) {
	    		    		if(temp.equals(strTemp))
	    					{
	    						int_term_freq++;	    						
	    					}
	    		    	}
	    				}
	    				/*Pattern p = Pattern.compile(temp);
	    				Matcher m = p.matcher( line );
	    				while (m.find()) {
	    					int_term_freq++;
	    				}*/
	    				//int_term_freq=	(int) (Pattern.compile(temp).splitAsStream(line).count()-1);
	    				
	    			//	int_term_freq=		(int) Arrays.stream(line.split("[ ,\\.]")).filter(s -> s.equals(temp)).count();			
	    				
	    		    	/*	if(temp.equals(file.next()))
		{
			int_term_freq++;	    						
		}*/
		
	    			}
	    			
	    			if(int_term_freq>0)
					{int_doc_freq++;
					hmDocIdTF.put(String.valueOf(i), String.valueOf(int_term_freq) );
					}
	    			hmToken.put(temp, hmDocIdTF);	
		//	}	
	    }
		System.out.println("counter---"+counter);
		lhmDocumentFrequencyToken.put(temp, String.valueOf(int_doc_freq));
		}
		    
		    long lEndTimeInvertedIndex = System.currentTimeMillis();
		    
		    System.out.println("time took to form inverted index matrix---"+(lEndTimeInvertedIndex-lStartTime));
		    System.out.println("\n Inverted Index matrix----> \n");
		    
		    for (Map.Entry<String, LinkedHashMap<String,String>> entry : hmToken.entrySet()) {
		        System.out.println(entry.getKey()+" : "+entry.getValue());
		    }
		    System.out.println("\nDocument Frequency of tokens--->\n");
		    System.out.println(lhmDocumentFrequencyToken);
		    
		    
		    
		    
		   // PrintWriter writer = new PrintWriter("C:\\Users\\Sony\\Desktop\\Google Drive Sync\\workspaces_IR\\invertedIndexFile.txt", "UTF-8");
		    
		    
		    BufferedWriter output = null;
		    File file = new File("C:\\Users\\Sony\\Desktop\\Google Drive Sync\\workspaces_IR\\18_march_ir\\test\\outputFiles\\invertedIndexFile.txt");
   		 
	            output = new BufferedWriter(new PrintWriter(file));
		    
	            for (Map.Entry<String, LinkedHashMap<String,String>> entry : hmToken.entrySet()) {
			    	
	            	output.write(entry.getKey()+" : "+entry.getValue());
	            	output.newLine();
			    }
	            output.close();
	            
	            InputStream fis = new FileInputStream("C:\\Users\\Sony\\Desktop\\Google Drive Sync\\workspaces_IR\\18_march_ir\\test\\outputFiles\\invertedIndexFile.txt"); // here change the file name to dynamic... 2nd place
	    	    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
	    	    BufferedReader br = new BufferedReader(isr);
	    		
	   LinkedHashMap<String, LinkedHashMap<String,String>> hmTokenBuiltFromText=new LinkedHashMap<String, LinkedHashMap<String,String>>(); // stores the inverted index matrix
	   LinkedHashMap<String,String> hmTempPostingList=new  LinkedHashMap<String,String>();		
	   
	   long lReadTimeInvertedIndexText = System.currentTimeMillis();
	   
	    		while ((line = br.readLine()) != null)
	    	    		
	    		{	    
	    			
	    			hmTempPostingList=new  LinkedHashMap<String,String>();			
	    	    				String[] strKeyValue = line.trim().split(":");
	    	    				//so strKeyValue[0] wil contain the key i.e. the term and value will contain the posting list
	    	    				
	    	    				String[] strKeyValuepostingList = strKeyValue[1].trim().substring(1	, strKeyValue[1].trim().length()-1).split(",");
	    	    				// 1=1, 8=3, 9=1, 13=1, so each element of strKeyValuepostingList will contain 1=1, 8=3, 9=1, and 13=1
	    	    				
	    	    				for(String strTemp:strKeyValuepostingList)
	    	    				{
	    	    					
	    	    					String[] strTempKeyValue=strTemp.trim().split("=");
	    	    					hmTempPostingList.put(strTempKeyValue[0], strTempKeyValue[1]);
	    	    				}
	    	    				
	    	    				hmTokenBuiltFromText.put(strKeyValue[0], hmTempPostingList)		;
	    		}
	    		
	    		long lReadEndTimeInvertedIndexText = System.currentTimeMillis();
	    		
			    System.out.println("\n\n\n\n\ntime took to form inverted index matrix from text file---"+(lReadEndTimeInvertedIndexText-lReadTimeInvertedIndexText));
			    System.out.println("size is------------------"+hmTokenBuiltFromText.size());
 for (Map.Entry<String, LinkedHashMap<String,String>> entry : hmTokenBuiltFromText.entrySet()) {
			    	
	            	System.out.println(entry.getKey()+" : "+entry.getValue());
	            	
			    }
 
 System.out.println("\n\n\n\n");
	    
		/***************** END  code to find DF and TF(inverted index) for unique tokens(word from) all the documents*****************/
	    
		/***************** START  code to find IDF =log10(N/df)*****************/
		    
		    LinkedHashMap<String,String> lhmInvertedDocumentFrequencyToken=new LinkedHashMap<String,String>();    
		    //to store the term and related IDF in the LinkedHashMap  lhmInvertedDocumentFrequencyToken
		    System.out.println("\nIDF for the terms are----\n");
		    for (String temp : listUniqueTokens) {
		    	
		    	double tempIDF=Math.log10(intNoOfDocuments/ Double.parseDouble(lhmDocumentFrequencyToken.get(temp)));	
		    	double tempIDF2= Math.round(tempIDF*1000)/1000.00d;
		    lhmInvertedDocumentFrequencyToken.put(temp, String.valueOf(tempIDF2));	
		    	
		    }
		    
		    System.out.println(lhmInvertedDocumentFrequencyToken); // write it to file
		    
		    
		    BufferedWriter output1 = null;
		    File file1 = new File("C:\\Users\\Sony\\Desktop\\Google Drive Sync\\workspaces_IR\\18_march_ir\\test\\outputFiles\\term_IDF.txt");
   		 
	            output1 = new BufferedWriter(new PrintWriter(file1));
	            
	            for (Map.Entry<String,String> entry : lhmInvertedDocumentFrequencyToken.entrySet()) {
			    	
	            	output1.write(entry.getKey()+" : "+entry.getValue());
	            	output1.newLine();
			    }
	            output1.close();
		/***************** END  code to find IDF =log10(N/df)*****************/    
	    
		/***************** START  code to find weight of each terms and to form document verctors *****************/
		
		LinkedHashMap<String, LinkedHashMap<String,Double>> lhmDocumentVectors=new LinkedHashMap<String, LinkedHashMap<String,Double>>(); 
		LinkedHashMap<String, LinkedHashMap<String,String>> lhmDocumentVectorsTermProx=new LinkedHashMap<String, LinkedHashMap<String,String>>(); 
	//the key will store the document ids and the value that is LHM<String,String> will store the term and respective weight of the term
		
		LinkedHashMap<String,Double> lhmTermWeight=new  LinkedHashMap<String,Double>() ;
		LinkedHashMap<String,String> lhmTermWeightTermProx=new  LinkedHashMap<String,String>() ;
		LinkedHashMap<String,Double> lhmTempQueryVectros=new  LinkedHashMap<String,Double>() ;
		
		
		int intTempTermFreq=0;
		/*so it will be like document_id will have hashmap that will have 'a' weight, 'arrived' weight , 'damaged' weight and son on for all the tokens*/ 
		Boolean flaglhmTempQueryVectros=true;
		for(int i=1;i<=intNoOfDocuments;i++)
		{
			lhmTermWeight=new  LinkedHashMap<String,Double>() ;
			lhmTermWeightTermProx=new  LinkedHashMap<String,String>() ;
			for (String temp : listUniqueTokens) {
				
				//lhmInvertedDocumentFrequencyToken.get(temp); // to fetch the IDF of that term from the linked hash map
				
				if(hmToken.get(temp).containsKey(String.valueOf(i)))// check whether the inverted index contains the doc no or not
				{
					intTempTermFreq=Integer.valueOf(hmToken.get(temp).get(String.valueOf(i)));//if yes,then inverted index contains the doc no so fetch the term frequency
				}
				else
				{
					intTempTermFreq=0;
				}
				
				double tempWeight= Math.round(Double.valueOf(lhmInvertedDocumentFrequencyToken.get(temp))*intTempTermFreq*1000)/1000.00d;
				StringBuilder sbTempTermWeightTermProx=new StringBuilder();
				
				if(!(tempWeight==0.0))  // inserting non zero weights in document vector only
				{
					sbTempTermWeightTermProx.append( String.valueOf(tempWeight)).append("|").append(lhmTermProximity.get(tempFileName+ String.valueOf(i)+".htm").
							get(temp).toString().substring(1, lhmTermProximity.get(tempFileName+ String.valueOf(i)+".htm").get(temp).toString().length()-1));
				lhmTermWeight.put(temp, (tempWeight));
				lhmTermWeightTermProx.put(temp, sbTempTermWeightTermProx.toString());
				
				}
				if(flaglhmTempQueryVectros) {
				lhmTempQueryVectros.put(temp, 0.0);
				}
				
			}
			
			
			
			flaglhmTempQueryVectros=false;
			lhmDocumentVectors.put(tempFileName+ String.valueOf(i)+".htm", lhmTermWeight)	; // change the name here 3rd place
			lhmDocumentVectorsTermProx.put(tempFileName+ String.valueOf(i)+".htm", lhmTermWeightTermProx)	; // change the name here 3rd place
			
			
		}
		//System.out.println("size of lhmDocumentVectors--->"+lhmDocumentVectors.size());
		 
		System.out.println("\nDocument vectors---->\n");
		
		
		    for (Map.Entry<String, LinkedHashMap<String,Double>> entry2 : lhmDocumentVectors.entrySet()) {
		        System.out.println(entry2.getKey()+" : "+entry2.getValue());
		    }
		    
		    System.out.println("\nDocument vectors with proximity---->\n");
			
		  //  Doc_1.htm : {affair=0.778|1666, afternoon=0.778|1703,
		    
		    BufferedWriter output111 = null;
		    File file111 = new File("C:\\Users\\Sony\\Desktop\\Google Drive Sync\\workspaces_IR\\18_march_ir\\test\\outputFiles\\docVector_tfIDF_termProximityWithSemiColon.txt");
   		 
	            output111 = new BufferedWriter(new PrintWriter(file111));
	            
	            
	            
		    for (Map.Entry<String, LinkedHashMap<String,String>> entry2 : lhmDocumentVectorsTermProx.entrySet()) {
		    	
		    	StringBuilder sbTemp=new StringBuilder();
		    	sbTemp.append(entry2.getKey()+" : "+ "{");
		    	for (Map.Entry<String,String> entry22 : entry2.getValue().entrySet()) {
		    		
		    		sbTemp.append(entry22.getKey()).append("=").append(entry22.getValue()).append(" ; ");
		    		
		    	}
		    	sbTemp.append("}");
		        System.out.println(sbTemp.toString());
		        output111.write(sbTemp.toString());
            	output111.newLine();
		    }
		    output111.close();
		    
		    BufferedWriter output11 = null;
		    File file11 = new File("C:\\Users\\Sony\\Desktop\\Google Drive Sync\\workspaces_IR\\18_march_ir\\test\\outputFiles\\docVector_tfIDF.txt");
   		 
	            output11 = new BufferedWriter(new PrintWriter(file11));
	            
	            for (Map.Entry<String, LinkedHashMap<String,Double>> entry : lhmDocumentVectors.entrySet()) {
			    	
	            	output11.write(entry.getKey()+" : "+entry.getValue());
	            	output11.newLine();
			    }
	            output11.close();
	            
	            
	            BufferedWriter output1111 = null;
			    File file1111 = new File("C:\\Users\\Sony\\Desktop\\Google Drive Sync\\workspaces_IR\\18_march_ir\\test\\outputFiles\\docVector_tfIDF_termProximity.txt");
	   		 
		            output1111 = new BufferedWriter(new PrintWriter(file1111));
		            
		            for (Map.Entry<String, LinkedHashMap<String,String>> entry : lhmDocumentVectorsTermProx.entrySet()) {
				    	
		            	output1111.write(entry.getKey()+" : "+entry.getValue());
		            	output1111.newLine();
				    }
		            output1111.close();
		    
		/***************** END  code to find weight of each terms and to form document verctors *****************/
		  
		/***************** START  code to find length of the document verctors *****************/
		    
		    LinkedHashMap<String,Double> lhmDocumentVectorLength=new  LinkedHashMap<String,Double>() ; // store document name and correspnding length
		    
		    double doubleSumOfSquares=0.0;
		    
		    for (Map.Entry<String, LinkedHashMap<String,Double>> entry : lhmDocumentVectors.entrySet()) {
		    	doubleSumOfSquares=0.0;
		    	
		    	 
		    	 for (Map.Entry<String, Double> entry1 : entry.getValue().entrySet()) {
		    	 
		    		 doubleSumOfSquares+= entry1.getValue()*entry1.getValue();
		    }
		    	 double tempRoundOffLength= Math.round(Math.sqrt(doubleSumOfSquares)*1000)/1000.00d;
		    	 lhmDocumentVectorLength.put(entry.getKey(),tempRoundOffLength);
		    	 
	}
		    System.out.println("\n Length of Document vectors---->\n");
		   // Doc_1.htm : {aayog=1.255|1,2,3;abcclio=0.285|4,5,6;abhinav=2.26|7,8,9;abid=1.13|10,11,12}
		    for (Map.Entry<String, Double> entry2 : lhmDocumentVectorLength.entrySet()) {
		        System.out.println(entry2.getKey()+" : "+entry2.getValue());
		    }
		    
		    BufferedWriter output2 = null;
		    File file2 = new File("C:\\Users\\Sony\\Desktop\\Google Drive Sync\\workspaces_IR\\18_march_ir\\test\\outputFiles\\docVector_Length.txt");
   		 
		    output2 = new BufferedWriter(new PrintWriter(file2));
	            
	            for (Map.Entry<String, Double> entry : lhmDocumentVectorLength.entrySet()) {
			    	
	            	output2.write(entry.getKey()+" : "+entry.getValue());
	            	output2.newLine();
			    }
	            output2.close();
		    
		    /***************** END  code to find length of the document vectors *****************/
		    
		    /***************** START  code to find  documents that contains query terms*****************/
		    
		    String query_1="arthur atlanta";
		   
		    String[] strQueryTokensList = query_1.trim().split("\\s+");
		    
		    LinkedHashMap<String,ArrayList<String>> lhmCandidateDocsContainsQueryTerm=new LinkedHashMap<String,ArrayList<String>>();
		    //lhmCandidateDocsContainsQueryTerm will contain query term and respective list of docs in which it appears
		    //hmToken contains the inverted index matrix, i.e. dictionary and posting list: unique terms and in how many docs ,how many times information
		    
		    ArrayList<String> listCandidateDocQueryToken=new ArrayList<String>();
		    Set<String> setCandDocContainsQueryTerms=new HashSet<>(); // used in calculating cosing sim between doc and query, in order to traverse the whole candidate doc
		    for(String tempToken:strQueryTokensList) // parsing through the individual query terms
		    {
		    	listCandidateDocQueryToken=new ArrayList<String>();
		    	if(hmToken.containsKey(tempToken)) //check in our inverted index whether query token exist in dictionary or nor, if yes then proceed else no processing
		    	{
		    		listCandidateDocQueryToken.addAll(hmToken.get(tempToken).keySet());
		    		lhmCandidateDocsContainsQueryTerm.put(tempToken, listCandidateDocQueryToken);
		    		
		    		setCandDocContainsQueryTerms.addAll(hmToken.get(tempToken).keySet());
		    	}
		    	
		    }
		    
		    System.out.println("\n query tokens and list of docs that contains the query tokens---->\n");
		    
		    System.out.println("********setCandDocContainsQueryTerms*************"+setCandDocContainsQueryTerms);
		    
		    if(lhmCandidateDocsContainsQueryTerm.size()>0)
		    {
		    for (Map.Entry<String,ArrayList<String>> entry : lhmCandidateDocsContainsQueryTerm.entrySet()) {
		        System.out.println(entry.getKey()+" : "+entry.getValue());
		    }
		    }
		    else
		    {
		    	System.out.println("!!!!!!!!!!!None of the documents contains the query terms!!!!!!!!!!!");
		    }
		    /***************** END  code to find  documents that contains query terms*****************/
		    
		    /***************** START  code to find  term frequency(TF) of the query terms in query*****************/
		    
		    LinkedHashMap<String,Integer> lhmQueryTokenTF=new LinkedHashMap<String,Integer>();//to store the frequency of terms in the QUERY
		    //key=string will store the token and value=string will contain the occurances
		    	    
		    for (String strQueryToken: strQueryTokensList) {
		        if (lhmQueryTokenTF.containsKey(strQueryToken)) {
		            // Map already contains the word key. Just increment it's count by 1
		        	lhmQueryTokenTF.put(strQueryToken, lhmQueryTokenTF.get(strQueryToken) + 1);
		        } else {
		            // Map doesn't have mapping for word. Add one with count = 1
		        	lhmQueryTokenTF.put(strQueryToken, 1);
		        }
		    }
		    
		    System.out.println("\n TF value for query vector is---->\n");
		    
		    for (Map.Entry<String, Integer> entry2 : lhmQueryTokenTF.entrySet()) {
		    	
		        System.out.println(entry2.getKey()+" : "+entry2.getValue());
		    }
		    
		    /***************** END  code to find  term frequency(TF) of the query terms in query*****************/  
		    
		    
		    /***************** START  code to find  the query vectors*****************/
		    
		    //LinkedHashMap<String, LinkedHashMap<String,Double>> lhmDocumentVectors=new LinkedHashMap<String, LinkedHashMap<String,Double>>();
		    LinkedHashMap<String, Double> lhmQueryVectors=new LinkedHashMap<String, Double>();
		    
		    if(lhmCandidateDocsContainsQueryTerm.size()>0) // check that whether query terms occurs in any doc or not, if yes then will create query vectors else not
		    {
		        if(lhmTempQueryVectros.size()>0) 
		        {
		    lhmQueryVectors=(lhmTempQueryVectros); 
		        }
		    
		        for (Map.Entry<String, Integer> entry2 : lhmQueryTokenTF.entrySet()) {
			    	
		        	if(lhmInvertedDocumentFrequencyToken.containsKey(entry2.getKey()))
		        	{
		 lhmQueryVectors.put(entry2.getKey(),entry2.getValue() * Double.parseDouble( lhmInvertedDocumentFrequencyToken.get(entry2.getKey())) );
			    }
		        }
		    
		    }
		    
		    System.out.println("\n query vectors---->\n");
		    
		    for (Map.Entry<String, Double> entry2 : lhmQueryVectors.entrySet()) {
		    	
		        System.out.println(entry2.getKey()+" : "+entry2.getValue());
		    }
		    ///***************** END  code to find  the query vectors*****************/
		    
		    /***************** START  code to find  the length of the query vector*****************/
		    double doubleSumOfSquaresQuery=0.0;
		    
		    
		    for (Double val : lhmQueryVectors.values()){
		    	
		    	if(val!=0.0)
		    	{
		    	doubleSumOfSquaresQuery += val*val;
		    }
		    }
		    double doubRoundOffQueryLength= Math.round(Math.sqrt(doubleSumOfSquaresQuery)*1000)/1000.00d;		    
    
    	
    	 System.out.println("\n Length of query vector is ----\n"+doubRoundOffQueryLength);
		    
    	 /***************** END  code to find  the length of the query vector*****************/
    	 
    	 /***************** START  code to find  cosine similarity between the query vector and candidate document*****************/
    	 
    	 LinkedHashMap<String,Double> lhmCosineSimCandDocAndQuery=new LinkedHashMap<String,Double>();
    	 
    	 if(lhmCandidateDocsContainsQueryTerm.size()>0) //i.e. there exist candidate documents that contains query terms
		    {
    		// lhmCandidateDocsContainsQueryTerm
    		 
    		for(String tempDocID: setCandDocContainsQueryTerms) // to traverse only the candidate docs for the query
    		{
    		Double doubTemp=0.0; // to store the sum ofproduct of weight of document vector and query vector 
    		Double doubDocLengthXQueryLength=0.0;
    		Double DoubTempProd=0.0;
    		// loop to traverse only candidate docs,i.e. in which the query terms occurs( for rest it will be orthogonal)
    			
    		 for (Map.Entry<String,Double> entry : lhmQueryVectors.entrySet()) {//traversing the query vectors,each element
    			 
    			 if(entry.getValue()!=0.0) //only concerned about those whose value is non 0.0
  		    	{
    				 doubTemp+=( lhmDocumentVectors.get(tempFileName+ tempDocID+".htm").get(entry.getKey()))*(entry.getValue()); // change here name
    				
    				 // to fetch the weight from candidate document for the token of query term whose weight!=0
    				// doubDocLengthXQueryLength=	 (lhmDocumentVectorLength.get(tempFileName+ tempDocID+".htm"))*doubRoundOffQueryLength;
  		    	}
    		} 
    		 doubDocLengthXQueryLength= 		(lhmDocumentVectorLength.get(tempFileName+ tempDocID+".htm"))*doubRoundOffQueryLength;
    		 DoubTempProd=doubTemp/doubDocLengthXQueryLength;
    		 
    		  lhmCosineSimCandDocAndQuery.put("sim(D"+ tempDocID+",Q)",(Math.round(DoubTempProd*1000)/1000.00d));
    		}
    			}
    	 
    	 System.out.println("\n Cosine Similarity between candidate doc and query vector  ----\n");
    	
    	 System.out.println(lhmCosineSimCandDocAndQuery);
    	 
    	 /***************** END  code to find  cosine similarity between the query vector and candidate document*****************/
    	 
    	 /***************** START  code to RANK(sort) the candidate document according to the cosim simalarity*****************/
    	 
    	 Set<Entry<String, Double>> setRankDoc = lhmCosineSimCandDocAndQuery.entrySet();
    	 
    	 List<Entry<String, Double>> listRankDoc = new ArrayList<Entry<String, Double>>(setRankDoc);
         Collections.sort( listRankDoc, new Comparator<Map.Entry<String, Double>>()
         {
             public int compare( Map.Entry<String, Double> object1, Map.Entry<String, Double> object2 )
             {
                 return (object2.getValue()).compareTo( object1.getValue() );
             }
         } );
         
         System.out.println("\nDocument Ranking----\n");
         
         for(Map.Entry<String, Double> entry:listRankDoc){
             System.out.println(entry.getKey()+" ==== "+entry.getValue());
         }
 		long lEndTime = System.currentTimeMillis();
 		
 		System.out.println("total time taken---"+(lEndTime-lStartTime));
         /***************** END  code to RANK(sort) the candidate document according to the cosim simalarity*****************/
		   /* for (Map.Entry<String,ArrayList<String>> entry : lhmCandidateDocsContainsQueryTerm.entrySet()) {
		        System.out.println(entry.getKey()+" : "+entry.getValue());
		    }*/
		       	 
	}
	catch(Exception e)
	{
		e.printStackTrace();
	}
	   
	}
	}

