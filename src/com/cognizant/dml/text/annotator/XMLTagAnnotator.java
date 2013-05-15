package com.cognizant.dml.text.annotator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSArray;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.component.ViewCreatorAnnotator;
import org.uimafit.descriptor.SofaCapability;
import org.uimafit.factory.initializable.InitializableFactory;

import com.cognizant.dml.text.annotator.type.Markup;
import com.cognizant.dml.text.annotator.type.MarkupAttribute;

@SofaCapability(inputSofas = CAS.NAME_DEFAULT_SOFA, outputSofas = XMLTagAnnotator.MARKUPLESS_VIEW)
public class XMLTagAnnotator extends JCasAnnotator_ImplBase {	
  private static final Log log=LogFactory.getLog(XMLTagAnnotator.class);
  public static final String MARKUPLESS_VIEW="MARKUPLESS_VIEW";
	
  private static final Pattern SKIP_TEXT_REGEX=Pattern.compile("[^<>]*", Pattern.DOTALL);
  private static final Pattern MARKUP_REGEX=Pattern.compile("</?\\s*(\\w+)((\\s*((\\w+)=([\\s'\"]?[^\"']*[\\s'\"]?))\\s*)*)\\s*/?>", Pattern.DOTALL); //treating start and end tags alike
  private static final Pattern ATTRIBUTE_REGEX=Pattern.compile("(\\w+)=[\\s'\"]?([^'\"]*)[\\s'\"]?");
  private static final int HORIZON_LENGTH = 1024; //equal to Scanner.BUFFER_LENGTH  
  
  protected Class<? extends Annotation> markupClass;
  protected Class<? extends Annotation> markupAttributeClass;

  @Override
  public void initialize(UimaContext context) throws ResourceInitializationException {
    try {
      super.initialize(context);
      markupClass = InitializableFactory.getClass("com.cognizant.dml.text.annotator.type.Markup", Annotation.class);
      markupAttributeClass = InitializableFactory.getClass("com.cognizant.dml.text.annotator.type.MarkupAttribute", Annotation.class);
    } 
    catch (Exception e) {
      throw new ResourceInitializationException(e);
    }
  }

  public void process(JCas jCas) throws AnalysisEngineProcessException {
	try {
		JCas defaultView = jCas.getView(CAS.NAME_DEFAULT_SOFA);
	    String text = defaultView.getDocumentText();
	    
	    JCas markuplessView = ViewCreatorAnnotator.createViewSafely(jCas, MARKUPLESS_VIEW);

		Scanner scanner=new Scanner(text);
		while(true) {
			scanner=scanner.skip(SKIP_TEXT_REGEX);
			
			String str=scanner.findWithinHorizon(MARKUP_REGEX,HORIZON_LENGTH);
			if(str!=null) {
				MatchResult result=scanner.match();
				String full=result.group();
	
				//find the right horizon index [(horizon.index*horizon.size)+result.start, (horizon.index*horizon.size)+result.end]
				//result.start, result.end giving wrong indexes, reverting to indexOf (full scan from begin)
				int start=text.indexOf(full);
				int end=start+full.length();
				//int horizon_index=text.indexOf(full)/HORIZON_LENGTH;
				//int horizon_start=horizon_index*HORIZON_LENGTH;
				
				Markup markup=new Markup(markuplessView);
				markup.setName(result.group(1));
				//markup.setBegin(horizon_start+result.start());
				//markup.setEnd(horizon_start+result.end());
				markup.setBegin(start);
				markup.setEnd(end);
				
				if(full.startsWith("</"))
					markup.setIsStart(false);
				else
					markup.setIsStart(true);
				
				//attributes	  
				Matcher matcher = ATTRIBUTE_REGEX.matcher(full);
				List<MarkupAttribute> attributes=new ArrayList<MarkupAttribute>();
				while (matcher.find()) {
					  MarkupAttribute attrib=new MarkupAttribute(markuplessView);
					  attrib.setName(matcher.group(1));
					  attrib.setValue(matcher.group(2));
					  attrib.setBegin(matcher.start(0));
					  attrib.setEnd(matcher.end(0));
					  attributes.add(attrib);
				}
				markup.setAttributes(new FSArray(markuplessView,attributes.size()));
				int i=0;
				for(MarkupAttribute attrib:attributes) {
					  markup.setAttributes(i++, attrib);
				}
				markup.addToIndexes(markuplessView);
							
				//replace XML Tags with spaces retaining the char index			
				//StringBuilder builder=new StringBuilder(text.substring(0, horizon_start+result.start()));
				StringBuilder builder=new StringBuilder(text.substring(0, start));
				for (int count=0;count<full.length();count++)
					builder.append(" ");			
				//builder.append(text.substring(horizon_start+result.end(), text.length()));
				builder.append(text.substring(end, text.length()));
				text=builder.toString();
			}
			else 
				break;
		}
		//log.info("MARKUPLESS: "+text.toString());
		markuplessView.setDocumentText(text.toString());
	} 
	catch (CASException e) {
		 throw new AnalysisEngineProcessException(e);
	} 
  }  
}
