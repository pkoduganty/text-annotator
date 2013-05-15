package com.cognizant.dml.text.annotator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.FSIndex;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.CasCreationUtils;
import org.cleartk.ne.term.TermMatchAnnotationCreator;
import org.cleartk.ne.term.util.SimpleTermFinder;
import org.cleartk.ne.term.util.TermFinder;
import org.cleartk.ne.term.util.TermList;
import org.cleartk.ne.term.util.TermMatch;
import org.cleartk.ne.type.Chunk;
import org.cleartk.token.tokenizer.PennTreebankTokenizer;
import org.cleartk.token.tokenizer.Token;
import org.springframework.core.io.ClassPathResource;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;
import org.uimafit.factory.AggregateBuilder;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.factory.ConfigurationParameterFactory;
import org.uimafit.factory.TypeSystemDescriptionFactory;
import org.uimafit.factory.initializable.InitializableFactory;
import org.uimafit.pipeline.SimplePipeline;

import uk.ac.shef.dcs.oak.jate.JATEException;
import uk.ac.shef.dcs.oak.jate.core.algorithm.AbstractFeatureWrapper;
import uk.ac.shef.dcs.oak.jate.core.algorithm.Algorithm;
import uk.ac.shef.dcs.oak.jate.core.algorithm.CValueAlgorithm;
import uk.ac.shef.dcs.oak.jate.core.algorithm.CValueFeatureWrapper;
import uk.ac.shef.dcs.oak.jate.core.algorithm.TFIDFAlgorithm;
import uk.ac.shef.dcs.oak.jate.core.algorithm.TFIDFFeatureWrapper;
import uk.ac.shef.dcs.oak.jate.core.algorithm.TermExAlgorithm;
import uk.ac.shef.dcs.oak.jate.core.algorithm.TermExFeatureWrapper;
import uk.ac.shef.dcs.oak.jate.core.algorithm.WeirdnessAlgorithm;
import uk.ac.shef.dcs.oak.jate.core.algorithm.WeirdnessFeatureWrapper;
import uk.ac.shef.dcs.oak.jate.core.feature.FeatureBuilderCorpusTermFrequencyMultiThread;
import uk.ac.shef.dcs.oak.jate.core.feature.FeatureBuilderDocumentTermFrequencyMultiThread;
import uk.ac.shef.dcs.oak.jate.core.feature.FeatureBuilderRefCorpusTermFrequency;
import uk.ac.shef.dcs.oak.jate.core.feature.FeatureBuilderTermNest;
import uk.ac.shef.dcs.oak.jate.core.feature.FeatureBuilderTermNestMultiThread;
import uk.ac.shef.dcs.oak.jate.core.feature.FeatureCorpusTermFrequency;
import uk.ac.shef.dcs.oak.jate.core.feature.FeatureDocumentTermFrequency;
import uk.ac.shef.dcs.oak.jate.core.feature.FeatureRefCorpusTermFrequency;
import uk.ac.shef.dcs.oak.jate.core.feature.FeatureTermNest;
import uk.ac.shef.dcs.oak.jate.core.feature.indexer.GlobalIndex;
import uk.ac.shef.dcs.oak.jate.core.feature.indexer.GlobalIndexBuilder;
import uk.ac.shef.dcs.oak.jate.core.feature.indexer.GlobalIndexBuilderMem;
import uk.ac.shef.dcs.oak.jate.core.npextractor.CandidateTermExtractor;
import uk.ac.shef.dcs.oak.jate.core.npextractor.ChunkExtractorOpenNLP;
import uk.ac.shef.dcs.oak.jate.core.npextractor.NGramExtractor;
import uk.ac.shef.dcs.oak.jate.core.npextractor.NounPhraseExtractorOpenNLP;
import uk.ac.shef.dcs.oak.jate.core.npextractor.WordExtractor;
import uk.ac.shef.dcs.oak.jate.io.ResultWriter2File;
import uk.ac.shef.dcs.oak.jate.model.Corpus;
import uk.ac.shef.dcs.oak.jate.model.CorpusImpl;
import uk.ac.shef.dcs.oak.jate.model.Document;
import uk.ac.shef.dcs.oak.jate.model.GazetteCorpusImpl;
import uk.ac.shef.dcs.oak.jate.model.Term;
import uk.ac.shef.dcs.oak.jate.util.control.Lemmatizer;
import uk.ac.shef.dcs.oak.jate.util.control.Normalizer;
import uk.ac.shef.dcs.oak.jate.util.control.StopList;
import uk.ac.shef.dcs.oak.jate.util.counter.TermFreqCounter;
import uk.ac.shef.dcs.oak.jate.util.counter.WordCounter;

import com.cognizant.dml.jate.model.SolrCorpus;
import com.cognizant.dml.jate.model.StringDocument;
import com.cognizant.dml.text.annotator.type.JATETermChunk;
import com.cognizant.dml.text.annotator.util.StemTermFinder;

public class JATEAnnotator extends JCasAnnotator_ImplBase {
  private static final Log log=LogFactory.getLog(JATEAnnotator.class);

  private static final int CVALUE_WT_INDEX=1, WEIRD_WT_INDEX=2, TFIDF_WT_INDEX=0, TERMEX_WT_INDEX=3;
  public static final String TERM_EXTRACTOR_WORD="word", TERM_EXTRACTOR_NP="np", TERM_EXTRACTOR_CHUNK="chunk", TERM_EXTRACTOR_NGRAM="ngram";
  
  private static final String WordDocIndexFileName = "JateWordDocIndex.model";
  private static final String TermDocIndexFileName = "JateTermDocIndex.model";

  private static final String FeatureCorpusWordFrequencyFileName = "JateFeatureCorpusWordFrequency.model";
  private static final String FeatureDocumentTermFrequencyFileName = "JateFeatureDocumentTermFrequency.model";
  private static final String FeatureCorpusTermFrequencyFileName = "JateFeatureCorpusTermFrequency.model";
  private static final String FeatureTermNestFileName = "JateFeatureTermNest.model";
  private static final String FeatureRefCorpusTermFrequencyFileName = "JateFeatureRefCorpusTermFrequency.model";
  private static final String LocalPrebuiltTermIndexFileName = "LocalPrebuiltTermIndex.model";

  public static final String PARAM_CORPUS_DIRECTORY = ConfigurationParameterFactory.createConfigurationParameterName(
		  JATEAnnotator.class, "corpusDirectory");
  public static final String PARAM_CORPUS_DIRECTORY_DESCRIPTION = "Path to filesystem directory that contains documents";
  @ConfigurationParameter(
      description = PARAM_CORPUS_DIRECTORY_DESCRIPTION)
  public String[] corpusDirectory;
    
  public static final String PARAM_SOLR_CORPUS_DIRECTORY = ConfigurationParameterFactory.createConfigurationParameterName(
		  JATEAnnotator.class, "solrCorpusDirectory");
  public static final String PARAM_SOLR_CORPUS_DIRECTORY_DESCRIPTION = "Path to Solr Index that contains documents";
  @ConfigurationParameter(
      description = PARAM_SOLR_CORPUS_DIRECTORY_DESCRIPTION)
  public String[] solrCorpusDirectory;

  public static final String PARAM_GAZETTE_CORPUS_DIRECTORY = ConfigurationParameterFactory.createConfigurationParameterName(
		  JATEAnnotator.class, "gazetteCorpusDirectory");
  public static final String PARAM_GAZETTE_CORPUS_DIRECTORY_DESCRIPTION = "Path to Gazette corpus";
  @ConfigurationParameter(
      description = PARAM_GAZETTE_CORPUS_DIRECTORY_DESCRIPTION)
  public String[] gazetteCorpusDirectory;

  public static final String PARAM_SOLR_ID_FIELD_NAME = ConfigurationParameterFactory.createConfigurationParameterName(
		JATEAnnotator.class,
      "solrIdFieldName");
  public static final String PARAM_SOLR_ID_FIELD_NAME_DESCRIPTION = "Document identifier field in Solr Index";
  @ConfigurationParameter(
      description = PARAM_SOLR_ID_FIELD_NAME_DESCRIPTION)
  public String solrIdFieldName;

  public static final String PARAM_SOLR_TEXT_BOOST_FACTOR = ConfigurationParameterFactory.createConfigurationParameterName(
		JATEAnnotator.class,
      "solrTextBoostFactor");
  public static final String PARAM_SOLR_TEXT_BOOST_FACTOR_DESCRIPTION = "Boost factor for Solr Field Text, to ensure high frequency";
  @ConfigurationParameter(
      description = PARAM_SOLR_TEXT_BOOST_FACTOR_DESCRIPTION)
  public String solrTextBoostFactor="5";
  private int solrBoostFactor;

  public static final String PARAM_SOLR_TEXT_FIELD_NAME = ConfigurationParameterFactory.createConfigurationParameterName(
		JATEAnnotator.class,
      "solrTextFieldName");
  public static final String PARAM_SOLR_TEXT_FIELD_NAME_DESCRIPTION = "Document text field in Solr Index";
  @ConfigurationParameter(
      description = PARAM_SOLR_TEXT_FIELD_NAME_DESCRIPTION)
  public String solrTextFieldName;

  public static final String PARAM_REF_CORPUS_FILE_NAME = ConfigurationParameterFactory.createConfigurationParameterName(
		JATEAnnotator.class,
      "refCorpusFileName");
  public static final String PARAM_REF_CORPUS_FILE_NAME_DESCRIPTION = "Provides the name of a file that contains " +
  		"term/word distributions over a reference corpus like BNC(British National Corpus)..";
  @ConfigurationParameter(
      mandatory = true,
      description = PARAM_REF_CORPUS_FILE_NAME_DESCRIPTION)
  public String refCorpusFileName;
  
  public static final String PARAM_CONFIDENCE_THRESHOLD = ConfigurationParameterFactory.createConfigurationParameterName(
		JATEAnnotator.class,
      "confidenceThreshold");
  public static final String PARAM_CONFIDENCE_THRESHOLD_DESCRIPTION = "confidence threshold, ignore terms with confidence less than threshold";
  @ConfigurationParameter(
      mandatory=true,
      description = PARAM_CONFIDENCE_THRESHOLD_DESCRIPTION)
  public String confidenceThreshold;

  public static final String PARAM_JATE_WEIGHTS = ConfigurationParameterFactory.createConfigurationParameterName(
		  JATEAnnotator.class, "jateWeights");
  public static final String PARAM_JATE_WEIGHTS_DESCRIPTION = "float weights for TFIDF, CValue, Weirdness, TermEx";
  @ConfigurationParameter(
      mandatory=true,
      description = PARAM_JATE_WEIGHTS_DESCRIPTION)
  public String[] jateWeights;
  public double[] weights=new double[4];

  public static final String PARAM_TERM_EXTRACTOR_TYPE = ConfigurationParameterFactory.createConfigurationParameterName(
		JATEAnnotator.class,
      "termExtractorType");
  public static final String PARAM_TERM_EXTRACTOR_TYPE_DESCRIPTION = "Type of Term Extractor - word, ngram, np, chunker";
  @ConfigurationParameter(
      mandatory=true,
      description = PARAM_TERM_EXTRACTOR_TYPE_DESCRIPTION)
  public String termExtractorType;
  
  public static final String PARAM_TERM_MATCH_ANNOTATION_CLASS_NAME = ConfigurationParameterFactory.createConfigurationParameterName(
		  JATEAnnotator.class,
      "termMatchAnnotationClassName");
  @ConfigurationParameter(
      defaultValue = "com.cognizant.dml.text.annotator.type.JATETermChunk",
      description = "names the class of the type system type that specifies the annotations "
          + "created of found term matches. One annotation is created for each term "
          + "match found of the given type specified by this parameter.")
  private String termMatchAnnotationClassName;

  public static final String PARAM_JATE_REF_CORPUS_PATH = ConfigurationParameterFactory.createConfigurationParameterName(
		  JATEAnnotator.class,
      "jatePrebuiltRefCorpusPath");
  @ConfigurationParameter(
      description = "path of pre-computed reference corpus, creates if not exists")
  private String jatePrebuiltRefCorpusPath;
  private File jatePrebuiltRefCorpus;

  TermMatchAnnotationCreator annotationCreator;
  Constructor<? extends Annotation> annotationConstructor;
  
  private CandidateTermExtractor npextractor;
  private WordExtractor wordextractor;
  private Corpus corpus;
  private GlobalIndexBuilder indexBuilder;
  
  private GlobalIndex wordDocIndex;
  private GlobalIndex termDocIndex;
  private GlobalIndex localTermDocIndex;
  private FeatureCorpusTermFrequency wordCorpusFreq;
  private FeatureCorpusTermFrequency termCorpusFreq;
  private FeatureDocumentTermFrequency termDocFreq;
  private FeatureTermNest termNest;
  private FeatureRefCorpusTermFrequency bncRef;

  private Normalizer lemmatizer;
  private WordCounter wordcounter;

  @Override
  public void initialize(UimaContext context) throws ResourceInitializationException {
      super.initialize(context);

      try {
            Class<? extends Annotation> annotationClass = InitializableFactory.getClass(
                termMatchAnnotationClassName,
                Annotation.class);
            annotationConstructor = annotationClass.getConstructor(new Class[] { //JATETermChunk(JCas cas, int begin, int end)
                JCas.class,
                java.lang.Integer.TYPE,
                java.lang.Integer.TYPE});

            weights[CVALUE_WT_INDEX]=Double.parseDouble(jateWeights[CVALUE_WT_INDEX]);
            weights[TFIDF_WT_INDEX]=Double.parseDouble(jateWeights[TFIDF_WT_INDEX]);
            weights[TERMEX_WT_INDEX]=Double.parseDouble(jateWeights[TERMEX_WT_INDEX]);
            weights[WEIRD_WT_INDEX]=Double.parseDouble(jateWeights[WEIRD_WT_INDEX]);

            solrBoostFactor = Integer.parseInt(solrTextBoostFactor);
            //stop words and lemmatizer are used for processing the extraction of candidate terms
        	StopList stop = new StopList(false);
        	lemmatizer = new Lemmatizer();

        	//Four CandidateTermExtractor are implemented: check JATE
    	    //1. An OpenNLP noun phrase extractor that extracts noun phrases as candidate terms
    		//	CandidateTermExtractor npextractor = new NounPhraseExtractorOpenNLP(stop, lemmatizer);
    	    //2. A generic N-gram extractor that extracts n(default is 5, see the property file) grams
    	    //CandidateTermExtractor npextractor = new NGramExtractor(stop, lemmatizer);
    	    //3. A word extractor that extracts single words as candidate terms.
    	    //CandidateTermExtractor wordextractor = new WordExtractor(stop, lemmatizer);
    	    //4. A chunk extractor based on OpenNLP Chunker as candidate terms.
        	if(TERM_EXTRACTOR_WORD.equalsIgnoreCase(termExtractorType))
        		npextractor = new WordExtractor(stop, lemmatizer);
        	else if(TERM_EXTRACTOR_NGRAM.equalsIgnoreCase(termExtractorType))
        		npextractor = new NGramExtractor(stop, lemmatizer);
        	else if(TERM_EXTRACTOR_NP.equalsIgnoreCase(termExtractorType))
        		npextractor = new NounPhraseExtractorOpenNLP(stop, lemmatizer);
        	else if(TERM_EXTRACTOR_CHUNK.equalsIgnoreCase(termExtractorType))
        		npextractor = new ChunkExtractorOpenNLP(stop, lemmatizer);
        	
    	    //This instance of WordExtractor is needed to build word frequency data, which are required by some algorithms
    		wordextractor = new WordExtractor(stop, lemmatizer, true, 4);
    	    //A WordCounter instance is required to count number of words in corpora/documents
    	    wordcounter = new WordCounter();
    	    indexBuilder = new GlobalIndexBuilderMem();

    		if(jatePrebuiltRefCorpusPath!=null && jatePrebuiltRefCorpusPath.length()>0)
    			jatePrebuiltRefCorpus=new File(jatePrebuiltRefCorpusPath);
    		else
    			throw new IOException("Invalid reference corpus path");

    		//WORD-DOC-INDEX
    	    if(!(Arrays.asList(jatePrebuiltRefCorpus.list()).contains(termExtractorType+WordDocIndexFileName) &&
    	    	Arrays.asList(jatePrebuiltRefCorpus.list()).contains(termExtractorType+TermDocIndexFileName) &&
    	    	Arrays.asList(jatePrebuiltRefCorpus.list()).contains(termExtractorType+FeatureCorpusWordFrequencyFileName) &&
    	    	Arrays.asList(jatePrebuiltRefCorpus.list()).contains(termExtractorType+FeatureCorpusTermFrequencyFileName) &&
    	    	Arrays.asList(jatePrebuiltRefCorpus.list()).contains(termExtractorType+FeatureDocumentTermFrequencyFileName) &&
    	    	Arrays.asList(jatePrebuiltRefCorpus.list()).contains(termExtractorType+FeatureTermNestFileName) &&
    	    	Arrays.asList(jatePrebuiltRefCorpus.list()).contains(termExtractorType+FeatureRefCorpusTermFrequencyFileName)
    	    	)) 
        	{
	    		corpus=null;//construct a composite corpus containing solr index(es) and/or file system directories
	    	    int solrIndex=0, directoryIndex=0, gazetteIndex=0;
	    	    if(solrCorpusDirectory!=null && solrCorpusDirectory.length>0 && !solrCorpusDirectory[0].matches("\\s*"))
	    	    	corpus=new SolrCorpus(solrCorpusDirectory[solrIndex++], solrIdFieldName, solrTextFieldName, solrBoostFactor);
	    	    else if(gazetteCorpusDirectory!=null && gazetteCorpusDirectory.length>0 && !gazetteCorpusDirectory[0].matches("\\s*"))
	    	    	corpus=new GazetteCorpusImpl(gazetteCorpusDirectory[gazetteIndex++]);
	    	    else if(corpusDirectory!=null && corpusDirectory.length>0 && !corpusDirectory[0].matches("\\s*"))
	    	    	corpus=new CorpusImpl(corpusDirectory[directoryIndex++]);
	    	    
	    	    for(int i=directoryIndex;corpusDirectory!=null && i<corpusDirectory.length && !corpusDirectory[i].matches("\\s*");i++)
	    	    	corpus.add(new CorpusImpl(corpusDirectory[i]));
	    	    for(int i=solrIndex;solrCorpusDirectory!=null && i<solrCorpusDirectory.length && !solrCorpusDirectory[i].matches("\\s*");i++)
	    	    	corpus.add(new SolrCorpus(solrCorpusDirectory[i], solrIdFieldName, solrTextFieldName, solrBoostFactor));
	    	    for(int i=gazetteIndex;gazetteCorpusDirectory!=null && i<gazetteCorpusDirectory.length && !gazetteCorpusDirectory[i].matches("\\s*");i++)
	    	    	corpus.add(new GazetteCorpusImpl(gazetteCorpusDirectory[i]));
        	}
    	    
	    	//##########################################################
        	//#         Step 1. Extract candidate terms/words from     #
        	//#         documents, and index the terms/words, docs     #
        	//#         and their relations (occur-in, containing)     #
        	//##########################################################

		    //##########################################################
		    //#         Step 2. Build various statistical features     #
		    //#         used by term extraction algorithms. This will  #
		    //#         need the indexes built above, and counting the #
		    //#         frequencies of terms                           #
		    //##########################################################

    	    //WORD-DOC-INDEX
    	    if(Arrays.asList(jatePrebuiltRefCorpus.list()).contains(termExtractorType+WordDocIndexFileName)) 
    	    {
    	    	wordDocIndex=(GlobalIndex) SerializationUtils.deserialize(new FileInputStream(
    	    			jatePrebuiltRefCorpus.getAbsolutePath()+File.separator+termExtractorType+WordDocIndexFileName));
    	    }
    	    else {
    		    //Next we need to count frequencies of candidate terms. This is a computational extensive process
    		    // and can take long for large corpus.
    		    /* #2 */
    	    	wordDocIndex = indexBuilder.build(corpus, wordextractor);
    	    	OutputStream wordDocIndexFile=new FileOutputStream(
    	    			jatePrebuiltRefCorpus.getAbsolutePath()+File.separator+termExtractorType+WordDocIndexFileName);
    	    	SerializationUtils.serialize(wordDocIndex, wordDocIndexFile);
    	    	wordDocIndexFile.close();
    	    }

    	    //TERM-DOC-INDEX
    	    if(Arrays.asList(jatePrebuiltRefCorpus.list()).contains(termExtractorType+TermDocIndexFileName)) 
        	{
    	    	termDocIndex=(GlobalIndex) SerializationUtils.deserialize(new FileInputStream(
    	    			jatePrebuiltRefCorpus.getAbsolutePath()+File.separator+termExtractorType+TermDocIndexFileName));
        	}
    	    else {
    	    	termDocIndex = indexBuilder.build(corpus, npextractor);
    	    	OutputStream termFreqFile=new FileOutputStream(
    	    			jatePrebuiltRefCorpus.getAbsolutePath()+File.separator+termExtractorType+TermDocIndexFileName);
    	    	SerializationUtils.serialize(termDocIndex, termFreqFile);
    	    	termFreqFile.close();
    	    }
    	    
    	    //WORD-CORPUS-INDEX
    	    if(Arrays.asList(jatePrebuiltRefCorpus.list()).contains(termExtractorType+FeatureCorpusWordFrequencyFileName)) 
        	{
    	    	wordCorpusFreq=(FeatureCorpusTermFrequency)SerializationUtils.deserialize(new FileInputStream(
    	    			jatePrebuiltRefCorpus.getAbsolutePath()+File.separator+termExtractorType+FeatureCorpusWordFrequencyFileName));
        	}
    	    else {
    	    	wordCorpusFreq = new FeatureBuilderCorpusTermFrequencyMultiThread(wordcounter, lemmatizer).build(wordDocIndex);
    	    	OutputStream wordFreqFile=new FileOutputStream(
    	    			jatePrebuiltRefCorpus.getAbsolutePath()+File.separator+termExtractorType+FeatureCorpusWordFrequencyFileName);
    	    	SerializationUtils.serialize(wordCorpusFreq, wordFreqFile);
    	    	wordFreqFile.close();
    	    }

    	    //TERM-CORPUS-INDEX
       	    if(Arrays.asList(jatePrebuiltRefCorpus.list()).contains(termExtractorType+FeatureCorpusTermFrequencyFileName)) 
       	    {
    	    	termCorpusFreq=(FeatureCorpusTermFrequency)SerializationUtils.deserialize(new FileInputStream(
    	    			jatePrebuiltRefCorpus.getAbsolutePath()+File.separator+termExtractorType+FeatureCorpusTermFrequencyFileName));
       	    }
       	    else {
       	    	termCorpusFreq = new FeatureBuilderCorpusTermFrequencyMultiThread(wordcounter, lemmatizer).build(termDocIndex);
    	    	OutputStream termFreqFile=new FileOutputStream(
    	    			jatePrebuiltRefCorpus.getAbsolutePath()+File.separator+termExtractorType+FeatureCorpusTermFrequencyFileName);
    	    	SerializationUtils.serialize(termCorpusFreq, termFreqFile);
    	    	termFreqFile.close();
       	    }
       	    
    	    //TERM-DOC-INDEX
    	    if(Arrays.asList(jatePrebuiltRefCorpus.list()).contains(termExtractorType+FeatureDocumentTermFrequencyFileName)) 
        	{
    	    	termDocFreq=(FeatureDocumentTermFrequency)SerializationUtils.deserialize(new FileInputStream(
    	    			jatePrebuiltRefCorpus.getAbsolutePath()+File.separator+termExtractorType+FeatureDocumentTermFrequencyFileName));
        	}
    	    else {
    	    	termDocFreq = new FeatureBuilderDocumentTermFrequencyMultiThread(wordcounter, lemmatizer).build(termDocIndex);
    	    	OutputStream termFreqFile=new FileOutputStream(
    	    			jatePrebuiltRefCorpus.getAbsolutePath()+File.separator+termExtractorType+FeatureDocumentTermFrequencyFileName);
    	    	SerializationUtils.serialize(termDocFreq, termFreqFile);
    	    	termFreqFile.close();
    	    }

    	    //TERM-NEST-INDEX
    	    if(Arrays.asList(jatePrebuiltRefCorpus.list()).contains(termExtractorType+FeatureTermNestFileName)) 
        	{
    	    	termNest=(FeatureTermNest)SerializationUtils.deserialize(new FileInputStream(
    	    			jatePrebuiltRefCorpus.getAbsolutePath()+File.separator+termExtractorType+FeatureTermNestFileName));
        	}
    	    else {
    	    	termNest = new FeatureBuilderTermNestMultiThread().build(termDocIndex);
    	    	//termNest = new FeatureBuilderTermNest().build(termDocIndex);
    	    	OutputStream termNestFile=new FileOutputStream(
    	    			jatePrebuiltRefCorpus.getAbsolutePath()+File.separator+termExtractorType+FeatureTermNestFileName);
    	    	SerializationUtils.serialize(termNest, termNestFile);
    	    	termNestFile.close();
    	    }

    	    //REF-INDEX
    	    if(Arrays.asList(jatePrebuiltRefCorpus.list()).contains(termExtractorType+FeatureRefCorpusTermFrequencyFileName)) 
        	{
    	    	bncRef=(FeatureRefCorpusTermFrequency)SerializationUtils.deserialize(new FileInputStream(
    	    			jatePrebuiltRefCorpus.getAbsolutePath()+File.separator+termExtractorType+FeatureRefCorpusTermFrequencyFileName));
    	    }
    	    else
    	    {
    	    	ClassPathResource refCorpusFile=new ClassPathResource(refCorpusFileName);
    	    	bncRef = new FeatureBuilderRefCorpusTermFrequency(refCorpusFile.getFile().getPath()).build(null);
    	    	OutputStream bncRefFile=new FileOutputStream(
    	    			jatePrebuiltRefCorpus.getAbsolutePath()+File.separator+termExtractorType+FeatureRefCorpusTermFrequencyFileName);
    	    	SerializationUtils.serialize(bncRef, bncRefFile);
    	    	bncRefFile.close();
    	    }

    	    //CONCEPT-TERM-INDEX(SOLR)
    	    if(Arrays.asList(jatePrebuiltRefCorpus.list()).contains(termExtractorType+LocalPrebuiltTermIndexFileName)) 
        	{
    	    	localTermDocIndex=(GlobalIndex)SerializationUtils.deserialize(new FileInputStream(
    	    			jatePrebuiltRefCorpus.getAbsolutePath()+File.separator+termExtractorType+LocalPrebuiltTermIndexFileName));
    	    }
    	    else
    	    {
    	    	Corpus localCorpus=new CorpusImpl();
    	    	//adding solr terms to the foreground corpus
    	    	for(int i=0;solrCorpusDirectory!=null && i<solrCorpusDirectory.length;i++) {
    		    	localCorpus.add(new SolrCorpus(solrCorpusDirectory[i], solrIdFieldName, solrTextFieldName, solrBoostFactor));
    	    	}
    	    	
    	    	localTermDocIndex=indexBuilder.build(localCorpus, npextractor);
    	    	OutputStream localTermDocIndexFile=new FileOutputStream(
    	    			jatePrebuiltRefCorpus.getAbsolutePath()+File.separator+termExtractorType+LocalPrebuiltTermIndexFileName);
    	    	SerializationUtils.serialize(localTermDocIndex, localTermDocIndexFile);
    	    	localTermDocIndexFile.close();
    	    }
    	    
      } 
      catch (Exception e) {
          throw new ResourceInitializationException(e);
      }
  }
  
  public void logResult(GlobalIndex index, String algorithm, Map<Term, Double> result, String outFolder) throws JATEException, IOException {
	ResultWriter2File writer = new ResultWriter2File(index);
    writer.output(result, outFolder + File.separator + algorithm + ".txt");
  }


  @Override
  public void process(JCas jCas) throws AnalysisEngineProcessException {
    try {
    	Corpus textCorpus=new CorpusImpl();
    	//Add text to be annotated to the Global Indexes
    	Document text=new StringDocument(jCas.getDocumentText());
    	Set<Document> docs=new HashSet<Document>();
    	docs.add(text);
    	textCorpus.add(text);

    	indexBuilder.add(wordDocIndex, text, wordextractor);
	    indexBuilder.add(termDocIndex, text, npextractor);
    	indexBuilder.add(localTermDocIndex, textCorpus, npextractor);
    	
    	FeatureCorpusTermFrequency localTermCorpusFreq = 
    			new FeatureBuilderCorpusTermFrequencyMultiThread(wordcounter, lemmatizer).build(localTermDocIndex);
    	termDocFreq = new FeatureBuilderDocumentTermFrequencyMultiThread(wordcounter, lemmatizer).add(docs, termDocIndex);//TODO-check this
    	termCorpusFreq = new FeatureBuilderCorpusTermFrequencyMultiThread(wordcounter, lemmatizer).add(docs, termDocIndex);//TODO-check this

		Algorithm tfidf = new TFIDFAlgorithm();
		AbstractFeatureWrapper tfidfFeatures = new TFIDFFeatureWrapper(termDocFreq,localTermCorpusFreq);
	    log.info("Running TFIDF Feature store builder and ATR..." + tfidf.toString());
		Map<Term, Double> tfidfResult = tfidf.execute(tfidfFeatures);
		logResult(termDocIndex, tfidf.toString()+termExtractorType, tfidfResult, jatePrebuiltRefCorpus.getAbsolutePath());
		//Log results Sorted by Confidence
		//List<Term> tfidfResultList=Arrays.asList(tfidfResult);
		//Collections.sort(tfidfResultList, new Term.TermConfidenceComparator());
		//logResult(termDocIndex, tfidf.toString()+termExtractorType+".sorted", tfidfResultList.toArray(new Term[]{}), jatePrebuiltRefCorpus.getAbsolutePath());
		
    	//Normalized Candidate Term Confidence scoring
    	Algorithm cvalue = new CValueAlgorithm();
		AbstractFeatureWrapper cValueFeatures = new CValueFeatureWrapper(termCorpusFreq, termNest);
		log.info("Running CValueFeature store builder and ATR..." + cvalue.toString());
		Map<Term, Double> cValueResult = cvalue.execute(cValueFeatures);
		logResult(termDocIndex, cvalue.toString()+termExtractorType, cValueResult, jatePrebuiltRefCorpus.getAbsolutePath());
		//Log results Sorted by Confidence
		//List<Term> cValueResultList=Arrays.asList(cValueResult);
		//Collections.sort(cValueResultList, new Term.TermConfidenceComparator());
		//logResult(termDocIndex, cvalue.toString()+termExtractorType+".sorted", cValueResultList.toArray(new Term[]{}), jatePrebuiltRefCorpus.getAbsolutePath());
		
		Algorithm weird = new WeirdnessAlgorithm();
		AbstractFeatureWrapper weirdFeatures = new WeirdnessFeatureWrapper(wordCorpusFreq, localTermCorpusFreq, bncRef);
		log.info("Running WeirdnessAlgorithm Feature store builder and ATR..." + weird.toString());
		Map<Term, Double> weirdResult = weird.execute(weirdFeatures);
		HashMap<String, Double> weirdResultMap = new HashMap<String, Double>();
		for(Term t:weirdResult.keySet()) {
			Set<String> variants=termDocIndex.retrieveVariantsOfTermCanonical(t.getConcept());
			if(variants==null || variants.isEmpty()) {
				weirdResultMap.put(t.getConcept(), t.getConfidence());
				//log.debug("WEIRD Score for TERM:"+t.getConcept()+" = "+t.getConfidence());
				continue;
			}	
			for(String c : variants) {
				weirdResultMap.put(c, t.getConfidence());
				//log.debug("WEIRD Score for TERM:"+c+" = "+t.getConfidence());
			}
		}
		logResult(termDocIndex, weird.toString()+termExtractorType, weirdResultMap, jatePrebuiltRefCorpus.getAbsolutePath());
		//Log results Sorted by Confidence
		//List<Term> weirdResultList=Arrays.asList(weirdResult);
		//Collections.sort(weirdResultList, new Term.TermConfidenceComparator());
		//logResult(termDocIndex, weird.toString()+termExtractorType+".sorted", weirdResultList.toArray(new Term[]{}), jatePrebuiltRefCorpus.getAbsolutePath());

		/*double alpha=0.4, beta=0.4, zeta=0.2;
		Algorithm termEx = new TermExAlgorithm(alpha, beta, zeta);
		AbstractFeatureWrapper termExFeatures = new TermExFeatureWrapper(termDocFreq, termCorpusFreq, bncRef);
	    log.info("Running TermEx Feature store builder and ATR..." + termEx.toString());
		Term[] termExResult = termEx.execute(termExFeatures);
		logResult(termDocIndex, termEx.toString()+termExtractorType, termExResult, jatePrebuiltRefCorpus.getAbsolutePath());
		//Log results Sorted by Confidence
		List<Term> termExResultList=Arrays.asList(termExResult);
		Collections.sort(termExResultList, new Term.TermConfidenceComparator());
		logResult(termDocIndex, termEx.toString()+termExtractorType+".sorted", termExResultList.toArray(new Term[]{}), jatePrebuiltRefCorpus.getAbsolutePath());
		*/
		
		
		HashMap<String, Double> finalResultMap = new HashMap<String, Double>();
		for(Term t: weirdResult.keySet()) {
			Set<String> variants=termDocIndex.retrieveVariantsOfTermCanonical(t.getConcept());
			if(variants==null || variants.isEmpty()) {
				finalResultMap.put(t.getConcept(), t.getConfidence());
				log.debug("FINAL Score for TERM:"+t.getConcept()+" = "+t.getConfidence());
				continue;
			}
			for(String c : variants) {
				Double tfidfValue=tfidfResult.get(t);
				Double cValue=cValueResult.get(t);
				Double weirdValue=weirdResultMap.get(c);
				
				double confidence =
						weights[TFIDF_WT_INDEX]*(tfidfValue==null?-0.1f:tfidfValue.doubleValue())+ //penalize ones not found 
						weights[CVALUE_WT_INDEX]*(cValue==null?-0.1f:cValue.doubleValue())+ //penalize ones not found
						weights[WEIRD_WT_INDEX]*(weirdValue==null?-0.1f:weirdValue.doubleValue())
						//+weights[TERMEX_WT_INDEX]*termExResult[i].getConfidence()
				;
				finalResultMap.put(c, confidence);
				log.debug("TERM: "+c+", CONFIDENCE(TFIDF*CVALUE*WEIRD)=("+weights[TFIDF_WT_INDEX]+"*"+(tfidfValue==null?-0.1f:tfidfValue.doubleValue())+
					")+("+weights[CVALUE_WT_INDEX]+"*"+(cValue==null?-0.1f:cValue.doubleValue())+
					")+("+weights[WEIRD_WT_INDEX]+"*"+(weirdValue==null?-0.1f:weirdValue.doubleValue())+") = "+confidence);
			}
		}
		
		for(Term t: tfidfResult.keySet()) {
			if(finalResultMap.containsKey(t.getConcept()))
				continue; //already calculated
			Set<String> variants=termDocIndex.retrieveVariantsOfTermCanonical(t.getConcept());
			if(variants==null || variants.isEmpty()) {
				finalResultMap.put(t.getConcept(), t.getConfidence());
				log.debug("FINAL Score for TERM:"+t.getConcept()+" = "+t.getConfidence());
				continue;
			}
			for(String c : variants) {
				Double tfidfValue=tfidfResult.get(t);
				Double cValue=cValueResult.get(t);
				Double weirdValue=weirdResultMap.get(c);
				
				double confidence =
						weights[TFIDF_WT_INDEX]*(tfidfValue==null?-0.1f:tfidfValue.doubleValue())+ //penalize ones not found 
						weights[CVALUE_WT_INDEX]*(cValue==null?-0.1f:cValue.doubleValue())+ //penalize ones not found
						weights[WEIRD_WT_INDEX]*(weirdValue==null?-0.1f:weirdValue.doubleValue())
						//+weights[TERMEX_WT_INDEX]*termExResult[i].getConfidence()
				;
				finalResultMap.put(c, confidence);
				log.debug("TERM: "+c+", CONFIDENCE(TFIDF*CVALUE*WEIRD)=("+weights[TFIDF_WT_INDEX]+"*"+(tfidfValue==null?-0.1f:tfidfValue.doubleValue())+
					")+("+weights[CVALUE_WT_INDEX]+"*"+(cValue==null?-0.1f:cValue.doubleValue())+
					")+("+weights[WEIRD_WT_INDEX]+"*"+(weirdValue==null?-0.1f:weirdValue.doubleValue())+") = "+confidence);
			}
		}

		logResult(termDocIndex, "FINAL"+termExtractorType, finalResultMap, jatePrebuiltRefCorpus.getAbsolutePath());
		//Log results Sorted by Confidence
		//List<Term> finalResultList=Arrays.asList(result);
		//Collections.sort(finalResultList, new Term.TermConfidenceComparator());
		//logResult(termDocIndex, "FINAL.sorted"+termExtractorType, finalResultList.toArray(new Term[]{}), jatePrebuiltRefCorpus.getAbsolutePath());
		

        TermFinder termFinder=new SimpleTermFinder(false, new PennTreebankTokenizer());
        org.cleartk.ne.term.util.TermList termList = new TermList("KEYWORD");
        for(String t: finalResultMap.keySet()) {
        	double confidence=finalResultMap.get(t);
        	if(confidence < Double.parseDouble(confidenceThreshold)) {
    			log.debug("Ignoring JATE Term: "+t+", Confidence: "+confidence);
        	}
        	else {
        		log.info("Considering JATE Term(Root): "+t+", Confidence: "+confidence);
				termList.add(new org.cleartk.ne.term.util.Term(Double.toString(confidence), t , termList));
        	}
        }
        termFinder.addTermList(termList);

		//create list of tokens
        List<Token> tokens = new ArrayList<Token>();      
        FSIterator<Annotation> tokenAnnotations = jCas.getAnnotationIndex(org.cleartk.token.type.Token.type).iterator();
        while (tokenAnnotations.hasNext()) {
        	Annotation annotation = tokenAnnotations.next();
        	tokens.add(new Token(annotation.getBegin(), annotation.getEnd(), annotation.getCoveredText()));
        }
        
        List<TermMatch> termMatches = termFinder.getMatches(tokens);
        for(TermMatch match: termMatches) {
        	log.info("Found Term: "+match.getTerm().getTermText()+
				", Start:"+match.getBegin()+", End:"+match.getEnd()+", Confidence: "+ match.getTerm().getId());

        	JATETermChunk term = new JATETermChunk(jCas, match.getBegin(), match.getEnd());
        	term.setCvalue(Double.parseDouble(match.getTerm().getId()));
		    term.setChunkType("KEYWORD");
		    term.addToIndexes(jCas);
        }
		

        /* using the syntax parse tree isn't giving the right char/token indexes
		 * 
		for(Term t: result) {
			if(t.getConfidence() < Double.parseDouble(cvalueThreshold)) {
				log.trace("Ignoring JATE Term: "+t.getConcept()+", Confidence: "+t.getConfidence());
				continue; //ignore term
			}
			else {
				log.trace("Found JATE Term: "+t.getConcept()+", Confidence: "+t.getConfidence());
			}
			
			List<Token> tokens = tokenizer.getTokens(t.getConcept());
			FSIndex<Annotation> topNodeIndex = jCas.getAnnotationIndex(org.cleartk.syntax.constituent.type.TopTreebankNode.type);
			FSIterator<Annotation> sentenceTopNodeIterator=topNodeIndex.iterator(); 
			while (sentenceTopNodeIterator.hasNext()) {
				org.cleartk.syntax.constituent.type.TopTreebankNode sentenceNode = 
						(org.cleartk.syntax.constituent.type.TopTreebankNode) sentenceTopNodeIterator.next();
				TopTreebankNode topNode = TreebankFormatParser.parse(sentenceNode.getTreebankParse());
				
				if(topNode.getText().contains(t.getConcept()) &&
						tokens.size() <= topNode.getTerminalCount()) {
					int tokenIndex=0, terminalIndex=0;
					//tokenIndex=0, termIndex=0 cat is great, cat is great nuisance
					//tokenIndex=3, termIndex=1 cat is great, this cat is great
					//tokenIndex=3, termIndex=4 cat is great, cat is cudly, cat is great
					//tokenIndex=0, termIndex=0 cat is great, cat is cudly
					while(tokenIndex<tokens.size() && terminalIndex<topNode.getTerminalCount()) {
						String tokenText=tokens.get(tokenIndex).getTokenText();							
						String leafNodeValue=lemmatizer.normalize(((TreebankNode)topNode.getTerminal(terminalIndex)).getText());
						//hoping that terminals preserve their order
						if(tokenText.equalsIgnoreCase(leafNodeValue)) {
							tokenIndex++;
						}
						else if(tokenIndex>0) {
							tokenIndex=0;
						}
						terminalIndex++;
					}
					TreebankNode termNode=topNode.getTerminal(terminalIndex-1).getParent();
					int termStart = sentenceNode.getBegin() + termNode.getTextBegin();
					int termEnd = sentenceNode.getBegin() + termNode.getTextEnd();
					log.debug("Found Term: "+t.getConcept()+", Conf: "+t.getConfidence()+
							", Start:"+termStart+", End:"+termEnd+", Type:"+termNode.getType());
					
				    Chunk term = new Chunk(jCas, termStart, termEnd);
				    term.setChunkType(termNode.getType());					    	  
				    term.addToIndexes(jCas);
				} 
			}
		} */
	} 
    catch (JATEException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }


	private void logResult(GlobalIndex index, String algorithm,
		HashMap<String, Double> weirdResultMap, String absolutePath) throws JATEException, IOException {
		
		Map<Term, Double> result=new HashMap<Term, Double>();
		for(String s:weirdResultMap.keySet()) {
			Double confidence=weirdResultMap.get(s);
			result.put(new Term(s, confidence), confidence);
		}
		logResult(index, algorithm, result, absolutePath);
	}

	public static void main(String[] args) throws Exception {
		if(args.length < 1) {
			System.err.println("Usage: java JateAnnotator <path-to-file>");
			System.exit(1);
		}
		
		ResourceBundle properties=ResourceBundle.getBundle("text-annotator");
		
		BufferedReader reader=new BufferedReader(new FileReader(args[0]));
		StringBuilder text=new StringBuilder();
		String str="";
		while((str=reader.readLine())!=null)
			text.append(str);

		JATEAnnotator annotator=new JATEAnnotator();
		log.debug("Annotating Text: "+text);
		AggregateBuilder builder=new AggregateBuilder();
		
		//StanfordCoreNLP
	    builder.add(AnalysisEngineFactory.createPrimitiveDescription(
	    		StanfordCoreNLPAnnotator.class,
	    		StanfordCoreNLPAnnotator.PARAM_ANNOTATORS,
	    		"tokenize, ssplit, pos, lemma, ner, parse, dcoref"
	    		));

	    //JATE Annotator
		builder.add(AnalysisEngineFactory.createPrimitiveDescription(
			JATEAnnotator.class,
			TypeSystemDescriptionFactory.createTypeSystemDescription("com.cognizant.dml.text.annotator.type.JATETermChunk"),
			JATEAnnotator.PARAM_SOLR_CORPUS_DIRECTORY, new String[]{"/usr/home/praveen/solr/data/"},
			JATEAnnotator.PARAM_REF_CORPUS_FILE_NAME, "com/cognizant/dml/text/annotator/resource/jatenlp/bnc_unifrqs.normal",
			JATEAnnotator.PARAM_SOLR_ID_FIELD_NAME, "id",
			JATEAnnotator.PARAM_SOLR_TEXT_FIELD_NAME, "subject.topic",
			JATEAnnotator.PARAM_CONFIDENCE_THRESHOLD, "1.0",
			JATEAnnotator.PARAM_TERM_MATCH_ANNOTATION_CLASS_NAME, JATETermChunk.class.getName(),
			JATEAnnotator.PARAM_JATE_REF_CORPUS_PATH, properties.getString("JATEAnnotator.ref.model.path"),
			JATEAnnotator.PARAM_TERM_EXTRACTOR_TYPE, JATEAnnotator.TERM_EXTRACTOR_CHUNK,
			JATEAnnotator.PARAM_JATE_WEIGHTS, properties.getString("JATEAnnotator.algorithm.weights").split(",")
		));

		log.info("Initializing CAS");		
		CAS cas = CasCreationUtils.createCas(builder.createAggregateDescription());			
		cas.setDocumentText(text.toString());
		// Run pipeline
		log.info("Invoking Simple Pipeline: "+builder.toString());
		SimplePipeline.runPipeline(cas, builder.createAggregateDescription());

		FSIndex<Annotation> chunkIndex = cas.getJCas().getAnnotationIndex(Chunk.type);
		FSIterator<Annotation> chunkIterator=chunkIndex.iterator(); 
		while (chunkIterator.hasNext()) {
			Annotation chunk=(Chunk) chunkIterator.next();
			log.info("Found Chunk: "+ chunk.getCoveredText()+ ", at ["+chunk.getBegin()+", "+chunk.getEnd()+"]");
		}
		log.info("Annotation complete, returning result.");		    
	}
}
