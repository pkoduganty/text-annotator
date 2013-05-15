package com.cognizant.dml.text.annotator.service;

import javax.servlet.ServletConfig;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.cognizant.dml.text.annotator.TextAnnotator;

@Path("/text")
public class TextEnrichmentService extends Application {	
	@Context ServletConfig config; 

	private TextAnnotator textAnnotator=null; 
	private TextAnnotator htmlAnnotator=null;
	private TextAnnotator xmlAnnotator=null;
	
	public TextEnrichmentService(@Context ServletConfig config) {
		try {
			textAnnotator=new TextAnnotator(config.getInitParameter("GAZETTE_FILE"), MediaType.TEXT_PLAIN);
			htmlAnnotator=new TextAnnotator(config.getInitParameter("GAZETTE_FILE"), MediaType.TEXT_HTML);
			xmlAnnotator=new TextAnnotator(config.getInitParameter("GAZETTE_FILE"), MediaType.TEXT_XML);
		}
		catch(Exception e) {
			throw new WebApplicationException(e);
		}
	}
    @POST
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces({MediaType.TEXT_PLAIN})
    public String textPost(String text) {
			return textAnnotator.run(text);
    }

    @POST
    @Consumes({MediaType.TEXT_HTML,MediaType.APPLICATION_XHTML_XML})
    @Produces({MediaType.TEXT_HTML})
    public String htmlPost(String text) {
			return htmlAnnotator.run(text);
    }

    @POST
    @Consumes({MediaType.TEXT_XML,MediaType.TEXT_HTML,MediaType.APPLICATION_XHTML_XML,MediaType.APPLICATION_XML})
    @Produces({MediaType.TEXT_XML})
    public String xmlPost(String text) {
			return xmlAnnotator.run(text);
    }
}
