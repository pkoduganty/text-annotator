package com.cognizant.dml.text.annotator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;
import org.uimafit.factory.ConfigurationParameterFactory;


/**
 * CollationAnnotator takes two annotation classes, extracts annotations  
 * @author praveen
 */
public class CollationNLPAnnotator extends JCasAnnotator_ImplBase {
	private static final Log log=LogFactory.getLog(CollationNLPAnnotator.class);

	public static final String PARAM_ANNOTATION_CLASS_NAME = ConfigurationParameterFactory.createConfigurationParameterName(
			CollationNLPAnnotator.class,
	      "tokenClassName");

	@ConfigurationParameter(
	      mandatory = true,
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
	  
	@Override
	public void process(JCas arg0) throws AnalysisEngineProcessException {
		// TODO Auto-generated method stub
		
	}
		
}

