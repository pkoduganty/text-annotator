package com.cognizant.dml.text.annotator.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.cleartk.ne.term.util.Term;
import org.cleartk.ne.term.util.TermFinder;
import org.cleartk.ne.term.util.TermList;
import org.cleartk.ne.term.util.TermMatch;
import org.cleartk.token.tokenizer.PennTreebankTokenizer;
import org.cleartk.token.tokenizer.Token;
import org.cleartk.token.tokenizer.Tokenizer;
import org.springframework.core.io.ClassPathResource;
import org.tartarus.snowball.SnowballProgram;
import org.tartarus.snowball.ext.EnglishStemmer;

import uk.ac.shef.dcs.oak.jate.util.control.Lemmatizer;

import com.googlecode.clearnlp.constituent.CTLibEn;
import com.googlecode.clearnlp.engine.EngineGetter;
import com.googlecode.clearnlp.morphology.AbstractMPAnalyzer;
import com.googlecode.clearnlp.reader.AbstractReader;

import dragon.nlp.tool.lemmatiser.EngLemmatiser;

/**
 * <br>
 * Copyright (c) 2007-2008, Regents of the University of Colorado <br>
 * All rights reserved.
 * 
 * <p>
 * 
 * This class provides a very simple and fast term finder. It performs a simple left to right term
 * matching doing a single pass through the text token by token. It finds only exact matches where
 * the terms are found in the text exactly as they appear (with the option of case insensitive
 * matching) in the term list.
 * 
 * I performed a very simple test with throughput and got the following results:
 * <ul>
 * <li>loaded 2 million terms in 10.182 seconds using TermList.loadSimpleFile()
 * <li>initialize instance of SimpleTermFinder with list by calling addTermList(). This took 96.416
 * seconds.
 * <li>A file containing 495,503 tokens was read in line by line (about 4000 lines) and tokenized.
 * Time spent tokenizing was 8.567 seconds.
 * <li>Each line was passed into the term finder getMatches() method one after the next after being
 * tokenized. 8,633 term matches were found in the 495,503 tokens in .243 seconds.
 * </ul>
 * This experiment was run on a dual-core 1.99GHz Pentium. The code for this experiment can be found
 * in SimpleTermFinderTests.testTime()
 * 
 * @author Philip Ogren
 * 
 */
public class StemTermFinder implements TermFinder {
  String name;
  Tokenizer tokenizer;

  boolean caseSensitive=true;
  boolean stem=false;
  boolean lemma=false;

  SnowballProgram stemmer=new EnglishStemmer();
  Lemmatizer morph=new Lemmatizer();
  //AbstractMPAnalyzer morph;

  Node topNode;

  public StemTermFinder(String name, boolean caseSensitive, String wordNormalization, Tokenizer tokenizer) {
	this.name=name;
    this.caseSensitive = caseSensitive;
    
    if ("STEM".equalsIgnoreCase(wordNormalization))
    	this.stem=true;
    else if ("LEMMA".equalsIgnoreCase(wordNormalization))
    	this.lemma=true;
    
    this.tokenizer = tokenizer;

/*    try {
        ClassPathResource morphModel=new ClassPathResource("com/cognizant/dml/text/annotator/models/clearnlp-dictionary-1.2.0.zip");
		morph = EngineGetter.getMPAnalyzer(AbstractReader.LANG_EN, morphModel.getInputStream());
	} 
    catch (IOException e) {
		e.printStackTrace();
	}
*/
    topNode = new Node();
  }

  public StemTermFinder() {
    this("", true, "ORIG", new PennTreebankTokenizer());
  }

  public void addTermList(TermList termList) {
    for (Term entry : termList.getTerms()) {

      List<Token> tokens = tokenizer.getTokens(entry.getTermText());

      if (!caseSensitive) {
        List<Token> lowerTokens = new ArrayList<Token>(tokens.size());
        for (Token token : tokens) {
          lowerTokens.add(new Token(token.getBegin(), token.getEnd(), token
              .getTokenText()
              .toLowerCase()));
        }
        tokens = lowerTokens;
      }
      
      if(stem) {
    	  List<Token> stemTokens = new ArrayList<Token>(tokens.size());
    	  for (Token token : tokens) {
    	      stemmer.setCurrent(new String(token.getTokenText()));
    	      stemmer.stem();
    	      String stemmed=new String(stemmer.getCurrent());
    	      
    	      stemTokens.add(new Token(token.getBegin(), token.getBegin()+stemmed.length(), stemmed));
          }
          tokens = stemTokens;
   	  }
      else if (lemma){ //if not stem then lemma
    	  List<Token> lemmaTokens = new ArrayList<Token>(tokens.size());
    	  for (Token token : tokens) {
    		  //String lemma=morph.getLemma(token.getTokenText(), CTLibEn.POS_NN);
    		  String lemma=morph.getLemma(token.getTokenText(), null);
    	      lemmaTokens.add(new Token(token.getBegin(), token.getBegin()+lemma.length(), lemma));
          }
          tokens = lemmaTokens;    	  
      }
      else { //original form, dont do anything
      }

      Node node = topNode;
      for (Token token : tokens) {
        if (!node.containsChild(token)) {
          node.addChild(token);
        }
        node = node.getChild(token);
      }
      node.addEntry(entry);
    }
  }

  private List<TermMatch> getMatches(Token token, LinkedList<Candidate> candidates) {
    List<TermMatch> returnValues = new ArrayList<TermMatch>();

    String tokenText = token.getTokenText();
    if (!caseSensitive)
      tokenText = tokenText.toLowerCase();

    if (stem) {
      stemmer.setCurrent(new String(token.getTokenText()));
      stemmer.stem();
      tokenText=new String(stemmer.getCurrent());    	
    }
    else { //lemma
	  tokenText=morph.getLemma(token.getTokenText(), CTLibEn.POS_NN);    	      
    }
        
    if (topNode.containsChild(token)) {
      candidates.add(new Candidate());
    }

    ListIterator<Candidate> candidatesIterator = candidates.listIterator();
    while (candidatesIterator.hasNext()) {
      Candidate candidate = candidatesIterator.next();
      if (candidate.node.containsChild(token)) {
        candidate.addToken(token);
        Node childNode = candidate.node.getChild(token);

        List<Term> entries = childNode.entries;
        if (entries != null) {
          for (Term entry : entries) {
            returnValues.add(createTermMatch(candidate.tokens, entry));
          }
        }
        if (childNode.hasChildren())
          candidate.node = childNode;
        else
          candidatesIterator.remove();
      } else {
        candidatesIterator.remove();
      }
    }
    return returnValues;
  }

  /**
   * @param tokens
   *          It is the responsibility of the caller of this method to be sure that the same
   *          tokenizer is used to create the tokens passed in that is passed into the constructor.
   * @see #StemTermFinder(boolean, Tokenizer)
   */
  public List<TermMatch> getMatches(List<Token> tokens) {
    LinkedList<Candidate> candidates = new LinkedList<Candidate>();
    List<TermMatch> returnValues = new ArrayList<TermMatch>();

    for (Token token : tokens) {
      returnValues.addAll(getMatches(token, candidates));
    }
    return returnValues;
  }

  private TermMatch createTermMatch(List<Token> tokens, Term entry) {
	    if (tokens == null || tokens.size() == 0)
	      return null;

	    int begin = tokens.get(0).getBegin();
	    int end = tokens.get(tokens.size() - 1).getEnd();
	    return new TypedTermMatch(begin, end, entry, this.name);
  }

  private class Candidate {
    List<Token> tokens;

    Node node;

    Candidate() {
      tokens = new ArrayList<Token>();
      node = topNode;
    }

    public void addToken(Token token) {
      tokens.add(token);
    }
  }

  class Node {
    List<Term> entries;

    Map<String, Node> children;

    public boolean containsChild(Token child) {
      if (children == null) {
        return false;
      } else
        return children.containsKey(child.getTokenText());
    }

    public boolean hasChildren() {
      if (children == null)
        return false;
      else if (children.size() == 0)
        return false;
      else
        return true;
    }

    public void addChild(Token child) {
      if (children == null) {
        children = new HashMap<String, Node>();
      }
      children.put(child.getTokenText(), new Node());
    }

    public Node getChild(Token child) {
      if (children == null || !children.containsKey(child.getTokenText())) {
        return null;
      }
      return children.get(child.getTokenText());
    }

    public void addEntry(Term entry) {
      if (entries == null)
        entries = new ArrayList<Term>();
      entries.add(entry);
    }

  }
}
