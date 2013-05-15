package com.cognizant.dml.text.annotator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.cas.Type;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.cleartk.ne.term.TermMatchAnnotationCreator;
import org.cleartk.ne.term.util.TermFinder;
import org.cleartk.ne.term.util.TermList;
import org.cleartk.ne.term.util.TermMatch;
import org.cleartk.ne.type.GazetteerNamedEntityMention;
import org.cleartk.token.tokenizer.PennTreebankTokenizer;
import org.cleartk.token.tokenizer.Token;
import org.cleartk.util.UIMAUtil;
import org.springframework.core.io.ClassPathResource;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;
import org.uimafit.factory.ConfigurationParameterFactory;
import org.uimafit.factory.initializable.InitializableFactory;

import com.cognizant.dml.text.annotator.util.StemTermFinder;
import com.cognizant.dml.text.annotator.util.TypedTermMatch;

public class GazetteerAnnotator extends JCasAnnotator_ImplBase {
  private static final Log log=LogFactory.getLog(GazetteerAnnotator.class);

  public static final String PARAM_TERM_LIST_FILE_NAMES_FILE_NAME = ConfigurationParameterFactory.createConfigurationParameterName(
		  GazetteerAnnotator.class,
      "termListFileNamesFileName");

  public static final String TERM_LIST_FILE_NAMES_FILE_NAME_DESCRIPTION = "Provides the name of a file that contains file names of term lists that are to be loaded. "
      + "Each line of the file should contain the name of a term list followed by the name of the file that contains the terms, a boolean ('true' or 'false')  "
      + "that indicates whether the file should be treated as case sensitive followed optionally by separator string to be used to separate  "
      + "an id from a term if the file contains ids. The values on each line should be tab delimited. ";

  @ConfigurationParameter(
      mandatory = true,
      description = TERM_LIST_FILE_NAMES_FILE_NAME_DESCRIPTION)
  public String termListFileNamesFileName;

  public static final String PARAM_TOKEN_CLASS_NAME = ConfigurationParameterFactory.createConfigurationParameterName(
		  GazetteerAnnotator.class,
      "tokenClassName");

  @ConfigurationParameter(
      mandatory = true,
      defaultValue = "org.cleartk.token.type.Token",
      description = "names the class of the type system type corresponding to tokens. ")
  private String tokenClassName;

  public static final String PARAM_TERM_MATCH_ANNOTATION_CREATOR_CLASS_NAME = ConfigurationParameterFactory.createConfigurationParameterName(
		  GazetteerAnnotator.class,
      "termMatchAnnotationCreatorClassName");

  @ConfigurationParameter(
      description = "provides the class name of a class that extends org.cleartk.ne.term.TermMatchAnnotationCreator. If this parameter is "
          + "not given a value, then the parameter 'termMatchAnnotationClassName'  must be given a value.")
  private String termMatchAnnotationCreatorClassName;

  public static final String PARAM_TERM_MATCH_ANNOTATION_CLASS_NAME = ConfigurationParameterFactory.createConfigurationParameterName(
		  GazetteerAnnotator.class,
      "termMatchAnnotationClassName");

  @ConfigurationParameter(
      defaultValue = "org.cleartk.ne.type.GazetteerNamedEntityMention",
      description = "names the class of the type system type that specifies the annotations "
          + "created of found term matches. One annotation is created for each term "
          + "match found of the given type specified by this parameter. This parameter is ignored if 'termMatchAnnotationCreatorClassName' is given a value.")
  private String termMatchAnnotationClassName;

  List<TermFinder> termFinderList=new ArrayList<TermFinder>();

  boolean allTokens = true;

  boolean typesInitialized = false;

  protected Class<? extends Annotation> sentenceClass;

  protected Type sentenceType;

  protected Class<? extends Annotation> tokenClass;

  protected Type tokenType;

  TermMatchAnnotationCreator annotationCreator;

  Constructor<? extends Annotation> annotationConstructor;

  @Override
  public void initialize(UimaContext context) throws ResourceInitializationException {
    try {
      super.initialize(context);

      // load the term lists
      ClassPathResource classpathTermList=new ClassPathResource(termListFileNamesFileName);
      BufferedReader input = new BufferedReader(new FileReader(classpathTermList.getFile()));
      String line;

      try {
        while ((line = input.readLine()) != null) {
          line = line.trim();
          if(line.startsWith("#")) //ignore comments
        	  continue;
          
          String[] columns = line.split("\t");
          String termListName = columns[0];
          String fileName = columns[1];
          boolean caseSensitive = Boolean.parseBoolean(columns[2]);
          String wordNormalization = columns.length >= 4 ? columns[3] : "ORIG";
          String separator = columns.length >= 5 ? columns[4] : null;

          TermFinder termFinder=new StemTermFinder(termListName, caseSensitive, wordNormalization, new PennTreebankTokenizer());

          ClassPathResource classpathTermsFile=new ClassPathResource(fileName);
          TermList termList = TermList.loadSimpleFile(termListName, classpathTermsFile.getFile(), separator);
          log.info("Loaded Gazette: "+termListName+", from: "+fileName);
          termFinder.addTermList(termList);
          termFinderList.add(termFinder);
        }
      } finally {
        input.close();
      }

      tokenClass = InitializableFactory.getClass(tokenClassName, Annotation.class);

      if (termMatchAnnotationCreatorClassName != null
          && !termMatchAnnotationCreatorClassName.equals("")) {
        annotationCreator = InitializableFactory.create(
            context,
            termMatchAnnotationCreatorClassName,
            TermMatchAnnotationCreator.class);
      } else {
        Class<? extends Annotation> annotationClass = InitializableFactory.getClass(
            termMatchAnnotationClassName,
            Annotation.class);
        annotationConstructor = annotationClass.getConstructor(new Class[] {
            JCas.class,
            java.lang.Integer.TYPE,
            java.lang.Integer.TYPE});
      }
    } catch (Exception e) {
      throw new ResourceInitializationException(e);
    }
  }

  private void initializeTypes(JCas jCas) {
    if (!allTokens)
      sentenceType = UIMAUtil.getCasType(jCas, sentenceClass);
    tokenType = UIMAUtil.getCasType(jCas, tokenClass);
    typesInitialized = true;
  }

  private Token createToken(Annotation annotation) {
    return new Token(annotation.getBegin(), annotation.getEnd(), annotation.getCoveredText());
  }

  @Override
  public void process(JCas jCas) throws AnalysisEngineProcessException {
    if (!typesInitialized)
      initializeTypes(jCas);

    List<Token> tokens = new ArrayList<Token>();
    if (allTokens) {
      FSIterator<Annotation> tokenAnnotations = jCas.getAnnotationIndex(tokenType).iterator();
      while (tokenAnnotations.hasNext()) {
        Annotation tokenAnnotation = tokenAnnotations.next();
        tokens.add(createToken(tokenAnnotation));
      }
      findTerms(jCas, tokens);
    } 
    else {
      FSIterator<Annotation> sentenceAnnotations = jCas.getAnnotationIndex(sentenceType).iterator();
      while (sentenceAnnotations.hasNext()) {
        tokens.clear();
        Annotation sentenceAnnotation = sentenceAnnotations.next();
        FSIterator<Annotation> tokenAnnotations = jCas.getAnnotationIndex(tokenType).subiterator(
            sentenceAnnotation);
        while (tokenAnnotations.hasNext()) {
          Annotation tokenAnnotation = tokenAnnotations.next();
          tokens.add(createToken(tokenAnnotation));
        }
        findTerms(jCas, tokens);
      }
    }
  }

  public void findTerms(JCas jCas, List<Token> tokens) throws AnalysisEngineProcessException {
    for(TermFinder termFinder:termFinderList) {
      findTerms(jCas, tokens, termFinder);
    }
  }

  public void findTerms(JCas jCas, List<Token> tokens, TermFinder termFinder) throws AnalysisEngineProcessException {
    List<TermMatch> termMatches = termFinder.getMatches(tokens);
    for (TermMatch termMatch : termMatches) {
      if (annotationCreator != null) {
        annotationCreator.createTermMatchAnnotation(jCas, termMatch);
      } 
      else {
        try {
          int begin = termMatch.getBegin();
          int end = termMatch.getEnd();
          //Annotation annotation = annotationConstructor.newInstance(new Object[] { jCas, begin, end });
          //TODO: Hardcoding Annotation
          //NamedEntity ne=new NamedEntity(jCas);
          //ne.setEntityType(((TypedTermMatch)termMatch).getType());
          //ne.setEntityId(termMatch.getTerm().getTermText());

          GazetteerNamedEntityMention nem = new GazetteerNamedEntityMention(jCas,begin,end);
          nem.setMentionType(((TypedTermMatch)termMatch).getType());
          //nem.setMentionedEntity(ne);
          nem.setMentionId(termMatch.getTerm().getTermText());
          nem.addToIndexes();
          //TODO: add mentionType annotation
          //TODO: add mentionEntity annotation 
        } catch (Exception e) {
          throw new AnalysisEngineProcessException(e);
        }
      }

    }
  }

  public void setTermListFileNamesFileName(String termListFileNamesFileName) {
    this.termListFileNamesFileName = termListFileNamesFileName;
  }

  public void setTokenClassName(String tokenClassName) {
    this.tokenClassName = tokenClassName;
  }

  public void setTermMatchAnnotationCreatorClassName(String termMatchAnnotationCreatorClassName) {
    this.termMatchAnnotationCreatorClassName = termMatchAnnotationCreatorClassName;
  }

  public void setTermMatchAnnotationClassName(String termMatchAnnotationClassName) {
    this.termMatchAnnotationClassName = termMatchAnnotationClassName;
  }

}
