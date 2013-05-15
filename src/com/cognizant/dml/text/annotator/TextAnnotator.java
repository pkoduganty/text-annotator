package com.cognizant.dml.text.annotator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.uima.UIMAException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.CasCreationUtils;
import org.apache.uima.util.InvalidXMLException;
import org.cleartk.ne.term.util.Term;
import org.cleartk.ne.term.util.TermList;
import org.cleartk.ne.type.Chunk;
import org.cleartk.ne.type.GazetteerNamedEntityMention;
import org.cleartk.ne.type.NamedEntityMention;
import org.cleartk.token.type.Token;
import org.springframework.core.io.ClassPathResource;
import org.uimafit.factory.AggregateBuilder;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.factory.TypeSystemDescriptionFactory;
import org.uimafit.pipeline.SimplePipeline;

import com.cognizant.dml.text.annotator.type.DBPediaResourceMention;
import com.cognizant.dml.text.annotator.type.JATETermChunk;
import com.cognizant.dml.text.annotator.type.Markup;
import com.cognizant.dml.text.annotator.type.MarkupAttribute;
import com.cognizant.dml.text.annotator.util.TypedTermMatch;


public class TextAnnotator {
	private static final Log log=LogFactory.getLog(TextAnnotator.class);

	private String gazetteFile;
	private String outputFormat;
	
	private ResourceBundle properties=ResourceBundle.getBundle("text-annotator");
	String[] ignoredNamedEntities={"NUMBER","ORDINAL","SET","PERCENT","DURATION","MONEY"};
	
	AggregateBuilder builder=new AggregateBuilder();
		
	public TextAnnotator(String gazetteFile, String outputFormat) throws ResourceInitializationException, IOException, InvalidXMLException {
		this.gazetteFile=gazetteFile;
		this.outputFormat=outputFormat;
		
		ClassPathResource textTypeSystemFile = new ClassPathResource("com/cognizant/dml/text/annotator/type/TypeSystem.xml");
		
		//XMLTag
		builder.add(AnalysisEngineFactory.createPrimitiveDescription(
				XMLTagAnnotator.class,
				TypeSystemDescriptionFactory.createTypeSystemDescription("com.cognizant.dml.text.annotator.type.Markup")
		));				
		
		//StanfordCoreNLP
	    builder.add(AnalysisEngineFactory.createPrimitiveDescription(
	    		StanfordCoreNLPAnnotator.class,
	    		StanfordCoreNLPAnnotator.PARAM_ANNOTATORS,
	    		properties.getString("StanfordCoreNLPAnnotator.annotators")
	    		), CAS.NAME_DEFAULT_SOFA, XMLTagAnnotator.MARKUPLESS_VIEW);

	    //DBPedia Spotlight Annotator
		/*builder.add(AnalysisEngineFactory.createPrimitiveDescription(
				DBPediaAnnotator.class,
				TypeSystemDescriptionFactory.createTypeSystemDescription("com.cognizant.dml.text.annotator.type.DBPediaResourceMention"),

				DBPediaAnnotator.PARAM_PROXY_HOST,
	    		properties.getString("DBPediaAnnotator.proxyHost"),
			ignoredNamedEntities	DBPediaAnnotator.PARAM_PROXY_PORT,
	    		Integer.parseInt(properties.getString("DBPediaAnnotator.proxyPort")),
				DBPediaAnnotator.PARAM_PROXY_USER,
	    		properties.getString("DBPediaAnnotator.proxyUsername"),
				DBPediaAnnotator.PARAM_PROXY_PASSWORD,
	    		properties.getString("DBPediaAnnotator.proxyPassword"),
	    		
				DBPediaAnnotator.PARAM_DBPEDIA_CONFIDENCE,
	    		properties.getString("DBPediaAnnotator.dbpediaConfidence"),
				DBPediaAnnotator.PARAM_DBPEDIA_SPOTTER,
	    		properties.getString("DBPediaAnnotator.dbpediaSpotter"),
				DBPediaAnnotator.PARAM_DBPEDIA_POLICY,
	    		properties.getString("DBPediaAnnotator.dbpediaPolicy"),
				DBPediaAnnotator.PARAM_DBPEDIA_DISAMBIGUATOR,
	    		properties.getString("DBPediaAnnotator.dbpediaDisambiguator"),
				DBPediaAnnotator.PARAM_DBPEDIA_SUPPORT,
	    		properties.getString("DBPediaAnnotator.dbpediaSupport"),
				DBPediaAnnotator.PARAM_DBPEDIA_TYPES,
	    		properties.getString("DBPediaAnnotator.dbpediaTypes")					
				), CAS.NAME_DEFAULT_SOFA, XMLTagAnnotator.MARKUPLESS_VIEW);
		
		//OpenNLP
		XMLInputSource openNLPEngineDescription = new XMLInputSource(
				(new ClassPathResource("com/cognizant/dml/text/annotator/engine/OpenNlpTextAnalyzer.xml")).getFile());
		builder.add(UIMAFramework.getXMLParser().parseAnalysisEngineDescription(openNLPEngineDescription),
													CAS.NAME_DEFAULT_SOFA, XMLTagAnnotator.MARKUPLESS_VIEW);*/
		//Gazetteer
		builder.add(AnalysisEngineFactory.createPrimitiveDescription(
    		GazetteerAnnotator.class,
    		GazetteerAnnotator.PARAM_TERM_LIST_FILE_NAMES_FILE_NAME,
    		this.gazetteFile,
    		GazetteerAnnotator.PARAM_TOKEN_CLASS_NAME,
    		Token.class.getName(),
    		GazetteerAnnotator.PARAM_TERM_MATCH_ANNOTATION_CLASS_NAME,
    		GazetteerNamedEntityMention.class.getName()
    	), CAS.NAME_DEFAULT_SOFA, XMLTagAnnotator.MARKUPLESS_VIEW);
					
	    //JATE Annotator - NGRAM
		/*builder.add(AnalysisEngineFactory.createPrimitiveDescription(
				JATEAnnotator.class,
				TypeSystemDescriptionFactory.createTypeSystemDescription("com.cognizant.dml.text.annotator.type.JATETermChunk"),
				JATEAnnotator.PARAM_SOLR_CORPUS_DIRECTORY, properties.getString("JATEAnnotator.solr.corpus.paths").split(","),
				JATEAnnotator.PARAM_CORPUS_DIRECTORY, properties.getString("JATEAnnotator.dir.corpus.paths").split(","),
				JATEAnnotator.PARAM_REF_CORPUS_FILE_NAME, properties.getString("JATEAnnotator.ref.corpus.path"),
				JATEAnnotator.PARAM_SOLR_ID_FIELD_NAME, properties.getString("JATEAnnotator.solr.field.id"),
				JATEAnnotator.PARAM_SOLR_TEXT_FIELD_NAME, properties.getString("JATEAnnotator.solr.field.text"),
				JATEAnnotator.PARAM_CONFIDENCE_THRESHOLD, properties.getString("JATEAnnotator.confidence.threshold"),
				JATEAnnotator.PARAM_TERM_MATCH_ANNOTATION_CLASS_NAME, JATETermChunk.class.getName(),
				JATEAnnotator.PARAM_JATE_REF_CORPUS_PATH, properties.getString("JATEAnnotator.ref.model.path"),
				JATEAnnotator.PARAM_TERM_EXTRACTOR_TYPE, JATEAnnotator.TERM_EXTRACTOR_NGRAM,
				JATEAnnotator.PARAM_JATE_WEIGHTS, properties.getString("JATEAnnotator.algorithm.weights").split(",")
		), CAS.NAME_DEFAULT_SOFA, XMLTagAnnotator.MARKUPLESS_VIEW);*/

		//JATE Annotator - CHUNKER
		builder.add(AnalysisEngineFactory.createPrimitiveDescription(
				JATEAnnotator.class,
				TypeSystemDescriptionFactory.createTypeSystemDescription("com.cognizant.dml.text.annotator.type.JATETermChunk"),
				JATEAnnotator.PARAM_SOLR_CORPUS_DIRECTORY, properties.getString("JATEAnnotator.solr.corpus.paths").split(","),
				JATEAnnotator.PARAM_GAZETTE_CORPUS_DIRECTORY, properties.getString("JATEAnnotator.gazette.corpus.paths").split(","),
				JATEAnnotator.PARAM_CORPUS_DIRECTORY, properties.getString("JATEAnnotator.dir.corpus.paths").split(","),
				JATEAnnotator.PARAM_REF_CORPUS_FILE_NAME, properties.getString("JATEAnnotator.ref.corpus.path"),
				JATEAnnotator.PARAM_SOLR_ID_FIELD_NAME, properties.getString("JATEAnnotator.solr.field.id"),
				JATEAnnotator.PARAM_SOLR_TEXT_FIELD_NAME, properties.getString("JATEAnnotator.solr.field.text"),
				JATEAnnotator.PARAM_SOLR_TEXT_BOOST_FACTOR, properties.getString("JATEAnnotator.solr.field.text.boost"),
				JATEAnnotator.PARAM_CONFIDENCE_THRESHOLD, properties.getString("JATEAnnotator.confidence.threshold"),
				JATEAnnotator.PARAM_TERM_MATCH_ANNOTATION_CLASS_NAME, JATETermChunk.class.getName(),
				JATEAnnotator.PARAM_JATE_REF_CORPUS_PATH, properties.getString("JATEAnnotator.ref.model.path"),
				JATEAnnotator.PARAM_TERM_EXTRACTOR_TYPE, JATEAnnotator.TERM_EXTRACTOR_CHUNK,
				JATEAnnotator.PARAM_JATE_WEIGHTS, properties.getString("JATEAnnotator.algorithm.weights").split(",")
		), CAS.NAME_DEFAULT_SOFA, XMLTagAnnotator.MARKUPLESS_VIEW);
	}

	private List<TypedTermMatch> getSorted(CAS cas) {
	    List<TypedTermMatch> nemList=new ArrayList<TypedTermMatch>();
	    Set<String> ignoreEntities=new HashSet<String>();
	    for(int i=0;i<ignoredNamedEntities.length;i++)
	    	ignoreEntities.add(ignoredNamedEntities[i]);
	    
	    for(Iterator<AnnotationFS> aiter=cas.getAnnotationIndex().iterator(); aiter.hasNext();) {
	    	AnnotationFS annotation=aiter.next();
	    	if(ignoreEntities.contains(annotation.getType().getName()) || 
	    			(annotation instanceof NamedEntityMention && ignoreEntities.contains(((NamedEntityMention)annotation).getMentionType()))) {
	    		log.debug("Ignoring Annotation - "+annotation.getType().getName()+": "+annotation.getCoveredText());
	    		continue;
	    	}
	    	else { 
	    		log.trace("Found Annotation - "+annotation.getType().getName()+": "+annotation.getCoveredText());
	    	}
	    	
	    	if(annotation instanceof DBPediaResourceMention) {
	    		DBPediaResourceMention m=(DBPediaResourceMention) annotation;
	    		log.info("Found DBPediaResource: "+m.getCoveredText()+", "+m.getResourceType());
	    		nemList.add(new TypedTermMatch(m.getBegin(),m.getEnd(),
	    						new Term(m.getURI(), m.getSurfaceForm(), null),	m.getResourceType()));
	    	}	    	
	    	else if(annotation instanceof GazetteerNamedEntityMention) {
	    		GazetteerNamedEntityMention m=(GazetteerNamedEntityMention) annotation;
	    		log.info("Found GazetteerNamedEntity: "+m.getCoveredText()+", "+m.getMentionType());
	    		nemList.add(new TypedTermMatch(m.getBegin(),m.getEnd(),new Term(m.getMentionId(), m.getCoveredText(), null),m.getMentionType()));
	    	}	    	
	    	else if(annotation instanceof NamedEntityMention) {
	    		NamedEntityMention m=(NamedEntityMention) annotation;
    			nemList.add(new TypedTermMatch(m.getBegin(),m.getEnd(),new Term(null, m.getCoveredText(), null),m.getMentionType()));
	    		log.info("Found NamedEntityMention: "+m.getCoveredText()+", "+m.getMentionType());
	    	}
	    	else if(annotation instanceof Markup) { //TODO - worst ever code, reusing nemList and TermList for XML Markup code
	    		Markup m=(Markup) annotation;
	    		
	    		TermList attributes=new TermList("XML_ATTRIBUTES");
	    		FSArray array=m.getAttributes();
	    		for(int i=0;i<array.size();i++)
	    			attributes.add(new Term(((MarkupAttribute)array.get(i)).getName(),((MarkupAttribute)array.get(i)).getValue(),null));
	    
	    		log.info("Found XMLEntity: "+m.getName()+", "+m.getCoveredText());
	    		nemList.add(
	    				new TypedTermMatch(
	    						m.getBegin(),
	    						m.getEnd(),
	    						new Term(m.getName(),m.getCoveredText(),attributes),
	    						m.getIsStart()?"XML_START":"XML_END"
	    				)
	    		);
	    	}
	    	else if("opennlp.uima.Organization".equals(annotation.getType().getName()) ||
	    			"opennlp.uima.Person".equals(annotation.getType().getName()) ||
	    			"opennlp.uima.Location".equals(annotation.getType().getName()) ||
	    			"opennlp.uima.Date".equals(annotation.getType().getName())
	    			) {
	    		AnnotationFS m=annotation;
	    		log.info("Found OpenNLPEntity: "+m.getCoveredText()+", "+m.getType());
	    		nemList.add(
	    				new TypedTermMatch(
	    						m.getBegin(),
	    						m.getEnd(),
	    						new Term(m.getType().getName(),m.getCoveredText(), null),
	    						m.getType().getShortName()
	    				)
	    		);
	    	}
	    	else if (annotation instanceof Chunk) {
	    		Chunk m=(Chunk) annotation;
	    		//TODO - Term should mention confidence and not type below
	    		nemList.add(new TypedTermMatch(m.getBegin(),m.getEnd(), 
	    				new Term(m.getCoveredText(), m.getCoveredText(), null),m.getChunkType()));
	    		log.info("Found JATE Chunk: "+m.getCoveredText()+", "+m.getChunkType());
	    	}
	    }

	    Collections.sort(nemList,new TypedTermMatch.TypedTermMatchComparator());
	    return nemList;
	}

	public String run(String text) {
		log.debug("Annotating Text: "+text);
		try {
			log.info("Initializing CAS");
			CAS cas = CasCreationUtils.createCas(builder.createAggregateDescription());
		
			cas.setDocumentText(text);
			// Run pipeline
			log.info("Invoking Simple Pipeline: "+builder.toString());
			SimplePipeline.runPipeline(cas, builder.createAggregateDescription());
			
			List<TypedTermMatch> nemList=getSorted(cas.getView(XMLTagAnnotator.MARKUPLESS_VIEW));
			
			int index=0;
			StringBuffer output=new StringBuffer();
			
			if(outputFormat!=null && outputFormat.equals(MediaType.TEXT_PLAIN)) 
			{
			    for(TypedTermMatch nem:nemList) {
			    	if(index<text.length() && index<=nem.getBegin()) {
			    		output.append(text.substring(index, nem.getBegin())); //text till entity begin
			    		
			    		if(nem.getType()!=null && !nem.getType().startsWith("XML") 
			    				&& nem.getTerm()!=null && nem.getTerm().getId()!=null)
			    			output.append("<entity type=\""+nem.getType()+"\" uri=\""+nem.getTerm().getId()+"\">");
			    		else if(nem.getType()!=null && !nem.getType().startsWith("XML"))
			    			output.append("<entity type=\""+nem.getType()+"\">");
			    		
			    		String entityText=text.substring(nem.getBegin(), nem.getEnd());
			    		log.info("Found entity: "+entityText+", type: "+nem.getType()+", spanning: ["+nem.getBegin()+":"+nem.getEnd()+"]");
			    		
			    		output.append(entityText);//entity text
			    		
			    		if(nem.getType()!=null && !nem.getType().startsWith("XML"))
			    			output.append("</entity>");
			    		
			    		index=nem.getEnd();
			    	}
			    }
			    if(index<text.length())
			    	output.append(text.substring(index, text.length()));

			    log.info("TEXT_PLAIN Annotation complete, returning result.");		    
	    		log.debug(output.toString());
			    return output.toString();
			}
			else if(MediaType.TEXT_HTML.equals(outputFormat)) 
			{
				output.append("<HTML><BODY>"); //prolog
				
			    for(TypedTermMatch nem:nemList) {
			    	if(index<text.length() && index<=nem.getBegin()) {
			    		output.append(text.substring(index, nem.getBegin())); //text till entity begin
			    		
			    		if(nem.getType()!=null && !nem.getType().startsWith("XML")) {
			    			output.append("<span title=\""+nem.getType()+"\" style=\"background-color:#FFFF99;font-weight:bold\">");
			    		}

			    		String entityText=text.substring(nem.getBegin(), nem.getEnd()); //
			    		log.info("Found entity: "+entityText+", type: "+nem.getType()+", spanning: ["+nem.getBegin()+":"+nem.getEnd()+"]");
			    		
			    		output.append(entityText); //entity text
			    		
			    		if(nem.getType()!=null && !nem.getType().startsWith("XML")) {
			    			output.append("</span>");
			    		}
			    		index=nem.getEnd();
			    	}
			    }
		    	if(index<text.length())
			    	output.append(text.substring(index, text.length()));
		    	output.append("</BODY></HTML>");

			    log.info("TEXT_HTML Annotation complete, returning result.");		    
	    		log.debug(output.toString());
			    return output.toString();
			}
			else if (MediaType.TEXT_XML.equals(outputFormat)) 
			{
				output.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<entities>\n"); //prolog
				
			    for(TypedTermMatch nem:nemList) {
			    	if(index<text.length() && index<=nem.getBegin()) {
			    		//output.append(text.substring(index, nem.getBegin())); //non-annotated text till entity begin - not copied for XML
			    		
			    		String entityText=text.substring(nem.getBegin(), nem.getEnd());
			    		if(nem.getType()!=null && !nem.getType().startsWith("XML") && nem.getTerm()!=null && nem.getTerm().getId()!=null )
			    			output.append("\t<entity type=\""+nem.getType()+
			    					"\" uri=\""+nem.getTerm().getId()+
			    					"\" begin=\""+nem.getBegin()+
			    					"\" end=\""+nem.getEnd()+
			    					"\" text=\""+nem.getTerm().getTermText()+
			    					"\">").append(entityText).append("</entity>\n");
			    		else if(nem.getType()!=null && !nem.getType().startsWith("XML"))
			    			output.append("\t<entity type=\""+nem.getType()+
			    					"\" begin=\""+nem.getBegin()+
			    					"\" end=\""+nem.getEnd()+
			    					"\" text=\""+nem.getTerm().getTermText()+
			    					"\">").append(entityText).append("</entity>\n");

			    		log.info("Found entity: "+entityText+", type: "+nem.getType()+", spanning: ["+nem.getBegin()+":"+nem.getEnd()+"]");
			    		index=nem.getEnd();
			    	}
			    }
			    /*if(index<text.length())
			    	output.append(text.substring(index, text.length()));*/ //non-annotated text not copied for XML
			    
			    output.append("</entities>");
	    		log.info("TEXT_XML Annotation complete, returning result.");		    
	    		log.debug(output.toString());
			    return output.toString();
			}
			else if (MediaType.APPLICATION_JSON.equals(outputFormat)) {
				/*{
				    "entities": [
				        { "type":"SERVICE", "uri":"", "begin":"10", "end":"28", "text":"service management"},
				        { "type":"ORGANIZATION","begin":"33", "end":"64", "text":"Communication Service Providers"},
				        { "type":"KEYWORD", "uri":"order and service management", "begin":"211", "end":"239", "text":"order and service management"}        
				    ]
				}*/
				output.append("{\n\t\"entities\": [\n"); //prolog
				
			    for(TypedTermMatch nem:nemList) {
			    	if(index<text.length() && index<=nem.getBegin()) {
			    		//output.append(text.substring(index, nem.getBegin())); //non-annotated text till entity begin - not copied for JSON
			    		
			    		String entityText=text.substring(nem.getBegin(), nem.getEnd());
			    		if(nem.getType()!=null && !nem.getType().startsWith("XML") && nem.getTerm()!=null && nem.getTerm().getId()!=null )
			    			output.append("\t\t{ " +
			    					"\"type\":\""+nem.getType()+"\", " +
			    					"\"uri\":\""+nem.getTerm().getId()+"\", " +
			    					"\"begin\":\""+nem.getBegin()+"\", " +
			    					"\"end\":\""+nem.getEnd()+"\", " +
			    					"\"text\":\""+nem.getTerm().getTermText()+"\"" +
			    					" },\n");
			    		else if(nem.getType()!=null && !nem.getType().startsWith("XML"))
			    			output.append("\t\t{ " +
			    					"\"type\":\""+nem.getType()+"\", " +
			    					"\"begin\":\""+nem.getBegin()+"\", " +
			    					"\"end\":\""+nem.getEnd()+"\", " +
			    					"\"text\":\""+nem.getTerm().getTermText()+"\"" +
			    					" },\n");

			    		log.info("Found entity: "+entityText+", type: "+nem.getType()+", spanning: ["+nem.getBegin()+":"+nem.getEnd()+"]");
			    		index=nem.getEnd();
			    	}
			    }
			    /*if(index<text.length())
			    	output.append(text.substring(index, text.length()));*/ //non-annotated text not copied for JSON
			    
			    //remove trailing comma
			    int trailingCommaIndex=output.lastIndexOf(",");
			    if(trailingCommaIndex > 0)
			    	output.deleteCharAt(output.lastIndexOf(","));
			    
			    output.append("\n\t]\n}");
	    		log.info("APPLICATION_JSON Annotation complete, returning result.");
	    		log.debug(output.toString());
			    return output.toString();
			}
		}
		catch (ResourceInitializationException e) {
			e.printStackTrace();
		} 
		catch (UIMAException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void main(String[] args) throws Exception {
		if(args.length < 1) {
			System.err.println("Usage: java ClearNLPAnnotator <path-to-file>");
			System.exit(1);
		}
		
		BufferedReader reader=new BufferedReader(new FileReader(args[0]));
		StringBuilder text=new StringBuilder();
		String str="";
		while((str=reader.readLine())!=null)
			text.append(str);
		
		/*Lemmatizer lemmatizer=new Lemmatizer();
		StopList stop = new StopList(false);
		CandidateTermExtractor extractor=new ChunkExtractorOpenNLP(stop, lemmatizer);
		Map<String, Set<String>> response=extractor.extract(text.toString());*/
		TextAnnotator annotator=new TextAnnotator("com/cognizant/dml/text/annotator/resource/termlist.txt", MediaType.TEXT_XML);
		String response=annotator.run(text.toString());
    	System.out.println("Annotated: "+response);
	}
}

