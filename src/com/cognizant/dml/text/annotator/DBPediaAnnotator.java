package com.cognizant.dml.text.annotator;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.CAS;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.CasCreationUtils;
import org.cleartk.util.UIMAUtil;
import org.uimafit.component.JCasAnnotator_ImplBase;
import org.uimafit.descriptor.ConfigurationParameter;
import org.uimafit.factory.AggregateBuilder;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.factory.ConfigurationParameterFactory;
import org.uimafit.factory.TypeSystemDescriptionFactory;
import org.uimafit.factory.initializable.InitializableFactory;
import org.uimafit.pipeline.SimplePipeline;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.cognizant.dml.text.annotator.type.DBPediaResourceMention;

public class DBPediaAnnotator extends JCasAnnotator_ImplBase {
	private static final Log log=LogFactory.getLog(DBPediaAnnotator.class);
	private static final String dbpediaResponseXMLElement="Resource";
	private static final String dbpediaResponseXMLResourceOffset="offset";
	private static final String dbpediaResponseXMLResourceSurfaceForm="surfaceForm";
	private static final String dbpediaResponseXMLResourceURI="URI";
	private static final String dbpediaResponseXMLResourceSupport="support";
	private static final String dbpediaResponseXMLResourceTypes="types";
	private static final String dbpediaResponseXMLResourceSimilarityScore="similarityScore";
	private static final String dbpediaResponseXMLResourcePercentageOfSecondRank="percentageOfSecondRank";

	public static final String PARAM_PROXY_HOST = ConfigurationParameterFactory.createConfigurationParameterName(
			DBPediaAnnotator.class,
	      "proxyHost");	
	@ConfigurationParameter(
		      mandatory = false,
		      defaultValue = "",
		      description = "proxy host")
	private String proxyHost;

	public static final String PARAM_PROXY_PORT = ConfigurationParameterFactory.createConfigurationParameterName(
			DBPediaAnnotator.class,
	      "proxyPort");	
	@ConfigurationParameter(
		      mandatory = false,
		      description = "proxy port")
	private int proxyPort;

	public static final String PARAM_PROXY_USER = ConfigurationParameterFactory.createConfigurationParameterName(
			DBPediaAnnotator.class,
	      "proxyUsername");		
	@ConfigurationParameter(
		      mandatory = false,
		      defaultValue = "",
		      description = "proxy user")
	private String proxyUsername;

	public static final String PARAM_PROXY_PASSWORD = ConfigurationParameterFactory.createConfigurationParameterName(
			DBPediaAnnotator.class,
	      "proxyPassword");	
	@ConfigurationParameter(
		      mandatory = false,
		      defaultValue = "",
		      description = "proxy user password")
	private String proxyPassword;

	public static final String PARAM_TOKEN_CLASS = ConfigurationParameterFactory.createConfigurationParameterName(
			DBPediaAnnotator.class,
	      "tokenClassName");
	@ConfigurationParameter(
	      mandatory = false,
	      defaultValue = "org.cleartk.token.type.Token",
	      description = "names the class of the type system type corresponding to tokens. ")
	private String tokenClassName;

	public static final String PARAM_DBPEDIA_CONFIDENCE = ConfigurationParameterFactory.createConfigurationParameterName(
			DBPediaAnnotator.class,
	      "dbpediaConfidence");
	@ConfigurationParameter(
		      mandatory = false,
		      defaultValue = "0.2",
		      description = "DBPedia spotlight rest api parameter for filtering based on confidence")
	private String dbpediaConfidence;

	public static final String PARAM_DBPEDIA_DISAMBIGUATOR = ConfigurationParameterFactory.createConfigurationParameterName(
			DBPediaAnnotator.class,
	      "dbpediaDisambiguator");
	@ConfigurationParameter(
		      mandatory = false,
		      defaultValue = "Default",
		      description = "DBPedia spotlight rest api parameter for disambiguation - Default/Document/Occurrences")
	private String dbpediaDisambiguator;

	public static final String PARAM_DBPEDIA_POLICY = ConfigurationParameterFactory.createConfigurationParameterName(
			DBPediaAnnotator.class,
	      "dbpediaPolicy");
	@ConfigurationParameter(
		      mandatory = false,
		      defaultValue = "whitelist",
		      description = "DBPedia spotlight rest api parameter for policy - whitelist")
	private String dbpediaPolicy;
	
	public static final String PARAM_DBPEDIA_SPOTTER = ConfigurationParameterFactory.createConfigurationParameterName(
			DBPediaAnnotator.class,
	      "dbpediaSpotter");
	@ConfigurationParameter(
		      mandatory = false,
		      defaultValue = "CoOccurrenceBasedSelector",
		      description = "DBPedia spotlight rest api parameter for identifying phrases - " +
		      		"CoOccurrenceBasedSelector/LingPipeSpotter/AtLeastOneNounSelector/NESpotter")
	private String dbpediaSpotter;

	public static final String PARAM_DBPEDIA_SUPPORT = ConfigurationParameterFactory.createConfigurationParameterName(
			DBPediaAnnotator.class,
	      "dbpediaSupport");
	@ConfigurationParameter(
		      mandatory = false,
		      defaultValue = "400",
		      description = "DBPedia spotlight rest api parameter for filtering based on prominence")
	private String dbpediaSupport;

	//refer http://dbpedia-spotlight.github.com/demo/ for types
	public static final String PARAM_DBPEDIA_TYPES = ConfigurationParameterFactory.createConfigurationParameterName(
			DBPediaAnnotator.class,
	      "dbpediaTypes");
	@ConfigurationParameter(
		      mandatory = false,
		      defaultValue = 
		    	  	"DBpedia:Organisation,DBpedia:Person,DBpedia:Place,DBpedia:ProgrammingLanguage,DBpedia:Software,DBpedia:Website," +
		      		"Freebase:/business,Freebase:/computer,Freebase:/finance,Freebase:/organization,Freebase:/people,Freebase:/projects," +
		      		"Schema:Organization,Schema:Person,Schema:City,Schema:Continent,Schema:Country,Schema:Product",
		      description = "DBPedia spotlight rest api parameter for filtering based on types")
	private String dbpediaTypes;
	
	protected Class<? extends Annotation> tokenClass;	
	protected Type tokenType;
	
	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
	    try {
	      super.initialize(context);
	      tokenClass = InitializableFactory.getClass(tokenClassName, Annotation.class);
	    } catch (Exception e) {
	      throw new ResourceInitializationException(e);
	    }
	}

	@Override
	public void process(JCas cas) throws AnalysisEngineProcessException {
	    tokenType = UIMAUtil.getCasType(cas, tokenClass);

        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {

            HttpHost proxy = new HttpHost(proxyHost, proxyPort);
        	
            HttpHost targetHost = new HttpHost("dbpedia.org", 80, "http");
		    HttpPost post = new HttpPost("http://spotlight.dbpedia.org/rest/annotate");
		
		    List <NameValuePair> nvps = new ArrayList <NameValuePair>();
		    nvps.add(new BasicNameValuePair("text", cas.getDocumentText()));
		    nvps.add(new BasicNameValuePair("disambiguator", dbpediaDisambiguator));
		    nvps.add(new BasicNameValuePair("policy", dbpediaPolicy));
		    nvps.add(new BasicNameValuePair("spotter", dbpediaSpotter));
		    nvps.add(new BasicNameValuePair("confidence", dbpediaConfidence));
		    nvps.add(new BasicNameValuePair("support", dbpediaSupport));
		    nvps.add(new BasicNameValuePair("types", dbpediaTypes));
		
		    post.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
		
		    HttpResponse response = null;
		    if(!proxyHost.isEmpty()) { 
	            httpclient.getCredentialsProvider().setCredentials(
	                    new AuthScope(proxyHost, proxyPort),
	                    new UsernamePasswordCredentials(proxyUsername, proxyPassword));
	            httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);

	            response = httpclient.execute(targetHost, post);
		    }
		    else
		    	response = httpclient.execute(post);
		    
		    String responseXML="";
		    HttpEntity entity = response.getEntity();
		    if (entity != null) {
		    	entity = new BufferedHttpEntity(entity);
	            responseXML=EntityUtils.toString(entity);
		    }
		    //log.info(responseXML);		    		
		    
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = dBuilder.parse(new ByteArrayInputStream(responseXML.getBytes()));
			doc.getDocumentElement().normalize();
	 
			NodeList nList = doc.getElementsByTagName(dbpediaResponseXMLElement);
			for (int rindex = 0; rindex < nList.getLength(); rindex++) {
			   Node rNode = nList.item(rindex);
			   if (rNode.getNodeType() == Node.ELEMENT_NODE) {
			      Element resource = (Element) rNode;
			      
			      String uri=resource.getAttribute(dbpediaResponseXMLResourceURI);
			      int begin=Integer.parseInt(resource.getAttribute(dbpediaResponseXMLResourceOffset));
			      String support=resource.getAttribute(dbpediaResponseXMLResourceSupport);
			      String surfaceForm=resource.getAttribute(dbpediaResponseXMLResourceSurfaceForm);
			      String types=resource.getAttribute(dbpediaResponseXMLResourceTypes);
			      String similarity=resource.getAttribute(dbpediaResponseXMLResourceSimilarityScore);
			      String secondRank=resource.getAttribute(dbpediaResponseXMLResourcePercentageOfSecondRank);
			      
			      DBPediaResourceMention nem = new DBPediaResourceMention(cas, begin, begin+surfaceForm.length());
			      nem.setURI(uri);
			      nem.setProminence(support);
			      nem.setSurfaceForm(surfaceForm);
			      nem.setOffset(begin);
			      nem.setSimilarityScore(Double.parseDouble(similarity));
			      nem.setPercentageOfSecondRank(Double.parseDouble(secondRank));
			      nem.setResourceType(types);
			    	  
			      log.debug("DBPediaResource: "+nem.getSurfaceForm()+", "+nem.getOffset()+", "+nem.getURI()+", "+nem.getResourceType());
			      nem.addToIndexes();
			   }
			}		    
        } catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpclient.getConnectionManager().shutdown();
        }		  
	}
		
	public static void main(String[] args) throws Exception {
		if(args.length < 2) {
			System.err.println("Usage: java ClearNLPAnnotator <path-to-file>");
			System.exit(1);
		}
		
		BufferedReader reader=new BufferedReader(new FileReader(args[0]));
		StringBuilder text=new StringBuilder();
		String str="";
		while((str=reader.readLine())!=null)
			text.append(str);

		AggregateBuilder builder=new AggregateBuilder();
	    builder.add(AnalysisEngineFactory.createPrimitiveDescription(
	    		StanfordCoreNLPAnnotator.class,
	    		StanfordCoreNLPAnnotator.PARAM_ANNOTATORS,
	    		"tokenize, ssplit"		    		
	    ));		
	    
		builder.add(AnalysisEngineFactory.createPrimitiveDescription(
				DBPediaAnnotator.class,
				TypeSystemDescriptionFactory.createTypeSystemDescription(DBPediaResourceMention.class)));
		
		CAS cas = CasCreationUtils.createCas(builder.createAggregateDescription());
		cas.setDocumentText(text.toString());
		SimplePipeline.runPipeline(cas, builder.createAggregateDescription());
		
	    for(Iterator<AnnotationFS> aiter=cas.getAnnotationIndex().iterator(); aiter.hasNext();) {	    	
	    	AnnotationFS annotation=aiter.next();
	    	if(annotation instanceof DBPediaResourceMention) {
	    		DBPediaResourceMention m=(DBPediaResourceMention) annotation;
	    		log.info("Found DBPediaResource: "+m.getCoveredText()+", <"+m.getURI()+">, support="+m.getProminence()+", similarity="+m.getSimilarityScore());
	    	}	    	
	    }
	}
	
}

